package presentation.view.application;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) {
		primaryStage.setScene(new UserLoginUI(new Group(),primaryStage));
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
