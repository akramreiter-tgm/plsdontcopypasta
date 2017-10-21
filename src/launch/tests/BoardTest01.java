package launch.tests;

import java.util.Scanner;

import debugMonitor.Panel;
import protocol.Coreprotocol;
import protocol.resources.Player;
import protocol.resources.card.Deck;
import protocol.resources.card.creature.SampleCreature;

public class BoardTest01 {
	public static void main(String[] args) {
		Player r = new Player("r");
		Player b = new Player("b");
		Coreprotocol core = new Coreprotocol(new Player[] {r,b}, 120);
		Deck dr = new Deck();
		Deck db = new Deck();
		for (@SuppressWarnings("unused") int i : new int[45]) {
			dr.addToDeck(new SampleCreature("r"));
			db.addToDeck(new SampleCreature("b"));
		}
		core.bd.deck.remove("r");
		core.bd.deck.remove("b");
		core.bd.deck.put("r",dr);
		core.bd.deck.put("b",db);
		Panel p = new Panel(core.bd);
		String[] str = core.bd.getOrderedFullBoard();
		for (String s : str) {System.out.print(s + ", ");}
		System.out.println();
		Thread ct = new Thread(core);
		Thread pt = new Thread(p);
		ct.start();
		pt.start();
		boolean p1 = true;
		while (true) {
			@SuppressWarnings("resource")
			Scanner sc = new Scanner(System.in);
			String s = sc.nextLine();
			if (p1) {r.inputQueue.add(s);} else {b.inputQueue.add(s);}
			if (s.startsWith("end")) {
				System.out.println("playerInput switched");
				if (p1) {p1=false;}else{p1=true;}
			}
		}
	}
}
