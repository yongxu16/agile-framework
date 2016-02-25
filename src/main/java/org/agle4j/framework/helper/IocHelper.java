package org.agle4j.framework.helper;

import java.lang.reflect.Field;
import java.util.Map;

import org.agle4j.framework.annotation.Inject;
import org.agle4j.framework.utils.CollectionUtil;
import org.agle4j.framework.utils.ReflectionUtil;
import org.apache.commons.lang3.ArrayUtils;

/**
 * 依赖注入助手类
 * @author hyx
 *
 */
public final class IocHelper {

	static {
		// 获取所有的Bean类与Bean实例之间的映射关系（简称Bean Map）
		Map<Class<?>, Object> beanMap = BeanHelper.getBeanMap() ;
		if(CollectionUtil.isNotEmpty(beanMap)) {
			// 遍历 Bean Map
			for(Map.Entry<Class<?>, Object> beanEntry: beanMap.entrySet()) {
				// 从 Beanmap中国区Bean类与Bean实例
				Class<?> beanClass = beanEntry.getKey() ;
				Object beanInstance = beanEntry.getValue() ;
				// 获取Bean类定义的所有成员变量(简称 Bean Field)
				Field[] beanFields = beanClass.getDeclaredFields() ;
				if(ArrayUtils.isNotEmpty(beanFields)){
					// 遍历 Bean Field
					for(Field beanField : beanFields) {
						// 判断当前Bean Field是否带有 Inject注解
						if(beanField.isAnnotationPresent(Inject.class)) {
							// 在 Bean Map中 获取Bean Field对应的实例
							Class<?> beanFieldClass = beanField.getType() ;
							Object benFieldInstance = beanMap.get(beanFieldClass) ;
							if(benFieldInstance != null) {
								// 通过反色初始化 BeanField的值
								ReflectionUtil.setField(beanInstance, beanField, benFieldInstance); ;
							}
						}
					}
				}
				
			}
		}
	}
}
