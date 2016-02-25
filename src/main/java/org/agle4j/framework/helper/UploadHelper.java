package org.agle4j.framework.helper;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.agle4j.framework.bean.FileParam;
import org.agle4j.framework.bean.FormParam;
import org.agle4j.framework.bean.Param;
import org.agle4j.framework.utils.CollectionUtil;
import org.agle4j.framework.utils.FileUtil;
import org.agle4j.framework.utils.StreamUtil;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 文件上传助手类
 * @author hanyx
 * @since 0.9
 */
public final class UploadHelper {

	private static final Logger LOG = LogManager.getLogger(UploadHelper.class) ;
	
	/**
	 * Apache Commons FileUpload 提供的 Servlet 文件上传对象
	 */
	private static ServletFileUpload servletFileUpload ;
	
	/**
	 * 初始化
	 */
	public static void init(ServletContext servletContext) {
		/*
		 * Web容器會提供每個ServletContext一個暫存目錄，
		 * 這個目錄的資訊可以藉由ServletContext的getAttribute()方法
		 * 取得javax.servlet.context.tempdir屬性來得知
		 */
		File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir") ;
		
		servletFileUpload = new ServletFileUpload(new DiskFileItemFactory(DiskFileItemFactory.DEFAULT_SIZE_THRESHOLD, repository)) ;
		int uploadLimit = ConfigHelper.getAppUploadLimit() ;
		if(uploadLimit != 0) {
			servletFileUpload.setFileSizeMax(uploadLimit * 1024 * 1024);
		}
	}
	
	/**
	 * 判断请求是否为 multipart 类型
	 */
	public static boolean isMultipart(HttpServletRequest request) {
		return ServletFileUpload.isMultipartContent(request) ;
	}
	
	/**
	 * 创建请求对象
	 */
	public static Param createParam(HttpServletRequest request) throws IOException {
		List<FormParam> formParamList = new ArrayList<>() ;
		List<FileParam> fileParamList = new ArrayList<>() ;
		try {
			Map<String, List<FileItem>> fileItemListMap = servletFileUpload.parseParameterMap(request) ;
			if(CollectionUtil.isNotEmpty(fileItemListMap)) {
				for(Map.Entry<String, List<FileItem>> fileItemListEntry : fileItemListMap.entrySet()) {
					String fieldName = fileItemListEntry.getKey() ;
					List<FileItem> fileItemList = fileItemListEntry.getValue() ;
					if(CollectionUtil.isNotEmpty(fileItemList)) {
						for(FileItem fileItem : fileItemList) {
							if(fileItem.isFormField()) {
								String fieldValue = fileItem.getString("UTF-8") ;
								formParamList.add(new FormParam(fieldName, fieldValue)) ;
							} else {
								String fileName = FileUtil.getRealFileName(new String(fileItem.getName().getBytes(), "UTF-8")) ;
								if(StringUtils.isNotEmpty(fileName)) {
									long fileSize = fileItem.getSize() ;
									String contentType = fileItem.getContentType() ;
									InputStream inputStream = fileItem.getInputStream() ;
									fileParamList.add(new FileParam(fieldName, fileName, fileSize, contentType, inputStream)) ;
								}
							}
						}
					}
				}
			}
			
		} catch (Exception e) {
			LOG.error("create param failure", e);
			throw new RuntimeException(e) ;
		}
		return new Param(formParamList, fileParamList);
	}
	
	/**
	 * 上传文件
	 */
	public static void uploadFile(String basePath, FileParam fileParam) {
		try {
			if (fileParam != null) {
				String filePath = basePath + fileParam.getFileName() ;
				FileUtil.createFile(filePath) ;		// 判断并创建目录
				LOG.debug("filePath: " + filePath);
				InputStream inputStream = new BufferedInputStream(fileParam.getInputStream()) ;
				OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(filePath)) ;
				StreamUtil.copyStream(inputStream, outputStream);
			}
		} catch (Exception e) {
			LOG.error("upload file failure", e);
			throw new RuntimeException(e) ;
		}
	}
	
	/**
	 * 批量上传文件
	 */
	public static void uploadFile(String basePath, List<FileParam> fileParamList) {
		try {
			if (CollectionUtil.isEmpty(fileParamList)) {
				for (FileParam fileParam : fileParamList) {
					uploadFile(basePath, fileParam);
				}
			}
		} catch (Exception e) {
			LOG.error("upload file failure", e);
			throw new RuntimeException(e) ;
		}
	}
}
