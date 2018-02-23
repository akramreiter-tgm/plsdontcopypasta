package communication;

import java.io.IOException;
import java.net.UnknownHostException;

import org.json.simple.parser.ParseException;

import protocol.Coreprotocol;
import protocol.resources.Player;

public class CpHosting implements Runnable {
	Thread corethread;
	@Override
	public void run() {
		ServerEndpoint[] p = new ServerEndpoint[2];
		while (p[0] == null) {
			try {
				p[0] = new ServerEndpoint();
				p[0].connect();
			} catch (Exception e) {
				p[0] = null;
				e.printStackTrace();
			}
		}
		System.out.println("player 1 connected");
		while (p[1] == null) {
			try {
				p[1] = new ServerEndpoint();
				p[1].connect();
			} catch (Exception e) {
				p[1] = null;
				e.printStackTrace();
			}
		}
		while (p[0].pname == null || p[1].pname == null || p[0].deck == null || p[1].deck == null) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		corethread = new Thread(new Coreprotocol(p, 120));
	}

}
