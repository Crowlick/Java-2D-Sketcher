package Contraints;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

import entities.Point;
import toolBox.Solver;

public class ConstraintManager 
{
	private int argsCount = 0;
	private ArrayList<Constraint> _constraints = new ArrayList<Constraint>();
	private ArrayList<Point> _points = new ArrayList<Point>();
	private ArrayList<ArrayList<Integer>> _pos = new ArrayList<ArrayList<Integer>>();
//	private double[][] _J = null;
	//private double[] _F = null;
	
	
	public int size() {return argsCount;}
	
	private void changeCMofObject(Constraint c)
	{
		for (Point p : c.getChilds())
			p.setConstraintManager(this);
	}
	
	public ConstraintManager() {}
	
	public ConstraintManager(ConstraintManager cm1, Constraint newConstr)
	{
		
		for (Constraint c : cm1._constraints)
		{
			addConstraint(c);
			changeCMofObject(c);
		}
		
		addConstraint(newConstr);
		changeCMofObject(newConstr);
	//	_J = new double[argsCount][argsCount];
		//_F = new double[argsCount];
	}
	
	
	public ConstraintManager(ConstraintManager cm1, ConstraintManager cm2, Constraint newConstr)
	{
		
		for (Constraint c : cm1._constraints)
		{
			addConstraint(c);
			changeCMofObject(c);
		}
		
		if (cm1 != cm2)
			for (Constraint c : cm2._constraints)
			{
				addConstraint(c);
				changeCMofObject(c);
			}
		
		addConstraint(newConstr);
		changeCMofObject(newConstr);

	//	_J = new double[argsCount][argsCount];
		//_F = new double[argsCount];
	}
	
	public double lagrangeFunc(double[] vars)
	{
		int start = _constraints.size();
		double sum = 0;
		for (int i = start; i < argsCount; i++)
			sum += vars[i] * vars[i];
		sum /= 2.;
		
		for (int i = 0; i < start; i++)
			sum += vars[i] *  _constraints.get(i).func(selectFromArray(vars, i));
		return sum;
	}
	
	public void addConstraint(Constraint c)
	{
		isHere(c);
		_constraints.add(c);
	}

	public double[] selectFromArray(double[] args, int n)
	{
		ArrayList<Integer> lay = _pos.get(n);
		int shift = _constraints.size();
		double[]  res = new double[lay.size() * 2];
		
		for (int i = 0; i < res.length; i++)
		{
			res[i] = args[lay.get(i / 2) + shift];
			res[++i] = args[lay.get(i / 2) + shift + 1];
		}
		
		return res;
	}
	
	public void isHere(Constraint c)
	{
		
		ArrayList<Integer> tmp = new ArrayList<Integer>();
		System.out.println("New constr");
		int max = argsCount - _constraints.size();
		
		for (Point p : c.getChilds())
		{
			int i = 0;
			while (i < _points.size() && p != _points.get(i))
				i++;
			if (i >= _points.size())
			{
				System.out.println("New Point pos " + max);
				tmp.add(max);
				max += 2;
				argsCount += 2;
				_points.add(p);
			}
			else
			{
				System.out.println("Old Point pos " + i * 2);
				tmp.add(i * 2);
			}
		}
		_pos.add(tmp);
		argsCount++;
	}

	
	public float[] makeOptimal()
	{
		if (_constraints.isEmpty())
			return null;
		float[] res = Solver.conv(Solver.newtonSolution(this));
		
		int shift = _constraints.size();
		
		for (int i = 0; i < _points.size(); i++)
		{
			_points.get(i).increasePos(new Vector3f(res[i * 2 + shift], res[i * 2 + 1 + shift], 0f));
			_points.get(i).update();
		}
		
		return res;
	}
	
	public double derrivative(double[] args, int i)
	{
		double h = 0.001;
		double x = args[i];
		
		args[i] = x + h;
		double v1 = lagrangeFunc(args);
		
		args[i] = x - h;
		double v2 = lagrangeFunc(args);
		
		args[i] = x;
		return (v1 - v2) / (2. * h);
	}
	
	public double derrivative(double[] args, int i, int j)
	{
		double h = 0.001;
		double x = args[i];
		
		if (i == j)
		{
			args[i] = x - h;
			double v1 = lagrangeFunc(args);
			args[i] = x;
			double v2 = lagrangeFunc(args);
			args[i] = x + h;
			double v3 = lagrangeFunc(args);
			args[i] = x;
			return (v1 - 2. * v2 + v3) / (h * h);
		}
		
		double y = args[j];
		
		args[i] = x - h;
		args[j] = y - h;
		double v1 = lagrangeFunc(args);
		
		args[i] = x + h;
		double v2 = lagrangeFunc(args);
		
		args[i] = x - h;
		args[j] = y + h;
		double v3 = lagrangeFunc(args);
		
		args[i] = x + h;
		double v4 = lagrangeFunc(args);
		
		args[i] = x;
		args[j] = y;
		return (v1 - v2 - v3 + v4) / (4. * h * h);
	}
	
	
	public double[] makeF(double[] args)
	{
		double[] _F = new double[argsCount];
		for (int i = 0; i < argsCount; i++)
			_F[i] = -derrivative(args, i);
		return _F;
	}
	
	
	public double[][] makeJ(double[] args)
	{
		
		double[][] _J = new double[argsCount][argsCount];
		for (int i = 0; i < argsCount; i++)
			for (int j = 0; j < argsCount; j++)
				_J[i][j] = derrivative(args, i, j);
		return _J;
	}
}

