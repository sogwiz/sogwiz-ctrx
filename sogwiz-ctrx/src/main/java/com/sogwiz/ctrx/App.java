package com.sogwiz.ctrx;

import org.openqa.selenium.WebDriver;

import com.sogwiz.ctrx.pageobjects.HomePage;
import com.sogwiz.ctrx.pageobjects.LoginPage;
import com.sogwiz.ctrx.util.ClientType;
import com.sogwiz.ctrx.util.ConfigDataReader;

/**
 * 1. entry point to the underlying app under test
 * 2. page factory that returns various page objects
 *
 */
public class App 
{
	public WebDriver driver ;
	
	public App(WebDriver webDriver){
		this.driver = webDriver;
	}
	
	/**
	 * 
	 * @return the clientType enum that represents what device / browser will be used by the tests and page classes
	 * <p>
	 * The clientType property is first read from the java runtime options -DclientType .  A fallback mechanism is used to check in the testdata.properties file
	 */
	public ClientType getClientType(){
		String clientType = ConfigDataReader.getInstance().getClientTypeString();
		return ClientType.fromString(clientType);
	}
	
	public LoginPage loginPage(){
		return (LoginPage) new LoginPage(getClientType()).withDriver(this.driver);
	}
	
	public HomePage homePage(){
		return (HomePage) new HomePage(getClientType()).withDriver(this.driver);
	}
	
	
}
