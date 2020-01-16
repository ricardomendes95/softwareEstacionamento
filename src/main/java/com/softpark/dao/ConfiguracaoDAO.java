package com.softpark.dao;

import com.softpark.model.Configuracao;
import com.softpark.util.DAO;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import javax.persistence.TypedQuery;
import java.util.List;

public class ConfiguracaoDAO extends DAO<Configuracao> {
    public void salvar(Configuracao configuracao) {
        save(configuracao);
    }

    public void alterar(Configuracao configuracao) {
        update(configuracao);
    }

    public void excluir(long id) {
        Configuracao configuracao = findById(id);
        delete(configuracao);
    }

    public Configuracao getConfig() {
        TypedQuery<Configuracao> query = getSession().createQuery("from Configuracao");
        List<Configuracao> config = query.getResultList();
        return !config.isEmpty() ? config.get(0) : null;
    }
}
