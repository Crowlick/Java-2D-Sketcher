package Contraints;


import org.lwjgl.util.vector.Vector3f;

import entities.Line;

public class Horisontal extends Constraint {

	private Line _l1;
	
	
	public Horisontal(Line l1)
	{
		_name = "Coin X";
		_appliedTo.add(l1.getStart());
		_appliedTo.add(l1.getEnd());
		_l1 = l1;
	}

	@Override
	public double func(double[] vars) {
		Vector3f p1 = _l1.getStart().getPos();
		Vector3f p2 = _l1.getEnd().getPos();
		
		return p1.y + vars[1] - p2.y - vars[3];
	}

}
