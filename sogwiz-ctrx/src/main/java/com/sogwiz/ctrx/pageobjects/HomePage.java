package com.sogwiz.ctrx.pageobjects;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

import junit.framework.Assert;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import com.sogwiz.ctrx.util.AppUtils;
import com.sogwiz.ctrx.util.ClientType;
import com.sogwiz.ctrx.util.ConfigDataReader;

/**
 * navigational functionality on the HomePage (the signed in page)
 * - allows the ability to create / schedule a new webinar
 * - allows for verifying the correctness that a webinar was created correctly
 * @author sargon
 * 
 *
 */
public class HomePage extends BasePageObject {

	public HomePage(ClientType cType) {
		super(cType);
	}
	
	public String scheduleNewWebinar(String name, String description, Date d){
		clickScheduleWebinarButton();
		
		//populate the name of the webinar
		WebElement elementName = driver.findElement(By.id("name"));
		elementName.clear();
		elementName.sendKeys(name);
		
		WebElement elementDescription = driver.findElement(By.id("description"));
		elementDescription.clear();
		elementDescription.sendKeys("description");
		
		WebElement elementStartTime = driver.findElement(By.id("webinarTimesForm.dateTimes_0.startTime"));
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		
		String timeStart = AppUtils.getHourAndMinutesString(d);
		elementStartTime.sendKeys("");
		elementStartTime.clear();
		//this is the only way to clear the time start field that i found which works safely
		//1. navigate to the end of the time string by pressing the down arrow key
		//2. press the backspace key 5 times for each of the following chars HH:MM
		elementStartTime.sendKeys(Keys.ARROW_DOWN, 
				Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
				timeStart);
		
		//activate with the calendar widget
		WebElement calendarButton = driver.findElement(By.cssSelector("img.ui-datepicker-trigger"));
		calendarButton.click();
		
		//before we click on the date element, we have to determine if we are now in the next month
		if(AppUtils.isNextMonth(d, ConfigDataReader.getInstance().getDaysOffset())){
			//we should navigate to the next month then
			WebElement nextMonthButton = driver.findElement(By.cssSelector("span.ui-icon.ui-icon-circle-triangle-e"));
			nextMonthButton.click();
		}
		
		WebElement dateOnCalLink = driver.findElement(By.linkText(
				String.valueOf(cal.get(Calendar.DAY_OF_MONTH))
				)
				);
		
		dateOnCalLink.click();
		
		/* 
		 * if we wanted to deal with the AM / PM selection
		WebElement elementSelectAMPM = driver.findElement(By.id("webinarTimesForm_dateTimes_0_startAmPm_trig"));
		//set it for AM
		if(cal.get(Calendar.AM_PM) == Calendar.PM){
			elementSelectAMPM.click();
			elementSelectAMPM.sendKeys(Keys.DOWN,Keys.ENTER);
		}else {
			//set it for AM
			elementSelectAMPM.click();
			elementSelectAMPM.sendKeys(Keys.UP,Keys.ENTER);
		}
		*/

		//TODO: NOTE: I'm merely including this delay for demo purposes
		// so that the page contents can be viewed before we hit submit in this demo
		try {
			System.out.println("NOTE: Just sleeping here for 8 seconds to let the viewer see the page... \nTest will continue in 8 seconds" );
			Thread.sleep(8000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		WebElement elementBtnSubmit = driver.findElement(By.id("schedule.submit.button"));
		elementBtnSubmit.click();
	
		//now let's get the id of the webinar and return it back to the creator
		String urlString = driver.getCurrentUrl();
		String webinarId = urlString.split("webinar=")[1];
		return webinarId;
	}
	
	public void clickScheduleWebinarButton(){
		WebElement buttonScheduleWebinar = driver.findElement(By.id("scheduleWebinar"));
		buttonScheduleWebinar.click();
	}

	public void navigateToMyWebinars(){
		WebElement linkMyWebinars = driver.findElement(By.linkText("My Webinars"));	
		linkMyWebinars.click();
	}
	
	public boolean verifyWebinarCreated(String name, String description, Date d, String webinarId){
		//check for if the webinar shows up in the list
		if( driver.findElements( By.id("webinar-" + webinarId) ).size() == 0){
			System.out.println("\nCouldn't find webinar using div id=webinar-" + webinarId);
			return false;	
		}
		System.out.println("\nSuccessfully found webinar using div id=webinar-" + webinarId);
			
		/*	then check if the name field is correct
			a quick way is to get the text of the span element in the webinar listings page
		 	the xpath is of the form
		 //div[@id='webinar-6111847956097122306']/ul[2]/div/li[2]/span
		*/
		String xPathTitle = "//div[@id='webinar-"+webinarId+"']/ul/li[3]/a/span";
		WebElement xPathWebinarTitle = driver.findElement(By.xpath(xPathTitle));
		//to verify the name, we'll check to see if what's on the web page is a substring of the original
		//this is make sure truncation doesn't break anything
		if(!name.contains(xPathWebinarTitle.getText().trim())){
			System.out.println("\nERROR: webinar names don't match up: \nActual: " + xPathWebinarTitle.getText().trim() + "\nExpected: " + name );
			return false;
		}
		System.out.println("CHECK: webinar names match up");
		
		/*	then check if the time field is correct
			a quick way is to get the text of the span element in the webinar listings page
			the xpath is of the form:
		 //div[@id='webinar-6111847956097122306']/ul[2]/div/li[2]/span
		*/
		String xPath = "//div[@id='webinar-"+webinarId+"']/ul[2]/div/li[2]/span";
		WebElement xPathTimeField = driver.findElement(By.xpath(xPath));
		String webPageTimeString = xPathTimeField.getText().trim().split(" ")[0];		
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
		String timeString = sdf.format(d).split(" ")[0];
		
		//the timestring on the web doesn't contain a 0 whereas the date conversion one does
		//eg 1:25 AM as opposed to 01:25 AM . Note the 0
		if(timeString.charAt(0)=='0'){
			//just disregard the first / 0th character which is the char '0'
			timeString = timeString.substring(1);
		}

		if (! timeString.trim().equalsIgnoreCase(webPageTimeString.trim()) ){
			System.out.println("\nERROR: times don't match up" );
			return false;
		}
		System.out.println("\nCHECK: start times match up for webPageTimeString and what we expect : " + webPageTimeString);
		
		return true;
	}
	
	
}
