package protocol.actions;

import java.util.ArrayList;
import java.util.Arrays;

import protocol.resources.Board;
import protocol.resources.Player;
import protocol.resources.card.Card;
import protocol.resources.card.Creature;
import protocol.resources.card.UltCreature;

public class Evolve {
	public Board bd;
	
	public Evolve(Board board) {
		bd = board;
	}
	
	/**
	 * evolves the Creature at the targ location
	 * into the UltCreature at the given index
	 * in the side deck 
	 * @param p
	 * @param targ
	 * @param targIndex
	 */
	public void evolve(Player p, String targ, int targIndex) {
		String[] tmp = getEvolveTargets(p.pname);
		for (String s : tmp) {
			if (targ.startsWith(s)) {
				System.out.println("evolve-target found");
				int[] tmp2 = getPossibleEvolutions(p.pname);
				for (int i : tmp2) {
					if (i == targIndex) {
						System.out.println("evolution in sidedeck found");
						try {
							Card c = bd.sidedeck.get(p.pname).get(targIndex);
							if (c instanceof UltCreature) {
								((UltCreature) c).evolve(bd.getCreature(targ));
								if (c.isEffectTriggered("entry")) {
									c.execute("entry", bd, new String[] {targ}, p);
								}
								bd.resources.get(p.pname)[2] -= c.getcCost();
								bd.get(targ).setCreature((Creature)c);
								bd.sidedeck.get(p.pname).remove(targIndex);
								bd.triggerExecutableEffects(p, "creatureevolved", new String[] {targ});
							}
						}catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
	
	/**
	 * returns the indexes of all possible
	 * evolutions from a Player's sidedeck
	 * @param (String) own
	 * @return int[]
	 */
	public int[] getPossibleEvolutions(String own) {
		ArrayList<Integer> tmp = new ArrayList<>();
		ArrayList<Card> tmp2 = bd.sidedeck.get(own).get();
		bla: for (int i = 0; i < tmp2.size(); i++) {
			Card c = tmp2.get(i);
			if (bd.resources.get(own)[2] >= c.getcCost()) {
				for (int j = 0; j < 4; j++) {
					if (c.resCost[j] > bd.resources.get(own)[3+j]) {
						System.out.println("heh");
						continue bla;
					}
				}
				tmp.add(i);
			}
		}
		int[] out = new int[tmp.size()];
		for (int i = 0; i < tmp.size(); i++) {
			out[i] = tmp.get(i);
		}
		System.out.println(out.length + " possible evolutions found");
		for (int i2 : out) {
			System.out.println(i2);
		}
		return out;
	}
	
	/**
	 * returns the locations of all creatures which can be
	 * evolved: Heroes and already evolved creatures are excluded
	 * @param (String) own
	 * @return int[]
	 */
	public String[] getEvolveTargets(String own) {
		ArrayList<String> tmp = new ArrayList<>(Arrays.asList(bd.getAdjecent("E1", 8)));
		for (String s : tmp.toArray(new String[0])) {
			Creature c = bd.getCreature(s);
			if (c == null) {
				tmp.remove(s);
				continue;
			}
			if (c.owner != own) tmp.remove(s);
			if (c.hero) tmp.remove(s);
			if (c.getCType() != "creature") tmp.remove(s);
		}
		System.out.println(tmp.size() + " evolveable creatures found");
		for (String s : tmp) {
			System.out.println("evolve-target at: " + s);
		}
		return tmp.toArray(new String[0]);
	}
}
