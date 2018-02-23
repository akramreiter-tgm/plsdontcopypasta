package jfxApplication;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.json.simple.*;
import org.json.simple.parser.*;

import communication.CommBoard;
import communication.CommMsg;
import communication.DeckResource;

public class BoardListener implements Runnable {
	private String nextmsg = "";
	private HashMap<String,String> contextData = new HashMap<>();
	private Socket endpoint;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private JFXMainApplication jfxapp;
	private String iadr = "localhost", name, deck;
	
	public BoardListener(JFXMainApplication app, String name, String adr, String deck) {
		jfxapp = app;
		iadr = adr;
		this.name = name;
		this.deck = deck;
	}
	
	@Override
	public void run() {
		while (true) {
			try {
				Object o = ois.readObject();
				if (o instanceof CommBoard) {
					jfxapp.drawBoard((CommBoard) o);
				} else if (o instanceof CommMsg) {
					ArrayList<CommMsg> cmg = new ArrayList<>(Arrays.asList(jfxapp.commBoard.messages));
					cmg.add((CommMsg) o);
					jfxapp.commBoard.messages = cmg.toArray(new CommMsg[0]);
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
			try {
				Thread.sleep(100);
			}catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
	
	public void connect() {
		try {
			for (int i : new int[25]) {
				try {
					JSONParser jp = new JSONParser();
					JSONObject jo = (JSONObject) jp.parse(new FileReader(new File("config\\network.json")));
					InetAddress iad = InetAddress.getByName(iadr);
					System.out.println("port:" + jo.get("port"));
					Long l = (Long) jo.get("port");
					System.out.println("connecting to " + iad.toString());
					endpoint = new Socket(iad, l.intValue());
					System.out.println("connection successful");
					ois = new ObjectInputStream(endpoint.getInputStream());
					oos = new ObjectOutputStream(endpoint.getOutputStream());
					System.out.println("streams created");
					break;
				}catch (Exception e) {
					// TODO: handle exception
				}
				try {
					Thread.sleep(200);
				}catch (Exception e) {
					// TODO: handle exception
				}
			}
			System.out.println("socket connected");
			oos.writeUTF(name);
			System.out.println("output/input streams gotten");
			DeckResource dr = new DeckResource(deck);
			System.out.println("deckresource created");
			oos.writeObject(dr);
			System.out.println("deckresource sent");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void initSocket(String iaddress) throws Exception {
		
	}
	
	/**
	 * @param opt
	 */
	public void amendNextMsg(String opt) {
		System.out.println("Boardlistener received: " + opt);
		if (nextmsg == "") {
			if (opt.startsWith("place")) {
				nextmsg = opt;
			} else if (opt.startsWith("moveav")) {
				String alone = contextData.get("standalone");
				if (alone != null) {
					if (alone.startsWith("board")) {
						nextmsg += "move"+alone.substring(5);
					}
				}
			} else if (opt.startsWith("acteffav")) {
				String alone = contextData.get("standalone");
				if (alone != null) {
					nextmsg += "exec"+alone;
					try {
						oos.writeUTF(nextmsg);
						System.out.println("sent message: " + nextmsg);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					nextmsg = "";
				}
			} else {
				contextData.put("standalone", opt);
			}
		} else if (nextmsg.startsWith("place")) {
			if (opt.startsWith("board")) {
				nextmsg += opt.substring(5);
				try {
					oos.writeUTF(nextmsg);
					System.out.println("sent message: " + nextmsg);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				nextmsg = "";
			}
		} else if (nextmsg.startsWith("move")) {
			if (opt.startsWith("board")) {
				try {
					oos.writeChars(nextmsg + opt.substring(5));
					System.out.println("sent message: " + nextmsg);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				nextmsg = "";
			}
		}
	}
}
