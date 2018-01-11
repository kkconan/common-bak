package com.money.game.core.util;

import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
public class IPUtil {
	
	
	public static String getLocalIP() {
//		return "127.127.0.1";
		InetAddress addr;
		String ip = null;
		try {
			addr = InetAddress.getLocalHost();
			ip = addr.getHostAddress();
		} catch (UnknownHostException e) {
			log.error(e.getMessage(), e);
		}
		
		return ip;
	}

}
