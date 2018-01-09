package protocol.resources.card;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

import protocol.resources.Board;
import protocol.resources.Player;

public abstract class Card implements Serializable, Cloneable {
	private static final long serialVersionUID = -7903979818004668869L;

	protected int cCost = 1, bCost = 1;
	protected HashSet<String> trigger = new HashSet<>();
	protected ArrayList<Effect> remoteEffects = new ArrayList<>();
	protected ArrayList<String> tags = new ArrayList<>();
	public String owner;
	public int[] resCost = new int[4];
	
	public String[] getTags() {
		return tags.toArray(new String[0]);
	}
	public int getcCost() {
		return cCost;
	}
	public void setcCost(int cCost) {
		this.cCost = cCost;
	}
	
	/**
	 * standard Constructor of Card;
	 * sets the Cards owner field to
	 * the given parameter own
	 * @param (String) own
	 */
	public Card(String own) {
		owner = own;
	}
	
	public abstract Card clone();
	
	
	/**
	 * executes an effect from the hand (cType = "spell")
	 * @throws Exception
	 */
	public abstract void execute(Board board, String[] location, Player player) throws Exception;
	
	/**
	 * executes all Remote-Effects of a Card (called at the end of Card.execute(String tr, Board board, String[] location, Player player))
	 * @param tr
	 * @param board
	 * @param location
	 * @param player
	 */
	public void executeRemotes(String tr, Board board, String[] location, Player player) {
		for (Effect e : remoteEffects) {
			for (String s : e.getTrigger())
				if (tr == s) e.execute(tr, board, location, player, this);
		}
	}
	
	/**
	 * adds an Effect to a Card, which may be triggered like the Card's 
	 * @param (Effect) e
	 */
	public void addRemoteEffect(Effect e) {
		for (String s : tags) {
			if (s == e.getTag()) return;
		}
		for (String s : e.getTrigger())
			trigger.add(s);
		if (e.getTag() != "") tags.add(e.getTag());
		remoteEffects.add(e);
	}
	
	/**
	 * checks if the String matches any trigger of this Card
	 * @param (String) t
	 */
	public boolean isEffectTriggered(String t) {
		for (String s : trigger) {
			if (s.equals(t)) return true;
		}
		return false;
	}
	
	/**
	 * returns the type of a Card (Creature,Spell,etc.)
	 * @return String
	 */
	public abstract String getCType();
	
	/**
	 * returns the specific name of a Card (example: "samplecreature")
	 * @return String
	 */
	public abstract String getCName();
	
	/**
	 * executes effect which require a certain criteria to be executed
	 * implementations should call Card.executeRemotes before returning
	 * if the effect deals damage Board.clearBoard() has to be called
	 * at the end of their code (between actual Code and executeRemotes) 
	 * @param (String) tr
	 * @param (Board) board
	 * @param (String[]) location
	 * @param (Player) player
	 */
	public abstract void execute(String tr, Board board, String[] location, Player player) throws Exception;
	
	/**
	 * sets current values back to base values
	 * (cCost -> bCost)
	 */
	public void reset() {
		cCost = bCost;
	}
	
	/**
	 * checks if an active ability can be triggered (may have Board-specific criteria)
	 * ownloc: either a Tilename on the board, "hand", "grave", "removed" or "deck"
	 * @param (String) ownloc: either a Tilename on the board, "hand", "grave", "removed" or "deck"
	 * @param (Board) b
	 */
	public abstract boolean actEffAvailable(Board b, String ownloc);
}
