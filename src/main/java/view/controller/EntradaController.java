package view.controller;

import com.softpark.dao.CategoriaDAO;
import com.softpark.dao.ConfiguracaoDAO;
import com.softpark.dao.EntradaDAO;
import com.softpark.impressaoJasper.ImprimirJasper;
import com.softpark.impressaoJasper.model.EntradaPrint;
import com.softpark.model.Categoria;
import com.softpark.model.Configuracao;
import com.softpark.model.Entrada;
import com.softpark.util.*;
import com.sun.javafx.scene.control.behavior.TabPaneBehavior;
import com.sun.javafx.scene.control.skin.TabPaneSkin;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.print.PrintException;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.bouncycastle.crypto.tls.ContentType.alert;

public class EntradaController implements Initializable {

    @FXML
    private AnchorPane pane;

    @FXML
    private AnchorPane anchorPrincipal;

    @FXML
    private Label lblData;

    @FXML
    private Label lblHora;

    @FXML
    private Label lblPlaca;

    @FXML
    private Label lblCategoria;

    @FXML
    private TextField txtData;

    @FXML
    private TextField txtHora;

    @FXML
    private TextField txtPlaca;

    @FXML
    private ComboBox<Categoria> cbCategoria;

    @FXML
    private Button btnConfirmar;

    @FXML
    private Button btnCancelar;

    @FXML
    private Button btnLimpar;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
       initDropShadow();
        labelSize();
        getHour();
        txtformatPlaca();
        preencherCombo();
        Platform.runLater(()->focus());
        comboSelectCarro();
    }

    public void focus(){
        txtPlaca.requestFocus();
    }

    private void txtformatPlaca(){
       txtPlaca.textProperty().addListener((observable, oldValue, newValue) -> {
           if(newValue.length()<11){
           txtPlaca.setText( newValue.replaceAll(
                   "([aA-zZ]{3})([0-9]{4})", "$1 - $2").toUpperCase());
           }else{
               txtPlaca.setText(oldValue);
           }
       });
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

    private void getHour(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            txtHora.setText(LocalDateTime.now().format(formatter).substring(11));
        }), new KeyFrame(Duration.seconds(1))
        );
        txtData.setText(LocalDateTime.now().format(formatter).substring(0, 10));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }

    private void labelSize(){
        try {
            double width = Dimensions.getWidth();
            double heigth = Dimensions.getHeight()/2;
            double prefSum = anchorPrincipal.getPrefHeight() + anchorPrincipal.getPrefWidth();
            LabelFormater.resizeComponents(width, heigth, prefSum,lblData,lblHora,lblCategoria,lblPlaca,txtData,txtHora,txtPlaca );
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void alert(String title, String header, String text){
        Alert dialogoInfo = new Alert(Alert.AlertType.INFORMATION);
        dialogoInfo.setTitle(title);
        dialogoInfo.setHeaderText(header);
        dialogoInfo.setContentText(text);
        dialogoInfo.showAndWait();
    }

    public void btnCancel(ActionEvent actionEvent) {
        TabPaneBehavior behavior = getBehavior();
        if(behavior.canCloseTab(PrincipalController.getPrincipalController().getTabPane().getSelectionModel().getSelectedItem())) {
            behavior.closeTab(PrincipalController.getPrincipalController().getTabPane().getSelectionModel().getSelectedItem());
        }

    }

    private TabPaneBehavior getBehavior() {
        return ((TabPaneSkin) PrincipalController.principalController .getTabPane().getSkin()).getBehavior();
    }

    public void btnLimpar(ActionEvent actionEvent) {
    txtPlaca.setText("");
    comboSelectCarro();
    txtPlaca.requestFocus();
    }

    private void preencherCombo(){
        CategoriaDAO cd = new CategoriaDAO();
        try {
            List<Categoria> list = cd.findAll();
            cbCategoria.getItems().addAll(list);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void comboSelectCarro(){
        for(int i =0 ; i<cbCategoria.getItems().size();i++){
            if(cbCategoria.getItems().get(i).getNome().toLowerCase().equals("carro")){
                cbCategoria.getSelectionModel().select(cbCategoria.getItems().get(i));
            }
        }
    }

    private Stage getStage(){
        return (Stage) anchorPrincipal.getScene().getWindow();
    }

    public void btcConfirm(ActionEvent actionEvent){
            Entrada entrada = new Entrada();
        if(ValidationFields.checkEmptyFields(txtPlaca)){
            entrada.setPlaca(txtPlaca.getText());
            entrada.setData(String.valueOf(LocalDateTime.now()));
            entrada.setCategoria(cbCategoria.getSelectionModel().getSelectedItem());
            EntradaDAO ed = new EntradaDAO();
            ed.salvar(entrada);
            Toast.makeText(getStage(),"Confirmado!",2000,500,500, anchorPrincipal.getWidth(),anchorPrincipal.getHeight());
            btnLimpar(actionEvent);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.US);

        String strLocalDate = entrada.getData();

        LocalDateTime localDate = LocalDateTime.parse(strLocalDate, formatter);

        String dt = DateTimeFormatter.ofPattern("dd-MM-yyyy").format(localDate);
        String hr = DateTimeFormatter.ofPattern("HH:mm:ss").format(localDate);

        Configuracao c = new ConfiguracaoDAO().getConfig();

        EntradaPrint ent = new EntradaPrint();
        ent.setNomeEmpresa(c.getEmpresa());
        ent.setCnpj("CNPJ: "+c.getCnpj());
        ent.setEndereco(c.getRua()+", "+c.getBairro()+", N: "+c.getNumero());
        ent.setDataEntrada("Data entrada: "+dt);
        ent.setHoraEntrada("Hora:"+hr);
        ent.setPlaca("Placa: "+entrada.getPlaca());
        ent.setTelefone("Telefone: "+c.getTelefone());
        ent.setValPrimeiraHora("Valor da primeira Hora: "+monetario(String.valueOf(entrada.getCategoria().getListaCodBarras().get(0).getValor())));

        List<EntradaPrint> lista = new ArrayList<>();
        lista.add(ent);
        try {
            new ImprimirJasper().imprimirEntrada(lista);
        } catch (Exception e) {
            alert("Erro!","Erro na tentativa de impressão!", "erro: "+e.getMessage()+", "+e.getCause()+", "+ e+", "+e.getStackTrace());
        }
        }
    }

    public String monetario(String value){
        DecimalFormat df = new DecimalFormat("##,###,###,##0.00", new DecimalFormatSymbols(new Locale("pt", "BR")));
        df.setMinimumFractionDigits(2);
        df.setParseBigDecimal (true);
        return "R$ "+df.format(Double.valueOf(value));
    }
}
