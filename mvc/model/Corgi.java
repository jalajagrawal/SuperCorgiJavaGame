package mvc.model;

import mvc.controller.Game;

import java.awt.*;
import java.util.ArrayList;


public class Corgi extends Sprite {

	// ==============================================================
	// FIELDS 
	// ==============================================================
	
	public static final double THRUST = 1.5;


	

	private boolean bFlame = false;
	
	private boolean bThrusting = false;

			
	private final double[] FLAME = { 23 * Math.PI / 24 + Math.PI / 2,
			Math.PI + Math.PI / 2, 25 * Math.PI / 24 + Math.PI / 2 };

	private int[] nXFlames = new int[FLAME.length];
	private int[] nYFlames = new int[FLAME.length];

	private Point[] pntFlames = new Point[FLAME.length];
    public double[] dLengthsAlts;
    public double[] dDegreesAlts;

	
	// ==============================================================
	// CONSTRUCTOR 
	// ==============================================================
	
	public Corgi() {
		super();
		setTeam(Team.FRIEND);
		ArrayList<Point> pntCs = new ArrayList<Point>();


        // white part
        pntCs.add(new Point(0,-8));
        pntCs.add(new Point(1,-9));
        pntCs.add(new Point(2,-7));
        pntCs.add(new Point(3,-5));
        pntCs.add(new Point(3,-2));
        pntCs.add(new Point(2,1));
        pntCs.add(new Point(1,3));
        pntCs.add(new Point(2,4));
        pntCs.add(new Point(5,5));
        pntCs.add(new Point(7,4));
        pntCs.add(new Point(10,4));
        pntCs.add(new Point(10,5));
        pntCs.add(new Point(9,7));
        pntCs.add(new Point(8,8));
        pntCs.add(new Point(8,11));
        pntCs.add(new Point(9,12));
        pntCs.add(new Point(11,14));
        pntCs.add(new Point(9,15));
        pntCs.add(new Point(6,14));
        pntCs.add(new Point(3,15));
        pntCs.add(new Point(0,14));
        pntCs.add(new Point(0,13));
        pntCs.add(new Point(2,13));
        pntCs.add(new Point(4,13));
        pntCs.add(new Point(5,11));
        pntCs.add(new Point(5,10));
        pntCs.add(new Point(4,9));
        pntCs.add(new Point(4,7));
        pntCs.add(new Point(2,6));
        pntCs.add(new Point(0,4));
        pntCs.add(new Point(-4,7));
        pntCs.add(new Point(-5,8));
        pntCs.add(new Point(-7,8));
        pntCs.add(new Point(-8,6));
        pntCs.add(new Point(-6,4));
        pntCs.add(new Point(-5,2));
        pntCs.add(new Point(-5,-4));
        pntCs.add(new Point(-6,-5));
        pntCs.add(new Point(-7,-6));
        pntCs.add(new Point(-7,-7));
        pntCs.add(new Point(-6,-8));
        pntCs.add(new Point(-5,-9));
        pntCs.add(new Point(-2,-9));
		assignPolarPoints(pntCs);




		setColor(new Color(234,183,121));
		
		//put corgi in the middle.
		setCenter(new Point(Game.DIM.width / 2, Game.DIM.height / 2));
		
		//with random orientation
		setOrientation(0);
		
		//this is the size of the corgi
		setRadius(50);



        // yellow part
        ArrayList<Point> pntAs = new ArrayList<Point>();
        pntAs.add(new Point(0,4));
        pntAs.add(new Point(1,3));
        pntAs.add(new Point(2,6));
        pntAs.add(new Point(10,17));
        pntAs.add(new Point(10,17));
        pntAs.add(new Point(5,12));
        pntAs.add(new Point(5,12));
        pntAs.add(new Point(4,15));
        pntAs.add(new Point(2,15));
        pntAs.add(new Point(0,14));
        pntAs.add(new Point(0,14));
        pntAs.add(new Point(2,14));
        pntAs.add(new Point(-5,14));
        pntAs.add(new Point(-6,13));
        pntAs.add(new Point(-8,11));
        pntAs.add(new Point(-10,10));
        pntAs.add(new Point(-8,9));
        pntAs.add(new Point(-11,7));
        pntAs.add(new Point(-8,5));
        pntAs.add(new Point(-8,-3));
        pntAs.add(new Point(-9,-4));
        pntAs.add(new Point(-7,-5));
        pntAs.add(new Point(-10,-8));
        pntAs.add(new Point(-5,-9));
        pntAs.add(new Point(-6,-8));
        pntAs.add(new Point(-7,-7));
        pntAs.add(new Point(-7,-6));
        pntAs.add(new Point(-6,-5));
        pntAs.add(new Point(-5,-4));
        assignPolorPointsAlts(pntAs);

    }
    public void drawAlt(Graphics g) {
        setXcoords( new int[dDegreesAlts.length]);
        setYcoords( new int[dDegreesAlts.length]);
        setObjectPoints( new Point[dDegrees.length]);

        for (int nC = 0; nC < dDegreesAlts.length; nC++) {

            setXcoord((int) (getCenter().x + getRadius()
                    * dLengthsAlts[nC]
                    * Math.sin(Math.toRadians(getOrientation()) + dDegreesAlts[nC])), nC);


            setYcoord((int) (getCenter().y - getRadius()
                    * dLengthsAlts[nC]
                    * Math.cos(Math.toRadians(getOrientation()) + dDegreesAlts[nC])), nC);
            //need this line of code to create the points which we will need for debris
            setObjectPoint( new Point(getXcoord(nC), getYcoord(nC)), nC);
        }

        g.setColor(Color.white);
        g.fillPolygon(getXcoords(), getYcoords(), dDegreesAlts.length);
    }

