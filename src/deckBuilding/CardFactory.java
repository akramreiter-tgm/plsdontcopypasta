package deckBuilding;

import deckBuilding.creature.*;
import protocol.resources.card.Card;
import protocol.resources.card.Creature;
import protocol.resources.card.Hero;
import protocol.resources.card.Spell;
import protocol.resources.card.UltCreature;

public class CardFactory {

	public static Card createCard(String s, String own) {
		Card out = null;
		if (s.startsWith("creature.")) {
			out = createCreature(s.substring(s.indexOf('.') + 1), own);
		} else if (s.startsWith("spell.")) {
			out = createSpell(s.substring(s.indexOf('.') + 1), own);
		} else if (s.startsWith("hero.")) {
			out = createHero(s.substring(s.indexOf('.') + 1), own);
		} else if (s.startsWith("ultcreature.")) {
			out = createUltCreature(s.substring(s.indexOf('.') + 1), own);
		}
		System.out.println(out.getCName() + ", " + out.owner);
		return out;
	}
	
	private static UltCreature createUltCreature(String substring, String own) {
		switch (substring) {
		
		default: return null;
		}
	}

	private static Hero createHero(String substring, String own) {
		switch (substring) {
		
		default: return null;
		}
	}

	private static Spell createSpell(String substring, String own) {
		switch (substring) {
		
		default: return null;
		}
	}

	private static Creature createCreature(String substring, String own) {
		switch (substring) {
		case "plague01": return new Plague01(own);
		case "plague02": return new Plague02(own);
		
		default: return null;
		}
	}

}
