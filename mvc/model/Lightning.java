package mvc.model;

import mvc.controller.Game;

import java.awt.*;
import java.util.ArrayList;

public class Lightning extends Sprite {


	public Lightning() {

		super();
		setTeam(Team.FRIEND);
		//defined the points on a cartesean grid
		ArrayList<Point> pntCs = new ArrayList<Point>();


		pntCs.add(new Point(3,0));
		pntCs.add(new Point(3,20));
		pntCs.add(new Point(-15,4));
		pntCs.add(new Point(0,10));
		pntCs.add(new Point(0,0));
		pntCs.add(new Point(0,-10));
		pntCs.add(new Point(-20,-5));
		pntCs.add(new Point(3,-5));


		assignPolarPoints(pntCs);
		

		setRadius(Game.DIM.width);

		//everything is relative to the corgi that fired the bubble bullet
		setDeltaX(0);
		setDeltaY(0);
		setCenter(new Point(Game.DIM.width / 2, 40));

		// lining color
		setColor(Color.white);

	}


	
	@Override
	public void move() {

		super.move();

		if (getExpire() == 0)
			Cc.getInstance().getOpsList().enqueue(this, CollisionOp.Operation.REMOVE);

		else
			setExpire(getExpire() - 1);

	}
	
	@Override
	public void draw(Graphics g){

		super.draw(g);
		g.setColor(Color.yellow);
		g.fillPolygon(getXcoords(), getYcoords(), dDegrees.length);
	}
	
	





}
