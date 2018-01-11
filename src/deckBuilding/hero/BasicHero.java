package deckBuilding.hero;

import protocol.resources.Board;
import protocol.resources.Player;
import protocol.resources.card.Hero;

public class BasicHero extends Hero {
	private static final long serialVersionUID = -5198526278355803217L;

	public BasicHero(String owner) {
		super(owner);
		bHealth = 50;
		bAttack = 0;
		bCost = 1000;
		vRange = 2;
		mRange = 1;
		aRange = 1;
		reset();
	}

	@Override
	public void execute(Board board, String[] location, Player player) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getCName() {
		// TODO Auto-generated method stub
		return "basichero";
	}

	@Override
	public void execute(String tr, Board board, String[] location, Player player) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean actEffAvailable(Board b, String ownloc) {
		return false;
	}
	
}
