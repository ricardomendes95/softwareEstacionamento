package com.softpark.model;

import javax.persistence.*;

@Entity
public class Precos implements Comparable<Precos>{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private float valor;

    private int hora;

    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinColumn(name="categoria_id")
    private Categoria categoria;


    public Precos() {}


    public Precos(float valor, int hora, Categoria categoria) {
        this.valor = valor;
        this.hora = hora;
        this.categoria = categoria;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public int getHora() {
        return hora;
    }

    public void setHora(int hora) {
        this.hora = hora;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    @Override
    public int compareTo(Precos o) {
        if (this.hora > o.getHora()) {
            return 1;
        } if (this.hora < o.getHora()) {
            return -1;
        }
        return 0;
    }
}
