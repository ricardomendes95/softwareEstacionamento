package com.softpark.dao;

import com.softpark.model.Entrada;
import com.softpark.model.Usuario;
import com.softpark.util.DAO;

import javax.persistence.Query;
import java.util.List;


public class UsuarioDAO extends DAO<Usuario> {
    public void salvar(Usuario usuario) {
        save(usuario);
    }

    public void alterar(Usuario usuario) {
        update(usuario);
    }

    public void excluir(long id) {
        Usuario usuario = findById(id);
        delete(usuario);
        getSession();
    }

    public List<Usuario> listAll() throws Exception {
        return findAll();
    }

}
