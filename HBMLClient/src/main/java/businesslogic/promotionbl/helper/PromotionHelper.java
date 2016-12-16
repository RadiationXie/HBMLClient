package businesslogic.promotionbl.helper;

import businesslogic.utility.TransferImpl;
import businesslogicservice.TransferService;
import dao.HotelDao;
import dao.PromotionDao;
import dao.UserDao;
import message.ResultMessage;
import model.DiscountType;
import model.MemberType;
import model.PromotionConditionHelper;
import model.PromotionFilter;
import po.PromotionPO;
import rmi.ClientRunner;
import vo.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by alex on 12/15/16.
 */
public class PromotionHelper {

    Map<Integer, PromotionVO> promotionList;
    PromotionDao promotionDao;
    UserDao userDao;
    HotelDao hotelDao;
    TransferService promotionTranserService;

    public PromotionHelper() {

        promotionDao= ClientRunner.remoteHelper.getPromotionDao();
        userDao=ClientRunner.remoteHelper.getUserDao();
        hotelDao=ClientRunner.remoteHelper.getHotelDao();
        promotionTranserService=new TransferImpl();

    }

    public Map<Integer, PromotionVO> getHotelPromotionList(PromotionFilter promotionFilter) throws Exception{

        Map<Integer,PromotionPO> map=promotionDao.getHotelPromotionList(promotionFilter);
        promotionList=new LinkedHashMap<>();
        for(int key:map.keySet()){
            promotionList.put(key,promotionTranserService.poToVo(map.get(key)));
        }
        return promotionList;

    }

    public Map<Integer, PromotionVO> getWebPromotionList(PromotionFilter promotionFilter)throws Exception {

        Map<Integer,PromotionPO> map=promotionDao.getWebPromotionList(promotionFilter);
        promotionList=new LinkedHashMap<>();
        for(int key:map.keySet()){
            promotionList.put(key,promotionTranserService.poToVo(map.get(key)));
        }
        return promotionList;

    }


    public PromotionVO getPromotion(int promotionID) {

        return promotionList.get(promotionID);

    }

