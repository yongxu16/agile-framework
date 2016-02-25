package org.agle4j.framework.helper;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Servlet 助手类
 * 
 * @author hanyx
 * @since 0.9.1
 */
public final class ServletHelper {

	private static final Logger LOG = LogManager.getLogger(ServletHelper.class) ;
	
	/**
	 * 使每个线程独自拥有一份  ServletHelper 实例
	 */
	private static final ThreadLocal<ServletHelper> SERVLET_HELPER_HOLDER = new ThreadLocal<ServletHelper>() ;
	
	private HttpServletRequest request ;
	private HttpServletResponse response ;
	
	private ServletHelper(HttpServletRequest request, HttpServletResponse response) {
		this.request = request ;
		this.response = response ;
	}
	
	/**
	 * 初始化
	 */
	public static void init(HttpServletRequest request, HttpServletResponse response) {
		SERVLET_HELPER_HOLDER.set(new ServletHelper(request, response));
	}
	
	/**
	 * 销毁
	 */
	public static void destory() {
		SERVLET_HELPER_HOLDER.remove();
	}
	
	/**
	 * 获取 Request 对象
	 */
	private static HttpServletRequest getRequset() {
		return SERVLET_HELPER_HOLDER.get().request ;
	}
	
	/**
	 * 获取 Response 对象
	 */
	private static HttpServletResponse getResponse() {
		return SERVLET_HELPER_HOLDER.get().response ;
	}
	
	/**
	 * 获取 Session 对象
	 * @return
	 */
	private static HttpSession getSession() {
		return getRequset().getSession() ;
	}
	
	/**
	 * 获取 ServletContext 对象
	 */
	private static ServletContext getSerlvetContext() {
		return getRequset().getServletContext() ;
	}
	
	/**
	 * 将属性放入 Request 中
	 */
	public static void setRequestAttribute(String key, Object value) {
		getRequset().setAttribute(key, value);
	}
	
	/**
	 * 从 Request 中获取属性
	 */
	public static <T> T getRequestAttribute(String key) {
		return (T) getRequset().getAttribute(key) ;
	}
	
	/**
	 * 从 Request 中移除属性
	 */
	public static void removeRequestAttribut(String key) {
		getRequset().removeAttribute(key);
	}
	
	/**
	 * 发送重定向响应
	 * @param location
	 */
	public static void sendRedirect(String location) {
		try {
			getResponse().sendRedirect(getRequset().getContextPath() + location);
		} catch (IOException e) {
			LOG.error("send redirect failure", e);
		}
	}
	
	/**
	 * 将属性放入 Session 中
	 */
	public static void setSessionAttribute(String key, Object value) {
		getSession().setAttribute(key, value);
	}
	
	/**
	 * 从 Session 中获取数据
	 */
	public static <T> T getSessionAttribute(String key) {
		return (T) getSession().getAttribute(key) ;
	}
	
	/**
	 * 从 Session 中移除属性
	 */
	public static void removeSessionAttribute(String key) {
		getSession().removeAttribute(key);
	}
	
	/**
	 * 使 Session 失效
	 */
	public static void invalidateSession() {
		getSession().invalidate();
	}
}
