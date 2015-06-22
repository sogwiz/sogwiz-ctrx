package com.sogwiz.ctrx.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

public class AppUtils {
	
	/**
     * simply replaces the ~ character with the user.home system property
     * allows the user to submit a path with the ~ operator
     * @param pathName
     * @return
     */
    public static String getAbsolutePath(String pathName) {
    	return pathName.replace("~/", System.getProperty("user.home") + "/");
    }
    
    /**
     * adds X number of days to the current date
     * I've tested that this works in the wraparound scenario into the next month
     * @param daysToAdd
     * @return
     */
    public static Date getDatePlusDays(int daysToAdd){
		Calendar tmp = Calendar.getInstance();
		tmp.add(Calendar.DATE, daysToAdd);
		Date start = tmp.getTime();
		
		return start;
	}	
    
    public static String getHourAndMinutesString(Date d){
    	SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
		String dateString = sdf.format(d);
	
		StringTokenizer strTok = new StringTokenizer(dateString);
		return strTok.nextToken();
    }
    
    /**
     * deterimine if we are in the next month given the original days offset
     * @param d
     * @param daysDelta
     * @return
     */
    public static boolean isNextMonth(Date d, int daysDelta){
    	Calendar tmp = Calendar.getInstance();
		tmp.setTime(d);
		int day = tmp.get(Calendar.DATE);
		
		if (day <= daysDelta){
			return true;
		}
    	
		return false;
    }
}
