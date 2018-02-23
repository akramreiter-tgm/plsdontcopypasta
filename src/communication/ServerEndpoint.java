package communication;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import protocol.resources.Player;

public class ServerEndpoint extends Player {
	private static final long serialVersionUID = 6368853442270136399L;
	protected Socket conn;
	protected ObjectInputStream in;
	protected ObjectOutputStream out;
	private boolean namefound = false;
	
	public ServerEndpoint() throws UnknownHostException, IOException, ParseException {
		super(null);
	}

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
				if (namefound) {
					Object o = in.readObject();
					if (o instanceof String) {
						String s = o.toString();
						if (s.length() > 0) {
							inputQueue.add(s);
						}
					} else if (o instanceof DeckResource) {
						deck = (DeckResource) o;
					}
				} else {
					pname = in.readUTF();
					namefound = true;
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
	
	public void connect() {
		try {
			JSONParser jp = new JSONParser();
			JSONObject jo = (JSONObject) jp.parse(new FileReader(new File("config\\network.json")));
			//InetAddress iad = InetAddress.getByName(iaddress);
			System.out.println("port:" + jo.get("port"));
			Long l = (Long) jo.get("port");
			ServerSocket sv = new ServerSocket(l.intValue());
			System.out.println("now listening on port " + l.intValue());
			conn = sv.accept();
			System.out.println("connection successful");
			in = new ObjectInputStream(conn.getInputStream());
			out = new ObjectOutputStream(conn.getOutputStream());
			System.out.println("streams created");
		}catch (Exception e) {
			// TODO: handle exception
		}
	}
}
