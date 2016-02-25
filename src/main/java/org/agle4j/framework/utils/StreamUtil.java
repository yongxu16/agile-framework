package org.agle4j.framework.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 流操作工具类
 * @author hanyx
 *
 */
public final class StreamUtil {

	private static final Logger LOG = LogManager.getLogger(StreamUtil.class) ;
	
	/**
	 * 从输入流中获取字符串
	 */
	public static String getString(InputStream is) {
		StringBuffer sb = new StringBuffer() ;
		try {
			List<String> lineList = IOUtils.readLines(is) ;
			for(String line : lineList) {
				sb.append(line) ;
			}
		} catch (Exception e) {
			LOG.error("get string failure", e);
			throw new RuntimeException(e) ;
		}
		return sb.toString() ;
	}
	
	/**
	 * 将输入流复制到输出流
	 */
	public static void copyStream(InputStream inputStream, OutputStream outputStream) {
		try {
			IOUtils.copy(inputStream, outputStream) ;
//			int length ;
//			byte[] buffer = new byte[4 * 1024] ;
//			while ((length = inputStream.read(buffer, 0, buffer.length)) != -1) {
//				outputStream.write(buffer, 0, length);
//			}
			outputStream.flush();
		} catch (IOException e) {
			LOG.error("copy stream failure", e);
			throw new RuntimeException(e) ;
		} finally {
			try {
				inputStream.close();
				outputStream.close();
			} catch (IOException e) {
				LOG.error("close stream failuer", e);
			}
		}
	}
}
