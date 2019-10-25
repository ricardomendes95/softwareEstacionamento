package com.softpark.dao;

import com.softpark.model.Entrada;
import com.softpark.model.Saida;
import com.softpark.util.DAO;

import javax.persistence.Query;
import java.time.LocalDateTime;
import java.util.List;


public class SaidaDAO extends DAO<Saida> {
    public void salvar(Saida saida) {
        save(saida);
        Entrada e = saida.getEntrada();
        e.setFinalizada(true);
        new EntradaDAO().alterar(e);
    }

    public void alterar(Saida saida) {
        update(saida);
    }

    public void excluir(long id) {
        Saida saida = findById(id);
        delete(saida);
        getSession();
    }

    //retorna valores entre duas datas, formato de entrada: 2019-08-16T02:53:15.685-03:00
    public List<Saida>  getSaidaBetweenDates(String startDate, String endDate) {
        Query query = getSession().createQuery("SELECT s FROM Saida s WHERE s.dataHora BETWEEN :startDate AND :endDate order by s.dataHora");
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        List<Saida> resultList = query.getResultList();
        return resultList;
    }
}
