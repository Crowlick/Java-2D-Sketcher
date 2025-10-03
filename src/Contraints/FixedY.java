package Contraints;

import org.lwjgl.util.vector.Vector3f;

import entities.Point;

public class FixedY extends Constraint {

	private Point _p;
	private float _y;
	private Vector3f _scale;
	
	public FixedY(Point p, float y, Vector3f scale)
	{
		_name = "Fixed Y";
		_appliedTo.add(p);
		_p = p;
		_y = y / scale.z;
		_scale = scale;
	}
	
	@Override
	public double func(double[] vars) {
		return _p.getPos().y + vars[1] - _y * _scale.z;
	}
}
