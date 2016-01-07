package mvc.model;

import java.awt.*;
import java.util.ArrayList;


public class Bullet extends Sprite {

	  private final double FIRE_POWER = 35.0;

	 
	
public Bullet(Corgi corg){
		
		super();
	    setTeam(Team.FRIEND);
		
		//defined the points on a cartesean grid
		ArrayList<Point> pntCs = new ArrayList<Point>();

		// draws the bubble
		pntCs.add(new Point(0,2)); //top point
		pntCs.add(new Point(1,2));
		pntCs.add(new Point(2,1));
		pntCs.add(new Point(2,0));
		pntCs.add(new Point(2,-1));
		pntCs.add(new Point(1,-2));
		pntCs.add(new Point(0,-2));
		pntCs.add(new Point(-1,-2));
		pntCs.add(new Point(-2,-1));
		pntCs.add(new Point(-2,0));
		pntCs.add(new Point(-2,1));
		pntCs.add(new Point(-1,2));

		assignPolarPoints(pntCs);

		//a bullet expires after 20 frames
	    setExpire( 20 );
	    setRadius(15);
	    

	    //everything is relative to the falcon ship that fired the bullet
	    setDeltaX( corg.getDeltaX() +
	               Math.cos( Math.toRadians( corg.getOrientation() ) ) * FIRE_POWER );
	    setDeltaY( corg.getDeltaY() +
	               Math.sin( Math.toRadians( corg.getOrientation() ) ) * FIRE_POWER );
	    setCenter( corg.getCenter() );




	}

	//implementing the expire functionality in the move method - added by Dmitriy
	public void move(){

		super.move();

		if (getExpire() == 0)
			Cc.getInstance().getOpsList().enqueue(this, CollisionOp.Operation.REMOVE);
		else
			setExpire(getExpire() - 1);

	}


	@Override
	public void draw(Graphics g) {
		super.draw(g);
		//fill this polygon
		g.setColor(new Color(32,148,202));
		g.fillPolygon(getXcoords(), getYcoords(), dDegrees.length);
		//now draw a white border
		g.setColor(Color.white);
		g.drawPolygon(getXcoords(), getYcoords(), dDegrees.length);
	}

}
