package com.softpark.util;

import org.hibernate.Session;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.lang.reflect.ParameterizedType;
import java.util.List;

public class DAO<T> {

    private final Session session;
    private final Class<T> persistentClass;

    @SuppressWarnings("unchecked")
    public DAO() {
        this.session = HibernateSession.getSession();
        this.persistentClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public Session getSession() {
        return session;
    }

    protected void save(T entity) {
        try {
            getSession().getTransaction().begin();
            getSession().save(entity);
            getSession().getTransaction().commit();
        } catch (Throwable t) {
            getSession().getTransaction().rollback();
            t.printStackTrace();
        } finally {
            close();
        }
    }

    protected void update(T entity) {
        try {
            getSession().getTransaction().begin();
            getSession().update(entity);
            getSession().getTransaction().commit();
        } catch (Throwable t) {
            getSession().getTransaction().rollback();
            t.printStackTrace();
        } finally {
            close();
        }
    }

    protected void delete(T entity) {
        try {
            getSession().getTransaction().begin();
            getSession().delete(entity);
            getSession().getTransaction().commit();
        } catch (Throwable t) {
            getSession().getTransaction().rollback();
            t.printStackTrace();
        } finally {
            close();
        }
    }

    public List<T> findAll() throws Exception {

        CriteriaBuilder builder = getSession().getCriteriaBuilder();

        CriteriaQuery<T> criteriaQuery = builder.createQuery(persistentClass);

        Root<T> tRoot = criteriaQuery.from(persistentClass);

        criteriaQuery.select(tRoot);

        return getSession().createQuery(criteriaQuery).getResultList();
    }

    public T findByName(String nome) {

        return (T) getSession().get(persistentClass, nome);
    }

    public T findById(long id) {
        try{
            return (T) getSession().get(persistentClass, id);
        }catch (Exception e){
            return null;
        }
    }

    private void close() {
        if (getSession() != null && getSession().isOpen()) {
            getSession().close();
        }
    }

    public Session getCurrentSession() {
        return getSession().getSessionFactory().getCurrentSession();
    }
}
