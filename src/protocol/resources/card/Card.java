package protocol.resources.card;

import java.io.Serializable;
import java.util.ArrayList;

import protocol.resources.Board;
import protocol.resources.Player;

public abstract class Card implements Serializable, Cloneable {
	private static final long serialVersionUID = -7903979818004668869L;

	protected int cCost = 1, bCost = 1;
	protected ArrayList<String> trigger = new ArrayList<>();
	protected ArrayList<Effect> remoteEffects = new ArrayList<>();
	public String owner;
	public int[] resCost = new int[4];
	protected int[] actEffNumValues = new int[0];
	
	public int getcCost() {
		return cCost;
	}
	public void setcCost(int cCost) {
		this.cCost = cCost;
	}
	
	public Card(String own) {
		owner = own;
	}
	
	public abstract Card clone();
	
	
	/**
	 * executes an effect from the hand (cType = "spell")
	 */
	public abstract void execute(Board board, String[] location, Player player);
	
	public void executeRemotes(String tr, Board board, String[] location, Player player) {
		for (Effect e : remoteEffects) {
			if (tr == e.getTrigger()) e.execute(board, location, player, this);
		}
	}
	
	public void addRemoteEffect(Effect e) {
		trigger.add(e.getTrigger());
		remoteEffects.add(e);
	}
	
	/*
	 * checks if the String matches a trigger of this creature
	 */
	public boolean isEffectTriggered(String t) {
		for (String s : trigger) {
			if (s.equals(t)) return true;
		}
		return false;
	}
	
	public abstract String getCType();
	public abstract String getCName();
}
