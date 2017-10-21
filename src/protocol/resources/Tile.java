package protocol.resources;

import java.io.Serializable;

import protocol.resources.card.Creature;

public class Tile implements Cloneable, Serializable {
	private static final long serialVersionUID = -8395629319339052722L;
	private Creature creature;
	private Trap trap;
	private Ground ground;
	
	public Tile() {
		ground = new Ground();
		ground.aeSourceCount = 0;
		ground.owner = "";
		ground.aeSourceCount = 0;
		trap = null;
		creature = null;
	}
	
	public Tile clone() {
		try {
			return (Tile) super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}
	public Creature getCreature() {
		return creature;
	}
	public void setCreature(Creature creature) {
		this.creature = creature;
	}
	public Trap getTrap() {
		return trap;
	}
	public void setTrap(Trap trap) {
		this.trap = trap;
	}
	public Ground getGround() {
		return ground;
	}
	public void setGround(Ground ground) {
		this.ground = ground;
	}
}
