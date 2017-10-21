package protocol.resources.card;

import java.io.Serializable;
import java.util.ArrayList;
import protocol.resources.Board;
import protocol.resources.Player;

public abstract class Creature extends Card implements Serializable, Cloneable {
	private static final long serialVersionUID = -3341280271172621674L;
	public boolean hero = false;
	protected int bAttack = 1, bHealth = 1, cAttack = 1, cHealth = 1;
	public int vRange = 1, mRange = 1, aRange = 1;
	public String mType = "line", aType = "line"; 
	// mType/aType: line: can move/attack in straight line; free: can attack anything in range (Board.getAdjecent)
	public boolean shield, taunt, root, stun, terrified, flying;
	public int moveAv, AtkAv, actEffAv;
	
	/**
	 * 
	 * @param (String) own
	 */
	public Creature(String own) {
		super(own);
	}
	
	public int getbAttack() {
		return bAttack;
	}
	public void setbAttack(int bAttack) {
		cAttack += bAttack - this.bAttack;
		if (cAttack < 0) cAttack = 0;
		this.bAttack = bAttack;
	}
	public int getbCost() {
		return bCost;
	}
	public void setbCost(int bCost) {
		this.bCost = bCost;
	}
	public int getbHealth() {
		return bHealth;
	}
	public void setbHealth(int bHealth) {
		if (bHealth > this.bHealth) {
			cHealth += bHealth - this.bHealth;
		}
		if (bHealth < cHealth) {
			cHealth = bHealth;
		}
		this.bHealth = bHealth;
	}
	public int getcAttack() {
		return cAttack;
	}
	public void setcAttack(int cAttack) {
		this.cAttack = cAttack;
	}
	
	public int getcHealth() {
		return cHealth;
	}
	public void setcHealth(int cHealth) {
		if (cHealth < 0) return;
		if (cHealth > bHealth) cHealth = bHealth;
		this.cHealth = cHealth;
	}
	
	/*
	 * Damages the creature equal to the given parameter i
	 * if cHealth reaches a value lower than 1 the "dead" field is set to true
	 */
	public void damage(int i) {
		if (shield) return;
		if (i < 0) return;
		cHealth -= i;
	}
	
	/*
	 * Heals the creature equal to the given parameter i
	 * if cHealth reaches a value higher than bHealth cHealth is set to bHealth
	 * "dead" is set to false if cHealth is higher than 0
	 */
	public void heal(int i) {
		if (i < 0) return;
		if (i + cHealth < 0) return;
		cHealth += i;
	}
	
	public void reset() {
		cAttack = bAttack;
		cHealth = bHealth;
		cCost = bCost;
	}
	
	/*
	 * checks if an active ability can be triggered (may have Board-specific criteria)
	 */
	public abstract boolean actEffAvailable(Board b, String ownloc);
	
	public boolean isDead() {
		return cHealth <= 0;
	}
	
	public boolean isDamaged() {
		return cHealth < bHealth;
	}
	
	public Creature clone() {
		Creature cl = clone();
		cl.trigger = new ArrayList<>();
		cl.remoteEffects = new ArrayList<>();
		return cl;
	}
	
	/*
	 * enables movement, attack, abilities (should be executed at turnstart)
	 */
	public void turnStart() {
		actEffAv = 1;
		AtkAv = 1;
		moveAv = 1;
	}
	
	/*
	 * executes effect which require a certain criteria to be executed
	 */
	public abstract void execute(String tr, Board board, String[] location, Player player);
	
	public String getCType() {
		return "creature";
	}
}
