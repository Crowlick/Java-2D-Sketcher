package entities;

import org.lwjgl.util.vector.Vector3f;

import Contraints.ConstraintManager;
import toolBox.ModelProvider;

public class Point extends Entity {
	private static float _radius = 0.005f;
	private Line _attached = null;
	
	private ConstraintManager _cm = new ConstraintManager();
	
	public Point(float x, float y)
	{
		super(ModelProvider.getModel("Circle"), new Vector3f(x, y, 0f), 0f, new Vector3f(1f, 1f, 1f));
		_color = new Vector3f(1f, 0f, 0f);
		//System.out.println("Created at " + _pos);
		_model = ModelProvider.getModel("Circle");
		_scale.scale(_radius);
	}
	
	public Point(Vector3f pos)
	{
		super(ModelProvider.getModel("Circle"), new Vector3f(pos.x, pos.y, 0f), 0f, new Vector3f(1f, 1f, 1f));
		_color = new Vector3f(1f, 0f, 0f);
		_model = ModelProvider.getModel("Circle");
		_scale.scale(_radius);
	}
	
	public Point(Vector3f pos, Line line)
	{
		super(ModelProvider.getModel("Circle"), new Vector3f(pos.x, pos.y, 0f), 0f, new Vector3f(1f, 1f, 1f));
		_color = new Vector3f(1f, 0f, 0f);
		_model = ModelProvider.getModel("Circle");
		_scale.scale(_radius);
		_attached = line;
	}
	
	public static float getRadius() {return _radius;}
	
	public Line getParent() {return _attached;}
	
	public void attachTo(Line line) {_attached = line;}
	
	
	public void setConstraintManager(ConstraintManager newCm)
	{
		_cm = newCm;
		if (_attached != null)
		{
			_attached.setConstraintManager(newCm);
			Point p1 = _attached.getStart();
			if (this == p1)
				_attached.getEnd()._cm = newCm;
				//if (_attached.getEnd().getCM() != newCm)
					//_attached.getEnd().setConstraintManager(newCm);
			else
				p1._cm = newCm;
			//	if (p1.getCM() != newCm)
					//p1.setConstraintManager(newCm);
		}
	}
	
	public ConstraintManager getCM() {return _cm;}
	
	@Override
	public void increasePos(Vector3f dr)
	{
		super.increasePos(dr);
		//if (_attached != null)
		//	_attached.update();
	}
	
	@Override
	public Point pointUnderMouse(Vector3f mousePos)
	{
		if (Vector3f.sub(_pos, mousePos, null).length() <= 0.01)
			return this;
		return null;
	}
	
	
	@Override
	public void setPos(Vector3f newPos)
	{
		super.setPos(newPos);
		//_cm.makeOptimal();
	//	update();
	}
	
	@Override
	public void update()
	{
		if (_attached != null)
			_attached.update();
	}
}
