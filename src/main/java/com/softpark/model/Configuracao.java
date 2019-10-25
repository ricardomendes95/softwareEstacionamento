package com.softpark.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Configuracao {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String empresa;
    private String cnpj;
    private String rua;
    private String bairro;
    private String numero;
    private String telefone;

    public Configuracao() {}

    public Configuracao(Long id, String empresa, String cnpj, String rua, String bairro, String numero, String telefone) {
        this.id = id;
        this.empresa = empresa;
        this.cnpj = cnpj;
        this.rua = rua;
        this.bairro = bairro;
        this.numero = numero;
        this.telefone = telefone;
    }

    public Configuracao(String empresa, String cnpj, String rua, String bairro, String numero, String telefone) {
        this.empresa = empresa;
        this.cnpj = cnpj;
        this.rua = rua;
        this.bairro = bairro;
        this.numero = numero;
        this.telefone = telefone;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
}
