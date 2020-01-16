package com.softpark.dao;

import com.softpark.model.Categoria;
import com.softpark.util.DAO;
import com.softpark.util.HibernateSession;
import org.hibernate.Hibernate;
import org.hibernate.Session;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.sql.SQLException;
import java.util.List;

public class CategoriaDAO extends DAO<Categoria> {
    public void salvar(Categoria categoria) {
        save(categoria);
    }

    public void alterar(Categoria categoria) {
        update(categoria);
    }

    public void excluir(long id) {
        Categoria categoria = findById(id);
        delete(categoria);
    }

    public List<Categoria> listAll(){
        List<Categoria> lista = null;

        Session session = HibernateSession.getSession();

        session.beginTransaction();
        lista = session.createQuery("SELECT c FROM Categoria c", Categoria.class).getResultList();
        session.close();
        return lista;
    }
}
