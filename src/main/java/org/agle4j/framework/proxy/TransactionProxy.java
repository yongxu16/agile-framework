package org.agle4j.framework.proxy;

import java.lang.reflect.Method;

import org.agle4j.framework.annotation.Transaction;
import org.agle4j.framework.helper.DatabaseHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 事务代理
 * 
 * @author hanyx
 *
 */
public class TransactionProxy implements Proxy {

	private static final Logger LOGGER = LogManager.getLogger(TransactionProxy.class);

	private static final ThreadLocal<Boolean> FLAG_HOLDER = new ThreadLocal<Boolean>() {
		protected Boolean initialValue() {
			return false;
		};
	};

	@Override
	public Object doProxy(ProxyChain proxyChain) throws Throwable {
		Object result;
		boolean flag = FLAG_HOLDER.get();
		Method method = proxyChain.getTargetMethod();
		if (!flag && method.isAnnotationPresent(Transaction.class)) {
			try {
				DatabaseHelper.beginTransaction();
				LOGGER.debug("begin transaction");
				result = proxyChain.doProxyChain();
				DatabaseHelper.commitTransaction();
				LOGGER.debug("commit");
			} catch (Exception e) {
				DatabaseHelper.rollbackTransaction();
				LOGGER.debug("rollback transaction");
				throw e;
			} finally {
				FLAG_HOLDER.remove();
			}
		} else {
			result = proxyChain.doProxyChain();
		}
		return result;
	}

}
