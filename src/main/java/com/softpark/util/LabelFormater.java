package com.softpark.util;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple Currency Field for JavaFX
 * @author Gustavo
 * @version 1.0
 */
public class LabelFormater {
    public static void fontSize(double width, double heigth, double tamanhoPref, Label label){
       double tamPorc= label.getFont().getSize() / tamanhoPref;
        int tam = (int) Math.round((width + heigth) * tamPorc);
        String style = label.getFont().getStyle();
        label.setFont( Font.font(label.getFont().getName(),tam));
    }

    /**
     * ***********ARRAY TO LIST UTILITY************
     */
    public static List<Node> arrayToList(Node[] n) {
        List<Node> listItems = new ArrayList<>();
        for (Node n1 : n) {
            listItems.add(n1);
        }
        return listItems;
    }


    public static void resizeComponents(double width, double heigth, double tamanhoPref, Node... itemToCheck) {
        if((width+heigth)<= 2048 && (width+heigth)> 1792){
            tamanhoPref = tamanhoPref/1.3;
        }
        if((width+heigth)==1400){
            tamanhoPref = tamanhoPref/1.7;
        }
        for (Node n : arrayToList(itemToCheck)) {
            // Validate TextFields
            if (n instanceof TextField) {
                TextField textField = (TextField) n;
                double tamPorc= textField.getFont().getSize() / tamanhoPref;
                int tam = (int) Math.round((width + heigth) * tamPorc);
                String style = textField.getFont().getStyle();
                textField.setFont( Font.font(textField.getFont().getName(),FontWeight.findByName(style),tam));
            } // Validate TextArea
            else if (n instanceof TextArea) {
                TextArea textArea = (TextArea) n;
                double tamPorc= textArea.getFont().getSize() / tamanhoPref;
                int tam = (int) Math.round((width + heigth) * tamPorc);
                String style = textArea.getFont().getStyle();
                textArea.setFont( Font.font(textArea.getFont().getName(),FontWeight.findByName(style),tam));
            }
            else if (n instanceof Label) {
                Label label = (Label) n;
                System.out.println("tamanho de fonte antes: "+label.getFont().getSize());
                double tamPorc= label.getFont().getSize() / tamanhoPref;
                int tam = (int) Math.round((width + heigth) * tamPorc);
                System.out.println("tamanho de fonte depois: "+tam);
                String style = label.getFont().getStyle();
                label.setFont( Font.font(label.getFont().getName(),FontWeight.findByName(style),tam));
            }
            else if (n instanceof Button) {
                Button button = (Button) n;
                System.out.println(button.getFont().toString());
                double tamPorc= button.getFont().getSize() / tamanhoPref;
                int tam = (int) Math.round((width + heigth) * tamPorc);
                String style = button.getFont().getStyle();
                button.setFont(Font.font(button.getFont().getFamily(),FontWeight.findByName(style),tam));
                System.out.println(button.getFont().toString());
            }
        }

    }



}