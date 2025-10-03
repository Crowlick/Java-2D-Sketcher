package engineTester;


import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import Contraints.AngleBetween;
import Contraints.CoincidenceX;
import Contraints.CoincidenceY;
import Contraints.Constraint;
import Contraints.ConstraintManager;
import Contraints.EqualLength;
import Contraints.FixedLength;
import Contraints.FixedX;
import Contraints.FixedY;
import Contraints.Horisontal;
import Contraints.Parallelism;
import Contraints.Perpendicular;
import Contraints.PointOnLine;
import Contraints.Vertical;
import entities.Axes;
import entities.Camera;
import entities.Entity;
import entities.Line;
import entities.Point;
import entities.Renderable;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.Renderer;
import shaders.StaticShader;
import toolBox.Maths;
import toolBox.ModelProvider;

public class Visualiser implements Runnable {

	public String config;
	public String data;
	public AniManager an;
	public Camera _camera = null;
	private boolean _isCameraMoving = false;
	private int _cameraMoveDirection = 0;
	private Boolean isMousePressed = true;
	private Entity _selectedEntity = null;
	private Renderable _selectedComp = null;
	private ArrayList<Renderable> _sceneComponents =  new ArrayList<Renderable>();
	private Renderer renderer = null;
	private Boolean _hasToSpawnPoint = false; 
	private Boolean _hasToSpawnLine = false;
	private Boolean _addConstraint = false;
	private int _cType = -1;
	private ArrayList<ConstraintManager> cm = new ArrayList<ConstraintManager>();
	private Point p1;
	private Point p2;
	private Line l1;
	private Line l2;
	private float _value;
	private int status = -1;
	public void run()
	{
		DisplayManager.createDisplay();
		Loader loader = new Loader();
		ModelProvider.addModel("Rectangle", Maths.rectangleModel(loader));
		ModelProvider.addModel("Circle", Maths.circleModel(loader));

		StaticShader shader = new StaticShader();
		StaticShader shader2 = new StaticShader("/shaders/vertexShader3.txt", "/shaders/fragmentShader.txt");
		renderer = new Renderer(shader);
		Axes axes = new Axes(loader);

		_camera = new Camera();
		
		boolean showAxes = true;

		Vector3f v1 = new Vector3f(0.5f, 0.5f, 0f);
		Vector3f v2 = new Vector3f(0f, 0f, 0f);
		
		Vector3f v3 = new Vector3f(-0.5f, 0.5f, 0f);
		Vector3f v4 = new Vector3f(-1f, -0.2f, 0f);
		
		Line l1 = new Line(v1, v2);
		Line l2 = new Line(v3, v4);
		ArrayList<Line> lines = new ArrayList<Line>();
		
		lines.add(l1);
		lines.add(l2);
		
		for (Line l : lines)
			_sceneComponents.add(l);
		
		while (!Display.isCloseRequested())
		{
			_camera.move(_sceneComponents);
			_camera.rotate();
			renderer.prepare();
			
			shader.start();
			shader.loadViewMatrix(_camera);
			
			for (Renderable sc : _sceneComponents)
				sc.render(renderer, shader);
			
			//renderer.render(p1, shader);
			//renderer.render(p2, shader);
			
			shader.stop();
			
			if (showAxes)
			{
				axes.rotate(_camera.getRot2());
				shader2.start();
				renderer.render(axes, shader2);
				shader2.stop();
			}
			
			DisplayManager.updateDisplay();
			if (Display.wasResized())
				renderer.onWindowResize(shader);

			showAxes = setAxes(showAxes);
			
			if (_isCameraMoving)
				moveCamera();
			
			
			if (_hasToSpawnPoint)
				addPoint();
			else if (_hasToSpawnLine)
				addLine();
			
			if (_addConstraint)
				selectConstraint();
			
			//else
			//{
				checkMouseRelease();
				checkMousePress();
			//}
		}
		ModelProvider.clean();
		shader.cleanUP();
		shader2.cleanUP();
		loader.cleanUP();
		an.close();
		DisplayManager.closeDisplay();
	}
	
