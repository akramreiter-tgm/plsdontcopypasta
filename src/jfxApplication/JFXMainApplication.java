package jfxApplication;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class JFXMainApplication extends Application {
	Integer size = 1000;
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		primaryStage.setTitle("Hello World!");
        Button btn = new Button();
        btn.setText("Say \"Hello World!\"");
        btn.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hello World!");
            }
        });
        Group root = new Group();
        root.getChildren().add(btn);
        Scene mainscene = new Scene(root, 500, 250);
        EventHandler<MouseEvent> inc = new EventHandler<MouseEvent>() {
        	
			@Override
			public void handle(MouseEvent event) {
				System.out.println("x:" + event.getSceneX() + ",y:" + event.getSceneY());
				primaryStage.setWidth(size);
				btn.setLayoutX(100);
				cycleNextSize();
			}
        	
		};
        mainscene.setOnMouseClicked(inc);
        primaryStage.setResizable(false);
        mainscene.setFill(Color.AZURE);
		primaryStage.setScene(mainscene);
		primaryStage.show();
	}
	
	public static void selflaunch() {
		launch();
	}
	
	@Override
	public void stop() {
		System.exit(0);
	}
	
	public void cycleNextSize() {
		size -= 100;
		if (size < 100) size = 1000;
		System.out.println(size);
	}
}
