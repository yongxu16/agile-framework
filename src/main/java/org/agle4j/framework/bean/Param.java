package org.agle4j.framework.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.agle4j.framework.utils.CastUtil;
import org.agle4j.framework.utils.CollectionUtil;
import org.agle4j.framework.utils.StringUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 请求参数对象
 * @author hanyx
 *
 */
public class Param {
	
	private static final Logger LOG = LogManager.getLogger(Param.class) ;
	
	private List<FormParam> formParamList ;
	
	private List<FileParam> fileParamList ;
	
	public Param(List<FormParam> formParamList) {
		this.formParamList = formParamList ;
	}
	
	public Param(List<FormParam> formParamList, List<FileParam> fileParamList) {
		this.formParamList = formParamList ;
		this.fileParamList = fileParamList ;
	}
	
	/**
	 * 获取请求参数映射
	 */
	public Map<String, Object> getFieldMap() {
		Map<String, Object> fieldMap = new HashMap<String, Object>() ;
		if(CollectionUtil.isNotEmpty(formParamList)) {
			for(FormParam formParam : formParamList) {
				String fieldName = formParam.getFieldName() ;
				Object fieldValue = formParam.getFieldValue() ;
				if(fieldMap.containsKey(fieldName)) {
					fieldValue = fieldMap.get(fieldName) + StringUtil.SEPARATOR + fieldValue ;
					LOG.debug(fieldValue);
				}
				fieldMap.put(fieldName, fieldValue) ;
			}
		}
		
		return fieldMap ;
	}

	/**
	 * 获取上传文件映射
	 */
	public Map<String, List<FileParam>> getFileMap() {
		Map<String, List<FileParam>> fileMap = new HashMap<String, List<FileParam>>() ;
		if(CollectionUtil.isNotEmpty(fileParamList)) {
			for(FileParam fileParam : fileParamList) {
				String fieldName = fileParam.getFieldName() ;
				List<FileParam> fileParamList ;
				if(fileMap.containsKey(fieldName)) {
					fileParamList = fileMap.get(fieldName) ;
				} else {
					fileParamList = new ArrayList<> () ;
				}
				fileParamList.add(fileParam) ;
				fileMap.put(fieldName, fileParamList) ;
				
			}
		}
		return fileMap ;
		
	}
	
	/**
	 * 获取所有上传文件
	 */
	public List<FileParam> getFileList(String fileName) {
		return getFileMap().get(fileName) ;
	}
	
	/**
	 * 获取唯一上传文件
	 */
	public FileParam getFile(String fileName) {
		List<FileParam> fileParamList = getFileMap().get(fileName) ;
		if(CollectionUtil.isNotEmpty(fileParamList) && fileParamList.size() == 1) {
			return fileParamList.get(0) ;
		}
		return null ;
	}
	
	/**
	 * 验证参数是否为空
	 */
	public boolean isEmpty() {
		return CollectionUtil.isEmpty(formParamList) && CollectionUtil.isEmpty(fileParamList) ;
	}
	
	/**
	 * 根据参数名获取long型参数值
	 */
	public long getLong(String name) {
		return CastUtil.castLong(getFieldMap().get(name)) ;
	}
	
	/**
	 * 根据参数名获取String型参数值
	 */
	public String getString(String name) {
		return CastUtil.castString(getFieldMap().get(name)) ;
	}
	
	/**
	 * 根据参数名获取String型参数值
	 */
    public int getInt(String name) {
        return CastUtil.castInt(getFieldMap().get(name));
    }
    
    /**
	 * 根据参数名获取boolean型参数值
	 */
    public boolean getBooleanm(String name) {
        return CastUtil.castBoolean(getFieldMap().get(name));
    }
    
    public Object get(String name) {
    	return getFieldMap().get(name) ;
    }
    
	/**
	 * 获取所有字段信息
	 */
	public Map<String, Object> getMap() {
		return getFieldMap() ;
	}
	
}
