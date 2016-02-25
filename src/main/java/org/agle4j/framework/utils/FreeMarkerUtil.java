package org.agle4j.framework.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapperBuilder;
import freemarker.template.Template;
import freemarker.template.Version;

public class FreeMarkerUtil {
	//模板路径
	public static final String PATH = "freemarkertest/template";

	public static Version version = new Version("2.3.22");
	public static Configuration config;
	
	// 模板对象
	public static Template template;

	public static DefaultObjectWrapperBuilder defaultObjectWrapperBuilder;
	
	// 创建 Configuration 对象
	static {
		//final File file = new File(ClassPathUtil.getClassPath(PATH));
		final File file = new File(FreeMarkerUtil.class.getResource("/"+PATH).getPath());
		config = new Configuration(version);
		// 设置默认编码 (至关重要 - 解决中文乱码问题)
		config.setDefaultEncoding("UTF-8");
		
		defaultObjectWrapperBuilder = new DefaultObjectWrapperBuilder(version);
		// 对象包装器
		//final ObjectWrapper objectWrapper = defaultObjectWrapperBuilder.build(); //= new DefaultObjectWrapper();
		
		try {
			// 设置模板的目录
			config.setDirectoryForTemplateLoading(file);
			// 设置对象包装器
			config.setObjectWrapper(defaultObjectWrapperBuilder.build());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据template和dataMap生成文档内容
	 * @param templateName 模板文件名
	 * @param dataMap 需要填充的数据
	 * @return 生成的文档内容
	 */
	public static <T> String buildContent(final String templateName, final Map<String, T> dataMap) {
		// 返回结果
		String result = null;
		try	{
			// 从模板目录下获取指定 ftl 模板文件
			template = config.getTemplate(templateName);
			// 设置文件编码
			//template.setEncoding("UTF-8");
			// 字符串 字符输出流
			final StringWriter stringWriter = new StringWriter();
			final BufferedWriter bufferedWriter = new BufferedWriter(stringWriter);
			// 合并 数据模型 与 模板
			template.process(dataMap, bufferedWriter);
			
			// 获取输出的字符串 (调用其 toString方法)
			result = stringWriter.toString();
			// 刷新
			bufferedWriter.flush();
			// 关闭输出流
			bufferedWriter.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
}
