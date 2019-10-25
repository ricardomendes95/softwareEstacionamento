package com.softpark.dao;

import com.softpark.model.Categoria;
import com.softpark.model.Entrada;
import com.softpark.model.Precos;
import com.softpark.model.Precos;
import com.softpark.util.DAO;
import com.softpark.util.HibernateSession;
import org.hibernate.Session;

import javax.persistence.Query;
import java.util.List;


public class PrecosDAO extends DAO<Precos> {
    public void salvar(Precos precos) {
        save(precos);
    }

    public void alterar(Precos precos) {
        update(precos);
    }

    public void excluir(long id) {
        Precos precos = findById(id);
        delete(precos);
        getSession();
    }

    public void deletePrecos(Long id){
        Session session = HibernateSession.getSession();
        session.beginTransaction();
        session.createNativeQuery("delete from precos where id ="+id).executeUpdate();
        session.close();
    }


}
