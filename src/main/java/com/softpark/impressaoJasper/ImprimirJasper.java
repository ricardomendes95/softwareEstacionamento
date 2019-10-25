package com.softpark.impressaoJasper;

import com.softpark.impressaoJasper.model.EntradaPrint;
import com.softpark.impressaoJasper.model.SaidaPrint;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ImprimirJasper {
    private String path; //Caminho base

    private String pathToReportPackage; // Caminho para o package onde estão armazenados os relatorios Jarper

    //Recupera os caminhos para que a classe possa encontrar os relatórios
    public ImprimirJasper() {
        this.path = this.getClass().getClassLoader().getResource("").getPath();
        this.pathToReportPackage = this.path + "../../src/main/java/com.softpark/impressaoJasper/jasper/";
        int pos = path.indexOf("target");
        path = path.substring(0,pos);
        this.pathToReportPackage = this.path + "src/main/java/com/softpark/impressaoJasper/jasper";
        System.out.println(this.path);

        System.out.println(this.pathToReportPackage);
    }


    //Imprime/gera uma lista de Clientes
    public void imprimirEntrada(List<EntradaPrint> entradaPrints) throws Exception
    {
        JasperReport report = JasperCompileManager.compileReport(getPathToReportPackage()+ "/Entrada.jrxml");

        JasperPrint print = JasperFillManager.fillReport(report, null, new JRBeanCollectionDataSource(entradaPrints));

        JasperPrintManager.printPage(print, 0, false);
    }

    public void imprimirSaida(List<SaidaPrint> saidaPrints) throws Exception
    {
        JasperReport report = JasperCompileManager.compileReport(getPathToReportPackage()+ "/Saida.jrxml");

        JasperPrint print = JasperFillManager.fillReport(report, null, new JRBeanCollectionDataSource(saidaPrints));

        JasperPrintManager.printPage(print, 0, false);
    }

    public String getPathToReportPackage() {
        return this.pathToReportPackage;
    }

    public String getPath() {
        return this.path;
    }
}
