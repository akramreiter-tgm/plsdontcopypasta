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

public class BoardListener implements Runnable {
	private String nextmsg = "";
	private HashMap<String,String> contextData = new HashMap<>();
	private Socket endpoint;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private JFXMainApplication jfxapp;
	public static String iadr = "localhost";
	
	public BoardListener(JFXMainApplication app, String name) {
		jfxapp = app;
		try {
			initSocket(iadr);
			ois = new ObjectInputStream(endpoint.getInputStream());
			oos = new ObjectOutputStream(endpoint.getOutputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
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
	
	public void initSocket(String iaddress) throws Exception {
		JSONParser jp = new JSONParser();
		JSONObject jo = (JSONObject) jp.parse(new FileReader(new File("config\\network.json")));
		InetAddress iad = InetAddress.getByName(iaddress);
		System.out.println("port:" + jo.get("port"));
		Long l = (Long) jo.get("port");
		endpoint = new Socket(iad, l.intValue());
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
						oos.writeChars(nextmsg);
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
					oos.writeChars(nextmsg);
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
