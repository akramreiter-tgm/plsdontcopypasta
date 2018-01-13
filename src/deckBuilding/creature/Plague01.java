package deckBuilding.creature;

import java.util.ArrayList;

import deckBuilding.effect.PlaguePassive;
import protocol.resources.Board;
import protocol.resources.Player;
import protocol.resources.card.Card;
import protocol.resources.card.Creature;

public class Plague01 extends Creature {
	private static final long serialVersionUID = 6186442153656667940L;

	public Plague01(String own) {
		super(own);
		this.bAttack = 6;
		this.bCost = 4;
		this.bHealth = 4;
		reset();
		this.mRange = 1;
		this.vRange = 1;
		this.resCost[3] = 1;
		this.flying = true;
		this.addRemoteEffect(new PlaguePassive());
	}

	@Override
	public void execute(Board board, String[] location, Player player) throws Exception {
		
	}

	@Override
	public String getCName() {
		// TODO Auto-generated method stub
		return "plague01";
	}

	@Override
	public void executeNative(String tr, Board board, String[] location, Player player) throws Exception {
		if (tr == "activehand") {
			Creature c = board.getCreature(board.getHeroLoc(this.owner));
			c.setcAttack(c.getcAttack() + 1);
			board.hand.get(player.pname).get().remove(this);
		}
	}

	@Override
	public boolean actEffAvailable(Board b, String ownloc) {
		if (ownloc.startsWith("hand")) {
			return true;
		}
		return false;
	}

}
