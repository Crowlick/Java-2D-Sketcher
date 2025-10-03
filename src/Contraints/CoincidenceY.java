package Contraints;


import entities.Point;

public class CoincidenceY extends Constraint {

	private Point _p1;
	private Point _p2;
	
	
	public CoincidenceY(Point p1, Point p2)
	{
		_name = "Coin Y";
		_appliedTo.add(p1);
		_appliedTo.add(p2);
		_p1 = p1;
		_p2 = p2;
	}

	@Override
	public double func(double[] vars) {
		return _p1.getPos().y + vars[1] - _p2.getPos().y - vars[3];
	}

}
