package presentation.view.customerui.customerui;

import businesslogic.hotelInfobl.HotelCustomerImpl;
import businesslogic.orderbl.OrderCustomerServiceImpl;
import businesslogicservice.hotelinfoblservice.HotelCustomerService;
import businesslogicservice.orderblservice.OrderCustomerService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import vo.HotelVO;
import vo.OrderVO;

public class UnCommentedHotelListPageController 
{
	@FXML private TableView list;
	@FXML private TableColumn nameColumn;
	@FXML private TableColumn starColumn;
	@FXML private TableColumn scoreColumn;
	@FXML private TableColumn priceColumn;
	@FXML private TableColumn buttonColumn;
	
	private Stage stage;
	private Scene commentFirstPageScene;
	private ObservableList<OrderVO> orderData;
	private OrderCustomerService customerService;
	private int userID;
	
	
	public void init(Stage stage, Scene commentFirstPageScene, int userID)
	{
		this.stage = stage;
		this.commentFirstPageScene = commentFirstPageScene;
		customerService = new OrderCustomerServiceImpl();
		this.userID = userID;
		initTable();
	}
	
	private void initTable()
	{
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("hotelName"));
		priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
		buttonColumn.setCellFactory(new Callback<TableColumn<OrderVO, Boolean>, TableCell<OrderVO, Boolean>>() {
			@Override
			public TableCell call(TableColumn param)
			{
				return new CommentButtonCell(stage);
			}
		});
		orderData = FXCollections.observableArrayList();
		if(customerService.getExecutedOrderList(userID)!=null)
		{
			for (OrderVO orderVO : orderData) 
			{
				orderData.add(orderVO);
			}
		}
		list.setItems(orderData);
	}
	
	public class CommentButtonCell extends TableCell<OrderVO, Boolean>
	{
		private Button commentButton = new Button("立即评价");
		
		public CommentButtonCell(Stage stage)
		{
			commentButton.setOnAction((ActionEvent e)->{
				 int seletedIndex=getTableRow().getIndex();
				 OrderVO selectedOrder = (OrderVO) list.getItems().get(seletedIndex);
				stage.setScene(new CommentSubmitPage(new Group(), stage, commentFirstPageScene, selectedOrder));
			});
		}
	}
}
