package org.agle4j.framework.helper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.agle4j.framework.bean.FormParam;
import org.agle4j.framework.bean.Param;
import org.agle4j.framework.utils.CodecUtil;
import org.agle4j.framework.utils.StreamUtil;
import org.agle4j.framework.utils.StringUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 请求助手类
 * @author hanyx
 * @since 0.9
 */
public final class RequestHelper {

	/**
	 * 创建请求对象
	 */
	public static Param createParam(HttpServletRequest request) throws IOException {
		List<FormParam> formParamList = new ArrayList<>() ;
		formParamList.addAll(parseParameterNames(request)) ;
		formParamList.addAll(parseInputStream(request)) ;
		return new Param(formParamList) ;
	}
	
	private static List<FormParam> parseParameterNames(HttpServletRequest request) {
		List<FormParam> formParamList = new ArrayList<>() ;
		Enumeration<String> paramNames = request.getParameterNames() ;
		while (paramNames.hasMoreElements()) {
			String fieldName = paramNames.nextElement();
			String[] fieldValues = request.getParameterValues(fieldName) ;
			if (ArrayUtils.isNotEmpty(fieldValues)) {
				Object fieldValue ;
				if (fieldValues.length == 1) {
					fieldValue = fieldValues[0] ;
				} else {
					StringBuilder sb = new StringBuilder("") ;
					for (int x = 0; x < fieldValues.length; x++) {
						sb.append(fieldValues[x]) ;
						if (x != fieldValues.length - 1) {
							sb.append(StringUtil.SEPARATOR) ;
						}
					}
					fieldValue = sb.toString() ;
				}
				formParamList.add(new FormParam(fieldName, fieldValue)) ;
			}
		}
		return formParamList ;
	}
	
	private static List<FormParam> parseInputStream(HttpServletRequest request) throws IOException {
		List<FormParam> formParamList = new ArrayList<>() ;
		String body = CodecUtil.decodeURL(StreamUtil.getString(request.getInputStream())) ;
		if (StringUtils.isNotEmpty(body)) {
			String[] kvs = StringUtils.split(body, "&") ;
			if (ArrayUtils.isNotEmpty(kvs)) {
				for (String kv : kvs) {
					String[] array = StringUtils.split(kv, "=") ;
					if (ArrayUtils.isNotEmpty(array) && array.length == 2) {
						String fieldName = array[0] ;
						String fieldValue = array[1] ;
						formParamList.add(new FormParam(fieldName, fieldValue)) ;
					}
				}
			}
		}
		return formParamList ;
	}
}
