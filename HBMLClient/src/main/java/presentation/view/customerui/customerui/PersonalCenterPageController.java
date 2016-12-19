package presentation.view.customerui.customerui;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;


import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class PersonalCenterPageController {
	@FXML private Button backButton;
	@FXML private Button editImageButton;
	@FXML private Button vipEnrollButton;
	@FXML private ImageView headImage;
	@FXML private Label nameLabel;
	@FXML private Label creditValueLabel;
	@FXML private Button myInfoButton;
	@FXML private Button hotelButton;
	
	private Stage stage;
	private Scene preScene;
	private Scene presentScene;
	
	private Desktop desktop;
	private FileChooser fileChooser;
	
	private String userName;
	public void init(Stage stage, Scene preScene, Scene presentScene, String userName)
	{
		this.stage = stage;
		this.preScene = preScene;
		this.presentScene = presentScene;
		this.userName = userName;
	}
	
	@FXML
	private void back()
	{
		stage.setScene(preScene);
	}
	
	@FXML
	private void editInfo()
	{
		stage.setScene(new PersonInfoPage(new Group(), stage, presentScene));
	}
	
	@FXML
	private void enroll()
	{
		stage.setScene(new EnrollVIPPage(new Group(), stage, presentScene));
	}
	
	@FXML
	private void editImage()
	{
		desktop = Desktop.getDesktop();
		fileChooser = new FileChooser();
		java.io.File choiceFile = fileChooser.showOpenDialog(stage);
		fileChooser.setTitle("选择头像");
		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("JPG","*.JPG"),
				new FileChooser.ExtensionFilter("PNG","*PNG")
				);
		if (choiceFile!=null) 
		{
			 try{
	                String path="/Users/xiezhenyu/Desktop/Pic";
	                String fileName=path+choiceFile.getName().toString();
	                File file=new File(fileName);
	                if(!file.exists()){
	                    File newfile=new File(path);
	                    newfile.mkdirs();
	                    FileInputStream input=new FileInputStream(choiceFile);
	                    FileOutputStream output=new FileOutputStream(fileName);

	                    byte[] b=new byte[1824*5];
	                    int length;
	                    while((length=input.read(b))!=-1){
	                        output.write(b,0,length);
	                    }

	                    output.flush();
	                    output.close();
	                    input.close();
	                }
	                Image image=new Image("file:///"+fileName);
	                headImage.setImage(image);

	            }catch (Exception e){
	                e.printStackTrace();
	            }
		}
	}
	
	@FXML 
	private void myHotel()
	{
		stage.setScene(new MyHotelListPage(new Group(), stage, presentScene));
	}
}
