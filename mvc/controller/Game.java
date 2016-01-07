package mvc.controller;

import mvc.model.*;
import mvc.view.GamePanel;
import sounds.Sound;

import javax.sound.sampled.Clip;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.util.Random;

// ===============================================
// == This Game class is the CONTROLLER
// ===============================================

public class Game implements Runnable, KeyListener {

	// ===============================================
	// FIELDS
	// ===============================================

	public static final Dimension DIM = new Dimension(900, 750); //the dimension of the game.
	private GamePanel gmpPanel;
	public static Random R = new Random();
	public final static int ANI_DELAY = 45; // milliseconds between screen
											// updates (animation)
	private Thread thrAnim;
	private int nLevel = 1;
	private int nTick = 0;
    private int background = 0;
    private static int BACKGROUND_CLOUD = 5;
    private int counter = 10;
    private int counting = 0;
	private int LEVEL = 800;
	private int level_tracker = 0;
	private boolean started = false;

	private boolean bMuted = false;


	private final int PAUSE = 80, // p key
			QUIT = 81, // q key
			UP = 38, // thrust; up arrow
			START = 83, // s key
			FIRE = 32, // space key
			MUTE = 77, // m-key mute
			SPECIAL = 70; 					// fire special weapon;  F key

	private Clip clpThrust;
	private Clip clpMusicBackground;

	private static final int SPAWN_BONE = 50;
	private static final int SPAWN_HEART = 110;



	// ===============================================
	// ==CONSTRUCTOR
	// ===============================================

	public Game() {

		gmpPanel = new GamePanel(DIM);
		gmpPanel.addKeyListener(this);
		clpThrust = Sound.clipForLoopFactory("whitenoise.wav");
		//http://www.orangefreesounds.com/8-bit-music/
		clpMusicBackground = Sound.clipForLoopFactory("bgm.wav");


	}

	// ===============================================
	// ==METHODS
	// ===============================================

