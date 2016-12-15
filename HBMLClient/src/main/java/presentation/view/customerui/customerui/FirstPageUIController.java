package presentation.view.customerui.customerui;

import java.time.LocalDate;
import com.sun.javafx.collections.MappingChange.Map;
import businesslogic.hotelInfobl.HotelCustomerImpl;
import businesslogicservice.hotelinfoblservice.HotelCustomerService;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.HotelFilter;
import vo.HotelVO;

public class FirstPageUIController {
	@FXML private TextField searchField;
	@FXML private Button searchButton;
	@FXML private Button firstPageButton;
	@FXML private Button orderButton;
	@FXML private Button commentButton;
	@FXML private Button personInfoButton;
	@FXML private ImageView hotelPicView;
	@FXML private Button loginButton;
	
	@FXML private Button searchTwoButton;
	@FXML private TextField address_field;
	@FXML private TextField circle_field;
	@FXML private DatePicker checkinTimePicker;
	@FXML private DatePicker checkoutTimePicker;
	@FXML private Button searchByConditionsButton;
	@FXML private CheckBox fiveStarCheckBox;
	@FXML private CheckBox fourStarCheckBox;
	@FXML private CheckBox threeStarCheckBox;
	@FXML private CheckBox twoStarCheckBox;
	@FXML private CheckBox oneStarCheckBox;
	
	private Scene firstPageUI;
	private Stage stage;
	
	private boolean state;
	private String userName;
	
	private Map<Integer, HotelVO> hotelList = null;
	
	public void init(Stage stage, Scene firstPageUI)
	{
		this.stage = stage;
		this.firstPageUI = firstPageUI;
		initDatePicker();
	}
	
	@FXML
	private void orderPartAction()
	{
		stage.setScene(new OrderFirstPageUIFromFirstPage(new Group(),stage,firstPageUI));
	}
	
	@FXML
	private void commentPartAction()
	{
		stage.setScene(new CommentPageUIFromFirstPage(new Group(),stage,firstPageUI));
	}
	
	@FXML
	private void personalInfoPartAction()
	{
		if(!state)
		{
			stage.setScene(new NullUserPage(new Group(), stage, firstPageUI));
		}else
		{
			stage.setScene(new PersonalCenterPage(new Group(), stage, firstPageUI, userName));
		}
	}
	
	@FXML
	private void search()
	{
		String searchInfo = "recommendedHotel";
		if(searchField.getText()!=null&&!searchField.getText().isEmpty())
		{
			searchInfo = searchField.getText();
		}
		stage.setScene(new HotelListPageUI(new Group(), stage, firstPageUI, searchInfo,state));
	}
	
	@FXML 
	private void searchByConditions()
	{
		String address = "江苏省南京市";
		String region = "栖霞区";
		String checkinTime = "2016:12:08";
		String checkoutTime = "2016:12:08";
		int star = 1;
		if(address_field.getText()!=null&&!address_field.getText().isEmpty())
		{
			address = address_field.getText();
		}
		if(circle_field.getText()!=null&&!circle_field.getText().isEmpty())
		{
			region = circle_field.getText();
		}
		
		//获取两个DatePicker里面的时间
		checkinTime = checkinTimePicker.getValue().toString();
		checkoutTime = checkoutTimePicker.getValue().toString();
		//获得星级
		if(fiveStarCheckBox.isSelected())
		{
			HotelFilter filter = new HotelFilter();
			filter.add("star", "=", star);
			HotelCustomerService serviceImpl = new HotelCustomerImpl();
//			hotelList = serviceImpl.getHotelList(filter, "score", new Date());
		}
		stage.setScene(new HotelListPageUI(new Group(), stage, firstPageUI, address, region, checkinTime, checkoutTime, star, state));
	}
	
	private void initDatePicker()
	{
		checkinTimePicker.setPromptText(LocalDate.now().toString());
		checkoutTimePicker.setPromptText(LocalDate.now().toString());
		checkinTimePicker.setValue(LocalDate.now());
		final Callback<DatePicker, DateCell> dateCellFactory = new Callback<DatePicker, DateCell>() 
		{
			@Override
			public DateCell call(final DatePicker datePicker)
			{
				return new DateCell()
						{
							@Override
							public void updateItem(LocalDate item, boolean empty)
							{
								super.updateItem(item, empty);
								if(item.isBefore(checkinTimePicker.getValue().plusDays(1)))
								{
									setDisable(true);
									 setStyle("-fx-background-color: #ffc0cb;");
								}
							}
						};
			}
		};
		checkoutTimePicker.setDayCellFactory(dateCellFactory);
		checkoutTimePicker.setValue(LocalDate.now().plusDays(1));
	}
	@FXML 
	private void login()
	{
		new LoginPageUI(stage, firstPageUI, this).showAndWait();
	}
	
	public boolean getState()
	{
		return state;
	}
	public void setState(boolean state, String userName)
	{
		this.state = state;
		this.userName = userName;
	}
}