	private Vector3f getMouseScreenPos()
	{
		Vector3f mouseScreenPos = new Vector3f();
		float a1 = (2f * Mouse.getX() / Display.getWidth()) - 1f;
		float a2 = (2f * Mouse.getY() / Display.getHeight()) - 1f;
		mouseScreenPos.setX(a1);
		mouseScreenPos.setY(a2);
		mouseScreenPos.setZ(-0.2002f);
		return mouseScreenPos;
	}
	
	public void pointOnLine()
	{
		if (status == 0)
		{
			Vector3f mp = checkMouseRelease2();
			if (mp == null)
				return;
			l1 = getLine(mp);
			if (l1 != null)
			{
				l1.setColor(new Vector3f(0,1,1));
				status = 1;
				return;
			}
			p1 = getPoint(mp);
			if (p1 == null)
				return;
			p1.setColor(new Vector3f(0,1,1));
			status = 1;
			return;
		}
		
		if (status == 1)
		{
			if (l1 == null)
				l1 = getLine();
			
			if (p1 == null)
				p1 = getPoint();
			
			if (p1 == null || l1 == null)
				return;
			ConstraintManager ccm = new ConstraintManager(p1.getCM(), l1.getStart().getCM(), new PointOnLine(p1, l1));
			ccm.makeOptimal();
			reset();
			
		}
	}
	
	
	public void parallelism()
	{
		if (status == 0)
		{
			l1 = getLine();
			if (l1 == null)
				return;
			l1.setColor(new Vector3f(0,1,0));
			status++;
		}
		
		if (status == 1)
		{
			l2 = getLine();
			if (l2 == null)
				return;
			
			ConstraintManager ccm = new ConstraintManager(l1.getStart().getCM(), l2.getStart().getCM(), new Parallelism(l1, l2));
			ccm.makeOptimal();
			l1.resetColor();
			reset();
		}
		
	}
	
	public void perpendicular()
	{
		if (status == 0)
		{
			l1 = getLine();
			if (l1 == null)
				return;
			l1.setColor(new Vector3f(0,1,0));
			status++;
		}
		
		if (status == 1)
		{
			l2 = getLine();
			if (l2 == null)
				return;
			
			ConstraintManager ccm = new ConstraintManager(l1.getStart().getCM(), l2.getStart().getCM(), new Perpendicular(l1, l2));
			ccm.makeOptimal();
			l1.resetColor();
			reset();
		}
		
	}
	
	public void equalLength()
	{
		if (status == 0)
		{
			l1 = getLine();
			if (l1 == null)
				return;
			l1.setColor(new Vector3f(0,1,0));
			status++;
		}
		
		if (status == 1)
		{
			l2 = getLine();
			if (l2 == null || l1 == l2)
				return;
			
			ConstraintManager ccm = new ConstraintManager(l1.getStart().getCM(), l2.getStart().getCM(), new EqualLength(l1, l2));
			ccm.makeOptimal();
			l1.resetColor();
			reset();
		}
	}
	
	public void angle()
	{
		if (status == 0)
		{
			l1 = getLine();
			if (l1 == null)
				return;
			l1.setColor(new Vector3f(0,1,0));
			status++;
		}
		
		if (status == 1)
		{
			l2 = getLine();
			if (l2 == null || l1 == l2)
				return;
			
			PointInputDialog2 dialog = new PointInputDialog2(null); 
	        dialog.setVisible(true); 
	        if (dialog.isConfirmed()) 
	        {
	            float x = dialog.getXCoordinate();
	            _value = x;
	        
			
			ConstraintManager ccm = new ConstraintManager(l1.getStart().getCM(), l2.getStart().getCM(), new AngleBetween(l1, l2, Math.toRadians(_value)));
			ccm.makeOptimal();
	        }
			l1.resetColor();
			reset();
		}
		
	}
	
