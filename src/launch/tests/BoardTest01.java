package launch.tests;

import java.util.Scanner;

import communication.CommBoard;
import communication.CommMsg;
import debugMonitor.Panel;
import debugMonitor.launchJFXThread;
import deckBuilding.CardFactory;
import deckBuilding.ultCreature.SampleUltCreature;
import jfxApplication.JFXMainApplication;
import protocol.Coreprotocol;
import protocol.resources.Player;
import protocol.resources.card.CardList;
import protocol.resources.card.Deck;

@SuppressWarnings("unused")
public class BoardTest01 {
	
	public static JFXMainApplication jfxapp;
	
	public static void main(String[] args) {
		Player r = new Player("r");
		Player b = new Player("b");
		Coreprotocol core = new Coreprotocol(new Player[] {r,b}, 120);
		Deck dr = new Deck();
		Deck db = new Deck();
		CardList sdr = new CardList();
		CardList sdb = new CardList();
		String[] cards = new String[] {"creature.plague01","creature.plague02"};
		for (@SuppressWarnings("unused") int i : new int[45]) {
			dr.addToDeck(CardFactory.createCard(cards[(int)(cards.length * Math.random())], "r"));
			db.addToDeck(CardFactory.createCard(cards[(int)(cards.length * Math.random())], "b"));
		}
		for (@SuppressWarnings("unused") int i : new int[10]) {
			sdr.add(new SampleUltCreature("r"));
			sdb.add(new SampleUltCreature("b"));
		}
		core.bd.deck.remove("r");
		core.bd.deck.remove("b");
		core.bd.deck.put("r",dr);
		core.bd.deck.put("b",db);
		core.bd.sidedeck.remove("r");
		core.bd.sidedeck.remove("b");
		core.bd.sidedeck.put("r",sdr);
		core.bd.sidedeck.put("b",sdb);
		//Panel p = new Panel(core.bd);
		String[] str = core.bd.getOrderedFullBoard();
		for (String s : str) {System.out.print(s + ", ");}
		System.out.println();
		Thread ct = new Thread(core);
		//Thread pt = new Thread(p);
		CommQueuePrinter cqp = new BoardTest01().new CommQueuePrinter(r, b);
		Thread cq = new Thread(cqp);
		ct.start();
		//pt.start();
		cq.start();
		new Thread(new launchJFXThread()).start();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("from boardTest: " + jfxapp.toString());
		CommBoard ccb = new CommBoard(core.bd, null, "r");
		jfxapp.drawBoard(ccb);
		while (true) {
			try {
				@SuppressWarnings("resource")
				Scanner sc = new Scanner(System.in);
				String s = sc.nextLine();
				r.inputQueue.add(s);
				b.inputQueue.add(s);
				Thread.sleep(100);
				ccb = new CommBoard(core.bd, null, "r");
				jfxapp.drawBoard(ccb);
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public class CommQueuePrinter implements Runnable {
		
		Player r,b;
		
		public CommQueuePrinter(Player p1, Player p2) {
			r = p1;
			b = p2;
		}

		@Override
		public void run() {
			while (true) {
				for (Object o : r.commQueue.toArray(new Object[0])) {
					if (o instanceof CommMsg) {
						System.out.println("------------------");
						CommMsg cmg = (CommMsg) o;
						System.out.println("red commqueue: " + cmg.description);
						//System.out.print(cmg.content.getClass().toGenericString());
						if (cmg.content instanceof int[]) {
							for (int i : (int[]) cmg.content) {
								System.out.print(i + ", ");
							}
						}
						if (cmg.content instanceof String[]) {
							for (String i : (String[]) cmg.content) {
								System.out.print(i + ", ");
							}
						}
						System.out.println("\n------------------");
					}
					r.commQueue.remove(o);
				}
				for (Object o : b.commQueue.toArray(new Object[0])) {
					if (o instanceof CommMsg) {
						System.out.println("------------------");
						CommMsg cmg = (CommMsg) o;
						System.out.println("blue commqueue: " + cmg.description);
						//System.out.print(cmg.content.getClass().toGenericString());
						if (cmg.content instanceof int[]) {
							for (int i : (int[]) cmg.content) {
								System.out.print(i + ", ");
							}
						}
						if (cmg.content instanceof String[]) {
							for (String i : (String[]) cmg.content) {
								System.out.print(i + ", ");
							}
						}
						System.out.println("\n------------------");
					}
					b.commQueue.remove(o);
				}
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
