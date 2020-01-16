package com.softpark.util;

import com.softpark.dao.ConfiguracaoDAO;
import com.softpark.impressaoJasper.ImprimirJasper;
import com.softpark.impressaoJasper.model.EntradaPrint;
import com.softpark.model.Configuracao;

import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Test {
    public static void main(String[] args) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.US);
        Configuracao c = new ConfiguracaoDAO().getConfig();
        System.out.println(c.getEmpresa());
        System.out.println(c.getCnpj());
        System.out.println(c.getRua());

        EntradaPrint ent = new EntradaPrint();
        ent.setNomeEmpresa(c.getEmpresa());
        ent.setCnpj(c.getCnpj());
        ent.setEndereco(c.getRua()+", "+c.getBairro()+", nº"+c.getNumero());
        ent.setDataEntrada("11/12/1254");
        ent.setHoraEntrada("16:25:00");
        ent.setPlaca("PLJ-1233");
        ent.setValPrimeiraHora("12");

        List<EntradaPrint> lista = new ArrayList<>();
        lista.add(ent);
        try {
            new ImprimirJasper().imprimirEntrada(lista);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
