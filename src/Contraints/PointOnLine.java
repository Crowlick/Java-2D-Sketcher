package Contraints;

import org.lwjgl.util.vector.Vector3f;

import entities.Line;
import entities.Point;

public class PointOnLine extends Constraint
{
	private Line _line;
	private Point _point;
	
	
	public PointOnLine(Line l, Point p) 
	{
		super();
		_name = "Point on Line";
		_line = l;
		_point = p;
		_appliedTo.add(_line.getStart());
		_appliedTo.add(_line.getEnd());
		_appliedTo.add(_point);
	}
	
	public PointOnLine(Point p, Line l) 
	{
		super();
		_name = "Point on Line";
		_line = l;
		_point = p;
		_appliedTo.add(_line.getStart());
		_appliedTo.add(_line.getEnd());
		_appliedTo.add(_point);
	}
	
	
	@Override
	public double func(double[] vars) {
		Vector3f p1 = _line.getStart().getPos();
		Vector3f p2 = _line.getEnd().getPos();
		Vector3f p3 = _point.getPos();
		double v1 = p3.x + vars[4] - p1.x - vars[0];
		double v2 = p2.y + vars[3] - p3.y - vars[5];
		
		double v3 = p3.y + vars[5] - p1.y - vars[1];
		double v4 = p2.x + vars[2] - p3.x - vars[4];
		
		return v1 * v2 - v3 * v4;
	}
	
}
