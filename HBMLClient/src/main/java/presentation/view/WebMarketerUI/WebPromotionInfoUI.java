package presentation.view.WebMarketerUI;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import vo.PromotionVO;

import java.io.IOException;

/**
 * Created by LENOVO on 2016/12/15.
 */
public class WebPromotionInfoUI extends VBox {
    public WebPromotionInfoUI(VBox infoVBox, PromotionVO promotionVO,Stage stage){
        FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("webmarketerfxml/WebPromotionInfo.fxml"));
        try{
            this.getChildren().add(fxmlLoader.load());
        }catch (IOException e) {
            e.printStackTrace();
        }

        WebPromotionInfoUIController webPromotionInfoUIController=fxmlLoader.getController();
        webPromotionInfoUIController.init(infoVBox,promotionVO,stage);
    }
}
