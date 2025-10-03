package Contraints;

import org.lwjgl.util.vector.Vector3f;

import entities.Line;

public class AngleBetween extends Constraint
{
	private Line _l1;
	private Line _l2;
	
	private double _alpha = Math.PI / 3.;
	
	public AngleBetween(Line l1, Line l2, double angle)
	{
		super();
		_name = new String("Perpendicular");
		_appliedTo.add(l1.getStart());
		_appliedTo.add(l1.getEnd());
		_appliedTo.add(l2.getStart());
		_appliedTo.add(l2.getEnd());
		_l1 = l1;
		_l2 = l2;
		_alpha = angle;
	}



	@Override
	public double func(double[] vars) 
	{
		Vector3f p1 = _l1.getStart().getPos();
		Vector3f p2 = _l1.getEnd().getPos();
		Vector3f p3 = _l2.getStart().getPos();
		Vector3f p4 = _l2.getEnd().getPos();
		
		double v1 = p2.x + vars[2] - p1.x - vars[0];
		double v2 = p4.x + vars[6] - p3.x - vars[4];
		
		double v3 = p2.y + vars[3] - p1.y - vars[1];
		double v4 = p4.y + vars[7] - p3.y - vars[5];

		return (v1 * v2 + v3 * v4) - Math.sqrt(v1 * v1 + v3 * v3) *  Math.sqrt(v2 * v2 + v4 * v4) * Math.cos(_alpha);
	}
}
