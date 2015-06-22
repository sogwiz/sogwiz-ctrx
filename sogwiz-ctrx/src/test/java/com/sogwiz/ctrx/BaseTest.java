package com.sogwiz.ctrx;

import java.util.Hashtable;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.sogwiz.ctrx.util.ConfigDataReader;



@Test
public class BaseTest {
	
	/**
     * This field receives a valid web driver object you can use to talk to app.
     */
    public WebDriver driver;
    ConfigDataReader configDataReader = ConfigDataReader.getInstance();
    public App app;
    
    @BeforeMethod
    public void openApp() throws Exception {
    	this.driver = createWebDriver(null);
    	openWebApp();
    	this.app = new App(this.driver);
    }
    
    @AfterMethod
    public void tearDown() throws Exception{
    	this.driver.quit();
    }

    
    public WebDriver createWebDriver(Hashtable extras) throws Exception {
		DesiredCapabilities capabilities = new DesiredCapabilities();
		
		System.setProperty("webdriver.chrome.driver", configDataReader.getDriverPath());
        
		ChromeOptions options = new ChromeOptions();
		WebDriver webDriver = new ChromeDriver(options);
        webDriver.manage().window().maximize();
        webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		return webDriver;
    }
    
    private void openWebApp()
	{
		String url;
		url=configDataReader.getBaseURL();	
		driver.get(url);		
	}
}
