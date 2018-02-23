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
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class JFXLauncherApplication extends Application {
	
	final Canvas canvas = new Canvas(400,400);
	final GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
	@Override
	public void start(Stage primaryStage) throws Exception {
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
