package com.softpark.impressaoJasper.model;

public class RelatorioPrint {

    String nomeEmpresa, placa, categoria, total, valorTotal, data;

    public RelatorioPrint(String nomeEmpresa, String placa, String categoria, String total,String valorTotal, String data) {
        this.nomeEmpresa = nomeEmpresa;
        this.placa = placa;
        this.categoria = categoria;
        this.total = total;
        this.valorTotal = valorTotal;
        this.data = data;
    }

    public String getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(String valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getNomeEmpresa() {
        return nomeEmpresa;
    }

    public void setNomeEmpresa(String nomeEmpresa) {
        this.nomeEmpresa = nomeEmpresa;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "RelatorioPrint{" +
                "nomeEmpresa='" + nomeEmpresa + '\'' +
                ", placa='" + placa + '\'' +
                ", categoria='" + categoria + '\'' +
                ", total='" + total + '\'' +
                ", valorTotal='" + valorTotal + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
