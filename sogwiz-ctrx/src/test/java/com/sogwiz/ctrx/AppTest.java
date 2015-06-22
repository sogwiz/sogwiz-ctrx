package com.sogwiz.ctrx;

import java.util.Calendar;
import java.util.Date;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.sogwiz.ctrx.util.AppUtils;
import com.sogwiz.ctrx.util.ConfigDataReader;



/**
 * Unit test for simple App.
 */
@Test
public class AppTest extends BaseTest
{
	
	@DataProvider (name = "webinarCombinations")
	public static Object [][] webinarValues(){
		return new Object [][] {{"webinar1", "description1"}, 
				{"webinar_really_really_really_really_really_really_long_name", "description2"},
				{"123125134", "description3_webinar with numbers only in name"}};
		
	}
	
	@Test (dataProvider = "webinarCombinations")
	void testAScheduleWebinar(String name, String description) throws Exception{
		app.loginPage().login(configDataReader.getDefaultUsername(), configDataReader.getDefaultPassword());
		
		Date d = AppUtils.getDatePlusDays(configDataReader.getDaysOffset());
		
		String webinarId = app.homePage().scheduleNewWebinar(name, description, d);
		System.out.println("\nCreated webinar with internal id " + webinarId + " \n");
		app.homePage().navigateToMyWebinars();
		
		//format of webinar on My Webinars page is webinar-<webinar_id>
		//call verify webinar with the name, description, date, and id
		Assert.assertTrue(app.homePage().verifyWebinarCreated(name, description, d, webinarId), "webinar should have valid values");
	}
	
	@Test 
	void testValidLogin() throws Exception{
		app.loginPage().login(configDataReader.getDefaultUsername(), configDataReader.getDefaultPassword());
        Assert.assertFalse(app.loginPage().verifyErrorMessagePresent(), "Error message should be displayed on invalid login");
	}
	
    @Test
    public void testInvalidLogin()
    {
        app.loginPage().login("username", "password");
        Assert.assertTrue(app.loginPage().verifyErrorMessagePresent(), "Error message should be displayed on invalid login");
    }
    
    @Test
    /**
     * test out the various date util functions 
     */
	public void testZDates(){
		Date d = AppUtils.getDatePlusDays(ConfigDataReader.getInstance().getDaysOffset());
		Assert.assertTrue(d.after(new Date()));
		
		//will only print true in the rollover scenario
		//won't verify that for now
		System.out.println(AppUtils.isNextMonth(d, ConfigDataReader.getInstance().getDaysOffset()));
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		Assert.assertEquals(AppUtils.getHourAndMinutesString(d).length(), 5, "Must generate a time of format hh:mm");
		
	}
}
