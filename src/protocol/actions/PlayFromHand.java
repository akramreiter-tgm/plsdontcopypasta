package protocol.actions;

import java.util.ArrayList;
import java.util.Arrays;

import protocol.resources.Board;
import protocol.resources.Ground;
import protocol.resources.Player;
import protocol.resources.card.Card;
import protocol.resources.card.Creature;

public class PlayFromHand {
	private Board bd;
	
	public PlayFromHand(Board b) {
		bd = b;
	}
	
	/**
	 * returns the indexes of all
	 * hand cards, where enough
	 * resources are available
	 * to play them from the hand
	 * @param (String) pname
	 * @return int[]
	 */
	public int[] getPlayableCards(String pname) {
		ArrayList<Integer> tmp = new ArrayList<>();
		ArrayList<Card> thand = bd.hand.get(pname).get();
		int i = 0;
		outer: for (Card c : thand) {
			if (c.getcCost() <= bd.resources.get(pname)[0]) {
				for (int j = 0; j < c.resCost.length; j++) {
					if (c.resCost[j] > bd.resources.get(pname)[3 + j]) {
						System.out.println("r"+(j+1) + " failed");
						continue outer;
					}
				}
			} else {
				System.out.println("e failed");
				continue outer;
			}
			if (c.getCType() == "creature") {
				if (getPlayableLand((Creature) c).length >= 1) tmp.add(i);
			} else if (c.getCType() == "spell") {
					if (c.actEffAvailable(bd, "hand")) tmp.add(i);
			}
			i++;
		}
		int[] result = new int[tmp.size()];
		for (int j = 0; j < tmp.size(); j++) {
			result[j] = tmp.get(j);
		}
		return result;
	}
	
	/**
	 * attempts to play the card at the given index
	 * in the hand at the targ on the board
	 * TODO add ability to play spells
	 * TODO trigger: "spellplayed"
	 * @param (Player) p
	 * @param (String) targ
	 * @param (int) index
	 * @throws "creatureplayed"
	 */
	public void PlayCard(Player p, String targ, int index) {
		Card c = bd.hand.get(p.pname).get(index);
		System.out.println(c.getCType());
		boolean playable = false;
		for (int i : getPlayableCards(p.pname)) if (i == index) playable = true;
		if (!playable) return;
		switch (c.getCType()) {
		case "creature":
			System.out.println("trying to play creature");
			playable = false;
			if (c instanceof Creature)
			for (String s : getPlayableLand((Creature) c)) {
				if (s.equals(targ)) playable = true;
			}
			if (playable) {	
				try {
					if (c.isEffectTriggered("entry")) {
						c.execute("entry", bd, new String[] {targ}, p);
					}
					bd.resources.get(p.pname)[0] -= c.getcCost();
					bd.get(targ).setCreature((Creature) c);
					bd.hand.get(p.pname).remove(index);
					bd.triggerExecutableEffects(p, "creatureplayed", new String[] {targ});
				}catch (Exception e) {
					e.printStackTrace();
					System.out.println("couldn't properly execute entry procedure");
				}
			}
			break;
		}
	}
	
	/**
	 * returns all Tiles a Creature can be
	 * played at from the hand
	 * @param (Creature) c
	 * @return String[]
	 */
	public String[] getPlayableLand(Creature c) {
		if (c == null) return new String[0];
		ArrayList<String> tmp = new ArrayList<>(Arrays.asList(bd.getAdjecent("E1", 8)));
		boolean[] allowedResTypes = new boolean[] {true,true,true,true};
		boolean noResReq = true;
		System.out.println("hi");
		if ((c.resCost[0] > 0)||(c.resCost[1] > 0)||(c.resCost[2] > 0)||(c.resCost[3] > 0)) {
			System.out.println("hi2");
			for (int i = 0; i < c.resCost.length; i++) {
				if (c.resCost[i] <= 0) allowedResTypes[i] = false;
			}
			noResReq = false;
			System.out.print(allowedResTypes[0] + "," + allowedResTypes[1] + "," + allowedResTypes[2] + "," + allowedResTypes[3]);
		}
		for (String s : tmp.toArray(new String[0])) {
			Ground g = bd.getGround(s);
			if (g.owner != c.owner) {
				tmp.remove(s);
				continue;
			}
			if (g.gType == 0) {
				tmp.remove(s);
				continue;
			}
			if (!noResReq) {
				if (g.gType >= 2) {
					if (!allowedResTypes[g.gType - 2]) tmp.remove(s);
				} else {
					tmp.remove(s);
				}
			}
		}
		System.out.println("playable tiles found: " + tmp.size());
		return tmp.toArray(new String[0]);
	}
}
