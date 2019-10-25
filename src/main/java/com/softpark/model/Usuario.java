package com.softpark.model;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "usuario")
public class Usuario extends RecursiveTreeObject<Usuario> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String nome;

    private String senha;

    private String tipo;


    public Usuario(String nome, String tipo, String senha) {
        this.nome = nome;
        this.senha = senha;
        this.tipo = tipo;
    }

    public Usuario(){}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    @Override
    public String toString() {
        return  nome ;
    }
}