	public static void main(String args[]) {
		EventQueue.invokeLater(new Runnable() { // uses the Event dispatch thread from Java 5 (refactored)
					public void run() {
						try {
							Game game = new Game(); // construct itself
							game.fireUpAnimThread();

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
	}

	private void fireUpAnimThread() { // called initially
		if (thrAnim == null) {
			thrAnim = new Thread(this); // pass the thread a runnable object (this)
			thrAnim.start();
		}
	}

	// implements runnable - must have run method
	public void run() {

		// lower this thread's priority; let the "main" aka 'Event Dispatch'
		// thread do what it needs to do first
		thrAnim.setPriority(Thread.MIN_PRIORITY);

		// and get the current time
		long lStartTime = System.currentTimeMillis();

		// this thread animates the scene
		while (Thread.currentThread() == thrAnim) {

			spawnBones();
			spawnHearts();
			gmpPanel.update(gmpPanel.getGraphics()); // update takes the graphics context we must
														// surround the sleep() in a try/catch block
														// this simply controls delay time between
														// the frames of the animation

			//this might be a good place to check for collisions
			checkCollisions();

			//only use timers if game has started, isnt over, and isnt paused
			if (started && !(Cc.getInstance().isGameOver()) && !(Cc.getInstance().isPaused())) {
				checkNewLevel();
				Cc.getInstance().addScore(1*nLevel);
				// spawn the snows
				if (counting  == counter) {
					spawnSnows(1);
					counting = 0;
				} else {
					counting++;
				}
				tick();

				// add clouds at the top
				addClouds();
			}




			try {
				// The total amount of time is guaranteed to be at least ANI_DELAY long.  If processing (update)
				// between frames takes longer than ANI_DELAY, then the difference between lStartTime -
				// System.currentTimeMillis() will be negative, then zero will be the sleep time
				lStartTime += ANI_DELAY;
				Thread.sleep(Math.max(0,
						lStartTime - System.currentTimeMillis()));
			} catch (InterruptedException e) {
				// just skip this frame -- no big deal
				continue;
			}
		} // end while
	} // end run


	private void checkNewLevel() {
		level_tracker++;
		if (level_tracker%LEVEL == 0) {
			Snow.increaseSpeed();
			nLevel++;
			level_tracker = 0;
		}
		Cc.getInstance().setLevel(nLevel);
	}


	private void checkCollisions() {

		Point pntFriendCenter, pntFoeCenter;
		int nFriendRadiux, nFoeRadiux;

		// check all the friends
		for (Movable movFriend : Cc.getInstance().getMovFriends()) {

            // corgi edge detection
            Point pnt = Cc.getInstance().getCorgi().getCenter();

			// checking corgi collision
            if ((movFriend instanceof Corgi) ) {
                //this just keeps the sprite inside the bounds of the frame
                if (pnt.y > Cc.getInstance().getCorgi().getDim().height || pnt.y < 0) {
					Cc.getInstance().getOpsList().enqueue(movFriend, CollisionOp.Operation.REMOVE);
					Cc.getInstance().spawnCorgi(false);
                }
            }

			// check all the foes
			for (Movable movFoe : Cc.getInstance().getMovFoes()) {

				pntFriendCenter = movFriend.getCenter();
				pntFoeCenter = movFoe.getCenter();
				nFriendRadiux = movFriend.getRadius();
				nFoeRadiux = movFoe.getRadius();

				//detect collision
				if (pntFriendCenter.distance(pntFoeCenter) < (nFriendRadiux + nFoeRadiux)) {

					//corgi
					if ((movFriend instanceof Corgi) ){

							Cc.getInstance().getOpsList().enqueue(movFriend, CollisionOp.Operation.REMOVE);
							Cc.getInstance().spawnCorgi(false);

					}
					//not corgi
					else {
                        killFoe(movFoe);
						Cc.getInstance().getOpsList().enqueue(movFriend, CollisionOp.Operation.REMOVE);
                        Cc.getInstance().addScore(100);
					}//end else

					//kill snow if hit
                    if (!(movFoe instanceof Cloud)) {
                        killFoe(movFoe);
                    }

					Sound.playSound("kapow.wav");

				}//end if
			}//end inner for
		}//end outer for


		//check for collisions between corgi and floaters
		if (Cc.getInstance().getCorgi() != null){
			// get corgi and floater points
			Point pntCorgCenter = Cc.getInstance().getCorgi().getCenter();
			int nCorgRadiux = Cc.getInstance().getCorgi().getRadius();
			Point pntFloaterCenter;
			int nFloaterRadiux;

			// check all the floaters
			for (Movable movFloater : Cc.getInstance().getMovFloaters()) {
				pntFloaterCenter = movFloater.getCenter();
				nFloaterRadiux = movFloater.getRadius();

				//detect collision
				if (pntCorgCenter.distance(pntFloaterCenter) < (nCorgRadiux + nFloaterRadiux)) {

					// if the floater is a bone, increase count (for lightnings) and remove floater
					if(movFloater instanceof Bone) {
						Bone.eatCount++;
						Cc.getInstance().getOpsList().enqueue(movFloater, CollisionOp.Operation.REMOVE);
						Sound.playSound("pacman_eatghost.wav");

						// if floater is a heart, increase life and remove floater
					} else if (movFloater instanceof Heart) {
						Cc.getInstance().addNumCorgis();
						Cc.getInstance().getOpsList().enqueue(movFloater, CollisionOp.Operation.REMOVE);
						Sound.playSound("pacman_eatghost.wav");
					}


				}//end if
			}//end inner for


		}//end if not null



		//we are dequeuing the opsList and performing operations in serial to avoid mutating the movable arraylists while iterating them above
		while(!Cc.getInstance().getOpsList().isEmpty()){
			CollisionOp cop =  Cc.getInstance().getOpsList().dequeue();
			Movable mov = cop.getMovable();
			CollisionOp.Operation operation = cop.getOperation();

			switch (mov.getTeam()){
				case FOE:
					if (operation == CollisionOp.Operation.ADD){
						Cc.getInstance().getMovFoes().add(mov);
					} else {
						Cc.getInstance().getMovFoes().remove(mov);
					}
					break;
				case FRIEND:
					if (operation == CollisionOp.Operation.ADD){
						Cc.getInstance().getMovFriends().add(mov);
					} else {
						Cc.getInstance().getMovFriends().remove(mov);
					}
					break;

				case FLOATER:
					if (operation == CollisionOp.Operation.ADD){
						Cc.getInstance().getMovFloaters().add(mov);
					} else {
						Cc.getInstance().getMovFloaters().remove(mov);
					}
					break;

				case DEBRIS:
					if (operation == CollisionOp.Operation.ADD){
						Cc.getInstance().getMovDebris().add(mov);
					} else {
						Cc.getInstance().getMovDebris().remove(mov);
					}
					break;


			}

		}
		//a request to the JVM is made every frame to garbage collect, however, the JVM will choose when and how to do this
		System.gc();

	}//end meth


	private void killFoe(Movable movFoe) {

		if (movFoe instanceof Snow){

			//we know this is a snow, so we can cast without threat of ClassCastException
            Snow snowed = (Snow)movFoe;

			if(snowed.getSize() == 1){
				//spawn larger snowss
				Cc.getInstance().getOpsList().enqueue(new Snow(snowed), CollisionOp.Operation.ADD);
				Cc.getInstance().getOpsList().enqueue(new Snow(snowed), CollisionOp.Operation.ADD);

			}

           Cc.getInstance().getOpsList().enqueue(movFoe, CollisionOp.Operation.REMOVE);
		}

		//remove the original Foe
	}




	//some methods for timing events in the game,
	//such as the appearance of UFOs, floaters (power-ups), etc.
	public void tick() {
		if (nTick == Integer.MAX_VALUE)
			nTick = 0;
		else
			nTick++;
	}

	public int getTick() {
		return nTick;
	}

	private void spawnBones() {
		//make the appearance of power-up dependent upon ticks and levels
		//the higher the level the more frequent the appearance
		if (nTick % (SPAWN_BONE) == 0) {
			Cc.getInstance().getOpsList().enqueue(new Bone(), CollisionOp.Operation.ADD);
		}
	}

	private void spawnHearts() {
		//make the appearance of power-up dependent upon ticks and levels
		//the higher the level the more frequent the appearance
		if (nTick % (SPAWN_HEART) == 0) {
			Cc.getInstance().getOpsList().enqueue(new Heart(), CollisionOp.Operation.ADD);
		}
	}

	// Called when user presses 's'
	private void startGame() {
		started = true;
		Cc.getInstance().clearAll();
		Cc.getInstance().initGame();
		Cc.getInstance().setLevel(0);
		Cc.getInstance().setPlaying(true);
		Cc.getInstance().setPaused(false);
		if (!bMuted)
		   clpMusicBackground.loop(Clip.LOOP_CONTINUOUSLY);
	}

	//this method spawns new asteroids
	private void spawnSnows(int nNum) {
		for (int nC = 0; nC < nNum; nC++) {
			//Asteroids with size of zero are big
            if (getTick()%5 == 0) {
                Cc.getInstance().getOpsList().enqueue(new Snow(1), CollisionOp.Operation.ADD);
            } else {
                Cc.getInstance().getOpsList().enqueue(new Snow(3), CollisionOp.Operation.ADD);
            }


		}
	}


	private boolean isLevelClear(){
		//if there are no more Asteroids on the screen
		boolean bSnowFree = true;

		for (Movable movFoe : Cc.getInstance().getMovFoes()) {
			if (movFoe instanceof Snow){
				bSnowFree = false;
				break;
			}
		}

		return bSnowFree;


	}

	// generates clouds at the top
    private void addClouds() {
        if (background == BACKGROUND_CLOUD) {
            Cc.getInstance().getOpsList().enqueue(new Cloud(), CollisionOp.Operation.ADD);
            background = 0;
        } else {
            background++;
        }

    }


	// Varargs for stopping looping-music-clips
	private static void stopLoopingSounds(Clip... clpClips) {
		for (Clip clp : clpClips) {
			clp.stop();
		}
	}

	// ===============================================
	// KEYLISTENER METHODS
	// ===============================================

	@Override
	public void keyPressed(KeyEvent e) {
		Corgi corg = Cc.getInstance().getCorgi();
		int nKey = e.getKeyCode();

		if (nKey == START && !Cc.getInstance().isPlaying())
			startGame();

		if (corg != null) {

			switch (nKey) {
			case PAUSE:
				Cc.getInstance().setPaused(!Cc.getInstance().isPaused());
				if (Cc.getInstance().isPaused())
					stopLoopingSounds(clpMusicBackground, clpThrust);
				else
					clpMusicBackground.loop(Clip.LOOP_CONTINUOUSLY);
				break;
			case QUIT:
				System.exit(0);
				break;
			case UP:
                corg.thrustOn();
				if (!Cc.getInstance().isPaused())
					clpThrust.loop(Clip.LOOP_CONTINUOUSLY);
				break;


			default:
				break;
			}
		}
	}



	@Override
	public void keyReleased(KeyEvent e) {
		Corgi corg = Cc.getInstance().getCorgi();
		int nKey = e.getKeyCode();


		if (corg != null) {
			switch (nKey) {
			case FIRE:
				Cc.getInstance().getOpsList().enqueue(new Bullet(corg), CollisionOp.Operation.ADD);
				Sound.playSound("laser.wav");
				break;

			//special does lightning strike
			case SPECIAL:
				if(Bone.getEatCount() > 0) {
					Bone.ateOne();
					Cc.getInstance().getOpsList().enqueue(new Lightning(), CollisionOp.Operation.ADD);
					// from http://soundbible.com/538-Blast.html
					Sound.playSound("lightning.wav");
				}
				break;

			case UP:
                corg.thrustOff();
				clpThrust.stop();
				break;

			case MUTE:
				if (!bMuted){
					stopLoopingSounds(clpMusicBackground);
					bMuted = !bMuted;
				}
				else {
					clpMusicBackground.loop(Clip.LOOP_CONTINUOUSLY);
					bMuted = !bMuted;
				}
				break;


			default:
				break;
			}
		}
	}

	@Override
	// Just need it b/c of KeyListener implementation
	public void keyTyped(KeyEvent e) {
	}

}


