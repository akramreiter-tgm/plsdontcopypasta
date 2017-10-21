package protocol.resources.card.creature;

import protocol.resources.Board;
import protocol.resources.Player;
import protocol.resources.card.Creature;

public class Hero extends Creature {
	private static final long serialVersionUID = 3258350196969582719L;
	
	public Hero(String owner) {
		super(owner);
		bHealth = 50;
		bAttack = 0;
		bCost = 0;
		vRange = 2;
		hero = true;
		mRange = 1;
		mType = "free";
		flying = false;
		reset();
		trigger.add("damaged");
		trigger.add("move");
	}

	@Override
	public boolean actEffAvailable(Board b, String ownloc) { //unused
		if (actEffAv > 0)
			return true;
		return false;
	}
	@Override
	public void execute(Board b, String[] loc, Player p) {} //unused

	@Override
	public void execute(String tr, Board board, String[] location, Player player) {
		switch (tr) {
		case "move":
		case "damaged":
			cAttack += 1;
			break;//debug feature TODO remove this boolshit
		}
		executeRemotes(tr, board, location, player);
	} //unused

	@Override
	public String getCName() {
		return "Hero";
	}
}
