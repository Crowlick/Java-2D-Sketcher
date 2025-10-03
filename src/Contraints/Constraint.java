package Contraints;

import java.util.ArrayList;

import entities.Point;

public abstract class Constraint {
	protected String _name = "Default Name";
	
	protected ArrayList<Point> _appliedTo = new ArrayList<Point>();
	
	public abstract double func(double[] vars);
	
	public String getName() {return new String(_name);}
	
	public ArrayList<Point> getChilds() {return _appliedTo;}
	
}
