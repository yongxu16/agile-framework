package org.agle4j.framework;

import org.agle4j.framework.helper.AopHelper;
import org.agle4j.framework.helper.BeanHelper;
import org.agle4j.framework.helper.ClassHelper;
import org.agle4j.framework.helper.ControllerHelper;
import org.agle4j.framework.helper.IocHelper;
import org.agle4j.framework.utils.ClassUtil;

/**
 * 加载相应的Helper 类
 * @author hanyx
 *
 */
public final class HelperLoder {

	public static void init() {
		Class<?>[] classList = {
				ClassHelper.class,
				BeanHelper.class,
				AopHelper.class,
				IocHelper.class,
				ControllerHelper.class
		} ;
		
		for(Class<?> cls : classList) {
			ClassUtil.loadClass(cls.getName(), true) ;
		}
		
	}
}
