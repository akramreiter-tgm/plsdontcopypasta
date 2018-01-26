package communication;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import protocol.resources.Player;

public class ServerEndpoint extends Player {
	private static final long serialVersionUID = 6368853442270136399L;
	protected Player endPoint;
	protected Socket conn;
	protected ObjectInputStream in;
	protected ObjectOutputStream out;
	
	public ServerEndpoint(String name) throws UnknownHostException, IOException, ParseException {
		super(name);
		JSONParser jp = new JSONParser();
		JSONObject jo = (JSONObject) jp.parse(new FileReader(new File("config\\network.json")));
		//InetAddress iad = InetAddress.getByName(iaddress);
		System.out.println("port:" + jo.get("port"));
		Long l = (Long) jo.get("port");
		ServerSocket sv = new ServerSocket(l.intValue());
		conn = sv.accept();
		in = new ObjectInputStream(conn.getInputStream());
		out = new ObjectOutputStream(conn.getOutputStream());
	}

	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		while (true) {
			try {
				while (commQueue.size() >= 0) {
					out.writeObject(commQueue.get(0));
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
			try {
				String s = in.readLine();
				if (s.length() > 0) {
					inputQueue.add(s);
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
			try {
				Thread.sleep(100);
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
