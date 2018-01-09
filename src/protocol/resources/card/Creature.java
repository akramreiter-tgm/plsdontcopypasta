package protocol.resources.card;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

public abstract class Creature extends Card implements Serializable, Cloneable {
	private static final long serialVersionUID = -3341280271172621674L;
	public boolean hero = false;
	protected int bAttack = 1, bHealth = 1, cAttack = 1, cHealth = 1;
	public int vRange = 1, mRange = 1, aRange = 1;
	public String mType = "line", aType = "line"; 
	// mType/aType: line: can move/attack in straight line; free: can attack anything in range (Board.getAdjecent)
	public boolean shield, taunt, root, stun, terrified, flying;
	public int moveAv, AtkAv, actEffAv, bMoveAv = 1, bAtkAv = 1;
	
	/**
	 * calls default Constructor of Card (sets owner field)
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
	
	/**
	 * Damages (= reduces cHealth) the creature equal to the given parameter i
	 * if cHealth reaches a value lower than 1 the "dead" field is set to true
	 */
	public void damage(int i) {
		if (shield) {
			shield = false;
			return;
		}
		if (i < 0) return;
		cHealth -= i;
	}
	
	/**
	 * Heals (= increases cHealth) the creature equal to the given parameter i
	 * if cHealth reaches a value higher than bHealth cHealth is set to bHealth
	 */
	public void heal(int i) {
		if (i < 0) return;
		if (i + cHealth < 0) return;
		cHealth += i;
	}
	
	@Override
	/**
	 * sets all current values (cAttack, cHealth, cCost) back
	 * to their base/default values (bAttack, bHealth, bCost)
	 */
	public void reset() {
		cAttack = bAttack;
		cHealth = bHealth;
		cCost = bCost;
	}
	
	/**
	 * checks if cHealth is 0 or lower 
	 * (criteria for a Creature to be considered dead)
	 * @return boolean
	 */
	public boolean isDead() {
		return cHealth <= 0;
	}
	
	/**
	 * checks if cHealth is lower than bHealth
	 * (criteria for a Creature to be considered damaged)
	 * @return boolean
	 */
	public boolean isDamaged() {
		return cHealth < bHealth;
	}
	
	/**
	 * produces a deep copy of a Creature
	 * @return Creature
	 */
	public Creature clone() {
		Creature cl = clone();
		cl.trigger = new HashSet<>();
		cl.remoteEffects = new ArrayList<>();
		return cl;
	}
	
	/**
	 * enables movement, attack, abilities (should be called at turnstart)
	 */
	public void turnStart() {
		actEffAv = 1;
		AtkAv = bAtkAv;
		moveAv = bMoveAv;
	}
	
	/**
	 * returns the CardType of this Creature ("creature")
	 * @return String
	 */
	public String getCType() {
		return "creature";
	}
}
