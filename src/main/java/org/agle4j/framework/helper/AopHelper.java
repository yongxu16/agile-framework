package org.agle4j.framework.helper;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.agle4j.framework.annotation.Aspect;
import org.agle4j.framework.annotation.Service;
import org.agle4j.framework.proxy.AspectProxy;
import org.agle4j.framework.proxy.Proxy;
import org.agle4j.framework.proxy.ProxyManager;
import org.agle4j.framework.proxy.TransactionProxy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * AOP 助手类
 * @author hyx
 *
 */
public final class AopHelper {

	private static final Logger LOG = LogManager.getLogger(AopHelper.class) ;
	
	/**
	 * 通过静态块来初始化整个AOP框架
	 */
	static {
		try {
			Map<Class<?>, Set<Class<?>>> proxyMap = createProxyMap() ;
			Map<Class<?>, List<Proxy>> targetMap = createTargetMap(proxyMap) ;
			for(Map.Entry<Class<?>, List<Proxy>> targetEntry : targetMap.entrySet()) {
				Class<?> targetClass = targetEntry.getKey() ;
				List<Proxy> proxyList = targetEntry.getValue() ;
				Object proxy = ProxyManager.createProxy(targetClass, proxyList) ;
				BeanHelper.setBean(targetClass, proxy);
			}
		} catch (Exception e) {
			LOG.error("aop failure", e);
		}
	}
	
	/**
	 * 获取Aspect注解中设置的注解类
	 * 带有此注解的所有类
	 */
	private static Set<Class<?>> createTargetClassSet(Aspect aspect) throws Exception {
		Set<Class<?>> targetClassSet = new HashSet<>() ;
		Class<? extends Annotation> annotation  = aspect.value() ;
		if(annotation != null && !annotation.equals(Aspect.class)) {
			targetClassSet.addAll(ClassHelper.getClassSetByAnnotation(annotation)) ;
		}
		return targetClassSet;
	}
	
	/**
	 * 获取 代理类及其目标类集合之间的映射关系，此处代理类值得是切面类
	 */
	private static Map<Class<?>, Set<Class<?>>> createProxyMap() throws Exception {
		Map<Class<?>, Set<Class<?>>> proxyMap = new HashMap<>() ;
		addAspectProxy(proxyMap);
		addTransactionProxy(proxyMap);
		return proxyMap ;
	}
	
	/**
	 * 获取 目标类与代理对象列表之间的映射关系集合
	 */
	private static Map<Class<?>, List<Proxy>> createTargetMap(Map<Class<?>,Set<Class<?>>> proxyMap) throws Exception{
		Map<Class<?>, List<Proxy>> targetMap = new HashMap<>() ;
		for(Map.Entry<Class<?>, Set<Class<?>>> proxyEntory : proxyMap.entrySet()) {
			Class<?> proxyClass = proxyEntory.getKey() ;
			Set<Class<?>> targetClassSet = proxyEntory.getValue() ;
			for(Class<?> targetClass : targetClassSet) {
				Proxy proxy = (Proxy) proxyClass.newInstance() ;
				if(targetMap.containsKey(targetClass)) {
					targetMap.get(targetClass).add(proxy) ;
				} else {
					List<Proxy> proxyList = new ArrayList<>() ;
					proxyList.add(proxy) ;
					targetMap.put(targetClass, proxyList) ;
				}
			}
		}
		return targetMap ;
	}
	
	private static void addAspectProxy(Map<Class<?>, Set<Class<?>>> proxyMap) throws Exception {
		Set<Class<?>> proxyClassSet = ClassHelper.getClassSetBySuper(AspectProxy.class) ;
		for(Class<?> proxyClass : proxyClassSet) {
			if(proxyClass.isAnnotationPresent(Aspect.class)) {
				Aspect aspect = proxyClass.getAnnotation(Aspect.class) ;
				Set<Class<?>> targetClassSet = createTargetClassSet(aspect) ;
				proxyMap.put(proxyClass, targetClassSet) ;
			}
		}
	}
	
	private static void addTransactionProxy(Map<Class<?>, Set<Class<?>>> proxyMap) {
		Set<Class<?>> serviceClassSet = ClassHelper.getClassSetByAnnotation(Service.class) ;
		proxyMap.put(TransactionProxy.class, serviceClassSet) ;
	}
}
