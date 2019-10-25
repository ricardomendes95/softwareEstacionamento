package com.softpark.model;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categoria")
public class Categoria extends RecursiveTreeObject<Categoria> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String nome;


    private int tolerancia;

    @OneToMany(mappedBy = "categoria", targetEntity = Precos.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Precos> listaCodBarras = new ArrayList<Precos>();


    public Categoria() {}

    public Categoria(long id, String nome, int tolerancia) {
        this.id = id;
        this.nome = nome;
        this.tolerancia = tolerancia;
    }

    public Categoria(String nome, int tolerancia, List<Precos> listaCodBarras) {
        this.nome = nome;
        this.tolerancia = tolerancia;
        this.listaCodBarras = listaCodBarras;
    }

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


    public int getTolerancia() {
        return tolerancia;
    }

    public void setTolerancia(int tolerancia) {
        this.tolerancia = tolerancia;
    }

    public List<Precos> getListaCodBarras() {
        return listaCodBarras;
    }

    public void setListaCodBarras(List<Precos> listaCodBarras) {
        this.listaCodBarras = listaCodBarras;
    }

    @Override
    public String toString() {
        return  nome;
    }
}
