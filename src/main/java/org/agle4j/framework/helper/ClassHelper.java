package org.agle4j.framework.helper;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import org.agle4j.framework.annotation.Controller;
import org.agle4j.framework.annotation.Service;
import org.agle4j.framework.utils.ClassUtil;


/**
 * 类操作助手类
 * @author hyx
 *
 */
public final class ClassHelper {

	/**
	 * 定义类集合（用于存放加载的类）
	 */
	private static final Set<Class<?>> CLASS_SET ;
	
	static {
		String basePackage = ConfigHelper.getAppBasePackage() ;
		CLASS_SET = ClassUtil.getClassSet(basePackage) ;
	}
	
	/**
	 * 获取应用包名下的所有类
	 */
	public static Set<Class<?>> getClassSet() {
		return CLASS_SET ;
	}
	
	/**
	 * 获取应用包名下所有Service 类
	 */
	public static Set<Class<?>> getServiceClassSet() {
		Set<Class<?>> classSet = new HashSet<>() ;
		for(Class<?> cls : CLASS_SET) {
			if(cls.isAnnotationPresent(Service.class)) {
				classSet.add(cls) ;
			}
		}
		return classSet ;
	}
	
	/**
	 * 获取应用包下所有Controler 类
	 */
	public static Set<Class<?>> getControllerClassSet() {
		Set<Class<?>> classSet = new HashSet<>() ;
		for(Class<?> cls : CLASS_SET) {
			if(cls.isAnnotationPresent(Controller.class)) {
				classSet.add(cls) ;
			}
		}
		return classSet ;
	}
	
	/**
	 * 获取应用包名下所有Bean类（包括：Service， Controller 类）
	 */
	public static Set<Class<?>> getBeanClassSet() {
		Set<Class<?>> beanClassSet = new HashSet<>() ;
		beanClassSet.addAll(getControllerClassSet()) ;
		beanClassSet.addAll(getServiceClassSet()) ;
		return beanClassSet ;
	}
	
	/**
	 * 获取应用包名下其父类（或接口）的所有子类（或实现类）
	 */
	public static Set<Class<?>> getClassSetBySuper(Class<?> superClass) {
		Set<Class<?>> classSet = new HashSet<>() ;
		for(Class<?> cls : CLASS_SET){
			if(superClass.isAssignableFrom(cls) && !superClass.equals(cls)) {
				classSet.add(cls) ;
			}
		}
		return classSet ;
	}
	
	/**
	 * 获取应用包名下带有某注解的所有类
	 */
	public static Set<Class<?>> getClassSetByAnnotation(Class<? extends Annotation> annotationClass){
		Set<Class<?>> classSet = new HashSet<>() ;
		for(Class<?> cls : CLASS_SET) {
			if(cls.isAnnotationPresent(annotationClass)) {
				classSet.add(cls);
			}
		}
		return classSet;
	}
	
	
}
