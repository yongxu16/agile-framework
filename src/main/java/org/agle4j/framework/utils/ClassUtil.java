package org.agle4j.framework.utils;

import java.io.File;
import java.io.FileFilter;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.agle4j.framework.helper.ConfigHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSON;

/**
 * 类操作工具类
 * @author hanyx
 */
public final class ClassUtil {

	private static final Logger LOG = LogManager.getLogger(ClassUtil.class) ;
	
	/**
	 * 获取当前线程级别 类加载器
	 * @return
	 */
	public static ClassLoader getClassLoader() {
		return Thread.currentThread().getContextClassLoader() ;
	}
	
	/**
	 * 加载类
	 * @param className
	 * @param isInitialized
	 * @return
	 */
	public static Class<?> loadClass(String className, boolean isInitialized) {
		Class<?> cls ;
		try {
			cls = Class.forName(className, isInitialized, getClassLoader()) ;
		} catch (ClassNotFoundException e) {
			LOG.error("load class failure", e);
			throw new RuntimeException(e) ;
		}
		return cls ;
	}
	
	/**
	 * 获取指定包名下的所有类
	 * @param packageName
	 * @return
	 */
	public static Set<Class<?>> getClassSet(String packageName) {
		Set<Class<?>> classSet = new HashSet<Class<?>>() ;
		try {
			Enumeration<URL> urls = getClassLoader().getResources(StringUtils.replace(packageName, ".", "/")) ;
			while (urls.hasMoreElements()) {
				URL url = (URL) urls.nextElement();
				if(url != null) {
					String protocol = url.getProtocol() ;
					if(StringUtils.equals(protocol, "file")) {
						String packagePath = StringUtils.replace(url.getPath(), "%20", " ") ;
						addClass(classSet, packagePath, packageName);
					} else if(StringUtils.equals(protocol, "jar")) {
						JarURLConnection jarURlConnection = (JarURLConnection) url.openConnection() ;
						if(jarURlConnection != null) {
							JarFile jarFile = jarURlConnection.getJarFile() ;
							if(jarFile != null) {
								Enumeration<JarEntry> jarEntiries = jarFile.entries() ;
								while (jarEntiries.hasMoreElements()) {
									JarEntry jarEntry = (JarEntry) jarEntiries.nextElement();
									String jarEntryName = jarEntry.getName() ;
									if(jarEntryName.endsWith(".class")) {
										String className = jarEntryName.substring(0, jarEntryName.lastIndexOf(".")).replace("/", ".") ;
										doAddClass(classSet, className);
									}
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			LOG.error("get class set failuer", e);
			throw new RuntimeException(e) ;
		}
		return classSet ;
	}
	
	private static void addClass(Set<Class<?>> classSet, String packagePath, String packageName) {
		
		File[] files = new File(packagePath).listFiles(new FileFilter() {
			@Override
			public boolean accept(File file) {
				return (file.isFile() && file.getName().endsWith(".class")) || file.isDirectory();
			}
		}) ;
		
		for (File file : files) {
			String fileName = file.getName();
			if(file.isFile()) {
				String className = fileName.substring(0, fileName.lastIndexOf("."));
				if (StringUtils.isNotEmpty(packageName)) {
					className = packageName + "." + className;
				}
				doAddClass(classSet, className);
			} else {
				String subPackagePath = fileName ;
				if(StringUtils.isNotEmpty(packageName)) {
					subPackagePath = packagePath + "/" + subPackagePath ;
				}
				String subPackageName = fileName ;
				if(StringUtils.isNotEmpty(packageName)) {
					subPackageName = packageName + "." + subPackageName ;
				}
				addClass(classSet, subPackagePath, subPackageName);
			}
		}
		
	}
	
	private static void doAddClass(Set<Class<?>> classSet, String className) {
		Class<?> cls = loadClass(className, false) ;
		classSet.add(cls) ;
	}
	
	public static void main(String[] args) {
		Set<Class<?>> classSet = ClassUtil.getClassSet(ConfigHelper.getAppBasePackage()) ;
		System.out.println(JSON.toJSONString(classSet));
	}
}
