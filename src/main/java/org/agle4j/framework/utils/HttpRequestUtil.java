package org.agle4j.framework.utils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * http 请求工具类
 * @author hanyx
 * @since
 */
public final class HttpRequestUtil {

	private static final Logger LOG = LogManager.getLogger(HttpRequestUtil.class) ;
	
	/**
	 * 发送 GET 请求
	 */
	public static String requestGet(String urlWithParam) {
		String content = null ;
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		try {
			HttpGet get = new HttpGet(urlWithParam);
			response = httpClient.execute(get);	//发送请求
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				content = EntityUtils.toString(entity);
			}
		} catch (Exception e) {
			LOG.error("send WeChat failur ", e);
		} finally {
			try {
				response.close();
				httpClient.close();
			} catch (IOException e) {
			}
		}
		return content ;
	}
	
	/**
	 * 发送 POST 请求
	 */
	public static String requestPost(String url, List<NameValuePair> paramList) {
		String content = null ;
		CloseableHttpClient httpClient = HttpClients.createDefault() ;
		CloseableHttpResponse response = null;
		try {
			HttpPost post = new HttpPost(url) ;
			post.setEntity(new UrlEncodedFormEntity(paramList));
			response = httpClient.execute(post) ;
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				content = EntityUtils.toString(entity);
			}
		} catch (Exception e) {
			LOG.error("request post failur ", e);
		}
		return content ;
	}
	
	/**
	 * 
	 * @param url
	 * @param paramMap
	 * @return
	 */
	public static String requestPost(String url, Map<String, Object> paramMap) {
		String content = null ;
		if (CollectionUtil.isNotEmpty(paramMap)) {
			CloseableHttpClient httpClient = HttpClients.createDefault() ;
			CloseableHttpResponse response = null;
			try {
				HttpPost post = new HttpPost(url) ;
				MultipartEntityBuilder builder = MultipartEntityBuilder.create();
				builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
				
				for (Map.Entry<String, Object> paramEntry : paramMap.entrySet()) {
					if (paramEntry.getValue() instanceof File) {
						builder.addPart(paramEntry.getKey(), new FileBody((File) paramEntry.getValue())) ;
					} else {
						builder.addPart(paramEntry.getKey(), new StringBody(CastUtil.castString(paramEntry.getValue()), ContentType.MULTIPART_FORM_DATA)) ;
					}
				}
				HttpEntity entity = builder.build() ;
				post.setEntity(entity) ;
				
				response = httpClient.execute(post) ;
				if (response.getStatusLine().getStatusCode() == 200) {
	                content = EntityUtils.toString(response.getEntity());
	            }
			} catch (Exception e) {
				LOG.error("request post with file failur ", e);
			}
		}
		return content ;
	}
	
	public static void main(String[] args) {
//		List<NameValuePair> params = new ArrayList<NameValuePair>();
//      params.add(new BasicNameValuePair("name", name));
		
		// 组装参数 start
		String name = "hanyx33333" ;
		String contact = "test" ;
		String telephone = "18604230463" ;
		File file = new File("d:\\Desert.jpg") ;
		
		Map<String, Object> paramMap = new HashMap<>() ;
		paramMap.put("name", name) ;
		paramMap.put("contact", contact) ;
		paramMap.put("telephone", telephone) ;
		paramMap.put("photo", file) ;
		// 组装参数 end
		String url = "http://127.0.0.1:8090/agile4j/customer_create" ;
		
		String content = HttpRequestUtil.requestPost(url, paramMap) ;
		LOG.debug(content);
		
	}
}
