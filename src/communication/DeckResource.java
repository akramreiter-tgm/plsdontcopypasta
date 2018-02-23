package communication;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;

public class DeckResource implements Serializable {
	private static final long serialVersionUID = -7292036278321979879L;
	public String[] deck = new String[45];
	public String[] side = new String[10];
	public DeckResource(String name) {
		try {
			RandomAccessFile rf = new RandomAccessFile(new File("deck\\" + name), "rw");
			HashMap<String,Integer> dupe = new HashMap<>();
			HashSet<String> sidedupe = new HashSet<>();
			int iterd = 0;
			int iters = 0;
			String mode = "none";
			String rl = "";
			brake: while (rl != "#end") {
				switch (mode) {
				case "deck":
					if (rl.startsWith("#return")||iterd >= 45) {
						mode = "none";
					} else {
						if (rl.length() > 0) {
							if (dupe.get(rl) == null) {
								deck[iterd] = rl;
								iterd += 1;
								dupe.put(rl, 1);
							} else if (dupe.get(rl) <= 2) {
								deck[iterd] = rl;
								iterd += 1;
								Integer i = dupe.get(rl);
								i += 1;
								dupe.put(rl,i);
							}
						}
					}
					break;
				case "side":
					if (rl.startsWith("#return")||iters >= 10) {
						mode = "none";
					} else {
						if (rl.length() > 0) {
							if (!sidedupe.contains(rl)) {
								side[iters] = rl;
								iters += 1;
								sidedupe.add(rl);
							}
						}
					}
					break;
				default:
					if (rl.startsWith("#")) {
						mode = rl.substring(1);
					}
					if (iterd >= 45 && iters >= 10) break brake;
				}
				try {
					rl = rf.readLine().trim();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
