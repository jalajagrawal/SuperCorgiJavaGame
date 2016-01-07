package mvc.view;

import mvc.controller.Game;
import mvc.model.Cc;
import mvc.model.Corgi;
import mvc.model.Movable;
import mvc.model.Bone;

import java.awt.*;
import java.util.ArrayList;


public class GamePanel extends Panel {
	
	// ==============================================================
	// FIELDS 
	// ============================================================== 
	 
	// The following "off" vars are used for the off-screen double-bufferred image. 
	private Dimension dimOff;
	private Image imgOff;
	private Graphics grpOff;
	
	private GameFrame gmf;
	private Font fnt = new Font("SansSerif", Font.BOLD, 12);
	private Font fntBig = new Font("SansSerif", Font.BOLD + Font.ITALIC, 36);
	private FontMetrics fmt; 
	private int nFontWidth;
	private int nFontHeight;
	private String strDisplay = "";

	

	// ==============================================================
	// CONSTRUCTOR 
	// ==============================================================
	
	public GamePanel(Dimension dim){
	    gmf = new GameFrame();
		gmf.getContentPane().add(this);
		gmf.pack();
		initView();
		gmf.setTitle("SUPER CORGI");
		gmf.setSize(dim);
		//gmf.setTitle("Game Base");
		gmf.setResizable(false);
		gmf.setVisible(true);
		this.setFocusable(true);
	}
	
	
	// ==============================================================
	// METHODS 
	// ==============================================================
	
	private void drawScore(Graphics g) {
		g.setColor(Color.white);
		g.setFont(fnt);

		// plots the little boxes that show up when player eats a bone

		if (!Cc.getInstance().isGameOver()) {
			g.drawString("CURRENT LEVEL: " + Cc.getInstance().getLevel(),nFontWidth, Game.DIM.height - 2*nFontHeight-30);

			g.drawString("LIGHTNING STRIKES AVAILABLE :  ",nFontWidth, Game.DIM.height - 2*nFontHeight-15);
			int j = 10;
			for (int i = 0; i < Bone.eatCount; i++) {
				g.setColor(Color.yellow);
				g.fillRect(nFontWidth+200+j,Game.DIM.height - 2*nFontHeight-25, 10, 10);
				j+= 15;
			}
		} else {
			Bone.resetEatCount();
		}


		g.setColor(Color.white);
		if (Cc.getInstance().getScore() != 0) {

			// prints the current score for the player
			g.drawString("SCORE :  " + Cc.getInstance().getScore(),nFontWidth, Game.DIM.height - 2*nFontHeight);

		} else {

			// doesn't print anything if no score is detected
			g.drawString("NO SCORE", nFontWidth, Game.DIM.height - 2*nFontHeight);

		}
	}
	
	@SuppressWarnings("unchecked")
	public void update(Graphics g) {
		if (grpOff == null || Game.DIM.width != dimOff.width
				|| Game.DIM.height != dimOff.height) {
			dimOff = Game.DIM;
			imgOff = createImage(Game.DIM.width, Game.DIM.height);
			grpOff = imgOff.getGraphics();
		}

		// Fill in background with a nice sky blue.
		grpOff.setColor(new Color(15,228,241));
		grpOff.fillRect(0, 0, Game.DIM.width, Game.DIM.height);

		drawScore(grpOff);
		
		if (!Cc.getInstance().isPlaying()) {
			displayTextOnScreen();
		} else if (Cc.getInstance().isPaused()) {
			strDisplay = "Game Paused";
			grpOff.drawString(strDisplay,
					(Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4);
		}
		
		//playing and not paused!
		else {
			
			//draw them in decreasing level of importance
			//friends will be on top layer and debris on the bottom
			iterateMovables(grpOff,
					(ArrayList<Movable>)  Cc.getInstance().getMovFriends(),
					(ArrayList<Movable>)  Cc.getInstance().getMovFoes(),
					(ArrayList<Movable>)  Cc.getInstance().getMovFloaters(),
					(ArrayList<Movable>)  Cc.getInstance().getMovDebris());


			drawNumberLivesLeft(grpOff);
			if (Cc.getInstance().isGameOver()) {
				Cc.getInstance().setPlaying(false);
				//bPlaying = false;
			}
		}
		//draw the double-Buffered Image to the graphics context of the panel
		g.drawImage(imgOff, 0, 0, this);
	} 


	
	//for each movable array, process it.
	private void iterateMovables(Graphics g, ArrayList<Movable>...movMovz){
		
		for (ArrayList<Movable> movMovs : movMovz) {
			for (Movable mov : movMovs) {

				mov.move();
				mov.draw(g);

			}
		}
		
	}
	

	// Draw the number of lives left on the bottom-right of the screen.
	private void drawNumberLivesLeft(Graphics g) {

		g.setColor(Color.white);
		g.drawString("LIVES LEFT: " +Cc.getInstance().getNumCorgis() ,
				(int) ((Game.DIM.width)*0.85), (int) ((Game.DIM.height)*0.95));
	}
	
	private void initView() {
		Graphics g = getGraphics();			// get the graphics context for the panel
		g.setFont(fnt);						// take care of some simple font stuff
		fmt = g.getFontMetrics();
		nFontWidth = fmt.getMaxAdvance();
		nFontHeight = fmt.getHeight();
		g.setFont(fntBig);					// set font info
	}
	
	// This method draws some text to the middle of the screen before/after a game
	private void displayTextOnScreen() {

		strDisplay = "SUPERCORGI: Help the corgi fly through the winter sky!";
		grpOff.drawString(strDisplay,
				(Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4-40);

		strDisplay = "GAME OVER";
		grpOff.drawString(strDisplay,
				(Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4);

		strDisplay = "UP ARROW KEY: FLY HIGHER (like in Flappy Bird)";
		grpOff.drawString(strDisplay,
				(Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4
						+ nFontHeight + 40);

		strDisplay = "SPACEBAR: SHOOT BUBBLE (HIT THE SNOW! SNOW WILL HURT YOU)";
		grpOff.drawString(strDisplay,
				(Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4
						+ nFontHeight + 80);

		strDisplay = "F: CAST LIGHTNING";
		grpOff.drawString(strDisplay,
				(Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4
						+ nFontHeight + 120);

		strDisplay = "Q: QUIT GAME";
		grpOff.drawString(strDisplay,
				(Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4
						+ nFontHeight + 160);

		strDisplay = "Brown Bone: 1 Lightning Cast";
		grpOff.drawString(strDisplay,
				(Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4
						+ nFontHeight + 200);
		strDisplay = "Heart: 1 Extra Life";
		grpOff.drawString(strDisplay,
				(Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4
						+ nFontHeight + 240);

		strDisplay = "S: START GAME";
		grpOff.drawString(strDisplay,
				(Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4
						+ nFontHeight + 280);

	}

}