    public SelectedPromotionVO getSelectedPromotion(OrderVO orderVO)throws Exception {
        int userID = orderVO.getUserID();
        int hotelID = orderVO.getHotelID();
        int currentPrice = orderVO.getPrice();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date generateTime = orderVO.getGenerateTime();
        Date birthday;
        Calendar calendar1=Calendar.getInstance();
        Calendar calendar2=Calendar.getInstance();
        calendar1.setTime(generateTime);

        UserVO userVO = promotionTranserService.poToVo(userDao.getUserData(userID));
        HotelVO hotelVO=promotionTranserService.poToVo(hotelDao.getHotelInfo(hotelID));
        PromotionFilter promotionFilter = new PromotionFilter();
        promotionFilter.add("startDate", "<", generateTime);
        promotionFilter.add("endDate", ">", generateTime);
        //first filters out unavailable promotions according to startDate and endDate
        Map<Integer, PromotionVO> hotelPromotionList = getHotelPromotionList(promotionFilter);
        Map<Integer, PromotionVO> webPromotionList = getWebPromotionList(promotionFilter);

        Map<Integer, SelectedPromotionVO> selectedPromotionVOMap = new LinkedHashMap<>();
        PromotionVO tempPromotion;
        SelectedPromotionVO tempSelectedPromotion;
        PromotionConditionHelper promotionConditionHelper;
        for (int key : hotelPromotionList.keySet()) {
            tempPromotion = hotelPromotionList.get(key);
            String tempPromotionName = tempPromotion.getName();
            float tempPrice = currentPrice;
            //case 1: in this case, the hotel promotion is a special promotion on for celebrating the customer's birthday
            promotionConditionHelper=new PromotionConditionHelper();
            promotionConditionHelper.add(tempPromotionName,"=","00","tempPromotionName");
            promotionConditionHelper.add(userVO.getMemberType(),"=",MemberType.Person,"memberType");
            promotionConditionHelper.add(orderVO.getHotelID(),"=",tempPromotion.getRegion(),"hotelID");
            try {
                birthday = simpleDateFormat.parse(userVO.getMemberInfo());
                calendar2.setTime(birthday);
                promotionConditionHelper.add(calendar1.get(Calendar.MONTH),"=",calendar2.get(Calendar.MONTH),"month");
                promotionConditionHelper.add(calendar1.get(Calendar.DAY_OF_MONTH),"=",calendar2.get(Calendar.DAY_OF_MONTH),"day");
                //以下为满减情况
                promotionConditionHelper.add(tempPromotion.getType(),"=",DiscountType.ManJian,"discountType");
                promotionConditionHelper.add(currentPrice,">=",tempPromotion.getRequirement(),"requirement");
                if(promotionConditionHelper.decodeConditions())tempPrice = currentPrice - tempPromotion.getDiscount();
                //以下为打折情况
                promotionConditionHelper.del("discountType");
                promotionConditionHelper.del("requirement");
                promotionConditionHelper.add(tempPromotion.getType(),"=",DiscountType.DaZhe,"discountType");
                if(promotionConditionHelper.decodeConditions())tempPrice = currentPrice * tempPromotion.getDiscount() / 100.0f;
            }catch(ParseException e){
                tempPrice=currentPrice;
            }
            //end of case 1

            //case 2: in this case, the hotel promotion is for companies that cooperate with the hotel
            promotionConditionHelper=new PromotionConditionHelper();
            promotionConditionHelper.add(tempPromotionName,"=","01","tempPromotionName");
            promotionConditionHelper.add(userVO.getMemberType(),"=",MemberType.Enterprise,"memberType");
            promotionConditionHelper.add(orderVO.getHotelID(),"=",tempPromotion.getRegion(),"hotelID");
            //以下为满减情况
            promotionConditionHelper.add(tempPromotion.getType(),"=",DiscountType.ManJian,"discountType");
            promotionConditionHelper.add(currentPrice,">=",tempPromotion.getRequirement(),"requirement");
            if(promotionConditionHelper.decodeConditions())tempPrice = currentPrice - tempPromotion.getDiscount();
            //以下为打折情况
            promotionConditionHelper.del("discountType");
            promotionConditionHelper.del("requirement");
            promotionConditionHelper.add(tempPromotion.getType(),"=",DiscountType.DaZhe,"discountType");
            if(promotionConditionHelper.decodeConditions())tempPrice = currentPrice * tempPromotion.getDiscount() / 100.0f;
            //end of case 2

            //case 3: this is normal case of hotel promotion
            promotionConditionHelper=new PromotionConditionHelper();
            promotionConditionHelper.add(tempPromotionName,"!=","00","tempPromotionName");
            promotionConditionHelper.add(tempPromotionName,"!=","01","tempPromotionName");
            promotionConditionHelper.add(tempPromotionName,"!=","10","tempPromotionName");
            promotionConditionHelper.add(orderVO.getHotelID(),"=",tempPromotion.getRegion(),"hotelID");
            promotionConditionHelper.add(userVO.getRank(),"<=",tempPromotion.getMaxRankAvailable(),"rank");
            promotionConditionHelper.add(userVO.getRank(),">=",tempPromotion.getMinRankAvailable(),"rank");
            //以下为满减情况
            promotionConditionHelper.add(tempPromotion.getType(),"=",DiscountType.ManJian,"discountType");
            promotionConditionHelper.add(currentPrice,">=",tempPromotion.getRequirement(),"requirement");
            if(promotionConditionHelper.decodeConditions())tempPrice = currentPrice - tempPromotion.getDiscount();
            //以下为打折情况
            promotionConditionHelper.del("discountType");
            promotionConditionHelper.del("requirement");
            promotionConditionHelper.add(tempPromotion.getType(),"=",DiscountType.DaZhe,"discountType");
            if(promotionConditionHelper.decodeConditions())tempPrice = currentPrice * tempPromotion.getDiscount() / 100.0f;
            tempSelectedPromotion = new SelectedPromotionVO(tempPromotion, tempPrice);
            selectedPromotionVOMap.put(key, tempSelectedPromotion);
        }
        //get the selected hotel promotion
        float minPrice=currentPrice;
        SelectedPromotionVO minSeletedPromotionVO=null;
        for(int key:selectedPromotionVOMap.keySet()){
            if(selectedPromotionVOMap.get(key).getPrice()<minPrice){
                minSeletedPromotionVO=selectedPromotionVOMap.get(key);
                minPrice=minSeletedPromotionVO.getPrice();
            }
        }
        selectedPromotionVOMap=new LinkedHashMap<>();//refresh the voMap

        for (int key : webPromotionList.keySet()) {
            tempPromotion = webPromotionList.get(key);
            String tempPromotionName = tempPromotion.getName();
            float tempPrice = currentPrice;
            //case 1: in this case, the web promotion is a special promotion that only the hotels in this region can benefit from this promotion
            promotionConditionHelper=new PromotionConditionHelper();
            promotionConditionHelper.add(tempPromotionName,"=","10","tempPromotionName");
            promotionConditionHelper.add(tempPromotion.getRegion(),"=",hotelVO.getRegion(),"region");
            promotionConditionHelper.add(userVO.getRank(),"<=",tempPromotion.getMaxRankAvailable(),"rank");
            promotionConditionHelper.add(userVO.getRank(),">=",tempPromotion.getMinRankAvailable(),"rank");
            //以下为满减情况
            promotionConditionHelper.add(tempPromotion.getType(),"=",DiscountType.ManJian,"discountType");
            promotionConditionHelper.add(currentPrice,">=",tempPromotion.getRequirement(),"requirement");
            if(promotionConditionHelper.decodeConditions())tempPrice = currentPrice - tempPromotion.getDiscount();
            //以下为打折情况
            promotionConditionHelper.del("discountType");
            promotionConditionHelper.del("requirement");
            promotionConditionHelper.add(tempPromotion.getType(),"=",DiscountType.DaZhe,"discountType");
            if(promotionConditionHelper.decodeConditions())tempPrice = currentPrice * tempPromotion.getDiscount() / 100.0f;
            //end of case 1

            //case 2: this is normal case of web promotion
            //in this case, no region info is included in VO, so the condition is weak
            promotionConditionHelper=new PromotionConditionHelper();
            promotionConditionHelper.add(tempPromotionName,"!=","00","tempPromotionName");
            promotionConditionHelper.add(tempPromotionName,"!=","01","tempPromotionName");
            promotionConditionHelper.add(tempPromotionName,"!=","10","tempPromotionName");
            promotionConditionHelper.add(userVO.getRank(),"<=",tempPromotion.getMaxRankAvailable(),"rank");
            promotionConditionHelper.add(userVO.getRank(),">=",tempPromotion.getMinRankAvailable(),"rank");
            //以下为满减情况
            promotionConditionHelper.add(tempPromotion.getType(),"=",DiscountType.ManJian,"discountType");
            promotionConditionHelper.add(currentPrice,">=",tempPromotion.getRequirement(),"requirement");
            if(promotionConditionHelper.decodeConditions())tempPrice = currentPrice - tempPromotion.getDiscount();
            //以下为打折情况
            promotionConditionHelper.del("discountType");
            promotionConditionHelper.del("requirement");
            promotionConditionHelper.add(tempPromotion.getType(),"=",DiscountType.DaZhe,"discountType");
            //end of case 2
            tempSelectedPromotion = new SelectedPromotionVO(tempPromotion, tempPrice);
            selectedPromotionVOMap.put(key, tempSelectedPromotion);
        }

        //get the selected web promotion
        for(int key:selectedPromotionVOMap.keySet()){
            if(selectedPromotionVOMap.get(key).getPrice()<minPrice){
                minSeletedPromotionVO=selectedPromotionVOMap.get(key);
                minPrice=minSeletedPromotionVO.getPrice();
            }
        }
        //get the selected promotion
        return minSeletedPromotionVO;
    }



    public ResultMessage addHotelPromotion(PromotionVO vo) throws Exception{

        return promotionDao.addPromotion(promotionTranserService.voToPo(vo));

    }

    public ResultMessage addWebPromotion(PromotionVO vo) throws Exception{

        return promotionDao.addPromotion(promotionTranserService.voToPo(vo));

    }

    public ResultMessage deleteHotelPromotion(int promotionID) throws Exception{

        return promotionDao.deletePromotion(promotionID);

    }

    public ResultMessage deleteWebPromotion(int promotionID) throws Exception{

        return promotionDao.deletePromotion(promotionID);

    }

    public ResultMessage modifyHotelPromotion(PromotionVO vo) throws Exception{

        return promotionDao.updatePromotion(promotionTranserService.voToPo(vo));

    }

    public ResultMessage modifyWebPromotion(PromotionVO vo)throws Exception{

        return promotionDao.updatePromotion(promotionTranserService.voToPo(vo));

    }
}