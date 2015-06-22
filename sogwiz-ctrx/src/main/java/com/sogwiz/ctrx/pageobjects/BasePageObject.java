package com.sogwiz.ctrx.pageobjects;

import org.openqa.selenium.WebDriver;

import com.sogwiz.ctrx.util.ClientType;


public abstract class BasePageObject {

	ClientType clientType;
	
	/**
     * This field receives a valid web driver object you can use to talk to the web app.
     */
	public WebDriver driver;
	
	public BasePageObject(ClientType cType){
		this.clientType = cType;
	}
	
	public BasePageObject withDriver(WebDriver webDriver){
		this.driver = webDriver;
		return this;
	}

}
