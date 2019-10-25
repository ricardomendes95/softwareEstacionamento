package com.softpark.impressaoJasper.model;

public class SaidaPrint {

    String nomeEmpresa, cnpj,endereco, telefone, dataEntrada, horaEntrada,horaSaida, placa, valPago, tempoPermanencia;

    public String getNomeEmpresa() {
        return nomeEmpresa;
    }

    public void setNomeEmpresa(String nomeEmpresa) {
        this.nomeEmpresa = nomeEmpresa;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(String dataEntrada) {
        this.dataEntrada = dataEntrada;
    }

    public String getHoraEntrada() {
        return horaEntrada;
    }

    public void setHoraEntrada(String horaEntrada) {
        this.horaEntrada = horaEntrada;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getHoraSaida() {
        return horaSaida;
    }

    public void setHoraSaida(String horaSaida) {
        this.horaSaida = horaSaida;
    }

    public String getValPago() {
        return valPago;
    }

    public void setValPago(String valPago) {
        this.valPago = valPago;
    }

    public String getTempoPermanencia() {
        return tempoPermanencia;
    }

    public void setTempoPermanencia(String tempoPermanencia) {
        this.tempoPermanencia = tempoPermanencia;
    }

    @Override
    public String toString() {
        return "SaidaPrint{" +
                "nomeEmpresa='" + nomeEmpresa + '\'' +
                ", cnpj='" + cnpj + '\'' +
                ", endereco='" + endereco + '\'' +
                ", telefone='" + telefone + '\'' +
                ", dataEntrada='" + dataEntrada + '\'' +
                ", horaEntrada='" + horaEntrada + '\'' +
                ", horaSaida='" + horaSaida + '\'' +
                ", placa='" + placa + '\'' +
                ", valPago='" + valPago + '\'' +
                ", tempoPermanencia='" + tempoPermanencia + '\'' +
                '}';
    }
}
