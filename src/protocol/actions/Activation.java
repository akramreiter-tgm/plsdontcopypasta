package protocol.actions;

import java.util.ArrayList;

import protocol.resources.Board;
import protocol.resources.Player;
import protocol.resources.card.Card;
import protocol.resources.card.Creature;

public class Activation {
	Board bd;
	
	public Activation(Board board) {
		bd = board;
	}
	
	/**
	 * returns all Tilenames on the board, which have a 
	 * creature at their location, which has an active effect
	 * that can be executed at the moment 
	 * @param (String) own
	 * @return String[]
	 */
	public String[] getAvActCardsBoard(String own) {
		ArrayList<String> out = new ArrayList<>();
		for (String s : bd.getAdjecent("E1",8)) {
			Creature c = bd.getCreature(s);
			if (c != null) {
				if ((c.owner.equals(own))&&(c.actEffAvailable(bd, s))) {
					out.add(s);
				}
			}
		}
		return out.toArray(new String[0]);
	}
	
	/**
	 * returns all indexes of Cards in the hand, which have 
	 * an active effect that can be executed at the moment 
	 * @param (String) own
	 * @return int[]
	 */
	public int[] getAvActCardsHand(String own) {
		ArrayList<Integer> out = new ArrayList<>();
		ArrayList<Card> tmp = bd.hand.get(own).get();
		for (int i = 0; i < tmp.size(); i++) {
			Card c = tmp.get(i);
			if (c.actEffAvailable(bd, "hand")) out.add(i);
		}
		int[] ret = new int[out.size()];
		for (int i = 0; i < out.size(); i++) {
			ret[i] = out.get(i);
		}
		return ret;
	}
	
	/**
	 * returns all indexes of Cards in the grave, which have 
	 * an active effect that can be executed at the moment 
	 * @param (String) own
	 * @return int[]
	 */
	public int[] getAvActCardsGrave(String own) {
		ArrayList<Integer> out = new ArrayList<>();
		ArrayList<Card> tmp = bd.grave.get(own).get();
		for (int i = 0; i < tmp.size(); i++) {
			Card c = tmp.get(i);
			if (c.actEffAvailable(bd, "grave")) out.add(i);
		}
		int[] ret = new int[out.size()];
		for (int i = 0; i < out.size(); i++) {
			ret[i] = out.get(i);
		}
		return ret;
	}
	
	/**
	 * returns all indexes of all removed Cards, which have 
	 * an active effect that can be executed at the moment 
	 * @param (String) own
	 * @return int[]
	 */
	public int[] getAvActCardsRemoved(String own) {
		ArrayList<Integer> out = new ArrayList<>();
		ArrayList<Card> tmp = bd.removed.get(own).get();
		for (int i = 0; i < tmp.size(); i++) {
			Card c = tmp.get(i);
			if (c.actEffAvailable(bd, "removed")) out.add(i);
		}
		int[] ret = new int[out.size()];
		for (int i = 0; i < out.size(); i++) {
			ret[i] = out.get(i);
		}
		return ret;
	}
	
	/**
	 * returns all indexes of Cards in the deck, which have 
	 * an active effect that can be executed at the moment 
	 * @param (String) own
	 * @return int[]
	 */
	public int[] getAvActCardsDeck(String own) {
		ArrayList<Integer> out = new ArrayList<>();
		ArrayList<Card> tmp = bd.deck.get(own).get();
		for (int i = 0; i < tmp.size(); i++) {
			Card c = tmp.get(i);
			if (c.actEffAvailable(bd, "deck")) out.add(i);
		}
		int[] ret = new int[out.size()];
		for (int i = 0; i < out.size(); i++) {
			ret[i] = out.get(i);
		}
		return ret;
	}
	
	/**
	 * executes the active effect of the Card
	 * at the given target
	 * targ-syntax:
	 * "hand"/"grave"/"removed"/"deck" + index
	 * OR
	 * "board" + tilename
	 * @param (Player) p
	 * @param (String) targ
	 * @see protocol.resources.card.Card#actEffAvailable(Board,String)
	 */
	public void activate(Player p, String targ) {
		try {
			if (targ.startsWith("board")) {
				targ = targ.substring(5);
				if (targ.length() >= 2) {
					for (String s : getAvActCardsBoard(p.pname)) {
						if (targ.startsWith(s)) {
							bd.getCreature(s).execute("active", bd, new String[] {s}, p);
						}
					}
				}
			}else if(targ.startsWith("hand")){
				try {
					int tIndex = Integer.parseInt(targ.substring(4));
					for (int i : getAvActCardsHand(p.pname)) {
						if (i == tIndex) {
							bd.hand.get(p.pname).get(i).execute("activehand", bd, new String[] {i+""}, p);
						}
					}
				}catch (Exception e) {
					e.printStackTrace();
				}
			}else if(targ.startsWith("grave")){
				try {
					int tIndex = Integer.parseInt(targ.substring(5));
					for (int i : getAvActCardsGrave(p.pname)) {
						if (i == tIndex) {
							bd.grave.get(p.pname).get(i).execute("activegrave", bd, new String[] {i+""}, p);
						}
					}
				}catch (Exception e) {
					e.printStackTrace();
				}
			}else if(targ.startsWith("removed")){
				try {
					int tIndex = Integer.parseInt(targ.substring(7));
					for (int i : getAvActCardsRemoved(p.pname)) {
						if (i == tIndex) {
							bd.removed.get(p.pname).get(i).execute("activeremoved", bd, new String[] {i+""}, p);
						}
					}
				}catch (Exception e) {
					e.printStackTrace();
				}
			}else if(targ.startsWith("deck")){
				try {
					String tname = targ.substring(4);
					for (int i : getAvActCardsDeck(p.pname)) {
						if (bd.deck.get(p.pname).get(i).getCName() == tname) {
							bd.deck.get(p.pname).get(i).execute("activedeck", bd, new String[] {i+""}, p);
						}
					}
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
