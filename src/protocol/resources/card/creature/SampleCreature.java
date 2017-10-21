package protocol.resources.card.creature;

import java.util.ArrayList;
import java.util.Arrays;

import protocol.resources.Board;
import protocol.resources.Player;
import protocol.resources.card.Creature;

public class SampleCreature extends Creature{
	
	private static final long serialVersionUID = 1989316314862612839L;
	
	public SampleCreature(String own) {
		super(own);
		bAttack = 3;
		bHealth = 4;
		bCost = 5;
		mRange = 3;
		mType = "line";
		vRange = 2;
		shield = true;
		flying = true;
		aRange = 2;
		aType = "free";
		resCost[0] = 1;
		resCost[2] = 1;
		reset();
	}
	@Override
	public boolean actEffAvailable(Board b, String ownloc) {
		if (actEffAv > 1) return true;
		return false;
	}

	@Override
	public void execute(String tr, Board board, String[] location, Player player) {
		if (tr == "active") {
			ArrayList<String> tmp = new ArrayList<>(Arrays.asList(board.playerNames.clone()));
			for (String s : tmp) {
				if (s == owner) tmp.remove(s);
			}
			for (String s : tmp) {
				Creature c = board.getCreature(board.getHeroLoc(s));
				c.damage(5);
				String s2 = "";
				for (String s3 : board.getAdjecent("E1", 8)) {
					if (board.getCreature(s3) == c) s2 = s3;
				}
				if (s2.length() > 0)
					if (c.isEffectTriggered("damaged")) c.execute(board, new String[] {s2, location[0]}, player);
					board.triggerExecutableEffects(player, "effectdamage", new String[] {location[0],s2});
			}
		}
		executeRemotes(tr, board, location, player);
	}

	@Override
	public void execute(Board board, String[] location, Player player) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public String getCName() {
		return "samplecreature";
	}
}
