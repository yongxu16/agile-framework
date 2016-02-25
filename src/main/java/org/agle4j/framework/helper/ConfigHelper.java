package org.agle4j.framework.helper;

import java.util.Properties;

import org.agle4j.framework.constant.ConfigConstant;
import org.agle4j.framework.utils.PropsUtil;

/**
 * 属性文件助手类
 * @author hanyx
 *
 */
public final class ConfigHelper {
	private static final Properties CONFIG_PROPS = PropsUtil.loadProps(ConfigConstant.CONFIG_FILE) ;
	
	/**
	 * 获取 JDBC 驱动
	 * @return
	 */
	public static String getJdbcDriver() {
		return PropsUtil.getString(CONFIG_PROPS, ConfigConstant.JDBC_DRIVER) ;
	}
	
	/**
	 * 获取 JDBC URL
	 * @return
	 */
	public static String getJdbcUrl() {
		return PropsUtil.getString(CONFIG_PROPS, ConfigConstant.JDBC_URl) ;
	}
	
	/**
	 * 获取 JDBC 用户名
	 * @return
	 */
	public static String getJdbcUsername() {
		return PropsUtil.getString(CONFIG_PROPS, ConfigConstant.JDBC_USERNAME) ;
	}
	
	/**
	 * 获取 JDBC 密码
	 * @return
	 */
	public static String getJdbcPassword() {
		return PropsUtil.getString(CONFIG_PROPS, ConfigConstant.JDBC_PASSWORD) ;
	}
	
	/**
	 * 获取应用基础包名
	 * @return
	 */
	public static String getAppBasePackage() {
		return PropsUtil.getString(CONFIG_PROPS, ConfigConstant.APP_BASE_PACKAGE) ;
	}
	
	/**
	 * 获取应用 JSP 路径
	 * @return
	 */
	public static String getAppJspPath() {
		return PropsUtil.getString(CONFIG_PROPS, ConfigConstant.APP_JSP_PATH) ;
	}
	
	/**
	 * 获取应用静态资源路径
	 * @return
	 */
	public static String getAppAssetPath() {
		return PropsUtil.getString(CONFIG_PROPS, ConfigConstant.APP_ASSET_PATH) ;
	}
	
	/**
	 * 获取应用文件上传限制
	 */
	public static int getAppUploadLimit() {
		return PropsUtil.getInt(CONFIG_PROPS, ConfigConstant.APP_UPLOAD_LIMIT, 10) ;
	}
	
	/**
	 * 过去文件上传路径
	 */
	public static String getAppUploadPath() {
		return PropsUtil.getString(CONFIG_PROPS, ConfigConstant.APP_UPLOAD_PATH, "/tmp/upload/") ;
	}
	
	public static String getString(String str) {
		return PropsUtil.getString(CONFIG_PROPS, str) ;
	}
	
	public static boolean getBoolean(String str) {
		return PropsUtil.getBoolean(CONFIG_PROPS, str) ;
	}
}
