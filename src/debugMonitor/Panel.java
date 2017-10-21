package debugMonitor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import protocol.resources.Board;
import protocol.resources.Tile;
import protocol.resources.card.Creature;

public class Panel extends JPanel implements Runnable {
	private static final long serialVersionUID = -9094740729987708019L;
	private double lastframe;
	private double[] lastDeltaTimes = new double[100];
	private int iterate;
	private int framecount = 0;
	JFrame frame = new JFrame();
	Board board;
	public Panel(Board bd) {
		board = bd;
		frame.setSize(1280, 720);
		frame.add(this);
		frame.setVisible(true);
		frame.addWindowListener(new WindowListener() {@Override public void windowOpened(WindowEvent e) {}@Override public void windowIconified(WindowEvent e) {}@Override public void windowDeiconified(WindowEvent e) {}@Override public void windowDeactivated(WindowEvent e) {}@Override public void windowClosing(WindowEvent e) {System.exit(0);}@Override public void windowClosed(WindowEvent e) {}@Override public void windowActivated(WindowEvent e) {}});
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 1280, 720);
		double ctime = System.currentTimeMillis();
		if (iterate == 99) iterate = -1;
		iterate++;
		if (lastframe > 0) lastDeltaTimes[iterate] = ctime - lastframe;
		try {
			double sum = 0;
			int count = 0;
			for (double d : lastDeltaTimes) if (d > 0) {sum += d;count++;}
			framecount = (int)(1000 * count/sum);
		}catch (Exception e) {}
		lastframe = ctime;
		g.setColor(Color.BLACK);
		g.drawString(framecount+"", 0, 10);
		g.drawString("e: " + board.resources.get(board.playerNames[0])[0] + ", e-gain: " + board.resources.get(board.playerNames[0])[1] + ", ae: " + board.resources.get(board.playerNames[0])[2], 0, 20);
		for (String s : board.getVisionRange(board.playerNames[0])) {
			int x = (Math.abs(s.charAt(0) - 'A' - 4) + (s.charAt(1) - '0') * 2) * 30;
			int y = (s.charAt(0) - 'A') * 60 + 50;
			Tile t = board.get(s);
			int i = t.getGround().gType;
			g.setColor(new Color(255 - i*30, 255 - i*30, 255 - i*30));
			g.fillRect(x, y, 50, 50);
			g.setColor(new Color(100, 100, 100));
			if (t.getGround().owner.equals(board.playerNames[0])) g.setColor(new Color(255,0,0));
			if (t.getGround().owner.equals(board.playerNames[1])) g.setColor(new Color(100,100,255));
			g.drawRect(x, y, 50, 50);
			if (t.getGround().aeSourceCount >= 1) {
				g.setColor(Color.GREEN);
				g.drawRect(x + 1, y + 1, 48, 48);
				g.drawRect(x + 2, y + 2, 46, 46);
			}
			if (t.getCreature() != null) {
				Creature c = t.getCreature();
				g.setColor(new Color(0,0,0));
				g.fillRect(x + 10, y + 10, 30, 30);
				if (c.owner.equals(board.playerNames[0])) g.setColor(new Color(255,0,0));
				if (c.owner.equals(board.playerNames[1])) g.setColor(new Color(100,100,255));
				g.drawRect(x + 10, y + 10, 30, 30);
				g.setColor(new Color(0,150,0));
				g.drawString(""+c.getcAttack(), x + 10, y + 50);
				g.drawString(""+c.getcHealth(), x + 30, y + 50);
				g.drawString(""+c.getcCost(), x + 10, y + 10);
			}
		}
		g.setColor(Color.ORANGE);
		for (String s : board.movement.getMovementRange(board.getHeroLoc(board.playerNames[0]))) {
			int x = (Math.abs(s.charAt(0) - 'A' - 4) + (s.charAt(1) - '0') * 2) * 30;
			int y = (s.charAt(0) - 'A') * 60 + 50;
			g.drawRect(x - 1, y - 1, 52, 52);
		}
		for (int i = 0; i < board.hand.get("r").get().size(); i++) {
			int y = 600;
			int x = i * 40 + 50;
			g.setColor(Color.BLACK);
			g.fillRect(x, y, 30, 30);
		}
	}
	@Override
	public void run() {
		while (true) {
			try {
				if ((lastDeltaTimes[iterate] < 16)&&(lastDeltaTimes[iterate] >= 0))
					Thread.sleep((long)(16-lastDeltaTimes[iterate]));
				repaint();
			}catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
}
