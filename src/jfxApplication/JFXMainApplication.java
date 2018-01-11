package jfxApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;

import javax.swing.UIManager.LookAndFeelInfo;

import communication.CommBoard;
import communication.CommCard;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import launch.tests.BoardTest01;

public class JFXMainApplication extends Application {
	Integer size = 1000;
	public HashMap<String,Image> imgMap = new HashMap<>();
	
	final static int CANVAS_WIDTH = 1280;
    final static int CANVAS_HEIGHT = 720;
    final static int TILE_HEIGHT = CANVAS_HEIGHT / 10;
	
	final Canvas canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
	final GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
	

    double x0, y0, x1, y1;
    Image image;

    @Override
    public void start(final Stage primaryStage) {
        initDraw(graphicsContext);
        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, 
                new EventHandler<MouseEvent>(){

            @Override
            public void handle(MouseEvent event) {
                
                x0 = event.getX();
                y0 = event.getY();

            }
        });
        
        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, 
                new EventHandler<MouseEvent>(){

            @Override
            public void handle(MouseEvent event) {

            }
        });

        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, 
                new EventHandler<MouseEvent>(){

            @Override
            public void handle(MouseEvent event) {
                
                x1 = event.getX();
                y1 = event.getY();
                
                double x = (x0 > x1) ? x1 : x0;
                double y = (y0 > y1) ? y1 : y0;
                double w = (x0 > x1) ? x0-x1 : x1-x0;
                double h = (y0 > y1) ? y0-y1 : y1-y0;
                graphicsContext.drawImage(imgMap.get("fog"), x, y, w, h);
                
            }
        });

        Group root = new Group();
        VBox vBox = new VBox();
        vBox.getChildren().addAll(canvas);
        root.getChildren().add(vBox);
        Scene scene = new Scene(root, CANVAS_WIDTH, CANVAS_HEIGHT);
        primaryStage.setTitle("FreeBSD");
        primaryStage.setScene(scene);
        primaryStage.show();
        BoardTest01.jfxapp = this;
        System.out.println(this.toString());
        //graphicsContext.drawImage(imgMap.get("fog"), 400, 400);
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
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void DrawBoard(CommBoard cb) {
		System.out.println("drawing cb");
		//graphicsContext.clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
		for (String s : cb.board.keySet()) {
			try {
				int x = Math.abs(s.charAt(0) - 'E') * TILE_HEIGHT / 2 + Math.abs(s.charAt(1) - '1') * TILE_HEIGHT + 10;
				int y = Math.abs(s.charAt(0) - 'A') * TILE_HEIGHT * 84 / 100 + 10;
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
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void selflaunch() {
		launch();
	}
}
