package org.weixin.base;

import java.lang.reflect.ParameterizedType;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@SuppressWarnings("unchecked")
public class BaseDaoImpl<T> implements BaseDao<T> {

	@PersistenceContext
	private EntityManager em;
	private Class<T> clazz;// 类对象

	public BaseDaoImpl() {
		// 通过发射获取T的真实类型
		// 通过反射获取当前类表示的实体的直接父类的Type
		ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
		// 返回参数数组
		this.clazz = (Class<T>) pt.getActualTypeArguments()[0];

	}

	public boolean save(T entity) {
		try {
			em.persist(entity);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean delete(String id) {
		try {
			em.remove(getById(id));
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean update(T entity) {
		try {
			em.merge(entity);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public T getById(String id) {
		if (id == null) {
			return null;
		}

		return em.find(clazz, id);
	}

	public List<T> getByIds(String[] ids) {
		if (ids == null || ids.length == 0) {
			return Collections.EMPTY_LIST;
		}

		return em.createQuery("from " + clazz.getSimpleName() + " where id in(ids)").setParameter("ids", ids).getResultList();
	}

	public List<T> findAll() {
		return em.createQuery("from " + clazz.getSimpleName()).getResultList();
	}

	/**
	 * 批处理
	 */
	public boolean batchEntityByHQL(String hql, Object... objects) {
		try {

			Query query = em.createQuery(hql);
			for (int i = 0; i < objects.length; i++) {
				query.setParameter(i + 1, objects[i]);
			}
			query.executeUpdate();

			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public List<T> batchResultsByHQL(String hql, Object... objects) {
		Query query = em.createQuery(hql);
		for (int i = 0; i < objects.length; i++) {
			query.setParameter(i + 1, objects[i]);
		}
		return query.getResultList();
	}

	public T batchUniqueResultByHQL(String hql, Object... objects) {
		Query query = em.createQuery(hql);
		for (int i = 0; i < objects.length; i++) {
			query.setParameter(i + 1, objects[i]);
		}

		return (T) query.getSingleResult();
	}

}
