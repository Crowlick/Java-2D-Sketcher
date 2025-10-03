package Contraints;


import entities.Point;

public class CoincidenceX extends Constraint {

	private Point _p1;
	private Point _p2;
	
	
	public CoincidenceX(Point p1, Point p2)
	{
		_name = "Coin X";
		_appliedTo.add(p1);
		_appliedTo.add(p2);
		_p1 = p1;
		_p2 = p2;
	}

	@Override
	public double func(double[] vars) {
		return _p1.getPos().x + vars[0] - _p2.getPos().x - vars[2];
	}

}
