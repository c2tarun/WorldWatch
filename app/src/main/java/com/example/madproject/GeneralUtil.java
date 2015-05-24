package com.example.madproject;


/*
 * HW05
 * GeneralUtil.java
 * Tarun Kumar Mall
 * Pragya Rai
 */
public final class GeneralUtil {
	
	public static boolean isEmpty(String str) {
		if(str == null)
			return true;
		if(str.trim().length() == 0)
			return true;
		return false;
	}

}
