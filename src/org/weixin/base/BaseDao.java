package org.weixin.base;

import java.util.List;

public interface BaseDao<T> {

	boolean save(T entity);

	boolean delete(String id);

	boolean update(T entity);

	T getById(String id);

	List<T> getByIds(String[] ids);

	List<T> findAll();

	/**
	 * 按照HQL语句批处理实体
	 */
	boolean batchEntityByHQL(String hql, Object... objects);

	List<T> batchResultsByHQL(String hql, Object... objects);

	T batchUniqueResultByHQL(String hql, Object... objects);

}
