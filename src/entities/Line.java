package entities;

import org.lwjgl.util.vector.Vector3f;

import Contraints.ConstraintManager;
import renderEngine.Renderer;
import shaders.StaticShader;
import toolBox.Maths;
import toolBox.ModelProvider;

public class Line implements Renderable
{
	private Point _point1 = null;
	private Point _point2 = null;
	private Entity _joint = null;
	
	private float _rectangleScale = Point.getRadius() / 2f;
	
	private Vector3f _locVect1 = null;
	
	private Vector3f _locVect2 = null;
	
	private ConstraintManager _cm = new ConstraintManager();
	
	public double len() {return Vector3f.sub(_point1.getPos(), _point2.getPos(), null).length();}
	
	public Line(Vector3f p1, Vector3f p2) 
	{
		_point1 = new Point(p1, this);
		_point2 = new Point(p2, this);
		
		Vector3f.sub(p2, p1, _locVect1);
		
		Vector3f center = Vector3f.add(p2, p1, null);
		center.scale(0.5f);
		
		_locVect1 = Vector3f.sub(p1, center, null);
		_locVect2 = Vector3f.sub(p2, center, null);
		
		Vector3f vect = Vector3f.sub(p2, p1, null);
		float leng = vect.length() / 2f;
		
		vect.normalise(vect);
		float angle = (float)Math.asin(-vect.y);
		angle = (float)Math.atan2(vect.y, vect.x);
		_joint = new Entity(ModelProvider.getModel("Rectangle"), center, angle, new Vector3f(leng, _rectangleScale, _rectangleScale));
		_joint.setColor(new Vector3f(0, 0.5f, 0.5f));
		_joint.setBaseColor(new Vector3f(0, 0.5f, 0.5f));
	}
	
	public Line(Point p1, Point p2) 
	{
		_point1 = p1;
		_point2 = p2;
		_point1.attachTo(this);
		_point2.attachTo(this);
		
		Vector3f.sub(p2.getPos(), p1.getPos(), _locVect1);
		
		Vector3f center = Vector3f.add(p2.getPos(), p1.getPos(), null);
		center.scale(0.5f);
		
		_locVect1 = Vector3f.sub(p1.getPos(), center, null);
		_locVect2 = Vector3f.sub(p2.getPos(), center, null);
		
		Vector3f vect = Vector3f.sub(p2.getPos(), p1.getPos(), null);
		float leng = vect.length() / 2f;
		
		vect.normalise(vect);
		float angle = (float)Math.asin(-vect.y);
		angle = (float)Math.atan2(vect.y, vect.x);
		_joint = new Entity(ModelProvider.getModel("Rectangle"), center, angle, new Vector3f(leng, _rectangleScale, _rectangleScale));
		_joint.setColor(new Vector3f(0, 0.5f, 0.5f));
		_joint.setBaseColor(new Vector3f(0, 0.5f, 0.5f));
	}
	
	public void update(float scale)
	{
		_point1.getPos().scale(scale);
		_point2.getPos().scale(scale);
		update();
	}
	
	public void update()
	{
		Vector3f center = Vector3f.add(_point2.getPos(), _point1.getPos(), null);
		center.scale(0.5f);
		_joint.setPos(center);
		_locVect1 = Vector3f.sub(_point1.getPos(), center, null);
		_locVect2 = Vector3f.sub(_point2.getPos(), center, null);
		
		Vector3f vect = Vector3f.sub(_point2.getPos(), _point1.getPos(), null);
		float leng = vect.length() / 2f;
		_joint.setScale(new Vector3f(leng, _rectangleScale, _rectangleScale));
		
		vect.normalise(vect);
		float angle = (float)Math.asin(-vect.y);
		angle = (float)Math.atan2(vect.y, vect.x);
		_joint.setRot(angle);
	}
	
	public Vector3f getPos() 
	{
		return _point1.getPos();
	}
	
	public void setConstraintManager(ConstraintManager newCm)
	{
		_cm = newCm;
	}
	
	@Override
	public void resetColor()
	{
		_joint.resetColor();
	}
	
	@Override
	public void setPos(Vector3f newPos)
	{
		_joint.setPos(newPos);
		_point1.setPos(Vector3f.add(newPos, _locVect1, null));
		_point2.setPos(Vector3f.add(newPos, _locVect2, null));
	}
	
	@Override
	public void setColor(Vector3f newColor)
	{
		_joint.setColor(newColor);
	}
	
	@Override
	public Renderable underMouse(Vector3f mousePos)
	{
		if (checkPart(_point1, mousePos) != null)
			return _point1;
		
		if (checkPart(_point2, mousePos) != null)
			return _point2;
		
		Vector3f proj = Maths.projectPointOnSegment(_point1._pos, _point2._pos, mousePos);
		
		if (proj == null)
			return null;
		
		if (Vector3f.sub(proj, mousePos, null).length() <= 0.005f)
		{
			return this;
		}
		
		return null;
	}
	
	private Entity checkPart(Entity part, Vector3f coords)
	{
		if (Vector3f.sub(part.getPos(), coords, null).length() <= 0.01f)
		{
			return part;
		}
		return null;
	}
	
	@Override
	public void render(Renderer renderer, StaticShader shader) {
		renderer.render(_point1, shader);
		renderer.render(_point2, shader);
		renderer.render(_joint, shader);
	}

	@Override
	public void scale(float f) {
		update(f);
	}

	@Override
	public Point pointUnderMouse(Vector3f mousePos) {
		if (checkPart(_point1, mousePos) != null)
			return _point1;
		
		if (checkPart(_point2, mousePos) != null)
			return _point2;
		return null;
	}

	@Override
	public Line lineUnderMouse(Vector3f mousePos) {
Vector3f proj = Maths.projectPointOnSegment(_point1._pos, _point2._pos, mousePos);
		
		if (proj == null)
			return null;
		
		if (Vector3f.sub(proj, mousePos, null).length() <= 0.005f)
		{
			return this;
		}
		
		return null;
	}
	
	public Point getStart() {return _point1;}
	public Point getEnd() {return _point2;}

	@Override
	public void resetBaseColor(Vector3f newBaseColor) {
		_joint.resetBaseColor(newBaseColor);
		
	}
	
}
