package org.agle4j.framework.bean;

import java.io.InputStream;

/**
 * 封装上传文件参数
 * @author hanyx
 *
 */
public class FileParam {
	
	private String fieldName ;			// 文件表单的字段名
	private String fileName ;			// 文件的文件名
	private long fileSize ;				// 上传文件的文件大小
	private String contentType ;		// 表示上传文件的content-type, 可判断文件类型
	private InputStream inputStream ;	// 文件的字节输入流

	public FileParam(String fieldName, String fileName, long fileSize, String contentType, InputStream inputStream) {
		this.fieldName = fieldName; 
		this.fileName = fileName ;
		this.fileSize = fileSize ;
		this.contentType = contentType ;
		this.inputStream = inputStream ;
	}

	public String getFieldName() {
		return fieldName;
	}

	public String getFileName() {
		return fileName;
	}

	public long getFileSize() {
		return fileSize;
	}

	public String getContentType() {
		return contentType;
	}

	public InputStream getInputStream() {
		return inputStream;
	}
	
}
