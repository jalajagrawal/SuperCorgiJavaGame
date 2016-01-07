package mvc.model;


import java.awt.*;
import java.util.Arrays;

import mvc.controller.Game;

public class Snow extends Sprite {

	
	private int nSpin;
	
	//radius of a large asteroid
	private final int RAD = 100;
	public static int FORWARD_SPEED = -12;
	
	//nSize determines if the Asteroid is Large (0), Medium (1), or Small (2)
	//when you explode a Large asteroid, you should spawn 2 or 3 medium asteroids
	//same for medium asteroid, you should spawn small asteroids
	//small asteroids get blasted into debris
	public Snow(int nSize){
		
		//call Sprite constructor
		super();

		setTeam(Team.FOE);

		//forward speed of snow
		setDeltaX(FORWARD_SPEED);

		// falling speed of snow
		int nDY = Game.R.nextInt(10);
		setDeltaY(nDY+3);
			
		assignRandomShape();
		
		//an nSize of zero is a big snow
		//a nSize of 1 or 2 is med or small asteroid respectively
		if (nSize == 0)
			setRadius(RAD);
		else
			setRadius(RAD/(nSize * 2));

        setCenter(new Point(getDim().width,getCenter().y));
		

	}

	public static void increaseSpeed() {
		FORWARD_SPEED -= 3;
		System.out.println(FORWARD_SPEED);
	}



    @Override
    public void draw(Graphics g){
        super.draw(g);
        g.setColor(Color.WHITE);
        g.fillPolygon(getXcoords(), getYcoords(), dDegrees.length);
    }
	
	public Snow(Snow snowed){
	

		//call Sprite constructor
		super();
		setTeam(Team.FOE);
		int  nSizeNew =	snowed.getSize() + 1;
		

		setSpin(nSpin);
			

		int nDX = Game.R.nextInt(10 + nSizeNew*2);

		// forward movement
        setDeltaX((nDX * FORWARD_SPEED)/10);

		//random delta-y
		int nDY = Game.R.nextInt(10+ nSizeNew*2);
		if(nDY %2 ==0)
			nDY = -nDY;
		setDeltaY(nDY);
			
		assignRandomShape();
		
		//an nSize of zero is a big snow
		//a nSize of 1 or 2 is med or small snow respectively

		setRadius(RAD/(nSizeNew * 2));
		setCenter(snowed.getCenter());
		
		
		

	}
	
	public int getSize(){
		
		int nReturn = 0;
		
		switch (getRadius()) {
			case 100:
				nReturn= 0;
				break;
			case 50:
				nReturn= 1;
				break;
			case 25:
				nReturn= 2;
				break;
		}
		return nReturn;
		
	}

	//overridden
	@Override
	public void move(){
		super.move();
		Point pnt = this.getCenter();
		if(pnt.x < 0 || pnt.y < 0 || pnt.y > getDim().height) {
			Cc.getInstance().getOpsList().enqueue(this, CollisionOp.Operation.REMOVE);
		}

	}

	

	public void setSpin(int nSpin) {
		this.nSpin = nSpin;
	}
	
	//making snow shape
	  public void assignRandomShape ()
	  {
	    int nSide = Game.R.nextInt( 13 ) + 7;
	    int nSidesTemp = nSide;

	    int[] nSides = new int[nSide];
	    for ( int nC = 0; nC < nSides.length; nC++ )
	    {
	      int n = nC * 48 / nSides.length - 4 + Game.R.nextInt( 8 );
	      if ( n >= 48 || n < 0 )
	      {
	        n = 0;
	        nSidesTemp--;
	      }
	      nSides[nC] = n;
	    }

	    Arrays.sort( nSides );

	    double[]  dDegrees = new double[nSidesTemp];
	    for ( int nC = 0; nC <dDegrees.length; nC++ )
	    {
	    	dDegrees[nC] = nSides[nC] * Math.PI / 24 + Math.PI / 2;
	    }
	   setDegrees( dDegrees);
	   
		double[] dLengths = new double[dDegrees.length];
			for (int nC = 0; nC < dDegrees.length; nC++) {
				if(nC %3 == 0)
				    dLengths[nC] = 1 - Game.R.nextInt(40)/100.0;
				else
					dLengths[nC] = 1;
			}
		setLengths(dLengths);

	  }

}