	public void fixedLength()
	{
		if (status == 0)
		{
			Vector3f mp = checkMouseRelease2();
			if (mp == null)
				return;
			l1 = getLine(mp);
			if (l1 == null)
			{
				p1 = getPoint(mp);
				if (p1 == null)
					return;
				p1.setColor(new Vector3f(0,1,1));
				status = 1;
			}
			else
			{
				PointInputDialog2 dialog = new PointInputDialog2(null); 
		        dialog.setVisible(true); 
		        if (dialog.isConfirmed()) 
		        {
		            float x = dialog.getXCoordinate();
		            _value = x;
				ConstraintManager ccm = new ConstraintManager(l1.getStart().getCM(), new FixedLength(l1, _value, _camera.getPos()));
				ccm.makeOptimal();
		        }
				reset();
			}
		}
		if (status == 1)
		{
			p2 = getPoint();
			if (p2 == null)
				return;
			PointInputDialog2 dialog = new PointInputDialog2(null); 
	        dialog.setVisible(true); 
	        if (dialog.isConfirmed()) 
	        {
	            float x = dialog.getXCoordinate();
	            _value = x;
			ConstraintManager ccm = new ConstraintManager(p1.getCM(), p2.getCM(), new FixedLength(p1, p2, _value, _camera.getPos()));
			ccm.makeOptimal();
	        }
			reset();
		}
	}
	
	public void vertical()
	{
		l1 = getLine();
		if (l1 == null)
			return;
		ConstraintManager ccm = new ConstraintManager(l1.getStart().getCM(), new Vertical(l1));
		ccm.makeOptimal();
		reset();

	}
	
	public void horisontal()
	{
		l1 = getLine();
		if (l1 == null)
			return;
		ConstraintManager ccm = new ConstraintManager(l1.getStart().getCM(), new Horisontal(l1));
		ccm.makeOptimal();
		reset();
	}
	
	public void fixPoint()
	{
		p1 = getPoint();
		if (p1 == null)
			return;
		ConstraintManager ccm = new ConstraintManager(p1.getCM(), new FixedX(p1, p1.getPos().x, _camera.getPos()));
		ccm = new ConstraintManager(p1.getCM(), new FixedY(p1, p1.getPos().y, _camera.getPos()));
		ccm.makeOptimal();
		p1.resetBaseColor(new Vector3f(0,0,0));
		reset();
	}
	
	public void coincedence()
	{
		if (status == 0)
		{
			p1 = getPoint();
			if (p1 == null)
				return;
				
			p1.setColor(new Vector3f(0f, 1f, 1f));
			status++;
		}
		else if (status == 1)
		{
			p2 = getPoint();
			if (p2 == null || p1 == p2)
				return;
			ConstraintManager ccm = new ConstraintManager(p1.getCM(), p2.getCM(), new CoincidenceX(p1, p2));
			ccm = new ConstraintManager(p1.getCM(), p2.getCM(), new CoincidenceY(p1, p2));
			ccm.makeOptimal();
			reset();
		}
	}
	
	public void reset()
	{
		if (p1 != null)
			p1.resetColor();
		p1 = null;
		if (p2 != null)
			p2.resetColor();
		p2 = null;
		if (l1 != null)
			l1.resetColor();
		l1 = null;
		if (l1 != null)
			l1.resetColor();
		l2 = null;
		_addConstraint = false;
		status = -1;
	}
	
	public void selectConstraint()
	{
		if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
		{
			reset();
		}
		
		switch (_cType)
		{
			case 0:
				pointOnLine();
				break;
			case 1:
				parallelism();
				break;
			case 2:
				fixedLength();
				break;
			case 3:
				fixPoint();
				break;
			case 4:
				equalLength();
				break;
			case 5:
				coincedence();
				break;
			case 6:
				perpendicular();
				break;
			case 7:
				horisontal();
				break;
			case 8:
				vertical();
				break;
			case 9:
				angle();
				break;
		}
	}
	
	
	private Point getPoint()
	{
		Point get = null;
		if (!checkMouseRelease())
			return null;
		Vector3f mouseScreenPos = getMouseWorldPos();
		for (Renderable r : _sceneComponents)
		{
			get = r.pointUnderMouse(mouseScreenPos);
			if (get != null)
				return get;
		}
		return null;
	}
	
