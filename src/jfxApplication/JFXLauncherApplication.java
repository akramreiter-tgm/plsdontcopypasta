package jfxApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class JFXLauncherApplication extends Application {
	
	final Canvas canvas = new Canvas(400,400);
	final GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
	@Override
	public void start(Stage primaryStage) throws Exception {
		Group root = new Group();
        VBox vBox = new VBox();
        vBox.getChildren().addAll(canvas);
        root.getChildren().add(vBox);
        Scene boardScene = new Scene(root, 400,400);
        primaryStage.setTitle("NullPointer");
        primaryStage.setScene(boardScene);
        //primaryStage.setFullScreen(true);
        
        graphicsContext.drawImage(new Image(new FileInputStream(new File("resources\\general\\whatever.png"))), 0, 0);
        boardScene.setOnMouseReleased(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				double y = event.getSceneY();
				double x = event.getSceneX();
				if (x >= 0 && x <= 400 && y >= 0 && y <= 400) {
					if (y > 200) {
						try {
							graphicsContext.drawImage(new Image(new FileInputStream(new File("resources\\general\\whatever.png"))), x, y);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
						//TODO join
					} else {
						System.out.println("hosting now");
						//TODO host
						
					}
				}
			}
		});
        primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
