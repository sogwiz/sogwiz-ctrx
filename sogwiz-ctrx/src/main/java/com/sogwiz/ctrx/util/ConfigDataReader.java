package com.sogwiz.ctrx.util;

import java.io.FileInputStream;
import java.util.*;

/**
 * glorified properties file reader
 * Most of the time, this util checks to see if the property is specified via the command line through jenkins
 * It then falls back to the properties file for the value
 * @author sargon
 *
 */
public class ConfigDataReader {
	
	public static final String LANGUAGE_SELECTOR = "intl.accept_languages";
	
	String File= "/config/testdata.properties";
	String PARAM_CLIENT_TYPE = "clientType";
	String PARAM_APP_PATH = "app";
	String PARAM_URL_BASE = "url_base";
	String PARAM_DRIVER_PATH = "driver_path";
	String PARAM_DEFAULT_USERNAME = "username";
	String PARAM_DEFAULT_PASSWORD = "password";
	String PARAM_DAYS_OFFSET = "days_offset";
	
	Properties data= new Properties();
	FileInputStream fis;
	private static ConfigDataReader INSTANCE = null;
	
	public static ConfigDataReader getInstance() {
		if(INSTANCE == null){
			INSTANCE = new ConfigDataReader();
		}
		return INSTANCE;
	}
	
	public ConfigDataReader()
	{
	try {
		String path = new java.io.File(".").getCanonicalPath();	
		fis= new FileInputStream(path+File);
		data.load(fis);
	} catch (Exception e) {
		e.printStackTrace();
	}
	}
	
	public String getDefaultBrowserType()
	{
		return "chrome";
	}
	
	public String getEnvironment()
	{
		return(data.getProperty("env"));
	}
	
	
	public String getBaseURL()
	{
		return(data.getProperty(PARAM_URL_BASE));
	}
	
	/**
	 * 
	 * @return the client type in a string format 
	 */
	public String getClientTypeString() {
		return System.getProperty(PARAM_CLIENT_TYPE)!=null && !System.getProperty(PARAM_CLIENT_TYPE).isEmpty() ?  System.getProperty(PARAM_CLIENT_TYPE) : data.getProperty( PARAM_CLIENT_TYPE );
	}
	
	public String getDriverPath(){
		return System.getProperty(PARAM_DRIVER_PATH)!=null ? 
				AppUtils.getAbsolutePath(System.getProperty(PARAM_DRIVER_PATH)) :
		AppUtils.getAbsolutePath( data.getProperty(PARAM_DRIVER_PATH) );
	}

	public String getDefaultUsername() throws Exception{
		if (System.getProperty(PARAM_DEFAULT_USERNAME)==null){
			throw new Exception("Must supply -Dusername param via maven command line execution\neg mvn clean install -Dusername=<INSERT_USERNAME> -Dpassword=<PASSWORD>");
		}
		return System.getProperty(PARAM_DEFAULT_USERNAME);
	}
	
	public String getDefaultPassword() throws Exception{
		if (System.getProperty(PARAM_DEFAULT_PASSWORD)==null){
			throw new Exception("Must supply -Dpassword param via maven command line execution\neg mvn clean install -Dusername=<INSERT_USERNAME> -Dpassword=<PASSWORD>");
		}
		return System.getProperty(PARAM_DEFAULT_PASSWORD);
	}
	
	public int getDaysOffset(){
		return Integer.valueOf(data.getProperty(PARAM_DAYS_OFFSET));
	}
	
}

