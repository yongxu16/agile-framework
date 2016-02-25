package org.agle4j.framework.utils;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSON;

/**
 * JSON 工具类
 * @author hanyx
 *
 */
public class JsonUtil {

	private static final Logger LOG = LogManager.getLogger(JsonUtil.class) ;
	
	/**
	 * 将POJO转为JSON
	 */
	public static <T> String toJson(T obj) {
		String json ;
		try {
			json = JSON.toJSONStringWithDateFormat(obj, "yyyy-MM-dd HH:ss:mm") ;
		} catch (Exception e) {
			LOG.error("convert POJO to JSON failure", e);
			throw new RuntimeException(e) ;
		}
		return json ;
	}
	
	/**
	 * 将 JSON 转为 POJO
	 */
	@SuppressWarnings("unchecked")
	public static <T> T fromJson2Pojo(String json, Class<?> type) {
		T pojo ;
		try {
			pojo = (T)JSON.parseObject(json, type) ;
		} catch (Exception e) {
			LOG.error("convert JOSN to POJO failure", e);
			throw new RuntimeException(e) ;
		}
		return pojo ;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> List<T> fromJson2PojoList(String json, Class<?> type) {
		List<T> pojoList ;
		try {
			pojoList = (List<T>) JSON.parseArray(json, type) ;
		} catch (Exception e) {
			LOG.error("convert JOSN to pojoList failure", e);
			throw new RuntimeException(e) ;
		}
		return pojoList ;
	}
	
//	public static void main(String[] args) {
//		Customer vo = new Customer() ;
//		vo.setId("0101");
//		vo.setName("hanyx");
//		String json = JsonUtil.toJson(vo) ;
//		System.out.println(json);
//		Object obj = JsonUtil.fromJson2PojoList(json, Customer.class) ;
//		System.out.println(JsonUtil.toJson(obj));
//	}
}
