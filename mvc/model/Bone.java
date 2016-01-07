package mvc.model;

import mvc.controller.Game;

import java.awt.*;
import java.util.ArrayList;

public class Bone extends Sprite {

	public static int eatCount = 0;
	private int nSpin;
	public static final int FORWARD_SPEED = -10;



	public static void ateOne(){
		eatCount--;
	}

	public static int getEatCount() {
		return eatCount;
	}

	public static void resetEatCount() {
		eatCount = 0;
	}

	public Bone() {

		super();
		setTeam(Team.FLOATER);
		ArrayList<Point> pntCs = new ArrayList<Point>();

		// drawing the bone
		pntCs.add(new Point(3,4));
		pntCs.add(new Point(5,5));
		pntCs.add(new Point(5,7));
		pntCs.add(new Point(3,8));
		pntCs.add(new Point(0,7));
		pntCs.add(new Point(-3,8));
		pntCs.add(new Point(-5,7));
		pntCs.add(new Point(-5,5));
		pntCs.add(new Point(-3,4));
		pntCs.add(new Point(-3,-4));
		pntCs.add(new Point(-5,-5));
		pntCs.add(new Point(-5,-7));
		pntCs.add(new Point(-3,-8));
		pntCs.add(new Point(0,-7));
		pntCs.add(new Point(3,-8));
		pntCs.add(new Point(5,-7));
		pntCs.add(new Point(5,-5));
		pntCs.add(new Point(3,-4));
		assignPolarPoints(pntCs);

		setExpire(250);
		setRadius(20);
		setColor(new Color(192,187,157));
		setDeltaX(15);
		setDeltaY(0);
		

		//random point on the screen
		setCenter(new Point(0,
				Game.DIM.height-Game.R.nextInt(Game.DIM.height-400)-100));

		//random orientation 
		 setOrientation(Game.R.nextInt(360));

	}

	public void move() {
		super.move();
		setOrientation(getOrientation() + getSpin());

		//adding expire functionality
		if (getExpire() == 0)
			Cc.getInstance().getOpsList().enqueue(this, CollisionOp.Operation.REMOVE);
		else
			setExpire(getExpire() - 1);


	}



	public int getSpin() {
		return this.nSpin;
	}


	@Override
	public void draw(Graphics g) {
		super.draw(g);
		//fill this polygon
		g.fillPolygon(getXcoords(), getYcoords(), dDegrees.length);
		//now draw a white border
		g.setColor(Color.WHITE);
		g.drawPolygon(getXcoords(), getYcoords(), dDegrees.length);
	}

}
