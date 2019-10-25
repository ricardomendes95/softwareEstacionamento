package view.controller;

import com.softpark.dao.ConfiguracaoDAO;
import com.softpark.dao.EntradaDAO;
import com.softpark.model.Configuracao;
import com.softpark.model.Entrada;
import com.softpark.util.Dimensions;
import com.softpark.util.LabelFormater;
import com.softpark.util.Toast;
import com.sun.javafx.scene.control.behavior.TabPaneBehavior;
import com.sun.javafx.scene.control.skin.TabPaneSkin;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;
import org.joda.time.DateTime;
import org.joda.time.Minutes;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class ConfigController implements Initializable {

    @FXML
    private AnchorPane pane;

    @FXML
    private AnchorPane anchorPrincipal;

    @FXML
    private Label lblEmpresa;

    @FXML
    private Label lblBairro;

    @FXML
    private Label lblCnpj;

    @FXML
    private Label lblNumero;

    @FXML
    private Label lblTelefone;

    @FXML
    private Label lblRua;

    @FXML
    private TextField txtEmpresa;

    @FXML
    private TextField txtRua;

    @FXML
    private TextField txtCnpj;

    @FXML
    private TextField txtBairro;

    @FXML
    private TextField txtTelefone;

    @FXML
    private TextField txtNumero;


    @FXML
    private Button btnSalvar;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
       initDropShadow();
        labelSize();
        txtTelefone.textProperty().addListener((observable, oldValue, newValue) -> {
            txtTelefone.setText(formatPhone(newValue));
        });

        Configuracao configuracao = new ConfiguracaoDAO().getConfig();
        if (configuracao != null) {
            txtEmpresa.setText(configuracao.getEmpresa());
            txtCnpj.setText(configuracao.getCnpj());
            txtRua.setText(configuracao.getRua());
            txtBairro.setText(configuracao.getBairro());
            txtTelefone.setText(configuracao.getTelefone());
            txtNumero.setText(configuracao.getNumero());
        }
        txtCnpj.textProperty().addListener((observable, oldValue, newValue) -> {
            if (removeMask(newValue).length() <= 11) {
                txtCnpj.setText(newValue.replaceAll(
                        "([0-9]{3})([0-9]{3})([0-9]{3})([0-9]{2})", "$1.$2.$3-$4"));
            }
            if (removeMask(newValue).length() == 14) {
                txtCnpj.setText(formatCPForCPNJ(newValue));
            }
        });


    }

        public String removeMask(String textField){
            return textField
                    .replace("(", "")
                    .replace(")", "")
                    .replace("-", "")
                    .replace("/", "")
                    .replace(".", "")
                    .replace(" ","");
        }

    public String formatPhone(String phone){
        if(phone == null){
            return "";
        }else{
            phone = phone.replace("(","").replace(")","").replace("-","").replace(" ","");
            boolean digitPhone8 = phone.length() < 11;
            return digitPhone8 ? phone.replaceAll(
                    "([0-9]{2})([0-9]{4})([0-9]{4})", "($1) $2-$3")
                    :  phone.replaceAll(
                    "([0-9]{2})([0-9]{5})([0-9]{4})", "($1) $2-$3"
            );}
    }
    public  String formatCPForCPNJ(String value) {
        value = value.replace(".","").replace("-","").replace("/","");
        boolean isCPF = value.length() < 12;
        return isCPF ?  value.replaceAll(
                "([0-9]{3})([0-9]{3})([0-9]{3})([0-9]{2})", "$1.$2.$3-$4")
                :value.replaceAll(
                "([0-9]{2})([0-9]{3})([0-9]{3})([0-9]{4})([0-9]{2})",
                "$1.$2.$3/$4-$5");
    }
    private void initDropShadow(){
        //Instantiating the Shadow class
        DropShadow dropShadow = new DropShadow();

        //definindo o tipo de desfoque para a sombra
        dropShadow.setBlurType(BlurType.GAUSSIAN);

        //definição de cor para a sombra
        dropShadow.setColor(Color.rgb(190,190,190));

        //Definindo a altura da sombra
        dropShadow.setHeight(5);

        //Definindo a largura da sombra
        dropShadow.setWidth(4);

        //definindo o deslocamento da sombra
        dropShadow.setOffsetX(5);
        dropShadow.setOffsetY(4);

        //Definindo a propagação da sombra
        dropShadow.setSpread(5);

        pane.setEffect(dropShadow);
    }




    private void labelSize(){
        try {
            double width = Dimensions.getWidth();
            double heigth = Dimensions.getHeight()/2;
            double prefSum = anchorPrincipal.getPrefHeight() + anchorPrincipal.getPrefWidth();
            LabelFormater.resizeComponents(width, heigth, prefSum,lblCnpj,txtCnpj,lblEmpresa, lblRua, lblBairro,lblNumero, lblTelefone,txtEmpresa, txtRua,txtBairro,txtNumero,txtTelefone,btnSalvar );
        }catch (Exception e){
        }
    }



    private TabPaneBehavior getBehavior() {
        return ((TabPaneSkin) PrincipalController.principalController .getTabPane().getSkin()).getBehavior();
    }


    public void btnSalvar(ActionEvent actionEvent) {
        Configuracao configuracao = new Configuracao();
        configuracao.setEmpresa(txtEmpresa.getText());
        configuracao.setCnpj(txtCnpj.getText());
        configuracao.setBairro(txtBairro.getText());
        configuracao.setNumero(txtNumero.getText());
        configuracao.setRua(txtRua.getText());
        configuracao.setTelefone(txtTelefone.getText());

        Configuracao c = new ConfiguracaoDAO().getConfig();

        if (c == null) {
            new ConfiguracaoDAO().salvar(configuracao);
        } else {
            c.setEmpresa(configuracao.getEmpresa());
            c.setCnpj(configuracao.getCnpj());
            c.setBairro(configuracao.getBairro());
            c.setNumero(configuracao.getNumero());
            c.setRua(configuracao.getRua());
            c.setTelefone(configuracao.getTelefone());
            new ConfiguracaoDAO().alterar(c);
        }
        Toast.makeText((Stage)pane.getScene().getWindow(),
                "Salvo!",
                2000,500,500, pane.getWidth(),pane.getHeight());
    }
}
