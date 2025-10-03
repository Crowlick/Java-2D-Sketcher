package entities;

import org.lwjgl.util.vector.Vector3f;

import renderEngine.RawModel;
import renderEngine.Renderer;
import shaders.StaticShader;

public class Entity implements Renderable
{
	protected RawModel _model = null;
	protected Vector3f _pos = new Vector3f(0f, 0f, 0f);
	protected float _rot;
	protected Vector3f _scale = new Vector3f(1f, 1f, 1f);
	protected Vector3f _color = new Vector3f(1f, 0f, 0f);
	private Vector3f _baseColor = new Vector3f(1f, 0f, 0f);
	public Entity(RawModel model, Vector3f pos, float rot, Vector3f scale)
	{
		_model = model;
		_pos = new Vector3f(pos);
		_rot = rot;
		_scale = new Vector3f(scale);
	}
	
	public Entity() {}
	
	public Entity(Vector3f pos, float rot, Vector3f scale)
	{
		_pos = new Vector3f(pos);
		_rot = rot;
		_scale = new Vector3f(scale);
	}
	
	public Entity(Vector3f pos, float rot, float scale)
	{
		_pos = new Vector3f(pos);
		_rot = rot;
		_scale = new Vector3f(scale, scale, scale);
	}
	
	public Entity(Vector3f pos) {_pos = new Vector3f(pos);}
	
	public void setModel(RawModel model) {_model = model;}
	
	public Entity(RawModel model)
	{
		_model = model;
	}
	
	public Entity(RawModel model, Vector3f pos, float rot, float scale)
	{
		_model = model;
		_pos = new Vector3f(pos);
		_rot = rot;
		_scale = new Vector3f(scale, scale, scale);
	}
	
	
	public void increasePos(Vector3f dv)
	{
		Vector3f.add(_pos, dv, _pos);
	}
	
	public void increaseRot(float dr)
	{
		_rot += dr;
	}
	
	public void setPos(Vector3f newPos) {_pos = new Vector3f(newPos);}
	
	public void setRot(float newRot) {_rot = newRot;}
	
	public void setScale(Vector3f newScale) {_scale =new Vector3f(newScale);}
	
	public void setColor(Vector3f newColor) {_color = new Vector3f(newColor);}
	
	public RawModel getModel() {return _model;}
	public Vector3f getPos() {return _pos;}
	public float getRot() {return _rot;}
	public Vector3f getScale() {return _scale;}
	public Vector3f getColor() {return _color;}
	
	public void setBaseColor(Vector3f newBC) {_baseColor = new Vector3f(newBC);}

	public void resetColor() {_color = new Vector3f(_baseColor);}
	
	@Override
	public Renderable underMouse(Vector3f mousePos) 
	{
		if (Vector3f.sub(_pos, mousePos, null).length() <= 0.01)
			return this;
		return null;
	}
	
	@Override
	public void update() 
	{
	}
	
	@Override
	public void render(Renderer renderer, StaticShader shader) 
	{
		renderer.render(this, shader);
	}

	@Override
	public void scale(float f) {
		_pos.scale(f);
	}

	@Override
	public Point pointUnderMouse(Vector3f mousePos) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Line lineUnderMouse(Vector3f mousePos) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void resetBaseColor(Vector3f newBaseColor) {
		_baseColor = new Vector3f(newBaseColor); 
		setColor(newBaseColor);
	}
	
}