	private Point getPoint(Vector3f mousePos)
	{
		Point get = null;
		for (Renderable r : _sceneComponents)
		{
			get = r.pointUnderMouse(mousePos);
			if (get != null)
				return get;
		}
		return null;
	}
	
	private Renderable getObject()
	{
		Renderable get = null;
		if (!checkMouseRelease())
			return null;
		Vector3f mouseWorldPos = getMouseWorldPos();
		for (Renderable r : _sceneComponents)
		{
			get = r.underMouse(mouseWorldPos);
			if (get != null)
				return get;
		}
		return null;
	}
	
	private Line getLine()
	{
		Line get = null;
		if (!checkMouseRelease())
			return null;
		Vector3f mouseWorldPos = getMouseWorldPos();
		for (Renderable r : _sceneComponents)
		{
			get = r.lineUnderMouse(mouseWorldPos);
			if (get != null)
				return get;
		}
		return null;
	}
	
	private Line getLine(Vector3f mousePos)
	{
		Line get = null;
		for (Renderable r : _sceneComponents)
		{
			get = r.lineUnderMouse(mousePos);
			if (get != null)
				return get;
		}
		return null;
	}
	
	private Vector3f getMouseWorldPos()
	{
		return Maths.getWorldPos(getMouseScreenPos(), _camera, renderer);
	}
	
	private Boolean checkMousePress() 
	{
		if (Mouse.isButtonDown(0) && isMousePressed)
		{
			if (p2 != null)
			{
				p2.resetColor();
				p2 = null;
			}
			isMousePressed = false;
			//return true;
			if (!_hasToSpawnLine && !_hasToSpawnPoint && !_addConstraint)
			{
					
				Vector3f mouseScreenPos = getMouseWorldPos();
				for (Renderable r : _sceneComponents)
				{
					p2 = r.pointUnderMouse(mouseScreenPos);
					if (p2 != null)
					{
						p2.setColor(new Vector3f(0f,0f,1f));
						break;
					}
				/*	_selectedComp = r.underMouse(mouseScreenPos);
					if (_selectedComp != null)
					{
						_selectedComp.setColor(new Vector3f(0f,0f,1f));
						break;
					}*/
				}
			}
		}
		float dr = new Vector3f(Mouse.getDX(), Mouse.getDY(), 0f).length();
		
		if (!_hasToSpawnLine && !_hasToSpawnPoint && Mouse.isButtonDown(0) && p2 != null && !_addConstraint && dr > 2)
			{
				Vector3f mouseScreenPos = getMouseWorldPos();
				 dr = Vector3f.sub(mouseScreenPos, p2.getPos(), null).length();
				//System.out.println(dr);
				if (dr < 0.01f)
					return true;
				//System.out.println(Mouse.getDX() + " " + Mouse.getDY());
				p2.setPos(mouseScreenPos);
				p2.getCM().makeOptimal();
				p2.update();
			}
		return false;
	}
	
	private Boolean checkMouseRelease() 
	{		
		if (!Mouse.isButtonDown(0) && !isMousePressed)
		{
			if (p2 != null)
			{
				p2.resetColor();
				p2 = null;
			}
			isMousePressed = true;
			return true;/*
			Vector3f mouseScreenPos = new Vector3f();
			float a1 = (2f * Mouse.getX() / Display.getWidth()) - 1f;
			float a2 = (2f *Mouse.getY() / Display.getHeight()) - 1f;
			mouseScreenPos.setX(a1);
			mouseScreenPos.setY(a2);
			mouseScreenPos.setZ(-0.2f);		
			int i = 0;
			while (i < lines.size() && (_selectedEntity = lines.get(i).checkMouse(_camera, ren, mouseScreenPos, new Vector3f(0,1,0))) == null)
				i++;
			System.out.println(_selectedEntity);*/
		}
		return false;
	}
	
