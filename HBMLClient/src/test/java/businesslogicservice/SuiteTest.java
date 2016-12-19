package businesslogicservice;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import businesslogicservice.creditblservice.CreditCustomerServiceTest;
import businesslogicservice.creditblservice.CreditWebMarketerServiceTest;
import businesslogicservice.hotelinfoblservice.HotelCustomerServiceTest;
import businesslogicservice.hotelinfoblservice.HotelRegionHelperTest;
import businesslogicservice.hotelinfoblservice.HotelStaffServiceTest;
import businesslogicservice.hotelinfoblservice.HotelWebManagerServiceTest;

	@RunWith(Suite.class)
	@Suite.SuiteClasses({CreditCustomerServiceTest.class,CreditWebMarketerServiceTest.class
		,HotelCustomerServiceTest.class,HotelRegionHelperTest.class,HotelStaffServiceTest.class,HotelWebManagerServiceTest.class})
	public class SuiteTest {

}
