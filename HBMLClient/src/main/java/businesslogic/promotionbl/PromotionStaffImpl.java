package businesslogic.promotionbl;

import businesslogicservice.promotionblservice.PromotionStaffService;
import message.ResultMessage;

public class PromotionStaffImpl implements PromotionStaffService{
	
	private PromotionDao promotionDao;
	
	@Override
	public ResultMessage addPromotion(PromotionPO po) {
		// TODO 自动生成的方法存根
		promotionDao=new PromotionDaoImpl_stub();
		try {
			promotionDao.addPromotion(po);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ResultMessage modifyPromotion(PromotionPO po) {
		// TODO 自动生成的方法存根
		promotionDao=new PromotionDaoImpl_stub();
		try {
			promotionDao.modifyPromotion(po);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ResultMessage deletePromotion(String id) {
		promotionDao=new PromotionDaoImpl_stub();
		try{
			promotionDao.deletePromotion(id);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

}
