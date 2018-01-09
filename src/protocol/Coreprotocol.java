package protocol;

import java.util.Arrays;
import java.util.HashSet;

import communication.CommMsg;
import protocol.resources.Board;
import protocol.resources.Player;

public class Coreprotocol implements Runnable{
	Player[] players;
	public Board bd;
	private static int turnDuration; //in seconds
	private static Player cp;
	public static double startTime;
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
			cp = players[count];
			count++;
			if (count == players.length) count = 0;
			bd.turnStart(cp);
			cp.inputQueue.clear(); //prevents queueing up input before turn start
			startTime = System.currentTimeMillis();
			turn: while (true) {
				String ci = "";
				System.out.println("current player: " + cp.pname);
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
						bd.activation.activate(cp, ci);
						continue turn;
					}
					if (ci.startsWith("place")) {
						String s1 = ci.substring(7,9).toUpperCase();
						bd.placement.placeLand(cp, s1, ci.substring(5,7).toLowerCase());
						System.out.println("placing " + ci.substring(5,7) + " at " + s1);
						continue turn;
					}
					if (ci.startsWith("evolve")) {
						bd.evolve.evolve(cp,ci.substring(6,8).toUpperCase(),Integer.parseInt(ci.substring(8)));
					}
					if (ci.startsWith("end")) {
						bd.triggerExecutableEffects(cp, "turnend", new String[] {});
						break turn;
					}
					if (ci.startsWith("show")) {
						ci = ci.substring(4);
						if (ci.startsWith("board")) {
							String[] s1 = bd.activation.getAvActCardsBoard(cp.pname);
							String[] s2 = bd.movement.getMoveOrAttackAvCreatures(cp.pname);
							HashSet<String> out = new HashSet<>(Arrays.asList(s1));
							out.addAll(Arrays.asList(s2));
							cp.commQueue.add(new CommMsg("cardsboard", out.toArray(new String[0]))); //TODO add activateable cards on board
						} else if (ci.startsWith("hand")) {
							int[] s1 = bd.activation.getAvActCardsHand(cp.pname);
							int[] s2 = bd.fromhand.getPlayableCards(cp.pname);
							int[] out = new int[s1.length + s2.length];
							for (int i = 0; i < out.length; i++) {
								if (i < s1.length) {
									out[i] = s1[i];
								} else {
									out[i] = s2[i - s1.length];
								}
							}
							int duplicates = 0;
							Arrays.sort(out);
							if (out.length > 1) {
								for (int i = 0; i < out.length - 1; i++) {
									if (out[i] == out[i + 1]) duplicates++;
								}
							}
							int[] nodupes = new int[out.length - duplicates];
							int offset = 0;
							for (int i = 0; i < nodupes.length; i++) {
								nodupes[i] = out[i+offset];
								if (i != nodupes.length - 1) {
									while (nodupes[i] == out[i+offset+1]) {
										offset++;
									}
								}
							}
							cp.commQueue.add(new CommMsg("cardshand",nodupes)); //TODO add activateable hand cards
						} else if (ci.startsWith("evolve1")) {
							//TODO add possible evolutions
						} else if (ci.startsWith("evolve2")) {
							//TODO add evolve targets
						} else if (ci.startsWith("place1")) {
							cp.commQueue.add(new CommMsg("grplace", bd.placement.getAvailablePlacement(cp.pname)));
						} else if (ci.startsWith("place2")) {
							cp.commQueue.add(new CommMsg("resplace", bd.placement.getAvailableResGround(cp.pname)));
						} else if (ci.startsWith("activebd")) {
							cp.commQueue.add(new CommMsg("actboard", bd.activation.getAvActCardsBoard(cp.pname)));
						} else if (ci.startsWith("grave")) {
							//TODO add activateable cards in grave
						} else if (ci.startsWith("deck")) {
							//TODO add activateable cards in deck
						} else if (ci.startsWith("removed")) {
							//TODO add activateable cards in removed
						} else if (ci.startsWith("move")) {
							cp.commQueue.add(new CommMsg("cardsboard", bd.movement.getMoveOrAttackAvCreatures(cp.pname))); //TODO add activateable cards on board
						}
					}
					if (ci.startsWith("surr")) System.exit(0); //TODO handle the situation of a player surrendering properly instead of just quitting
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * gets the most recent input from the current player
	 * @return String
	 */
	public static String getInput() {
		String ci = "";
		while (ci.length() == 0) {
			ci = cp.getInput();
			if ((System.currentTimeMillis() - startTime) > turnDuration * 1000) ci = "end";
			try {
				Thread.sleep(33,333);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("returned input: " + ci);
		return ci.toLowerCase();
	}
}
