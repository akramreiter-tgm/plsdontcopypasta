package jfxApplication;

import java.io.File;
import java.io.FileReader;
import java.net.InetAddress;
import java.net.Socket;
import protocol.resources.Player;
import org.json.simple.*;
import org.json.simple.parser.*;

public class BoardListener extends Player implements Runnable {
	private static final long serialVersionUID = 3132223538611747663L;
	
	private String nextmsg;
	private Socket endpoint;
	
	public BoardListener(String name, String iadr) {
		super(name);
		try {
			initSocket(iadr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated constructor stub
	}
	
	
	
	@Override
	public void run() {
		
	}
	
	public void initSocket(String iaddress) throws Exception {
		JSONParser jp = new JSONParser();
		JSONObject jo = (JSONObject) jp.parse(new FileReader(new File("config\\network.json")));
		InetAddress iad = InetAddress.getByName(iaddress);
		System.out.println("port:" + jo.get("port"));
		Long l = (Long) jo.get("port");
		endpoint = new Socket(iad, l.intValue());
		//endpoint = new Socket(iad, new Jso)
	}
}
