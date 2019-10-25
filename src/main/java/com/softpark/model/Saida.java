package com.softpark.model;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Saida {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToOne
    private Entrada entrada;

    private String dataHora;

    private float valor;

    private String tempoDePermanencia;

    public Saida() {}

    public Saida(long id, Entrada entrada, String dataHora, float valor, String tempoDePermanencia) {
        this.id = id;
        this.entrada = entrada;
        this.dataHora = dataHora;
        this.valor = valor;
        this.tempoDePermanencia = tempoDePermanencia;
    }

    public Saida(Entrada entrada, String dataHora, float valor, String tempoDePermanencia) {
        this.entrada = entrada;
        this.dataHora = dataHora;
        this.valor = valor;
        this.tempoDePermanencia = tempoDePermanencia;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Entrada getEntrada() {
        return entrada;
    }

    public void setEntrada(Entrada entrada) {
        this.entrada = entrada;
    }

    public String getDataHora() {
        return dataHora;
    }

    public void setDataHora(String dataHora) {
        this.dataHora = dataHora;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public String getTempoDePermanencia() {
        return tempoDePermanencia;
    }

    public void setTempoDePermanencia(String tempoDePermanencia) {
        this.tempoDePermanencia = tempoDePermanencia;
    }
}
