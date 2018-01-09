package deckBuilding.effect;

import protocol.resources.Board;
import protocol.resources.Player;
import protocol.resources.card.Card;
import protocol.resources.card.Effect;
import protocol.resources.card.Creature;

public class PlaguePassive extends Effect {
	private static final int damage = 1;
	@Override
	public void execute(String trigger, Board b, String[] loc, Player p, Card card) {
		System.out.print("attempting to do plague-da");
		if (trigger.startsWith("turnend")) {
			System.out.print("ma");
			if (p.pname == card.owner) {
				System.out.print("g");
				if ((card.getCType().contains("creature"))||(card.getCType() == "hero")) {
					System.out.println("e to " + card.getCName());
					Creature c = (Creature) card;
					c.damage(damage);
					b.clearBoard();
				}
			}
		}
	}

	@Override
	public String[] getTrigger() {
		return new String[] {"turnend"};
	}

	@Override
	public String getTag() {
		return "plague";
	}
}