    //assign for alt imag
    protected void assignPolorPointsAlts(ArrayList<Point> pntCs) {
        dDegreesAlts = convertToPolarDegs(pntCs);
        dLengthsAlts = convertToPolarLens(pntCs);

    }
	
	
	// ==============================================================
	// METHODS 
	// ==============================================================

	public void move() {
        Point pnt = getCenter();
        double dX = pnt.x + getDeltaX();
        double dY = pnt.y + getDeltaY();

        //this just keeps the sprite inside the bounds of the frame
        setCenter(new Point((int) dX, (int) dY));


		if (bThrusting) {
			bFlame = true;

			double dAdjustY = 1.0
					* THRUST;
			setDeltaX(0);
			setDeltaY(getDeltaY() + -dAdjustY);
		} else {
            setDeltaY(getDeltaY() + THRUST);
        }


	} //end move

	public void thrustOn() {
		bThrusting = true;
	}

	public void thrustOff() {
		bThrusting = false;
		bFlame = false;
	}



	public void draw(Graphics g) {

		Color colShip = (new Color(234,183,121));


		//thrusting
		if (bFlame) {
			g.setColor(Color.cyan);
			//the flame
			for (int nC = 0; nC < FLAME.length; nC++) {
				if (nC % 2 != 0) //odd
				{
					pntFlames[nC] = new Point((int) (getCenter().x + 2
							* getRadius()
							* Math.sin(Math.toRadians(getOrientation())
									+ FLAME[nC])), (int) (getCenter().y - 2
							* getRadius()
							* Math.cos(Math.toRadians(getOrientation())
									+ FLAME[nC])));

				} else //even
				{
					pntFlames[nC] = new Point((int) (getCenter().x + getRadius()
							* 1.1
							* Math.sin(Math.toRadians(getOrientation())
									+ FLAME[nC])),
							(int) (getCenter().y - getRadius()
									* 1.1
									* Math.cos(Math.toRadians(getOrientation())
											+ FLAME[nC])));

				} //end even/odd else

			} //end for loop

			for (int nC = 0; nC < FLAME.length; nC++) {
				nXFlames[nC] = pntFlames[nC].x;
				nYFlames[nC] = pntFlames[nC].y;

			}

			g.fillPolygon(nXFlames, nYFlames, FLAME.length);

		} //end if flame


        drawAlt(g);
        drawShipWithColor(g,colShip);


	} //end draw()




	public void drawShipWithColor(Graphics g, Color col) {
		super.draw(g);

        // draw yellow part of corgi
		g.setColor(col);
		g.drawPolygon(getXcoords(), getYcoords(), dDegrees.length);
        g.fillPolygon(getXcoords(), getYcoords(), dDegrees.length);

        // draw eyes
        g.setColor(Color.black);
        g.fillOval((int) (getCenter().x+0.65
                * getRadius()
        ), (int) (getCenter().y - 0.30
                * getRadius()),6,8);

        g.fillOval((int) (getCenter().x+0.4
                * getRadius()
        ), (int) (getCenter().y - 0.30
                * getRadius()),6,8);



    }




	
} //end class
