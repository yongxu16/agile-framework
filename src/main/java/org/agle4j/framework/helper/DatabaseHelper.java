package org.agle4j.framework.helper;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.agle4j.framework.utils.CollectionUtil;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



/**
 * 数据库操作助手类
 * @author hanyx
 *
 */
public final class DatabaseHelper {

	private static final Logger LOG = LogManager.getLogger(DatabaseHelper.class) ;
	
	/**
	 * AbstractListHandler -- 返回多行List的抽象类
	 * ArrayHandler --  返回一行的Object[]
	 * ArrayListHandler -- 返回List，每行是Object[]
	 * BeanHandler -- 返回第一个Bean对象
	 * BeanListHandler -- 返回List，每行是Bean
	 * ColumnListHandler -- 返回一列的List
	 * KeyedHandler -- 返回Map，具体见代码
	 * MapHandler -- 返回单个Map
	 * MapListHandler -- 返回List，每行是Map
	 * ScalarHandler -- 返回列的头一个值
	 */
	private static final QueryRunner QUERY_RUNNER ;
	
	private static final ThreadLocal<Connection> CONNECTION_HOLDER ;
	
	private static final BasicDataSource DATA_SOURCE ;
	

	static {
		CONNECTION_HOLDER = new ThreadLocal<Connection>() ;
		QUERY_RUNNER = new QueryRunner() ;
		
		DATA_SOURCE = new BasicDataSource() ;
		DATA_SOURCE.setDriverClassName(ConfigHelper.getJdbcDriver());
		DATA_SOURCE.setUrl(ConfigHelper.getJdbcUrl());
		DATA_SOURCE.setUsername(ConfigHelper.getJdbcUsername());
		DATA_SOURCE.setPassword(ConfigHelper.getJdbcPassword());
	}
	
	/**
	 * 获取数据库连接池
	 * @return
	 */
	public static Connection getConnection() {
		Connection conn = CONNECTION_HOLDER.get() ;
		if(conn == null ) {
			try {
				conn = DATA_SOURCE.getConnection() ;
			} catch (SQLException e) {
				LOG.error("get connection failure" , e);
				throw new RuntimeException(e) ;
			} finally {
				CONNECTION_HOLDER.set(conn);
			}
		}
		return conn ;
	}
	
	/**
	 * 查询实体列表
	 * @param entityClass
	 * @param sql
	 * @param params
	 * @return
	 */
	public static <T> List<T> queryEntityList(Class<T> entityClass, String sql, Object... params) {
		List<T> entityList = null ;
		try {
			Connection conn = getConnection() ;
			entityList = QUERY_RUNNER.query(conn, sql,new BeanListHandler<T>(entityClass), params) ;
		} catch (SQLException e) {
			LOG.error("query entity list failure", e);
			throw new RuntimeException(e) ;
		}
		return entityList ;
	}
	
	/**
	 * 查询实体
	 * @param entityClass
	 * @param sql
	 * @param params
	 * @return
	 */
	public static <T> T queryEntity(Class<T> entityClass, String sql, Object... params) {
		T entity = null ;
		try {
			Connection conn = getConnection() ;
			entity = QUERY_RUNNER.query(conn,sql ,new BeanHandler<T>(entityClass) ,params) ;
		} catch (SQLException e) {
			LOG.error("query entity failure", e);
			throw new RuntimeException(e) ;
		}
		return entity ;
	}
	
	/**
	 * 执行查询语句
	 * @param sql
	 * @param params
	 * @return List<Map<String, Object>> 
	 */
	public static List<Map<String, Object>> executeQuery(String sql, Object... params) {
		List<Map<String, Object>> result = new ArrayList<>() ;
		try {
			Connection conn = getConnection() ;
			result = QUERY_RUNNER.query(conn, sql, new MapListHandler(), params) ;
		} catch (Exception e) {
			LOG.error("execute query failure", e);
			throw new RuntimeException(e) ;
		}
		return result ;
	}
	
	/**
	 * 执行 SQL 查询, 返回String
	 * @param sql
	 * @param params
	 * @return
	 */
	public static String query(String sql, Object...params) {
		String result ;
		try {
			Connection conn = getConnection() ;
			result = QUERY_RUNNER.query(conn, sql, new ScalarHandler<String>(), params) ;
		} catch (Exception e) {
			LOG.error("execute query failure", e);
			throw new RuntimeException(e) ;
		}
		return result ;
	}
	
	/**
	 * 执行 SQL 查询, 返回String
	 * @param sql
	 * @param params
	 * @return
	 */
	public static Set<String> querySet(String sql, Object...params) {
		Set<String> result = new HashSet<>() ;
		try {
			List<String> resultList = queryEntityList(String.class, sql, params) ;
			if (CollectionUtil.isNotEmpty(resultList)) {
				result.addAll(resultList) ;
			}
		} catch (Exception e) {
			LOG.error("execute query failure", e);
			throw new RuntimeException(e) ;
		}
		return result ;
	}
	
