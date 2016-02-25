package org.agle4j.framework;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.agle4j.framework.bean.Data;
import org.agle4j.framework.bean.Handler;
import org.agle4j.framework.bean.Param;
import org.agle4j.framework.bean.View;
import org.agle4j.framework.helper.BeanHelper;
import org.agle4j.framework.helper.ConfigHelper;
import org.agle4j.framework.helper.ControllerHelper;
import org.agle4j.framework.helper.RequestHelper;
import org.agle4j.framework.helper.ServletHelper;
import org.agle4j.framework.helper.UploadHelper;
import org.agle4j.framework.utils.JsonUtil;
import org.agle4j.framework.utils.ReflectionUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 请求转发器
 * @author hanyx
 * loadOnStartup = 0 （Tomcat）启动时自动加载
 */
@SuppressWarnings("serial")
@WebServlet(urlPatterns ="/*", loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet {

	private static final Logger LOG = LogManager.getLogger(DispatcherServlet.class) ;
	@Override
	public void init(ServletConfig servletConfig) throws ServletException {
		// 初始化相关 Helper 类
		HelperLoder.init();
		
		// 获取 ServletContext 对象 (用于注册 Servlet)
		ServletContext servletContext = servletConfig.getServletContext() ;
		
		// 注册处理 JSP的 Servlet
		// 动态加载一个Servlet name： jsp ， mapping ：/WEB-INF/view/*
		ServletRegistration jspServlet = servletContext.getServletRegistration("jsp") ;
		// 将与给定的 URL 模式的 servlet 映射添加由这个 ServletRegistration 的 Servlet。
		jspServlet.addMapping(ConfigHelper.getAppJspPath() + "*") ;
		
		// 注册处理 静态资源的默认Servlet
		ServletRegistration defaultServlet = servletContext.getServletRegistration("default") ;
		defaultServlet.addMapping(ConfigHelper.getAppAssetPath() + "*") ;
		
		UploadHelper.init(servletContext);
	}
	
	@Override
	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		ServletHelper.init(request, response);
		try {
			
			// 获取请求方法与请求路径 
			String requestMethod = request.getMethod().toLowerCase() ; //POST 等
			LOG.debug("request.getMethod() :" + requestMethod);
			String requestPath = request.getPathInfo() ;
			LOG.debug("request.getPathInfo() :" + requestPath);
			
			if (requestPath.equals("/favicon.ico")) { // 地址栏旁边小图片
				return ;
			}
			
			Handler handler = ControllerHelper.getHandler(requestMethod, requestPath) ;
			if(handler != null) {
				
				// 获取 Controller类及其Bean 实例
				Class<?> controllerClass = handler.getContorllerClass() ;
				Object controllerBean = BeanHelper.getBean(controllerClass) ;
				
				Param param ;
				if (UploadHelper.isMultipart(request)) {
					param = UploadHelper.createParam(request) ;
				} else {
					param = RequestHelper.createParam(request) ;
				}
				
				Object result ;
				// 调用 Action 方法
				Method actionMethod = handler.getActionMethod() ;
				
				if(param.isEmpty()) {
					result = ReflectionUtil.invokeMethod(controllerBean, actionMethod) ;
				} else {
					result = ReflectionUtil.invokeMethod(controllerBean, actionMethod, param) ;
				}
				
				// 处理 Acton 方法返回值
				if(result instanceof View) {
					handleViewResult((View) result, request, response);
				} else if (result instanceof Data) {
					handleDataResult((Data) result, response);
				}
			}
		} finally {
			ServletHelper.destory();
		}
	}
	
	private void handleViewResult(View view, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String path = view.getPath() ;
		if (StringUtils.isNotEmpty(path)) {
			if(path.startsWith("/")) {
				response.sendRedirect(request.getContextPath() + path); 
			} else {
				Map<String, Object> model = view.getModel() ;
				for(Map.Entry<String, Object> entry : model.entrySet()) {
					request.setAttribute(entry.getKey(), entry.getValue());
				}
				request.getRequestDispatcher(ConfigHelper.getAppJspPath() + path).forward(request, response);
			}
		}
	}
	
	private void handleDataResult(Data data, HttpServletResponse response) throws IOException {
		Object model = data.getModel() ;
		if(model != null) {
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			PrintWriter writer = response.getWriter() ;
			String json = JsonUtil.toJson(model) ;
			writer.write(json);
			writer.flush();
			writer.close();
		}
	}
}
