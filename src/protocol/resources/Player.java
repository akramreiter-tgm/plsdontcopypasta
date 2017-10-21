package protocol.resources;

import java.io.Serializable;
import java.util.ArrayList;

import debugMonitor.Panel;

public class Player implements Serializable, Cloneable {
	private static final long serialVersionUID = 3416634269924164243L;
	public String pName;
	public ArrayList<String> inputQueue = new ArrayList<>();
	public ArrayList<Object> animQueue = new ArrayList<>(); 
	
	public Player(String name) {
		pName = name;
	}
	
	/*
	 * returns the oldest input that hasn't been removed
	 * if no input is found, this function returns an empty String
	 * to avoid NullPointerExceptions
	 */
	public String getInput() {
		try {
			return inputQueue.remove(0);
		}catch (Exception e) {
			return "";
		}
	}
	
	public void addToAnimQueue(Object o) {
		animQueue.add(o);
	}
}
