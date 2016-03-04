package org.weixin.base;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SuppressWarnings("unchecked")
public class BaseServiceImpl<T> implements BaseService<T> {

	private BaseDao<T> dao;
	private Class<T> clazz;// 类对象

	public BaseServiceImpl() {
		// 通过发射获取T的真实类型
		// 通过反射获取当前类表示的实体的直接父类的Type
		ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
		// 返回参数数组
		this.clazz = (Class<T>) pt.getActualTypeArguments()[0];

	}

	/**
	 * 注入dao
	 */
	@Resource
	public void setDao(BaseDao<T> dao) {
		this.dao = dao;
	}

	public boolean save(T entity) {

		return dao.save(entity);

	}

	public boolean delete(String id) {
		return dao.delete(id);
	}

	public boolean update(T entity) {
		return dao.update(entity);
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public T getById(String id) {
		return dao.getById(id);
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public List<T> getByIds(String[] ids) {
		return dao.getByIds(ids);
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public List<T> findAll() {
		return dao.findAll();
	}

	public boolean batchEntityByHQL(String hql, Object... objects) {
		return dao.batchEntityByHQL(hql, objects);
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public List<T> batchResultsByHQL(String hql, Object... objects) {
		return dao.batchResultsByHQL(hql, objects);
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public T batchUniqueResultByHQL(String hql, Object... objects) {
		return dao.batchUniqueResultByHQL(hql, objects);
	}
}
