package org.agle4j.framework.bean;

import java.lang.reflect.Method;

/**
 * 封装 Action 信息
 * @author hanyx
 *
 */
public class Handler {

	/**
	 * 封装 Action 信息
	 */
	private Class<?> contorllerClass ;
	
	/**
	 * Action 方法
	 */
	private Method actionMethod ;
	
	public Handler(Class<?> controllerClass, Method actionMethod) {
		this.contorllerClass = controllerClass ;
		this.actionMethod = actionMethod ;
	}

	public Class<?> getContorllerClass() {
		return contorllerClass;
	}

	public Method getActionMethod() {
		return actionMethod;
	}
	
}
