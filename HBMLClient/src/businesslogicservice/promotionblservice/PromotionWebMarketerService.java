package businesslogicservice.promotionblservice;


import businesslogic.promotionbl.Promotion;
import message.ResultMessage;
import po.PromotionPO;

public interface PromotionWebMarketerService {
	public ResultMessage addPromotion(PromotionPO po);
	public ResultMessage modifyPromotion(PromotionPO po);
}
