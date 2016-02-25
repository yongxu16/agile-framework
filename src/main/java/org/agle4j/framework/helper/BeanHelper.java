package org.agle4j.framework.helper;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.agle4j.framework.utils.ReflectionUtil;

/**
 * Bean 助手类
 * @author hyx
 *
 */
public final class BeanHelper {

	/**
	 * 定义 Bean 映射（用于存放Bean类 与 Bean实例的映射关系）
	 */
	private static final Map<Class<?>, Object> BEAN_MAP = new HashMap<>() ;
	
	static {
		Set<Class<?>> beanClassSet = ClassHelper.getBeanClassSet() ;
		for(Class<?> cls : beanClassSet) {
			Object obj = ReflectionUtil.newInstance(cls) ;
			BEAN_MAP.put(cls, obj) ;
		}
	}
	
	/**
	 * 获取 Bean 映射
	 */
	public static Map<Class<?>,Object> getBeanMap() {
		return BEAN_MAP ;
	}
	
	/**
	 * 获取Bean实例
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(Class<T> cls) {
		if(!BEAN_MAP.containsKey(cls)) {
			throw new RuntimeException("can not get ben by clss:" + cls) ;
		}
		return (T) BEAN_MAP.get(cls) ;
	}
	
	/**
	 * 设置 Bean实例
	 */
	public static void setBean(Class<?> cls, Object obj) {
		BEAN_MAP.put(cls, obj) ;
	}
}