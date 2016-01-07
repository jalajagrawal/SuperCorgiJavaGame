package mvc.model;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Cloud extends Sprite {

    private final int MAX_EXPIRE = getDim().width/12;

    //for drawing alternative shapes
    //you could have more than one of these sets so that your sprite morphs into various shapes
    //throughout its life
    private int radius;

    public Cloud() {

        super();
        setTeam(Team.FOE);
        //defined the points on a cartesean grid
        ArrayList<Point> pntCs = new ArrayList<Point>();

        // base point
        pntCs.add(new Point(6,0));

        assignPolarPoints(pntCs);




        //a cruis missile expires after 25 frames
        setExpire(MAX_EXPIRE);
        this.radius = numberGenerator(getDim().height/4);
        setRadius(this.radius);

        //everything is relative to the corgi that fired the bubble
        setDeltaX(Snow.FORWARD_SPEED);
        setDeltaY(0);
        setDeltaY(0);

        setCenter(new Point(-10,40));

        setColor(Color.DARK_GRAY);

    }
    /**
     * Generates a number from 0 to max
     * @return a random integer from 0 to max
     */
    public static int numberGenerator(int max) {
        Random rand = new Random();
        int num = rand.nextInt(max);
        if (num == 0) {
            return((int) (max*1.1));
        } else {
            return Math.max(num,60);
        }

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

        // draw cloud (i.e. large circle)
        super.draw(g);
        g.fillPolygon(getXcoords(), getYcoords(), dDegrees.length);
        g.setColor(Color.WHITE);
        g.fillOval((getCenter().x-this.radius), (getCenter().y-this.radius),this.radius*2,this.radius*2);


    }





}
