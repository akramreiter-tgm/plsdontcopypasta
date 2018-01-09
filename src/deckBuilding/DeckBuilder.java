package deckBuilding;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import deckBuilding.hero.BasicHero;
import protocol.resources.card.Hero;

public class DeckBuilder {
	
	public static DeckContainer build(String deckname, String owner) {
		try {
			@SuppressWarnings("resource")
			RandomAccessFile source = new RandomAccessFile(new File("deck/"+deckname+".deck"), "r");
			String s = source.readLine().trim();
			DeckContainer output = new DeckContainer();
			try {
				loop: while (true) {
					switch (s) {
					case "#main":
						s = source.readLine().trim();
						while ((s != null)&&(s.length() > 0)) {
							if (!s.startsWith("ultcreature."))
								output.main.get().add(CardFactory.createCard(s, owner));
							s = source.readLine().trim();
						}
						break;
					case "#side":
						s = source.readLine().trim();
						while ((s != null)&&(s.length() > 0)) {
							if (s.startsWith("ultcreature."))
								output.side.get().add(CardFactory.createCard(s, owner));
							s = source.readLine().trim();
						}
						break;
					case "#hero":
						s = source.readLine().trim();
						while ((s != null)&&(s.length() > 0)) {
							if (s.startsWith("hero."))
								output.hero = (Hero) CardFactory.createCard(s, owner);
							s = source.readLine().trim();
						}
						break;
					case "#end":
						break loop;
					}
				}
			} catch (StackOverflowError e) {
				e.printStackTrace();
				System.out.println("TLDR: some idiot forgot to include \"#end\" in his .deck file \n");
			}
			if (output.hero == null) {
				output.hero = new BasicHero(owner);
			}
			return output;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
