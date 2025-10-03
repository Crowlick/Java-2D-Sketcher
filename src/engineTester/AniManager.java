package engineTester;

import java.awt.Button;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JDialog;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import renderEngine.DisplayManager;
import renderEngine.OBJLoader;

// ********************************** AreYouSure***************************
public class AniManager extends JDialog implements ActionListener,KeyListener,ChangeListener
{
	private static final long serialVersionUID = 1L;
	Button b1,b2,b3,b4,b5,b6,b7,b8,b9,b10,b11,b12, b13, b14,b15,b16,b17,b18,b19,b20,b21;
	JSlider animationSlider;
	
	private Thread _a = null;
	private Visualiser _vis = null;
	private boolean _isSliderDragged = false;
	private static final float _incr = 0.2f / DisplayManager.getFPS();

	public AniManager(String config, String data, String config_path, Float dt)
	{
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		//super(Map.win,"Подтверждаете ? ",true);
		setLayout(null);
		//setResizable(false);
		//setModal(true);
				
		b1=new Button(">");
		b1.setSize(20,20);
		b1.setLocation(10,10);
		b1.addActionListener(this);
		//b1.addKeyListener(this);
			  
		b2=new Button("||");
		b2.setSize(20,20);
		b2.setLocation(40,10);
		b2.addActionListener(this);
		b2.addKeyListener(this);
			  
		b3=new Button("+");
		b3.setSize(20,20);
		b3.setLocation(10,40);
		b3.addActionListener(this);
		b3.addKeyListener(this);
			  
		b4=new Button("-");
		b4.setSize(20,20);
		b4.setLocation(40,40);
		b4.addActionListener(this);
		b4.addKeyListener(this);	
		
		
		b5=new Button("^");
		b5.setSize(20,20);
		b5.setLocation(25,70);
		b5.addKeyListener(this);
		b5.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e)
			{
				_vis.setCameraMoveTrue(1);
				//_vis.addPoint((float)Math.random(), (float)Math.random());
			/*	System.out.println("ber");
				_vis.setSpawn();
				System.out.println("end");*/
			}
			@Override
			public void mouseReleased(MouseEvent e)
			{
				_vis.setCameraMoveFalse();
			}
		});
		
		b6=new Button("<-");
		b6.setSize(20,20);
		b6.setLocation(10,100);
		b6.addKeyListener(this);
		b6.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e)
			{
				_vis.setCameraMoveTrue(2);
			}
			@Override
			public void mouseReleased(MouseEvent e)
			{
				_vis.setCameraMoveFalse();
			}
		});
		
		b7=new Button("->");
		b7.setSize(20,20);
		b7.setLocation(40,100);
		b7.addKeyListener(this); 
		b7.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e)
			{
				_vis.setCameraMoveTrue(3);
			}
			@Override
			public void mouseReleased(MouseEvent e)
			{
				_vis.setCameraMoveFalse();
			}
		});
		
		b8=new Button("v");
		b8.setSize(20,20);
		b8.setLocation(25,130);
		b8.addKeyListener(this);
		b8.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e)
			{
				_vis.setCameraMoveTrue(4);
			}
			@Override
			public void mouseReleased(MouseEvent e)
			{
				_vis.setCameraMoveFalse();
			}
		});
		
		b9=new Button("New Lines");
		b9.setSize(80,20);
		b9.setLocation(10,160);
		b9.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e)
			{
				//_vis.setCameraMoveTrue(5);
				_vis.setSpawnLine(0);
			}
			@Override
			public void mouseReleased(MouseEvent e)
			{
				//_vis.setCameraMoveFalse();
			}
		});
		
		b10=new Button("New Point");
		b10.setSize(70,20);
		b10.setLocation(10,190);
		b10.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e)
			{
			//_vis.setSpawn();
				PointInputDialog dialog = new PointInputDialog(null); 
		        dialog.setVisible(true); // Показываем окно и ждем, пока пользователь его закроет

		        // После того как окно закрыто, проверяем, нажал ли пользователь "OK"
		        if (dialog.isConfirmed()) {
		            float x = dialog.getXCoordinate();
		            float y = dialog.getYCoordinate();

		            // Теперь вызываем метод для создания точки с полученными координатами
		            // Вам нужно будет создать такой метод в классе Visualiser
		            _vis.addPoint(x, y); 
		        }
			}
			@Override
			public void mouseReleased(MouseEvent e)
			{
				_vis.setCameraMoveFalse();
			}
		});
			  
		b11=new Button("New Line 2");
		b11.setSize(80,20);
		b11.setLocation(70,10);
		b11.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e)
			{
				_vis.setSpawnLine(2);
			}
			@Override
			public void mouseReleased(MouseEvent e)
			{
				_vis.setCameraMoveFalse();
			}
		});
		
		b12=new Button("Point On Line");
		b12.setSize(80,20);
		b12.setLocation(70,40);
		b12.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e)
			{
				_vis.reset();
				_vis.addConstraint(0);
			}
			@Override
			public void mouseReleased(MouseEvent e)
			{
				//_vis.setCameraMoveFalse();
			}
		});
		
		b13=new Button("Parallelism");
		b13.setSize(80,20);
		b13.setLocation(70,70);
		b13.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e)
			{
				_vis.reset();
				_vis.addConstraint(1);
			}
			@Override
			public void mouseReleased(MouseEvent e)
			{
				//_vis.setCameraMoveFalse();
			}
		});
		
		b14=new Button("Fixed Lenght");
		b14.setSize(80,20);
		b14.setLocation(70,100);
		b14.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e)
			{
				_vis.reset();
				_vis.addConstraint(2);
			}
			@Override
			public void mouseReleased(MouseEvent e)
			{
				//_vis.setCameraMoveFalse();
			}
		});
		
		b15=new Button("Fix Point");
		b15.setSize(80,20);
		b15.setLocation(70,130);
		b15.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e)
			{
				_vis.reset();
				_vis.addConstraint(3);
			}
			@Override
			public void mouseReleased(MouseEvent e)
			{
				//_vis.setCameraMoveFalse();
			}
		});
		
		b16=new Button("Equal Length");
		b16.setSize(80,20);
		b16.setLocation(160,10);
		b16.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e)
			{
				_vis.reset();
				_vis.addConstraint(4);
			}
			@Override
			public void mouseReleased(MouseEvent e)
			{
				//_vis.setCameraMoveFalse();
			}
		});
		
		b17=new Button("Coincidence");
		b17.setSize(80,20);
		b17.setLocation(160,40);
		b17.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e)
			{
				_vis.reset();
				_vis.addConstraint(5);
			}
			@Override
			public void mouseReleased(MouseEvent e)
			{
				//_vis.setCameraMoveFalse();
			}
		});
		
		b18=new Button("Perpendicular");
		b18.setSize(80,20);
		b18.setLocation(160,70);
		b18.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e)
			{
				_vis.reset();
				_vis.addConstraint(6);
			}
			@Override
			public void mouseReleased(MouseEvent e)
			{
				//_vis.setCameraMoveFalse();
			}
		});
		
		b19=new Button("Horisontal");
		b19.setSize(80,20);
		b19.setLocation(160,100);
		b19.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e)
			{
				_vis.reset();
				_vis.addConstraint(7);
			}
			@Override
			public void mouseReleased(MouseEvent e)
			{
				//_vis.setCameraMoveFalse();
			}
		});
		
		b20=new Button("Verical");
		b20.setSize(80,20);
		b20.setLocation(160,130);
		b20.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e)
			{
				_vis.reset();
				_vis.addConstraint(8);
			}
			@Override
			public void mouseReleased(MouseEvent e)
			{
				//_vis.setCameraMoveFalse();
			}
		});
		
		b21=new Button("Angle");
		b21.setSize(80,20);
		b21.setLocation(160,160);
		b21.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e)
			{
				/*PointInputDialog dialog = new PointInputDialog(null); 
		        dialog.setVisible(true); 
		        if (dialog.isConfirmed()) {
		            float x = dialog.getXCoordinate();
		            float y = dialog.getYCoordinate();
		            _vis.reset();
		            _vis.setValue(x);
					_vis.addConstraint(9);
		        }*/
				_vis.reset();
				_vis.addConstraint(9);
			}
			@Override
			public void mouseReleased(MouseEvent e)
			{
				//_vis.setCameraMoveFalse();
			}
		});
		
		
		setLocation(150,300);
		setSize(150,260);
		
		animationSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 0);
		animationSlider.setBounds(10, 220, 120, 30);
		animationSlider.addChangeListener(this);
		animationSlider.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e)
			{
				_isSliderDragged = true;
			}
			@Override
			public void mouseReleased(MouseEvent e)
			{
				_isSliderDragged = false;
			//	_pause = false;
			}
		});
		
		setLocation(20, 300);
        setSize(300, 280);
		add(b1);add(b2);add(b3);add(b4);add(b5);add(b6);add(b7);add(b8);add(b9);
		add(b10);add(b11);add(b12);add(b13);add(b14);add(b15);add(b16);add(b17);
		add(b18);add(b19);add(b20);add(b21);
		add(animationSlider);
		
		OBJLoader.init(config_path);
		_vis = new Visualiser();
		_vis.an = this;
		_vis.config = config;
		_vis.data = data;
		
		_a = new Thread(_vis);
		_a.setDaemon(true);
		_a.start();
		setVisible(true);
		/*try
		{
			_a.join();
		}catch (InterruptedException e)
		{
			e.printStackTrace();
		}*/

	}
	
	public void close() 
	{
		/*try
		{
			_a.join();
		}catch (InterruptedException e)
		{
			e.printStackTrace();
		}*/
		dispose();
	}
	
	public void setAnimationSliderMax(int maxFrame) {
        if (animationSlider != null) {
            animationSlider.setMaximum(maxFrame);
        }
    }
	
	public void setAnimationSliderMin(int minFrame) {
        if (animationSlider != null) {
            animationSlider.setMaximum(minFrame);
        }
    }
	
	public void actionPerformed(ActionEvent e)
	{

		if(e.getActionCommand().equals("^")){ 
			System.out.println("Up !!");
		}
		if(e.getActionCommand().equals("->")){ 
			System.out.println("Rigth !!");
		}
		if(e.getActionCommand().equals("<-")){ 
			System.out.println("Left !!");
		}
		if(e.getActionCommand().equals("v")){ 
			System.out.println("Down !!");
		}
		if(e.getActionCommand().equals("Closer")){ 
			System.out.println("forward !!");
		}
		if(e.getActionCommand().equals("Farther")){ 
			System.out.println("backward!!");
		}
		if(e.getActionCommand().equals("Fit")){ 
			_vis.resetCamera();
		}
		//  setVisible(false);
		
	}
	 
	public void keyPressed(KeyEvent e)
	{
	//	char a=e.getKeyChar();
		//int in=e.getKeyCode();
		//Object oo=e.getSource();
		//String ttt= (String)oo.toString();
		//System.out.println("ttt="+ttt+"  code="+in);
		//if ((ttt.lastIndexOf(">") !=-1) && (a=='\n')) ;
		//if ((ttt.lastIndexOf("NO") !=-1) && (in==27)) ;
		// setVisible(false);	
	}
	
	public void keyTyped(KeyEvent e){}
	public void keyReleased(KeyEvent e){}

	@Override
	public void stateChanged(ChangeEvent e) {
		JSlider source = (JSlider)e.getSource();
		if (source == animationSlider)
			if (_isSliderDragged);
				//System.out.println(source.getValue());
	}
	
	public void updSlider(int val)
	{
		animationSlider.setValue(val);
	}

}

