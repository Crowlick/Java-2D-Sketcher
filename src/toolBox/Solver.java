package toolBox;

import java.util.ArrayList;
import java.util.function.Function;

import Contraints.ConstraintManager;

public class Solver 
{
	private static ConstraintManager _cm = null;
	
	public static void attach(ConstraintManager cm) {_cm = cm;}
	
	public static double calc(double[] aa)
	{
		return _cm.lagrangeFunc(aa);
	}
	
	public static float[] conv(double[] A)
	{
		float[] a = new float[A.length];
		for (int i = 0; i < a.length; i++)
			a[i] = (float)(A[i]);
		return a;
	}
	
	public static double[] solve(double[][] A, double[] B)
	{		
		double[] result = new double[B.length];
		double n = 1.;
		double crit = 1e-6;
		Maths.show(A);
		Maths.show(B);
		for (int i = 0; i < B.length; i++)
		{
			
			if (Math.abs(A[i][i]) <= crit)
			{
				int c = i;
				while (Math.abs(A[c][i]) <= crit)
					if (++c >= B.length)
					{
						System.out.println("No answ");
						Maths.show(A);
						return B;
					}
				double[] tmp = A[c];
				A[c] = A[i];
				A[i] = tmp;
				double tm = B[c];
				B[c] = B[i];
				B[i] = tm;
			}
			for (int k = 0; k < B.length; k++)
			{
				if (k == i)
					continue;
				B[k] = (A[i][i] * B[k] - A[k][i] * B[i]) / n;
				for (int j = B.length - 1; j >= 0; j--)
				{
					if (j == i)
						continue;
					A[k][j] = (A[i][i] * A[k][j] - A[k][i] * A[i][j]) / n;
				}
				A[k][i] = (A[i][i] * A[k][i] - A[k][i] * A[i][i]) / n;
			}
			n = A[i][i];
		}
		
		for (int i = B.length - 1; i >= 0; i--)
		{
			result[i]  = B[i] / A[i][i];
			for (int j = B.length - 1; j > i; j--)
				result[i] -= result[j] * A[i][j] / A[i][i];
		}
		System.out.println("res");
		Maths.show(result);
		return result;
	}
	
	
	public static double[] solve2(double[][] A, double[] B)
	{		
		double[] result = new double[B.length];
		double n = 0.;
		double crit = 1e-6;
		Maths.show(A);
		Maths.show(B);
		for (int i = 0; i < B.length; i++)
		{
			
			if (Math.abs(A[i][i]) <= crit)
			{
				System.out.println("On " + i + " " + i + " Zero");
				int c = i;
				while (Math.abs(A[c][i]) <= crit)
					if (++c >= B.length)
					{
						System.out.println("On " + c + " " + i + " Zero");
						System.out.println("No answ");
						Maths.show(A);
						return B;
					}
				System.out.println("Changing " + A[c][i] + " with " + A[i][i]);
				double[] tmp = A[c];
				A[c] = A[i];
				A[i] = tmp;
				double tm = B[c];
				B[c] = B[i];
				B[i] = tm;
				Maths.show(A);
			}
			System.out.println("On " + i + " " + i + "  " + A[i][i]);
			for (int k = i; k + 1 < B.length; k++)
			{
				n = A[k + 1][i];
				if (n==0.)
					continue;
				for (int j = i; j < B.length; j++)
				{
					A[k+1][j] -=  A[i][j] * n / A[i][i];	
				}
				B[k+1] -=  B[i] * n / A[i][i];
			}
		}
		
		for (int i = B.length - 1; i >= 0; i--)
		{
			result[i]  = B[i] / A[i][i];
			for (int j = B.length - 1; j > i; j--)
				result[i] -= result[j] * A[i][j] / A[i][i];
		}
		System.out.println("res");
		Maths.show(result);
		return result;
	}
	
	/*private static double[][] makeJ(ArrayList<Function<double[], Double>> funcs, double[] args)
	{
		double[][] J = new double[funcs.size()][funcs.size()];
		
		for (int i = 0; i < J.length; i++)
		{
			for (int j = 0; j < J.length; j++)
				J[i][j] = Maths.derrivative(funcs.get(i), args, j);
		}
		
		return J;
	}*/
	
	private static double[][] makeJ(ConstraintManager cm, double[] args)
	{
		double[][] J = new double[cm.size()][cm.size()];
		
		for (int i = 0; i < J.length; i++)
		{
			for (int j = 0; j < J.length; j++)
				J[i][j] = cm.derrivative(args, i, j);
		}
		
		return J;
	}
	
	private static double[] makeF(ConstraintManager cm, double[] args)
	{
		double[] res = new double[cm.size()];
		for (int i = 0; i < res.length; i++)
			res[i] = -cm.derrivative(args, i);//-cm.aboba(i).apply(args);
		return res;
	}
	/*
	private static double[] makeF(ArrayList<Function<double[], Double>> funcs, double[] args)
	{
		double[] res = new double[funcs.size()];
		for (int i = 0; i < res.length; i++)
			res[i] = -funcs.get(i).apply(args);
		return res;
	}*/
	
	/*public static float[] newtonSolution(ArrayList<Function<float[], Float>> funcs) 
	{
		float[] res = new float[funcs.size()];
		float[] err = new float[res.length];
		float eps = 0.1f;
		int iterations = 0;
		int maxIters = 70;
		do
		{
			float[][] J = makeJ(funcs, res);
			err = solve(J, makeF(funcs, res));
			sum(res, err);
			iterations++;
		} while (arrayNorm(err) >= eps && iterations < maxIters);
		
		if (iterations >= maxIters)
			return new float[funcs.size()];
		
		/*System.out.println(iterations);
		if (iterations > 7)
		{
			System.out.println("Cant Solve This!");
			System.exit(-1);
			
		}*//*
		System.out.println(iterations);
		return res;
	}*/
	
	
	public static double[] newtonSolution(ConstraintManager cm) 
	{
		double[] res = new double[cm.size()];
		double[] err = new double[res.length];
		double eps = 0.001;
		int iterations = 0;
		int maxIters = 70;
		do
		{
			err = solve(cm.makeJ(res), cm.makeF(res));
			//err = solve(cm.makeJ(res), makeF(cm, res));
			sum(res, err);
			iterations++;
		} while (arrayNorm(err) >= eps && iterations < maxIters);
		
		System.out.println(iterations);
		
		if (iterations >= maxIters)
			return new double[cm.size()];
		return res;
	}
	
	public static void sum(double[] a, double[] b)
	{
		for (int i = 0; i < a.length; i++)
			a[i] += b[i];
	}
	
	public static double arrayNorm(double[] arr)
	{
		double sum = 0f;
		
		for (double f : arr)
		{
			sum += f * f;
		}
		return Math.sqrt(sum);
	}
}
