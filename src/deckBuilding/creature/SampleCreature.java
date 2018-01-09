package deckBuilding.creature;

import java.util.ArrayList;
import java.util.Arrays;

import protocol.resources.Board;
import protocol.resources.Player;
import protocol.resources.card.Card;
import protocol.resources.card.Creature;

public class SampleCreature extends Creature{
	
	private static final long serialVersionUID = 1989316314862612839L;
	
	public SampleCreature(String own) {
		super(own);
		bAttack = 3;
		bHealth = 4;
		bCost = 1;
		mRange = 3;
		mType = "free";
		vRange = 2;
		shield = true;
		flying = true;
		aRange = 3;
		aType = "free";
		reset();
	}
	@Override
	public boolean actEffAvailable(Board b, String ownloc) {
		if (ownloc.startsWith("hand")) {
			return true;
		}
		if (ownloc.startsWith("board")) {
			if (actEffAv >= 1) return true;
		}
		return false;
	}

	@Override
	public void execute(String tr, Board board, String[] location, Player player) {
		if (tr == "activehand") {
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
					if (c.isEffectTriggered("effectdamaged"))
						try {
							c.execute(board, new String[] {s2, location[0]}, player);
						} catch (Exception e) {
							e.printStackTrace();
						}
					board.triggerExecutableEffects(player, "effectdamage", new String[] {location[0],s2});
					board.clearBoard();
			}
			int i = 0;
			ArrayList<Card> cl = board.hand.get(owner).get();
			while (i < cl.size()) {
				if (cl.get(i) == this) {
					board.hand.get(owner).remove(i);
					board.grave.get(owner).add(this);
				}
				i++;
			}
		}
		executeRemotes(tr, board, location, player);
	}

	@Override
	public void execute(Board board, String[] location, Player player) {
		
	}
	
	@Override
	public String getCName() {
		return "samplecreature";
	}
}
