package entities;

import org.lwjgl.util.vector.Vector3f;

import renderEngine.Renderer;
import shaders.StaticShader;

public interface Renderable 
{
	public void render(Renderer renderer, StaticShader shader);
	
	public void scale(float f);
	
	public Renderable underMouse(Vector3f mousePos);
	
	public Point pointUnderMouse(Vector3f mousePos);
	
	public Line lineUnderMouse(Vector3f mousePos);
	
	public void setColor(Vector3f newColor);
	
	public void setPos(Vector3f newPos);
	
	public void resetColor();
	
	public void update();
	
	public void resetBaseColor(Vector3f newBaseColor);
	
}
