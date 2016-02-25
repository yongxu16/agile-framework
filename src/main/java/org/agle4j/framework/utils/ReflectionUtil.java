package org.agle4j.framework.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSON;

/**
 * 反射工具类
 * @author hyx
 */
public final class ReflectionUtil {

	private static final Logger LOG = LogManager.getLogger(ReflectionUtil.class) ;
	
	/**
	 * 创建实例
	 */
	public static Object newInstance(Class<?> cls) {
		Object instance ;
		try {
			instance = cls.newInstance() ;
		} catch (Exception e) {
			LOG.error("new instance failure", e);
			throw new RuntimeException(e) ;
		} 
		return instance ;
	}
	
	public static Object newInstance(String className) {
		Class<?> cls = null;
		try {
			cls = Class.forName(className);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return newInstance(cls) ;
	}
	
	/**
	 * 调用方法
	 */
	public static Object invokeMethod(Object obj, Method method, Object...args) {
		Object result ;
		try {
			method.setAccessible(true);
			result = method.invoke(obj, args) ;
		} catch (Exception e) {
			LOG.error("invoke method failure", e);
			throw new RuntimeException(e) ;
		}
		return result ;
	}
	
	/**
	 * 设置成员变量值
	 */
	public static void setField(Object obj, Field field, Object value) {
		try {
			field.setAccessible(true);
			field.set(obj, value);
		} catch (Exception e) {
			LOG.error("set field failure", e);
			throw new RuntimeException(e) ;
		}
	}
	
	public static void main(String[] args) throws Exception {
		Class<?> cls = Class.forName("cn.hyx.invoicing.customer.entity.Customer") ;
		Object obj = ReflectionUtil.newInstance(cls) ;
		
		Field name = cls.getDeclaredField("name") ;
		ReflectionUtil.setField(obj, name, "hanyx");
		
		Method method = cls.getDeclaredMethod("setEmail", String.class) ;
		ReflectionUtil.invokeMethod(obj, method, "yongxu16@163.com") ;
		
//		PropertyUtils.setProperty(obj, "name", "hanyx");
//		MethodUtils.invokeMethod(obj, "setEmail", "yongxu16@163.com") ;
		
		System.out.println(JSON.toJSONString(obj));
	}
}
