package Contraints;

import org.lwjgl.util.vector.Vector3f;

import entities.Line;

public class EqualLength extends Constraint {

	private Line _l1;
	private Line _l2;
	
	public EqualLength(Line l1, Line l2)
	{
		_name = new String("Equal Length");
		_l1 = l1;
		_appliedTo.add(l1.getStart());
		_appliedTo.add(l1.getEnd());
		_l2 = l2;
		_appliedTo.add(l2.getStart());
		_appliedTo.add(l2.getEnd());
	}
	
	
	@Override
	public double func(double[] vars) {
		Vector3f p1 = _l1.getStart().getPos();
		Vector3f p2 = _l1.getEnd().getPos();
		
		Vector3f p3 = _l2.getStart().getPos();
		Vector3f p4 = _l2.getEnd().getPos();
		
		double v1 = p1.x + vars[0] - p2.x - vars[2];
		double v2 = p1.y + vars[1] - p2.y - vars[3];
		
		double v3 = p3.x + vars[4] - p4.x - vars[6];
		double v4 = p3.y + vars[5] - p4.y - vars[7];
		
		return v1 * v1 + v2 * v2 - v3 * v3 - v4 * v4;
	}
}
