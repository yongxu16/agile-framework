package org.agle4j.framework.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 属性文件工具类
 * @author hanyx
 *
 */
public final class PropsUtil {

	private static final Logger LOG = LogManager.getLogger(PropsUtil.class);

	/**
	 * 加载属性文件
	 */
	public static Properties loadProps(String fileName) {
		Properties props = null ;
		InputStream is = null ;
		try {
			is = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName) ;
			if(is == null) {
				throw new FileNotFoundException(fileName + " file is not found") ;
			}
			props = new Properties() ;
			props.load(is);
		} catch (IOException e) {
			LOG.error("load properties file failure", e);
		} finally {
			if(is != null) {
				try {
					is.close();
				} catch (IOException e) {
					LOG.error("close input stream failure", e);
				}
			}
		}
		return props;
	}
	
	/**
	 * 获取字符类属性(默认值为 空字符串)
	 */
	public static String getString(Properties props, String key) {
		return getString(props, key, "") ;
	}
	
	/**
	 * 获取字符型属性(可指定默认值)
	 */
	public static String getString(Properties props, String key, String defaultValue) {
		String value = defaultValue ;
		if(props.containsKey(key)) {
			value = props.getProperty(key) ;
		}
		return value ;
	}
	
	/**
	 * 获取数值类型属性(默认值为 0)
	 */
	public static int getInt(Properties props, String key) {
		return getInt(props, key, 0) ;
	}
	
	/**
	 * 获取数值类型属性(可指定默认值)
	 */
	public static int getInt(Properties props, String key, int defaultValue) {
		int value = defaultValue ;
		if(props.containsKey(key)) {
			value = Integer.valueOf(props.getProperty(key)) ;
		}
		return value ;
	}
	
	/**
	 * 获取布尔类型属性(默认值为 false)
	 */
	public static boolean getBoolean(Properties props, String key) {
		return getBoolean(props, key, false) ;
	}
	
	/**
	 * 获取布尔类型属性(可指定默认值)
	 */
	public static boolean getBoolean(Properties props, String key, boolean defaultValue) {
		boolean value = defaultValue ;
		if(props.containsKey(key)) {
			value = Boolean.valueOf(props.getProperty(key)) ;
		}
		return value ;
	}
}
