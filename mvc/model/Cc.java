package mvc.model;

import sounds.Sound;

import java.util.ArrayList;
import java.util.List;


public class Cc {

	private  int nNumCorgi;
	private  int nLevel;
	private  long lScore;
	private  Corgi corgi;
	private  boolean bPlaying;
	private  boolean bPaused;
	
	// These ArrayLists with capacities set
	private List<Movable> movDebris = new ArrayList<Movable>(300);
	private List<Movable> movFriends = new ArrayList<Movable>(100);
	private List<Movable> movFoes = new ArrayList<Movable>(200);
	private List<Movable> movFloaters = new ArrayList<Movable>(50);

	private GameOpsList opsList = new GameOpsList();

	//added by Dmitriy
	private static Cc instance = null;

	// Constructor made private - static Utility class only
	private Cc() {}


	public static Cc getInstance(){
		if (instance == null){
			instance = new Cc();
		}
		return instance;
	}


	public  void initGame(){
		setLevel(1);
		setScore(0);

		setNumCorgis(3);
        //setNum
		spawnCorgi(true);
	}
	
	// The parameter is true if this is for the beginning of the game, otherwise false
	// When you spawn a new falcon, you need to decrement its number
	public  void spawnCorgi(boolean bFirst) {
		if (getNumCorgis() != 0) {
            corgi = new Corgi();
			opsList.enqueue(corgi, CollisionOp.Operation.ADD);
			if (!bFirst)
			    setNumCorgis(getNumCorgis() - 1);
		}

		// http://www.online-convert.com/result/51771beccd82e77e5b136c966b4a83ee
		Sound.playSound("boop.wav");

	}

	public GameOpsList getOpsList() {
		return opsList;
	}


	public  void clearAll(){
		movDebris.clear();
		movFriends.clear();
		movFoes.clear();
		movFloaters.clear();
	}

	public  boolean isPlaying() {
		return bPlaying;
	}

	public  void setPlaying(boolean bPlaying) {
		this.bPlaying = bPlaying;
	}

	public  boolean isPaused() {
		return bPaused;
	}

	public  void setPaused(boolean bPaused) {
		this.bPaused = bPaused;
	}
	
	public  boolean isGameOver() {		//if the number of falcons is zero, then game over
		if (getNumCorgis() == 0) {
			return true;
		}
		return false;
	}

	public  int getLevel() {
		return nLevel;
	}

	public   long getScore() {
		return lScore;
	}

	public  void setScore(long lParam) {
		lScore = lParam;
	}

    public  void addScore(long lParam) {
        lScore += lParam;
    }

	public void setLevel(int n) {
		nLevel = n;
	}

	public  int getNumCorgis() {
		return nNumCorgi;
	}

	public  void addNumCorgis() {
		nNumCorgi++;
	}

	public  void setNumCorgis(int nParam) {
		nNumCorgi = nParam;
	}
	
	public  Corgi getCorgi(){
		return corgi;
	}


	public  List<Movable> getMovDebris() {
		return movDebris;
	}



	public  List<Movable> getMovFriends() {
		return movFriends;
	}



	public  List<Movable> getMovFoes() {
		return movFoes;
	}


	public  List<Movable> getMovFloaters() {
		return movFloaters;
	}




}
