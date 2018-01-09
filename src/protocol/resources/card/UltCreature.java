package protocol.resources.card;

import protocol.resources.card.Creature;

public abstract class UltCreature extends Creature {
	private static final long serialVersionUID = -4206272234399833956L;

	public UltCreature(String own) {
		super(own);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String getCType() {
		return "ultcreature";
	}
	
	public void evolve(Creature c) {
		this.bHealth += c.getcHealth();
		this.bAttack += c.getcAttack();
		reset();
	}
}
