package deckBuilding.ultCreature;

import protocol.resources.Board;
import protocol.resources.Player;
import protocol.resources.card.Creature;
import protocol.resources.card.UltCreature;

public class SampleUltCreature extends UltCreature{
	private static final long serialVersionUID = -2278684718074174410L;

	public SampleUltCreature(String own) {
		super(own);
		bAttack = 2;
		bHealth = 2;
		bCost = 3;
		flying = true;
		vRange = 4;
		mRange = 4;
		mType = "free";
		reset();
		trigger.add("entry");
	}

	@Override
	public void execute(Board board, String[] location, Player player) throws Exception {}

	@Override
	public String getCName() {
		return "sampleultcreature";
	}

	@Override
	public void executeNative(String tr, Board board, String[] location, Player player) {
		if (tr == "entry") {
			try {
				Creature h = board.getCreature(board.getHeroLoc(owner));
				h.setcAttack(h.getcAttack() + this.bAttack);
				board.triggerExecutableEffects(player, "creaturebuffed", new String[] {board.getHeroLoc(owner)});
			}catch (Exception e) {
				e.printStackTrace();
			}
		}		
	}

	@Override
	public boolean actEffAvailable(Board b, String ownloc) {
		return false;
	}
	
}
