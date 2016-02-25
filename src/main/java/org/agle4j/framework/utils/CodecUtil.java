package org.agle4j.framework.utils;

import java.net.URLDecoder;
import java.net.URLEncoder;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 编码与解码操作工具类
 * @author hanyx
 *
 */
public final class CodecUtil {

	private static final Logger LOG = LogManager.getLogger(CodecUtil.class) ;
	
	/**
	 * 将 URL 编码
	 */
	public static String encodURL(String source) {
		String target ;
		try {
			target = URLEncoder.encode(source, "UTF-8") ;
		} catch (Exception e) {
			LOG.error("encode url failure", e);
			throw new RuntimeException(e) ;
		}
		return target ;
	}
	
	/**
	 * 将 URL 解码
	 */
	public static String decodeURL(String source) {
		String target ;
		try {
			target = URLDecoder.decode(source, "UTF-8") ;
		} catch (Exception e) {
			LOG.error("decode url failure", e);
			throw new RuntimeException(e) ;
		}
		return target ;
	}
	
	/**
	 * MD5加密
	 * @return
	 */
	public static String md5(String source) {
		return DigestUtils.md5Hex(source);
	}
	
	/**
	 * SHA256加密
	 * @return
	 */
	public static String sha256(String source) {
		return DigestUtils.sha256Hex(source) ;
	}
	
	public static void main(String[] args) {
		String url = "http://entertain.naver.com/read?oid=109&aid=0003235966&gid=999339&cid=1011465" ;
		String str1 = CodecUtil.encodURL(url) ;
		String str2 = CodecUtil.decodeURL(str1) ;
		System.out.println(str1);
		System.out.println(str2);
	}
}
