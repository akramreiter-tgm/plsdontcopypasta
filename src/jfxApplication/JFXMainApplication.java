package jfxApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

import communication.CommBoard;
import communication.CommCard;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import launch.tests.BoardTest01;

public class JFXMainApplication extends Application {
	Integer size = 1000;
	public HashMap<String,Image> imgMap = new HashMap<>();
	public final BoardListener blisten = new BoardListener("r", "localhost");
	
	static int CANVAS_WIDTH = 845;
    static int CANVAS_HEIGHT = 600;
    static double TILE_HEIGHT = CANVAS_HEIGHT / 9.2;
	
	final Canvas canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
	final GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
	
	String displayedContent = "board";

    double x0, y0, x1, y1;
    Image image;
	private CommBoard commBoard;
	private CommCard cdisplay;
	private int xopt = -1;
	private int yopt = -1;

    @Override
    public void start(final Stage primaryStage) {
        //Thread bl = new Thread(blisten);
        //bl.start();
        BoardTest01.jfxapp = this;
        primaryStage.setFullScreenExitHint("");
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        initDraw(graphicsContext);
        Group root = new Group();
        VBox vBox = new VBox();
        vBox.getChildren().addAll(canvas);
        root.getChildren().add(vBox);
        Scene scene = new Scene(root, CANVAS_WIDTH, CANVAS_HEIGHT);
        primaryStage.setTitle("NullPointer");
        primaryStage.setScene(scene);
        //primaryStage.setFullScreen(true);
        primaryStage.show();
        scene.setOnMouseReleased(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				double x = event.getSceneX();
				double y = event.getSceneY();
				//x board min = TH * 0.1
				//x board max = TH * 9.1
				//y board min = TH * 0.17
				//y board max = TH * 7.73
				enc: if (displayedContent == "board") {
					if ((x >= TILE_HEIGHT * 0.1)&&(x <= TILE_HEIGHT * 9.1)&&(y >= 0.17 * TILE_HEIGHT)&&(y <= 7.73 * TILE_HEIGHT)) {
						System.out.println(((y - (0.17 * TILE_HEIGHT)) / 0.84 / TILE_HEIGHT));
						char first = (char)(((y - (0.17 * TILE_HEIGHT)) / 0.84 / TILE_HEIGHT) + 'A');
						char second = (char)((x - (0.1 + Math.abs(first - 'E')) * TILE_HEIGHT / 2) / TILE_HEIGHT + '1');
						if (second <= '0') break enc;
						if (second > ('9' - Math.abs(first - 'E'))) break enc;
						System.out.println(first + "" + second);
						try {
							cdisplay = commBoard.board.get(first+""+second).creature;
						}catch (Exception e) {
							// TODO: handle exception
						}
					}
					if ((y >= TILE_HEIGHT * 8.1)&&(y <= CANVAS_HEIGHT)) {
						int handid = (int)((x - (0.1 * TILE_HEIGHT)) / (TILE_HEIGHT * 1.05));
						System.out.println("hand"+handid);
						try {
							cdisplay = commBoard.hand[handid];
						}catch (Exception e) {
							// TODO: handle exception
						}
					}
				} else if ((xopt >= 0)&&(yopt >= 0)) {
					//TODO 
				} else {
					int idx = (int)((x - (0.1 * TILE_HEIGHT))/(TILE_HEIGHT * 1.1));
					int idy = (int)((y - (0.1 * TILE_HEIGHT))/(TILE_HEIGHT * 1.1));
					if (idx < 0) idx = 0;
					if (idx > 9) idx = 9;
					if (idy < 0) idy = 0;
					int idt = idy * 10 + idx;
					System.out.println("id:"+idt);
				}
				
			}
		});
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @SuppressWarnings("incomplete-switch")
			@Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case G: 
                    	if (displayedContent.startsWith("grave")) {
                    		displayedContent = "board";
                    	} else {
                    		displayedContent = "grave";
                    	}
                    	break;
                    case D: 
                    	if (displayedContent.startsWith("deck")) {
                    		displayedContent = "board";
                    	} else {
                    		displayedContent = "deck";
                    	}
                    	break;
                    case R:
                    	if (displayedContent.startsWith("removed")) {
                    		displayedContent = "board";
                    	} else {
                    		displayedContent = "removed";
                    	}
                    	break;
                    case E:
                    	if (displayedContent.startsWith("enemygrave")) {
                    		displayedContent = "board";
                    	} else {
                    		displayedContent = "enemygrave";
                    	}
                    	break;
                    case B:
                    	displayedContent = "board";
                    	break;
                    case X:
                    	if (displayedContent.startsWith("enemyremoved")) {
                    		displayedContent = "board";
                    	} else {
                    		displayedContent = "enemyremoved";
                    	}
                    	break;
                    case F:
                    	primaryStage.setFullScreen(!primaryStage.isFullScreen());
                    	if (primaryStage.isFullScreen()) {
                    		setDim(primaryStage.getWidth(), primaryStage.getHeight());
                    	} else {
                    		setDim(primaryStage.getWidth(), primaryStage.getHeight() - 45);
                    	}
                    	canvas.setWidth(CANVAS_WIDTH);
                    	canvas.setHeight(CANVAS_HEIGHT);
                }
                drawBoard(null);
            }
        });
        System.out.println(this.toString());
        //graphicsContext.drawImage(imgMap.get("fog"), 400, 400);
        graphicsContext.clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
        primaryStage.getIcons().add(imgMap.get("fog"));
    }
    
    private void initDraw(GraphicsContext gc){
    	loadGeneralTiles();
        double canvasWidth = gc.getCanvas().getWidth();
        double canvasHeight = gc.getCanvas().getHeight();
        
        gc.setFill(Color.LIGHTGRAY);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(5);

        gc.fill();
        gc.strokeRect(
                0,              //x of the upper left corner
                0,              //y of the upper left corner
                canvasWidth,    //width of the rectangle
                canvasHeight);  //height of the rectangle

        gc.setLineWidth(1);
        
        image = imgMap.get("res1");
    }
	
	public boolean loadTile(String cat, String name) {
		try {
			System.out.println("reading: \"resources\\"+cat+"\\" + name + ".png\"");
			imgMap.put(cat+name, new Image(new FileInputStream(new File("resources\\"+cat+"\\" + name + ".png"))));
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public void stop() {
		System.exit(0);
	}
	
	public void loadGeneralTiles() {
		try {
			imgMap.put("res1", new Image(new FileInputStream(new File("resources\\general\\Res1.png"))));
			imgMap.put("res2", new Image(new FileInputStream(new File("resources\\general\\Res2.png"))));
			imgMap.put("res3", new Image(new FileInputStream(new File("resources\\general\\Res3.png"))));
			imgMap.put("res4", new Image(new FileInputStream(new File("resources\\general\\Res4.png"))));
			imgMap.put("rframe", new Image(new FileInputStream(new File("resources\\general\\redFrame.png"))));
			imgMap.put("bframe", new Image(new FileInputStream(new File("resources\\general\\blueFrame.png"))));
			imgMap.put("fog", new Image(new FileInputStream(new File("resources\\general\\fogImg.png"))));
			imgMap.put("void", new Image(new FileInputStream(new File("resources\\general\\voidTile.png"))));
			imgMap.put("esrc", new Image(new FileInputStream(new File("resources\\general\\energySourceImg.png"))));
			imgMap.put("esrcv", new Image(new FileInputStream(new File("resources\\general\\energySourceVoid.png"))));
			imgMap.put("land", new Image(new FileInputStream(new File("resources\\general\\landImg.png"))));
			imgMap.put("cardframe", new Image(new FileInputStream(new File("resources\\general\\cardFrame.png"))));
			for (int i = 0; i < 10; i++) {
				imgMap.put("char" + i, new Image(new FileInputStream(new File("resources\\general\\font\\" + i + ".png"))));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Draws the most recently received CommBoard
	 * also calls function to display partView when displayedContent isn't "board"
	 * @param cb
	 */
	public void drawBoard(CommBoard cb) {
		if (cb != null) {
			commBoard = cb;
		} else {
			cb = commBoard;
		}
		System.out.println("drawing cb");
		graphicsContext.clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
		for (String s : cb.board.keySet()) {
			try {
				double x = Math.abs(s.charAt(0) - 'E') * TILE_HEIGHT / 2 + Math.abs(s.charAt(1) - '1') * TILE_HEIGHT + 10;
				double y = Math.abs(s.charAt(0) - 'A') * TILE_HEIGHT * 0.84 + 10;
				//System.out.println("ground type " + cb.board.get(s).ground.gType + "(" + s + ") , drawn at x:" + x + " y:" + y);
				switch (cb.board.get(s).ground.gType) {
				case -1 : graphicsContext.drawImage(imgMap.get("fog"), x, y, TILE_HEIGHT, TILE_HEIGHT); break;
				case 0 : graphicsContext.drawImage(imgMap.get("void"), x, y, TILE_HEIGHT, TILE_HEIGHT); break;
				case 1 : graphicsContext.drawImage(imgMap.get("land"), x, y, TILE_HEIGHT, TILE_HEIGHT); break;
				case 2 : graphicsContext.drawImage(imgMap.get("res1"), x, y, TILE_HEIGHT, TILE_HEIGHT); break;
				case 3 : graphicsContext.drawImage(imgMap.get("res2"), x, y, TILE_HEIGHT, TILE_HEIGHT); break;
				case 4 : graphicsContext.drawImage(imgMap.get("res3"), x, y, TILE_HEIGHT, TILE_HEIGHT); break;
				case 5 : graphicsContext.drawImage(imgMap.get("res4"), x, y, TILE_HEIGHT, TILE_HEIGHT); break;
				}
				
				if (cb.board.get(s).ground.aeSourceCount >= 1) {
					if (cb.board.get(s).ground.gType >= 1) {
						graphicsContext.drawImage(imgMap.get("esrc"), x, y, TILE_HEIGHT, TILE_HEIGHT);
					} else if (cb.board.get(s).ground.gType == 0) {
						graphicsContext.drawImage(imgMap.get("esrcv"), x, y, TILE_HEIGHT, TILE_HEIGHT);
					}
				}
				if (cb.board.get(s).ground.gType >= 1) {
					if (cb.board.get(s).ground.owner.startsWith(cb.player)) {
						graphicsContext.drawImage(imgMap.get("rframe"), x, y, TILE_HEIGHT, TILE_HEIGHT);
					} else {
						graphicsContext.drawImage(imgMap.get("bframe"), x, y, TILE_HEIGHT, TILE_HEIGHT);
					}
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		for (String s : cb.board.keySet()) {
			try {
				double x = Math.abs(s.charAt(0) - 'E') * TILE_HEIGHT / 2 + Math.abs(s.charAt(1) - '1') * TILE_HEIGHT + 10;
				double y = Math.abs(s.charAt(0) - 'A') * TILE_HEIGHT * 84 / 100 + 10;
				if (cb.board.get(s).creature != null) {
					CommCard cc = cb.board.get(s).creature;
					if (imgMap.get(cc.ctype + cc.cname) != null) {
						graphicsContext.drawImage(imgMap.get(cc.ctype + cc.cname), x, y, TILE_HEIGHT, TILE_HEIGHT);
					}
					else {
						if (loadTile(cc.ctype, cc.cname)) {
							graphicsContext.drawImage(imgMap.get(cc.ctype + cc.cname), x, y, TILE_HEIGHT, TILE_HEIGHT);
						}
					}
					ArrayList<Integer> atk = new ArrayList<>();
					ArrayList<Integer> hp = new ArrayList<>();
					int attack = cc.atk;
					int health = cc.health;
					if (attack == 0) atk.add(0);
					if (health == 0) hp.add(0);
					System.out.println(health);
					while (attack > 0) {
						atk.add(attack % 10);
						attack = attack / 10;
					}
					while (health > 0) {
						hp.add(health % 10);
						health = health / 10;
					}
					
					System.out.println(cc.cname + ": " + cc.atk + " Atk, " + cc.health + " Health");
					for (int i = 0; i < atk.size(); i++) {
						graphicsContext.drawImage(imgMap.get("char"+atk.get(atk.size() - 1 - i).toString()), x + TILE_HEIGHT * (0.18 * i + 0.05), y + TILE_HEIGHT * 0.65, TILE_HEIGHT * 0.25, TILE_HEIGHT * 0.35);
					}
					for (int i = 0; i < hp.size(); i++) {
						graphicsContext.drawImage(imgMap.get("char"+hp.get(hp.size() - 1 - i).toString()), x + TILE_HEIGHT * (0.9 - 0.18 * (hp.size() - i)), y + TILE_HEIGHT * 0.65, TILE_HEIGHT * 0.25, TILE_HEIGHT * 0.35);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				graphicsContext.setFill(Color.BLACK);
				graphicsContext.fillText("Energy: " + cb.energy, TILE_HEIGHT * 9.5, 35);
				graphicsContext.fillText("Energygain: " + cb.energygain, TILE_HEIGHT * 9.5, 50);
				graphicsContext.fillText("AltEnergy: " + cb.aen, TILE_HEIGHT * 9.5, 65);
				graphicsContext.fillText("EnemyEnergy: " + cb.enemyenergy, TILE_HEIGHT * 9.5, 80);
				graphicsContext.fillText("EnemyAltEnergy: " + cb.enemyaen, TILE_HEIGHT * 9.5, 95);
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		for (int i = 0; i < cb.hand.length; i++) {
			CommCard cc = cb.hand[i];
			double x = TILE_HEIGHT * 0.1 + i * TILE_HEIGHT * 1.05;
			double y = TILE_HEIGHT * 8.1;
			graphicsContext.drawImage(imgMap.get("cardframe"), x, y, TILE_HEIGHT, TILE_HEIGHT);
			if (imgMap.get(cc.ctype + cc.cname) != null) {
				graphicsContext.drawImage(imgMap.get(cc.ctype + cc.cname), x, y, TILE_HEIGHT, TILE_HEIGHT);
			}
			else {
				if (loadTile(cc.ctype, cc.cname)) {
					graphicsContext.drawImage(imgMap.get(cc.ctype + cc.cname), x, y, TILE_HEIGHT, TILE_HEIGHT);
				}
			}
		}
		switch (displayedContent) {
		case "grave":
			DrawCardsInForeground(cb.grave);
			break;
		case "deck":
			DrawCardsInForeground(cb.deck);
			break;
		case "removed":
			DrawCardsInForeground(cb.removed);
			break;
		case "enemygrave":
			DrawCardsInForeground(cb.enemygrave);
			break;
		case "enemyremoved":
			DrawCardsInForeground(cb.enemyremoved);
			break;
		}
	}
	
	/**
	 * grays out board draws by drawBoard(CommBoard).
	 * Draws cards in CommCard[] cl over it.
	 * @param cl
	 */
	public void DrawCardsInForeground(CommCard[] cl) {
		graphicsContext.setFill(Color.rgb(0, 0, 0, 0.6));
		graphicsContext.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
		for (int i = 0; i < cl.length; i++) {
			CommCard cc = cl[i];
			double x = TILE_HEIGHT * 0.2 + (i % 10) * TILE_HEIGHT * 1.1;
			double y = TILE_HEIGHT * (i / 10) * 1.1 + TILE_HEIGHT * 0.2;
			graphicsContext.drawImage(imgMap.get("cardframe"), x, y, TILE_HEIGHT, TILE_HEIGHT);
			if (imgMap.get(cc.ctype + cc.cname) != null) {
				graphicsContext.drawImage(imgMap.get(cc.ctype + cc.cname), x, y, TILE_HEIGHT, TILE_HEIGHT);
			}
			else {
				if (loadTile(cc.ctype, cc.cname)) {
					graphicsContext.drawImage(imgMap.get(cc.ctype + cc.cname), x, y, TILE_HEIGHT, TILE_HEIGHT);
				}
			}
		}
	}
	
	public static void selflaunch() {
		launch();
	}
	
	public static void setDim(double w, double h) {
		if (w > 0) CANVAS_WIDTH = (int) w;
		if (h > 0) CANVAS_HEIGHT = (int) h;
		TILE_HEIGHT = h / 9.2;
	}
}
