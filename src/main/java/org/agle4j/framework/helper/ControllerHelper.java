package org.agle4j.framework.helper;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.agle4j.framework.annotation.Action;
import org.agle4j.framework.bean.Handler;
import org.agle4j.framework.bean.Request;
import org.agle4j.framework.utils.CollectionUtil;
import org.apache.commons.lang3.ArrayUtils;

/**
 * 控制器助手类
 * @author hanyx
 *
 */
public final class ControllerHelper {
	
	/**
	 * 用于存放请求与处理器的映射关系(简称 Action Map)
	 */
	private static final Map<Request, Handler> ACTION_MAP = new HashMap<>() ;
	
	static {
		// 获取所有的 Controller 类
		Set<Class<?>> controllerClassSet = ClassHelper.getControllerClassSet() ;
		if(CollectionUtil.isNotEmpty(controllerClassSet)) {
			// 遍历这些 Controller 类
			for(Class<?> controllerClass : controllerClassSet) {
				// 获取Controller 类中 定义的方法
				Method[] mothods = controllerClass.getMethods() ;
				if(ArrayUtils.isNotEmpty(mothods)) {
					// 遍历这些 Controller 类中方法
					for(Method method : mothods) {
						// 判断当前方法是否带有 Actioin 注解
						if(method.isAnnotationPresent(Action.class)) {
							// 从 Action 注解中获取 URL映射规则
							Action actioin = method.getAnnotation(Action.class) ;
							String mapping = actioin.value() ;
							// 验证URL映射规则 (/:结束)
							if(mapping.matches("\\w+:/\\w*")) {
								String[] array = mapping.split(":") ;
								if(ArrayUtils.isNotEmpty(array) && array.length==2) {
									// 获取请求方法与请求路径
									String requestMethod = array[0] ;
									String requestPath = array[1] ;
									Request request = new Request(requestMethod, requestPath) ;
									Handler handler = new Handler(controllerClass, method) ;
									// 初始化 Action Map
									ACTION_MAP.put(request, handler) ;
								}
							}
							
						}
					}
				}
			}
		}
	}
	
	/**
	 * 获取Handler
	 */
	public static Handler getHandler(String requestMethod, String requestPath) {
		Request request = new Request(requestMethod, requestPath) ;
		return ACTION_MAP.get(request) ;
	}
}
