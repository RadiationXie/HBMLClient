package presentation.view.customerui.customerui;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import businesslogic.hotelInfobl.HotelCustomerImpl;
import businesslogicservice.hotelinfoblservice.HotelCustomerService;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.DateHelper;
import model.HotelFilter;
import vo.HotelVO;
import vo.RoomInfoVO;

public class HotelListPageController {
	@FXML private Button backButton;
	@FXML private TextField searchField;
	@FXML private Button searchButton;
	
	@FXML private TableColumn nameColumn; 
	@FXML private TableColumn addressColumn;
	@FXML private TableColumn starColumn;
	@FXML private TableColumn scoreColumn;
	@FXML private TableColumn priceColumn;
	@FXML private TableColumn checkButtonColumn;
	@FXML private TableColumn makeOrderButtonColumn;
	
	@FXML private ComboBox<String> first;
	@FXML private ComboBox<String> second;
	@FXML private ComboBox<String> third;
	
	@FXML private TableView list;
	 private Stage stage;
	 private Scene firstPage;
	 private Scene hotelListPageScene;
	 
	 private String province;
	 private String city;
	 private int region;
	 private Date checkinTime;
	 private int star;
	 private String choice = "推荐排序";
	 private ObservableList<HotelVO> hotelData;
	 
	 private HotelCustomerService service;
	 private boolean state;
	 private boolean searchByName;
	 private String hotelName;
//	 private Map<String, Integer> nameMapID;
	 private HotelVO selectedHotel;
	 private RoomInfoVO defaultRoom;
	 private HotelFilter filter;
	 
//	 public void init(Stage stage, Scene firstPage,Scene hotelListPageScene, String hotelName, Date checkinTime, boolean state)
//	 {
//		 this.stage = stage;
//		 this.firstPage = firstPage;
//		 this.state = state;
//		 this.hotelName = hotelName;
//		 this.checkinTime = checkinTime;
//		 this.searchByName = true;
//		 this.hotelListPageScene = hotelListPageScene;
//		 initTable();
//	 }
	 public void init(Stage stage, Scene firstPage, String province, String city, int region,String hotelName, Date checkinTime,  int star,boolean state)
	 {
		 this.stage = stage;
		 this.firstPage = firstPage;
		 this.state = state;
		 this.province = province;
		 this.city = city;
		 this.region = region;
		 this.hotelName = hotelName;
		 this.checkinTime = checkinTime;
		 this.star = star;
		 this.searchByName = false;
		 searchField.setPromptText(hotelName);
		 initTable();
	 }
	 @FXML
	 private void back()
	 {
		 stage.setScene(firstPage);
	 }
	 
	 private void initTable()
	 {
		 initComboBox();
		 nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		 addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
		 starColumn.setCellValueFactory(new PropertyValueFactory<>("star"));
		 scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
		 priceColumn.setCellValueFactory(new PropertyValueFactory<>("lowestPrice"));
		 checkButtonColumn.setCellFactory(new Callback<TableColumn<HotelVO, Boolean>, TableCell<HotelVO, Boolean>>()
		 {
			 @Override
			 public TableCell call(TableColumn param)
			 {
				 return new CheckInfoButtonCell(stage);
			 }
		});
		 makeOrderButtonColumn.setCellFactory(new Callback<TableColumn<HotelVO, Boolean>, TableCell<HotelVO, Boolean>>() 
		 {
			 @Override
			 public TableCell call(TableColumn param)
			 {
				 return new MakeOrderButtonCell(stage);
			 }
		});
		 hotelData = FXCollections.observableArrayList();
		 
//		 if(searchByName)
//		 {
//			 filter = new HotelFilter();
//			 filter.add("name", "like","'%"+ hotelName+"%'");
//			 System.out.println(hotelName);
//			 System.out.println(DateHelper.dateToString(checkinTime));
//		 }else
//		 {
			 filter = new HotelFilter();
			 if(hotelName!=null){
				 filter.add("name", "like", "'%"+hotelName+"%'"); 
			 }
			 filter.add("region", "=", region);
			 filter.add("star", "=", star);
			 
//		 }
//		 nameMapID = new LinkedHashMap<>();
		 
		 if(service.getHotelList(filter, "star", checkinTime) != null)
		 {
			 for (HotelVO hotelVO : service.getHotelList(filter, "star", checkinTime).values()) 
			 {
				hotelData.add(hotelVO);
//				nameMapID.put(hotelVO.getName(), hotelVO.getId());
			} 
		 }

		 list.setItems(hotelData);
	 }
	 
