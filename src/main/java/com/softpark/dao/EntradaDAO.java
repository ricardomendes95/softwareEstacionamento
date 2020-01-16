package com.softpark.dao;

import com.softpark.model.Entrada;
import com.softpark.util.DAO;

import java.util.List;

public class EntradaDAO extends DAO<Entrada> {
    public void salvar(Entrada entrada) {
        save(entrada);
    }

    public void alterar(Entrada entrada) {
        update(entrada);
    }

    public void excluir(long id) {
        Entrada entrada = findById(id);
        delete(entrada);
    }

    public List<Entrada> buscaEntradas() {
        List<Entrada> listaEntradas = getSession().createQuery("select e from Entrada e where e.finalizada = 0").getResultList();
        return listaEntradas;
    }
}