	private Vector3f checkMouseRelease2() 
	{		
		if (!Mouse.isButtonDown(0) && !isMousePressed)
		{
			if (p2 != null)
			{
				p2.resetColor();
				p2 = null;
			}
			isMousePressed = true;
			return getMouseWorldPos();
		}
		return null;
	}
	
	public void addPoint(float x, float y)
	{
		float scale = _camera.getPos().z;
		_sceneComponents.add(new Point(scale * x, scale* y));
	}
	
	public void addLine()
	{
		if (status < 0)
			return;
		else if (status == 0)
		{
			p1 = addPoint();
			if (p1 != null)
				status++;
		}
		else if (status == 1 && p1 != null)
		{
			p2 = addPoint();
			if (p2 == null)
				return;
			_sceneComponents.remove(p1);
			_sceneComponents.remove(p2);
			_sceneComponents.add(new Line(p1, p2));
			status = -1;
			p1 = null;
			p2 = null;
			_hasToSpawnLine = false;
				
		}
		else if (status == 2)
		{
			p1 = getPoint();
			if (p1 == null)
				return;
			p1.setColor(new Vector3f(1,0,1));
			status = 3;
		}
		else if (status == 3)
		{
			p2 = getPoint();
			if (p2 == null)
				return;
			p1.resetColor();
			if (p1.getParent() != null)
				p1 = new Point(p1.getPos());
			else
				_sceneComponents.remove(p1);
			
			
			if (p2.getParent() != null)
				p2 = new Point(p2.getPos());
			else
				_sceneComponents.remove(p2);
			
			_sceneComponents.add(new Line(p1, p2));
			
			
			status = -1;
			p1 = null;
			p2 = null;
			_hasToSpawnLine = false;
				
			
		}
	}
	
	public void addConstraint(int type)
	{
		_cType = type;
		_addConstraint = true;
		status = 0;
	}
	
	public void setSpawn() {_hasToSpawnPoint = true;}
	public void setSpawnLine(int statuss) {_hasToSpawnLine = true; status = statuss;}
	
	public Point addPoint()
	{
		if (!checkMouseRelease())
			return null;
		Vector3f mouseWorldPos = getMouseWorldPos();
		Point p = new Point(mouseWorldPos);
		_sceneComponents.add(p);
		_hasToSpawnPoint = false;
		return p;
	}
	
	public static boolean setPause(boolean p)
	{
		if (Keyboard.isKeyDown(Keyboard.KEY_P))
			return true;
		if (Keyboard.isKeyDown(Keyboard.KEY_O))
			return false;
		return p;
	}
	
	public void moveCamera()
	{
		switch (_cameraMoveDirection)
		{
			case 1:
				_camera.moveUp();
				break;
			case 2:
				_camera.moveLeft();
				break;
			case 3:
				_camera.moveRight();
				break;
			case 4:
				_camera.moveDown();
				break;
			case 5:
				_camera.moveForward();
				break;
			case 6:
				_camera.moveBackward();
				break;
			default:
				break;
		}
	}
	
	public void setValue(float val) {_value = val;}
	
	public void setCameraMoveTrue(int direction) {_isCameraMoving = true; _cameraMoveDirection = direction;}
	
	public void setCameraMoveFalse() {_isCameraMoving = false;}
	
	public void resetCamera() {_camera.reset();}
	
	public static boolean setAxes(boolean p)
	{
		if (Keyboard.isKeyDown(Keyboard.KEY_J))
			return true;
		if (Keyboard.isKeyDown(Keyboard.KEY_K))
			return false;
		return p;
	}
}

