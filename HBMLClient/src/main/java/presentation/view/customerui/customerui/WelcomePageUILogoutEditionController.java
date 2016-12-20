package presentation.view.customerui.customerui;

import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class WelcomePageUILogoutEditionController 
{
		@FXML private Button backFirstPageButton;
		@FXML private Button logoutButton;
		@FXML private Label nameLabel;
		
		private Stage stage;
		private Scene preScene;
		private FirstPageUIController controller;
		private String userName;
	
		public void init(Stage stage, Scene preScene, FirstPageUIController controller, String userName)
		{
			this.stage = stage;
			this.preScene = preScene;
			this.controller = controller;
			this.userName = userName;
			nameLabel.setText(userName);
		}
		
		@FXML 
		private void logout()
		{
			controller.setState(false);
			stage.setScene(new WelcomePageUI(new Group(), stage));
		}
		
		@FXML
		private void back()
		{
			stage.setScene(preScene);
		}
}
