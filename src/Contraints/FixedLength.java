package Contraints;

import org.lwjgl.util.vector.Vector3f;

import entities.Line;
import entities.Point;

public class FixedLength extends Constraint {

	private double _len = 1f;
	private Line _line = null;
	private Point _p1 = null;
	private Point _p2 = null;
	private Vector3f _scale;
	
	public FixedLength(Line line, float len, Vector3f scale)
	{
		_name = new String("Fixed Length");
		_line = line;
		_p1 = line.getStart();
		_p2 = line.getEnd();
		_appliedTo.add(_p1);
		_appliedTo.add(_p2);
		_len = len / scale.z;
		_scale = scale;
	}
	
	public FixedLength(Point p1, Point p2, double len, Vector3f scale)
	{
		_name = new String("Fixed Length");
		_p1 = p1;
		_p2 = p2;
		_appliedTo.add(_p1);
		_appliedTo.add(_p2);
		_len = len / scale.z;
		_scale = scale;
	}
	
	
	@Override
	public double func(double[] vars) {
		Vector3f p1 = _p1.getPos();
		Vector3f p2 = _p2.getPos();
		
		double v1 = p1.x + vars[0] - p2.x - vars[2];
		double v2 = p1.y + vars[1] - p2.y - vars[3];
		return v1 * v1 + v2 * v2 - _len * _len * _scale.z * _scale.z;
	}
}
