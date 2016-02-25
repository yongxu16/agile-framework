package org.agle4j.framework.utils;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 转型操作工具类
 * @author hanyx
 *
 */
public final class CastUtil {

	private static final Logger LOG = LogManager.getLogger(CastUtil.class) ;
	/**
	 * 转为 String 型
	 */
	public static String castString(Object obj) {
		return castString(obj, "") ;
	}
	
	/**
	 * 转为 String 型 (可指定默认值)
	 */
	public static String castString(Object obj, String defaultValue) {
		return obj != null ? String.valueOf(obj) : defaultValue ;
	}
	
	/**
	 * 转为 int 型
	 */
	public static int castInt(Object obj) {
		return castInt(obj, 0) ;
	}
	
	/**
	 * 转为 int 型 (提供默认值)
	 */
	public static int castInt(Object obj, int defaultValue) {
		int intValue = defaultValue ;
		if(obj != null) {
			String strValue = castString(obj) ;
			if(StringUtils.isNotEmpty(strValue)) {
				try {
					intValue = Integer.parseInt(strValue) ;
				} catch (NumberFormatException e) {
					intValue = defaultValue ;
				}
			}
		}
		return intValue ;
	}
	
	/**
	 * 转为 boolean
	 */
	public static boolean castBoolean(Object obj) {
		return castBoolean(obj, false) ;
	}
	
	/**
	 * 转为 boolean (提供默认值)
	 */
	public static boolean castBoolean(Object obj, boolean defaultValue) {
		boolean booleanValue = defaultValue ;
		if(obj != null) {
			booleanValue = Boolean.parseBoolean(castString(obj)) ;
		}
		return booleanValue ;
	}
	
	/**
	 * 转为 Bigdecimal
	 */
	public static BigDecimal castBigDecimal(Object obj) {
		return castBigDecimal(obj, BigDecimal.ZERO) ;
	}
	
	/**
	 * 转为 Bigdecimal (可指定默认值)
	 */
	public static BigDecimal castBigDecimal(Object obj, BigDecimal defaultValue) {
		BigDecimal bigDecimalValue = defaultValue ;
		if(obj != null) {
			try {
				bigDecimalValue = new BigDecimal(castString(obj)) ;
			} catch (Exception e) {
				bigDecimalValue = defaultValue ;
				LOG.error("cast bigdecimal failure", e);
			}
		}
		return bigDecimalValue ;
	}
	
	/**
	 * 转为 long
	 */
	public static long castLong(Object obj) {
		return castLong(obj, 0L) ;
	}
	
	/**
	 * 转为 long (可指定默认值)
	 */
	public static long castLong(Object obj, long defaultValue) {
		long langValue = defaultValue ;
		if(obj != null) {
			langValue = Long.parseLong(castString(obj)) ;
		}
		return langValue ;
	}
}
