package com.sogwiz.ctrx.util;

public enum ClientType {
	
	UNKOWN ("unkown"),
	BROWSER ("browser"),
	IPHONE ("iphone"),
	ANDROID ("android"),
	CHROME ("chrome"),
	FIREFOX ("firefox"),
	SAFARI ("safari");
	
	String name;
	
	private ClientType(String s) {
		name = s;
	}
	
	public static ClientType fromString(String s) {
		if(s.equalsIgnoreCase("iphone")){
			return ClientType.IPHONE;
		}else if (s.equalsIgnoreCase("android")){
			return ClientType.ANDROID;
		}else if (s.equalsIgnoreCase("firefox")){
			return ClientType.FIREFOX;
		}else if (s.equalsIgnoreCase("chrome")){
			return ClientType.CHROME;
		}else if (s.equalsIgnoreCase("safari")){
			return ClientType.SAFARI;
		}
		
		return ClientType.BROWSER;
	}
	
	public static boolean isBrowser(ClientType cType){
		if(cType.equals(ClientType.CHROME) || cType.equals(ClientType.SAFARI) || cType.equals(ClientType.FIREFOX) ){
			return true;
		}
		return false;
	}
	
	

}