	/**
	 * 执行更新语句
	 * @param sql
	 * @param params
	 * @return
	 */
	public static int executeUpdate(String sql, Object...params) {
		int rows = 0 ;
		try {
			Connection conn = getConnection() ;
			rows = QUERY_RUNNER.update(conn, sql, params) ;
		} catch (SQLException e) {
			LOG.error("execute update failure", e);
			throw new RuntimeException(e) ;
		}
		return rows ;
	}
	
	/**
	 * 插入实体
	 * @param entityclass
	 * @param fieldMap
	 * @return
	 */
	public static <T> boolean insertEntity(Class<T> entityClass, Map<String, Object> fieldMap) {
		if(CollectionUtil.isEmpty(fieldMap)) {
			LOG.error("can not insert ectity: fieldMap is  empty");
			return false ;
		}
		
		String sql = "INSERT INTO " + getTableName(entityClass) ;
		StringBuilder columns= new StringBuilder("(") ;
		StringBuilder values= new StringBuilder("(") ;
		for(String fieldName : fieldMap.keySet()) {
			columns.append(fieldName).append(", ") ;
			values.append("?, ") ;
		}
		columns.replace(columns.lastIndexOf(", "), columns.length(), ")") ;
		values.replace(values.lastIndexOf(", "), values.length(), ")") ;
		sql += columns + " VALUES " + values ;
		
		Object[] params = fieldMap.values().toArray() ;
		
		return executeUpdate(sql, params) == 1 ;
	}
	
	/**
	 * 更新实体
	 * @param entityClass
	 * @param id
	 * @param fieldMap
	 * @return
	 */
	public static <T> boolean updateEntity(Class<T> entityClass, String id, Map<String, Object> fieldMap) {
		if(CollectionUtil.isEmpty(fieldMap)) {
			LOG.error("con not update entity : fieldMap is empty");
			return false ;
		}
		
		String sql = "UPDATE " + getTableName(entityClass) + "SET " ;
		StringBuilder columns = new StringBuilder() ;
		for(String fieldName : fieldMap.keySet()) {
			columns.append(fieldName).append("=?, ") ;
		}
		sql += columns.substring(0, columns.lastIndexOf(", ")) + " WHERE id=?" ;
		
		List<Object> paramList = new ArrayList<>() ;
		paramList.addAll(fieldMap.values()) ;
		paramList.add(id) ;
		
		return executeUpdate(sql, paramList.toArray()) == 1 ;
	}
	
	/**
	 * 删除实体
	 * @param entityClass
	 * @param id
	 * @return
	 */
	public static <T> boolean deleteEntity(Class<T> entityClass, String id) {
		String sql = "DELETE FROM " + getTableName(entityClass) + " WHERE id=? " ;
		return executeUpdate(sql, id) ==1 ;
	}
	
	private static String getTableName(Class<?> entityClass) {
		return entityClass.getSimpleName() ;
	}
	
	/**
	 * 执行 SQL 文件
	 * @param filePath
	 */
	public static void executeSqlFile(String filePath) {
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath) ;
		try {
			List<String> sqlList = IOUtils.readLines(is, "utf-8") ;
			for(String sql : sqlList) {
				if(StringUtils.isNotEmpty(sql)) {
					executeUpdate(sql) ;
				}
			}
		} catch (IOException e) {
			LOG.error("execute sql file failure", e);
			throw new RuntimeException(e) ;
		}
	}
	
	/**
	 * 开启事务
	 */
	public static void beginTransaction() {
		Connection conn = getConnection() ;
		if(conn != null) {
			try {
				conn.setAutoCommit(false);
			} catch (SQLException e) {
				LOG.error("begin transaction failure", e);
				throw new RuntimeException(e) ;
			} finally {
				CONNECTION_HOLDER.set(conn);
			}
		}
	}
	
	/**
	 * 提交事务
	 */
	public static void commitTransaction() {
		Connection conn = getConnection() ;
		if(conn != null) {
			try {
				conn.commit();
				conn.close();
			} catch (SQLException e) {
				LOG.error("commit transaction failure", e);
				throw new RuntimeException(e) ;
			} finally {
				CONNECTION_HOLDER.remove();
			}
		}
	}
	
	/**
	 * 回滚事务
	 */
	public static void rollbackTransaction() {
		Connection conn = getConnection() ;
		if(conn != null) {
			try {
				conn.rollback();
				conn.close();
			} catch (SQLException e) {
				LOG.error("rollback transaction failure", e);
				throw new RuntimeException(e) ;
			} finally {
				CONNECTION_HOLDER.remove();
			}
		}
	}
	
	public static BasicDataSource getDataSource() {
		return DATA_SOURCE ;
	}
}
