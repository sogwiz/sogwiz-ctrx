package com.sogwiz.ctrx.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.sogwiz.ctrx.util.ClientType;

public class LoginPage extends BasePageObject {

	public LoginPage(ClientType cType){
		super(cType);
	}
	
	public void login(String userName, String password){
		// Find the text input elements by id
        WebElement elementEmail = driver.findElement(By.id("emailAddress"));
        WebElement elementPassword = driver.findElement(By.id("password"));
        
        elementEmail.sendKeys(userName);
        elementPassword.sendKeys(password);
        
        WebElement signInButton = driver.findElement(By.id("submit"));
        signInButton.click();
        
	}
	
	/**
	 * returns true if error message appears on the page. This is useful for negative testing
	 * @return
	 */
	public boolean verifyErrorMessagePresent(){
		try{
			WebElement elementError = driver.findElement(By.id("emailAddress.errors"));
			return elementError.isDisplayed();
		}catch(Exception e){
			return false;
		}
	}
}
