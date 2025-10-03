package Contraints;

import org.lwjgl.util.vector.Vector3f;

import entities.Point;

public class FixedX extends Constraint {

	private Point _p;
	private float _x;
	private Vector3f _scale;
	
	
	public FixedX(Point p, float x, Vector3f scale)
	{
		_name = "Fixed X";
		_appliedTo.add(p);
		_p = p;
		_x = x / scale.z;
		_scale = scale;
	}

	@Override
	public double func(double[] vars) {
		return _p.getPos().x + vars[0] - _x * _scale.z;
	}

}
