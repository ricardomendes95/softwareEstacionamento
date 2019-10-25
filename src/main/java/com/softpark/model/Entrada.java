package com.softpark.model;

import javax.persistence.*;

@Entity
public class Entrada {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String placa;

    @ManyToOne
    private Categoria categoria;

    private String dataHora;

    private boolean finalizada;

    public Entrada() {}

    public Entrada(long id, String placa, Categoria categoria, String data) {
        this.id = id;
        this.placa = placa;
        this.categoria = categoria;
        this.dataHora = data;
    }

    public Entrada(String placa, Categoria categoria, String data) {
        this.placa = placa;
        this.categoria = categoria;
        this.dataHora = data;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public String getData() {
        return dataHora;
    }

    public void setData(String data) {
        this.dataHora = data;
    }

    public boolean isFinalizada() {
        return finalizada;
    }

    public void setFinalizada(boolean finalizada) {
        this.finalizada = finalizada;
    }

    @Override
    public String toString() {
        return
                placa;

    }
}
