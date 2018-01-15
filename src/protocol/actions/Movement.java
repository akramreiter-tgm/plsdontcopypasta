package protocol.actions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import protocol.resources.Board;
import protocol.resources.Player;
import protocol.resources.card.Creature;

public class Movement {
	Board bd;
	public Movement (Board b) {
		bd = b;
	}
	
	/**
	 * returns all Tilenames of valid movement
	 * targets for the Creature at loc
	 * @param (String) loc
	 * @return String[]
	 */
	public String[] getMovementRange(String loc) {
		try {
			Creature c = bd.getCreature(loc);
			HashSet<String> tmp = new HashSet<>();
			String[] vr = bd.getVisionRange(c.owner);
			String[] adjt = bd.getAdjecent(loc);
			for (String s : adjt) {
				try {
					if (bd.getCreature(s).taunt) return new String[0];
				}catch (Exception e) {
					// TODO: handle exception
				}
			}
			boolean fly = c.flying;
			switch (c.mType) {
			case "free":
				tmp = new HashSet<>(Arrays.asList(bd.getAdjecent(loc, c.mRange)));
				if (!fly) {
					for (String s : tmp.toArray(new String[0])) if (bd.getGround(s).gType == 0) tmp.remove(s);
				}
				break;
			default:
				if (fly) {
					tmp = new HashSet<>(Arrays.asList(bd.getLanesFly(loc, c.mRange)));
				} else {
					tmp = new HashSet<>(Arrays.asList(bd.getLanesGround(loc, c.mRange)));
				}
				break;
			}
			
			for (String s : tmp.toArray(new String[0])) {
				if (bd.getCreature(s) != null) tmp.remove(s);
				if ((bd.getGround(s).gType == 0) && !fly) tmp.remove(s);
				boolean inc = false;
				for (String s2 : vr) {
					if (s2.equals(s)) inc = true; 
				}
				if (!inc) tmp.remove(s);
			}
			return tmp.toArray(new String[0]);
		} catch (Exception e) {
			return new String[0];
		}
	}
	
	/**
	 * returns all Tilenames of valid attack targets
	 * for the Creature at loc
	 * @param (String) loc
	 * @return String[]
	 */
	public String[] getAttackRange(String loc) {
		try {
			Creature c = bd.getCreature(loc);
			HashSet<String> tmp = new HashSet<>();
			String[] vr = bd.getVisionRange(c.owner);
			switch (c.aType) {
			case "free":
				tmp = new HashSet<>(Arrays.asList(bd.getAdjecent(loc, c.aRange)));
				break;
			default:
				tmp = new HashSet<>(Arrays.asList(bd.getLanesFly(loc, c.aRange)));
				break;
			}
			for (String s : tmp.toArray(new String[0])) {
				if (bd.getCreature(s) == null) {
					tmp.remove(s);
					continue;
				}
				if (bd.getCreature(s).owner == c.owner) {
					tmp.remove(s);
					continue;
				}
				boolean inc = false;
				for (String s2 : vr) {
					if (s2.equals(s)) inc = true; 
				}
				if (!inc) tmp.remove(s);
			}
			for (String s : tmp) System.out.print(s + ", ");
			System.out.println();
			return tmp.toArray(new String[0]);
		} catch (Exception e) {
			return new String[0];
		}
	}
	
	/**
	 * If targ is within the movement-range
	 * of the Creature located at src which
	 * is owned by the Player p, the Creature
	 * will be relocated to targ
	 * If targ is within attack-range and
	 * an enemy Creature is located at targ
	 * of the Creature located at src which
	 * is owned by the Player p, the src-Creature
	 * will damage the targ-Creature and if src is
	 * within the attack-range of the targ-Creature
	 * the src-Creature will be damaged
	 * @throws "creaturedamaged"
	 * @throws "creatureattack"
	 * @throws "creaturemove"
	 * @param src
	 * @param targ
	 * @param p
	 */
	public void moveOrAttackCreature(String src, String targ, Player p) {
		boolean inRange = false;
		boolean inARange = false;
		for (String s : getMovementRange(src)) {
			if (s.equals(targ)) inRange = true;
		}
		for (String s : getAttackRange(src)) {
			if (s.equals(targ)) inARange = true;
		}
		if (inRange) {
			Creature c = bd.getCreature(src);
			if (c.moveAv <= 0) return;
			bd.get(src).setCreature(null);
			bd.get(targ).setCreature(c);
			c.moveAv -= 1;
			if (c.isEffectTriggered("move"))
				try {
					c.execute("move", bd, new String[]{src,targ}, p);
				} catch (Exception e) {
					e.printStackTrace();
				}
			bd.triggerExecutableEffects(p, "creaturemove", new String[] {src,targ});
			return;
		}
		if (inARange) {
			Creature c1 = bd.getCreature(src);
			if (c1.AtkAv <= 0) return;
			Creature c2 = bd.getCreature(targ);
			boolean c2damaged = (!c2.shield);
			boolean c1damaged = (!c1.shield);
			int c2atk = c2.getcAttack();
			int c1atk = c1.getcAttack();
			c2.damage(c1atk);
			boolean counterattack = false;
			for (String s : getAttackRange(targ)) {
				if (s.equals(src)) counterattack = true;
			}
			if (c2damaged) System.out.println("Creature at " + targ + " took " + c1atk + " damage");
			if ((counterattack)&&(c1damaged)) {
				c1.damage(c2atk);
				System.out.println("Creature at " + src + " took " + c2atk + " damage");
			}
			if ((c2atk > 0)&&(counterattack)&&(c1.isEffectTriggered("damaged")&&(c1damaged)))
				try {
					c1.execute("damaged", bd, new String[] {src,targ}, p);
				} catch (Exception e) {
					e.printStackTrace();
				}
			if ((c1atk > 0)&&(c2.isEffectTriggered("damaged")&&(c2damaged)))
				try {
					c2.execute("damaged", bd, new String[] {targ,src}, p);
				} catch (Exception e) {
					e.printStackTrace();
				}
			if (c1.isEffectTriggered("attack"))
				try {
					c1.execute("attack", bd, new String[] {src,targ}, p);
				} catch (Exception e) {
					e.printStackTrace();
				}
			if (c2.isEffectTriggered("attack"))
				try {
					c2.execute("attack", bd, new String[] {targ,src}, p);
				} catch (Exception e) {
					e.printStackTrace();
				}
			c1.AtkAv -= 1;
			c1.moveAv = 0;
			bd.triggerExecutableEffects(p, "creatureattack", new String[] {src,targ});
			if ((c1.getcAttack() > 0)&&(c2damaged))
				bd.triggerExecutableEffects(bd.getPlayer(c2.owner), "creaturedamaged", new String[] {targ,src});
			if ((c2.getcAttack() > 0)&&(counterattack)&&(c1damaged))
				bd.triggerExecutableEffects(p, "creaturedamaged", new String[] {src,targ});
			bd.clearBoard();
		}
	}
	
	public String[] getMoveOrAttackAvCreatures(String own) {
		ArrayList<String> out = new ArrayList<>(Arrays.asList(bd.getAdjecent("E1",8)));
		for (String s : out.toArray(new String[0])) {
			Creature c = bd.getCreature(s);
			if (c == null) {
				out.remove(s);
				continue;
			}
			if (c.owner != own) {
				out.remove(s);
				continue;
			}
			if ((getMovementRange(s).length == 0) && (getAttackRange(s).length == 0)) {
				out.remove(s);
				continue;
			}
		}
		return out.toArray(new String[0]);
	}
}
