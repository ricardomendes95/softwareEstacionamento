package com.softpark.impressaoJasper;

import com.softpark.impressaoJasper.model.EntradaPrint;
import com.softpark.impressaoJasper.model.PatioPrint;
import com.softpark.impressaoJasper.model.RelatorioPrint;
import com.softpark.impressaoJasper.model.SaidaPrint;
import javafx.scene.control.Alert;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class ImprimirJasper {

    //Recupera os caminhos para que a classe possa encontrar os relatórios
    public ImprimirJasper() { }

    //Imprime/gera uma lista de Clientes
    public void imprimirEntrada(List<EntradaPrint> entradaPrints) throws Exception
    {
        JasperDesign jasperDesign = JRXmlLoader.load(getClass().getResourceAsStream("/jasper/Entrada.jrxml"));

        JasperReport report = JasperCompileManager.compileReport(jasperDesign);

        JasperPrint print = JasperFillManager.fillReport(report, null, new JRBeanCollectionDataSource(entradaPrints));

        JasperPrintManager.printPage(print, 0, false);

    }

    public void imprimirSaida(List<SaidaPrint> saidaPrints) throws Exception
    {
        JasperDesign jasperDesign = JRXmlLoader.load(getClass().getResourceAsStream("/jasper/Saida.jrxml"));

        JasperReport report = JasperCompileManager.compileReport(jasperDesign);

        JasperPrint print = JasperFillManager.fillReport(report, null, new JRBeanCollectionDataSource(saidaPrints));

        JasperPrintManager.printPage(print, 0, false);
    }

    public void imprimirPatio(List<PatioPrint> patioPrints) throws Exception
    {
        JasperDesign jasperDesign = JRXmlLoader.load(getClass().getResourceAsStream("/jasper/Patio.jrxml"));

        JasperReport report = JasperCompileManager.compileReport(jasperDesign);

        JasperPrint print = JasperFillManager.fillReport(report, null, new JRBeanCollectionDataSource(patioPrints));

        JasperPrintManager.printPage(print, 0, false);
    }

    public void imprimirRelatorio(List<RelatorioPrint> relatorioPrints) throws Exception
    {
        JasperDesign jasperDesign = JRXmlLoader.load(getClass().getResourceAsStream("/jasper/Relatorio.jrxml"));

        JasperReport report = JasperCompileManager.compileReport(jasperDesign);

        JasperPrint print = JasperFillManager.fillReport(report, null, new JRBeanCollectionDataSource(relatorioPrints));

        JasperPrintManager.printPage(print, 0, false);
    }

}
