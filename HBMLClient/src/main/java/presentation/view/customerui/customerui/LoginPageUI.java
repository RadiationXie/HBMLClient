package presentation.view.customerui.customerui;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LoginPageUI extends Stage
{
	public LoginPageUI() 
	{
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../customeruiFXML/LoginPage.fxml"));
		Parent root = null;
		try {
			root = fxmlLoader.load();
			Scene scene = new Scene(root);
			this.setScene(scene);
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		LoginPageUIController controller = fxmlLoader.getController();
		controller.init(this);
	}
}
