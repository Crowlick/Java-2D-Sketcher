package main;

import java.util.ArrayList;
import java.util.function.Function;

import org.lwjgl.util.vector.Vector3f;

import Contraints.AngleBetween;
import Contraints.ConstraintManager;
import Contraints.FixedLength;
import Contraints.FixedX;
import Contraints.FixedY;
import Contraints.Parallelism;
import Contraints.PointOnLine;
import engineTester.AniManager;
import entities.Line;
import entities.Point;
import toolBox.Maths;
import toolBox.Solver;


public class Main {
		
	public static void main(String[] args) {
		AniManager anim = new AniManager("src/data/1.txt", "src/data/2.txt", "./", 0.1f);	
		
		
		Vector3f v1 = new Vector3f(0.5f, 0.5f, 0f);
		Vector3f v2 = new Vector3f(0f, 0f, 0f);
		
		Vector3f v3 = new Vector3f(-0.5f, 0.5f, 0f);
		Vector3f v4 = new Vector3f(-1f, -0.2f, 0f);
		
		
		Line l1 = new Line(v1, v2);
		Line l2 = new Line(v3, v4);
		
		AngleBetween aa = new AngleBetween(l1, l2, Math.PI/6.);
		
		ConstraintManager man = new ConstraintManager(l1.getStart().getCM(), l2.getStart().getCM(), aa);
		man.makeOptimal();
		man.makeOptimal();
		System.out.println(aa.func(new double[] {0, 0, 0, 0, 0, 0, 0, 0}));
	}
	
	public static Float f1(float[] x) {return 3f * x[0] * x[0] + (float)Math.sin(x[1]);}
	public static Float f2(float[] x) {return x[0] + (float)Math.pow(Math.abs(x[1]), 3f/2f) - 5f;}
}