	 private void initComboBox()
	 {
		 service = new HotelCustomerImpl();
		 
		 ObservableList<String> optionsOne = FXCollections.observableArrayList(
				 "推荐排序","好评优先","低价排序");
		 ObservableList<String> optionsTwo = FXCollections.observableArrayList(
				 "五星级","四星级","三星级","二星级","一星级");
		 ObservableList<String> optionsThree = FXCollections.observableArrayList(
				 );
		 first.setItems(optionsOne);
		 second.setItems(optionsTwo);
		 third.setItems(optionsThree);
		
		 first.setPromptText("推荐排序");
		 second.setPromptText("星级排序");
		 third.setPromptText("位置区域");
		 
		 first.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>(){
			 @Override
			 public void changed(ObservableValue<? extends Number> ob, Number oldValue, Number newValue){
				 choice = optionsOne.get(newValue.intValue());
				 if(choice.equals("好评优先"))
				 {
					 if(service.getHotelList(filter, "score", checkinTime) != null)
					 {
						 hotelData.clear();
						 for (HotelVO hotelVO : service.getHotelList(filter, "score", checkinTime).values()) 
						 {
							hotelData.add(hotelVO);
						} 
					 }
				 }
				 else if(choice.equals("低价排序"))
				 {
					 if(service.getHotelList(filter, "lowestPrice", checkinTime) != null)
					 {
						 hotelData.clear();
						 for (HotelVO hotelVO : service.getHotelList(filter, "lowestPrice", checkinTime).values()) 
						 {
							hotelData.add(hotelVO);
						} 
					 }
				 }else 
				 {
					 if(service.getHotelList(filter, "star", checkinTime) != null)
					 {
						 hotelData.clear();
						 for (HotelVO hotelVO : service.getHotelList(filter, "star", checkinTime).values()) 
						 {
							hotelData.add(hotelVO);
						} 
					 }
				 }
			 }
		 });
		 second.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>(){
			 @Override
			 public void changed(ObservableValue<? extends Number> ob, Number oldValue, Number newValue){
				 choice = optionsTwo.get(newValue.intValue());
				 //"五星级","四星级","三星级","二星级","一星级"
				 if(choice.equals("一星级"))
				 {
					 star = 1;
					 filter.del("star");
					 filter.add("star", ">", star);
				 }
				 else if(choice.equals("二星级"))
				 {
					 star = 2;
					 filter.del("star");
					 filter.add("star", ">", star);
				 }
				 else if(choice.equals("三星级"))
				 {
					 star = 3;
					 filter.del("star");
					 filter.add("star", ">", star);
				 }
				 else if(choice.equals("四星级"))
				 {
					 star = 4;
					 filter.del("star");
					 filter.add("star", ">", star);
				 }
				 else if(choice.equals("五星级"))
				 {
					 star = 5;
					 filter.del("star");
					 filter.add("star", ">", star);
				 }
			 }
		 });
	 }

	 public class CheckInfoButtonCell extends TableCell<HotelVO, Boolean>
	 {
		 private Button checkButton = new Button("查看");
		 public CheckInfoButtonCell(Stage stage)
		 {
			 DropShadow shadow = new DropShadow();
			 checkButton.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e)->{
				checkButton.setEffect(shadow); 
			 });
			 checkButton.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e)->{
				 checkButton.setEffect(null);
			 });
		
			 checkButton.setOnAction((ActionEvent e)->{
//				 hotelName = 把列表的选中项里面的vo取出来
				 int seletedIndex=getTableRow().getIndex();
				 selectedHotel = (HotelVO) list.getItems().get(seletedIndex);
				 stage.setScene(new HotelInfoUI(new Group(), stage, firstPage, selectedHotel,checkinTime));
			 });
		 }
		 protected void updateItem(Boolean t, boolean empty)
		 {
			 super.updateItem(t, empty);
			 if(empty)
			 {
				 setGraphic(null);
				 setText(null);
			 }else
			 {
				 setGraphic(checkButton);
				 setText(null);
			 }
		 }
	 }
	 
	 public class MakeOrderButtonCell extends TableCell<HotelVO, Boolean>
	 {
		 private Button makeOrderButton = new Button("预订");
		 
		 public MakeOrderButtonCell(Stage stage)
		 {	 }
		 
		 protected void updateItem(Boolean t, boolean empty) 
		 {
			super.updateItem(t, empty);
			if(empty)
			{
				setGraphic(null);
				setText(null);
			}else
			{
				setGraphic(makeOrderButton);
				setText(null);
			}
		 }
		 
	 }
	 
	 @FXML
		private void search()
		{
		 	hotelData.clear();
			String searchInfo = null;
			if(searchField.getText()!=null&&!searchField.getText().isEmpty())
			{
				searchInfo = searchField.getText();
				filter.del("name");
				filter.add("name", "like", "'%"+ searchInfo+"%'");
			}
			
			 if(service.getHotelList(filter, "star", checkinTime) != null)
			 {
				 for (HotelVO hotelVO : service.getHotelList(filter, "star", checkinTime).values()) 
				 {
					hotelData.add(hotelVO);
//					nameMapID.put(hotelVO.getName(), hotelVO.getId());
				} 
			 }
			 list.setItems(hotelData);
//			stage.setScene(new HotelListPageUI(new Group(), stage, firstPage, searchInfo,checkinTime,state));
		}
}
