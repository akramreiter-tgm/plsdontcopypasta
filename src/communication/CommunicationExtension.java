package communication;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import protocol.resources.Player;

public class CommunicationExtension extends Player {
	private static final long serialVersionUID = 6368853442270136399L;
	protected Player endPoint;
	protected Socket conn1, conn2;
	protected ObjectInputStream in;
	protected ObjectOutputStream out;
	
	public CommunicationExtension(String name, String targIp, int port) throws UnknownHostException, IOException {
		super(name);
		conn1 = new Socket(targIp, port);
		conn1.bind(new InetSocketAddress(targIp, port));
		try {
			Thread.sleep(100); //avoid spam
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		conn2 = new Socket(targIp, port);
		conn2.bind(new InetSocketAddress(targIp, port));
		in = new ObjectInputStream(conn1.getInputStream());
		out = new ObjectOutputStream(conn2.getOutputStream());
	}
}
