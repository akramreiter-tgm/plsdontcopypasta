package protocol.actions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import protocol.resources.Board;
import protocol.resources.Ground;
import protocol.resources.Player;
import protocol.resources.card.Creature;

public class Placement {
	private Board bd;
	public Placement(Board board) {
		bd = board;
	}
	
	/**
	 * returns all tiles where the given Player(pname)
	 * could place regular Ground (gtype = 1)
	 * @param (String) pname
	 * @return String[]
	 */
	public String[] getAvailablePlacement(String pname) {
		System.out.println(bd.getHeroLoc(pname));
		ArrayList<String> tmp = new ArrayList<>(Arrays.asList(bd.getAdjecent("E1", 8)));
		for (String s : tmp.toArray(new String[0]))
			if (bd.getGround(s).owner != pname) {
				if (bd.getGround(s).gType > 0) {
					Creature c = bd.getCreature(s);
					if (c != null) {
						if (c.owner == pname) {
							System.out.println("Creature on enemy tile found");
							continue;
						}
					}
				}
				tmp.remove(s);
			}
		HashSet<String> pland = new HashSet<>();
		for (String s : tmp)
			for (String s2 : bd.getAdjecent(s)) {
				if (bd.getGround(s2).gType == 0)
					pland.add(s2);
			}
		return pland.toArray(new String[0]);
	}
	
	/**
	 * returns all tiles where the given Player(pname)
	 * could place Resources (gtype = 2-5)
	 * @param (String) pname
	 * @return String[]
	 */
	public String[] getAvailableResGround(String pname) {
		ArrayList<String> tmp = new ArrayList<>(Arrays.asList(bd.getAdjecent("E1", 8)));
		for (String s : tmp.toArray(new String[0]))
			if ((bd.getGround(s).owner != pname)||(bd.getGround(s).gType != 1)) tmp.remove(s);
		return tmp.toArray(new String[0]);
	}
	
	/**
	 * places ground or a resource-tile (depending on the type-param)
	 * at the targ location if it is 
	 * @param (Player) p
	 * @param (String) targ
	 * @param (String) type
	 * @throws "groundplaced"
	 * @throws "resourceplaced"
	 */
	public void placeLand(Player p, String targ, String type) {
		int t = 1;
		if (type.charAt(0) == 'r') {
			t += type.charAt(1) - '0';
		}
		if (t == 1) {
			if (bd.resources.get(p.pname)[0] < Board.groundcost) return;
			boolean placeAv = false;
			for (String s : getAvailablePlacement(p.pname)) {
				if (s.equals(targ)) placeAv = true;
			}
			if (placeAv) {
				Ground g = bd.getGround(targ);
				g.gType = t;
				g.owner = p.pname;
				bd.resources.get(p.pname)[0] -= Board.groundcost;
				bd.triggerExecutableEffects(p, "groundplaced", new String[] {targ});
			}
		} else {
			if (bd.resources.get(p.pname)[0] < Board.rescost) return;
			boolean placeAv = false;
			for (String s : getAvailableResGround(p.pname)) {
				if (s.equals(targ)) placeAv = true;
			}
			if (placeAv) {
				Ground g = bd.getGround(targ);
				g.gType = t;
				g.owner = p.pname;
				bd.resources.get(p.pname)[0] -= Board.rescost;
				
				bd.resources.get(p.pname)[t+1]++;
				System.out.println(bd.resources.get(p.pname)[3] + "," + bd.resources.get(p.pname)[4] + "," + bd.resources.get(p.pname)[5] + "," + bd.resources.get(p.pname)[6]);
				bd.triggerExecutableEffects(p, "resourceplaced", new String[] {targ});
			}
			bd.resources.get(p.pname)[t+1]++;
		}
	}
}
