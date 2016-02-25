package org.agle4j.framework.proxy;

/**
 * 代理接口
 * @author hanyx
 *
 */
public interface Proxy {

	/**
	 * 执行链式代理
	 */
	Object doProxy(ProxyChain proxyChain) throws Throwable ;
}
