package protocol.resources;

import java.io.Serializable;
import java.util.ArrayList;

import communication.DeckResource;

public abstract class Player implements Serializable, Cloneable, Runnable {
	private static final long serialVersionUID = 3416634269924164243L;
	public String pname;
	public DeckResource deck;
	public ArrayList<String> inputQueue = new ArrayList<>();
	public ArrayList<Object> commQueue = new ArrayList<>();
	
	/**
	 * default constructor for Player
	 * defines the player's name
	 * @param name
	 */
	public Player(String name) {
		pname = name;
	}
	
	/**
	 * returns the oldest input that hasn't been removed
	 * if no input is found, this function returns an empty String
	 * to avoid NullPointerExceptions
	 * @return String
	 */
	public String getInput() {
		try {
			return inputQueue.remove(0);
		}catch (Exception e) {
			return "";
		}
	}
}
