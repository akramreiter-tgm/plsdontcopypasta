package deckBuilding.creature;

import java.util.ArrayList;
import java.util.Arrays;

import deckBuilding.effect.PlaguePassive;
import protocol.Coreprotocol;
import protocol.resources.Board;
import protocol.resources.Player;
import protocol.resources.card.Creature;

public class Plague02 extends Creature {
	private static final long serialVersionUID = 6186442153656667940L;

	public Plague02(String own) {
		super(own);
		this.bAttack = 3;
		this.bCost = 6;
		this.bHealth = 5;
		reset();
		this.mRange = 1;
		this.vRange = 2;
		this.aRange = 2;
		this.resCost[3] = 1;
		this.flying = true;
		this.trigger.add("entry");
	}

	@Override
	public void execute(Board board, String[] location, Player player) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getCName() {
		return "plague02";
	}

	@Override
	public void execute(String tr, Board board, String[] location, Player player) throws Exception {
		hi: if (tr == "entry") {
			System.out.println("starting entry");
			ArrayList<String> tmp = new ArrayList<>(Arrays.asList(board.getAdjecent(location[0])));
			System.out.println("trying to remove invalid targets");
			for (String s : tmp.toArray(new String[0])) {
				System.out.println("getting creature at " + s);
				if (board.getCreature(s) == null) {
					System.out.println("no creature found at " + s);
					tmp.remove(s);
				} else if (board.getCreature(s).owner == this.owner) {
					System.out.println("creature found at " + s + " has same owner");
					tmp.remove(s);
				}
			}
			if (tmp.size() == 0) break hi;
			System.out.println("adding stuff to commQueue");
			player.commQueue.add(tmp);
			System.out.println("getting input");
			String s2 = Coreprotocol.getInput().toUpperCase();
			System.out.println("actually executing stuff; available targets: " + tmp.size());
			for (String s : tmp) {
				System.out.println(s2 + " starts with " + s);
				if (s2.startsWith(s)) {
					try {
						board.getCreature(s2).addRemoteEffect(new PlaguePassive());
						System.out.println("is turnend triggered?: " + board.getCreature(s2).isEffectTriggered("turnend"));
					}catch (Exception e) {
						// TODO: handle exception
					}
				}
			}
		}
		System.out.println("executing remote effects");
		executeRemotes(tr, board, location, player);
	}

	@Override
	public boolean actEffAvailable(Board b, String ownloc) {
		// TODO Auto-generated method stub
		return false;
	}

}
