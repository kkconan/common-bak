package com.money.game.core.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class StaticProperties {

	/** 协议存放目录 */
	@Value("${lecurrent.splitby:15:00}")
	String splitby;
	
	public static String stSplitby;
	
	
	
	static boolean is15;
	static boolean is24;
	
	static int hour;
	static int minute;
	static int second = 0;
	static int millisecond = 0;
	/**
	 * 创建文件目录
	 */
	@PostConstruct
	public void init(){
		
		StaticProperties.stSplitby = splitby;
		
		if ("00:00".equals(stSplitby))  {
			is24 = true;
			is15 = false;
		} else {
			is24 = false;
			is15 = true;
		}
		String[] arr = StaticProperties.stSplitby.split(":");
		StaticProperties.hour = Integer.parseInt(arr[0]);
		StaticProperties.minute = Integer.parseInt(arr[1]);
	}
	
	public static boolean isIs15() {
		return is15;
	}
	
	public static boolean isIs24() {
		return is24;
	}
	
	public static int getHour() {
		return hour;
	}
	
	public static int getMinute() {
		return minute;
	}
	
	public static int getSecond() {
		return second;
	}
	
	public static int getMillisecond() {
		return millisecond;
	}
	
}
