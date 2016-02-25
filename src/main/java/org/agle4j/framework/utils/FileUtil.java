package org.agle4j.framework.utils;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 文件操作工具类
 * 
 * @author hanyx
 * @since 0.9
 */
public final class FileUtil {

	private static final Logger LOG = LogManager.getLogger(FileUtil.class) ;
	
	/**
	 * 获取真实文件名(自动去掉文件路径)
	 */
	public static String getRealFileName(String fileName) {
		return FilenameUtils.getName(fileName) ;
	}
	
	/**
	 * 创建 文件
	 */
	public static File createFile(String filePath) {
		File file ;
		try {
			file = new File(filePath) ;
			File parentDir = file.getParentFile() ;
			if(!parentDir.exists()) {
				FileUtils.forceMkdir(parentDir);
			}
		} catch (Exception e) {
			LOG.error("create file failure", e);
			throw new RuntimeException(e) ;
		}
		return file ;
	}
}
