package org.agle4j.framework.utils;

import java.util.UUID;

/**
 * 字符串工具类
 * @author hanyx
 *
 */
public final class StringUtil {

	public static final String SEPARATOR = String.valueOf((char) 29) ;
	
	public static String getUUID() {
		return UUID.randomUUID().toString().replace("-", "") ;
	}
}
