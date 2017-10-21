package protocol;

import protocol.resources.Board;
import protocol.resources.Player;

public class Coreprotocol implements Runnable{
	Player[] players;
	public Board bd;
	private int turnDuration; //in seconds
	public Coreprotocol(Player[] players, int td) {
		this.players = players;
		bd = new Board(players);
		turnDuration = td;
	}
	@Override
	public void run() {
		int count = 0;
		Object exception = null;
		while (exception == null) {
			Player cp = players[count];
			count++;
			if (count == players.length) count = 0;
			bd.turnStart(cp);
			double startTime = System.currentTimeMillis();
			turn: while (true) {
				String ci = "";
				System.out.println("current player: " + cp.pName);
				while (ci.length() == 0) {
					ci = cp.getInput();
					if ((System.currentTimeMillis() - startTime) > turnDuration * 1000) ci = "end";
					try {
						Thread.sleep(33,333);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				ci.toLowerCase();
				try {
					if (ci.startsWith("move")) {
						String s1 = ci.substring(4,6).toUpperCase();
						String s2 = ci.substring(6,8).toUpperCase();
						System.out.println(s1 + " move to " + s2);
						bd.movement.moveOrAttackCreature(s1, s2, cp);
						continue turn;
					}
					if (ci.startsWith("hand")) {
						int index = ci.charAt(4) - '0';
						String targ = ci.substring(5, 7).toUpperCase();
						System.out.println("playing handcard at index " + index + " at tile " + targ);
						bd.fromhand.PlayCard(cp, targ, index);
						continue turn;
					}
					if (ci.startsWith("exec")) {
						ci = ci.substring(4);
						if (ci.startsWith("hand")) {
							
						}
						if (ci.startsWith("grave")) {
							
						}
						if (ci.startsWith("deck")) {
							
						}
						if (ci.startsWith("board")) {
							
						}
						continue turn;
					}
					if (ci.startsWith("place")) {
						String s1 = ci.substring(7,9).toUpperCase();
						bd.placement.placeLand(cp, s1, ci.substring(5,7).toLowerCase());
						System.out.println("placing " + ci.substring(5,7) + " at " + s1);
						continue turn;
					}
					if (ci.startsWith("end")) break turn;
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
