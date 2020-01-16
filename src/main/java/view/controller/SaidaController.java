package view.controller;

import com.softpark.Index;
import com.softpark.dao.CategoriaDAO;
import com.softpark.dao.ConfiguracaoDAO;
import com.softpark.dao.EntradaDAO;
import com.softpark.dao.SaidaDAO;
import com.softpark.impressaoJasper.ImprimirJasper;
import com.softpark.impressaoJasper.model.EntradaPrint;
import com.softpark.impressaoJasper.model.SaidaPrint;
import com.softpark.model.*;
import com.softpark.util.Dimensions;
import com.softpark.util.LabelFormater;
import com.softpark.util.Toast;
import com.softpark.util.ValidationFields;
import com.sun.javafx.scene.control.behavior.TabPaneBehavior;
import com.sun.javafx.scene.control.skin.TabPaneSkin;
import impl.org.controlsfx.autocompletion.SuggestionProvider;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;
import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.joda.time.Seconds;

import javax.print.PrintException;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class SaidaController implements Initializable {

    @FXML
    private AnchorPane pane;

    @FXML
    private AnchorPane anchorPrincipal;

    @FXML
    private Label lblDataEntrada;

    @FXML
    private Label lblHoraEntrada;

    @FXML
    private Label lblPlaca;

    @FXML
    private Label lblCategoria;

    @FXML
    private Label lblPer;

    @FXML
    private Label lblPermanencia;

    @FXML
    private Label lblVal;

    @FXML
    private Label lblValor;

    @FXML
    private Label lblHora;

    @FXML
    private TextField txtData;

    @FXML
    private TextField txtHora;

    @FXML
    private TextField txtPlaca;

    @FXML
    private TextField txtCategoria;

    @FXML
    private TextField txtPlacaConsult;

    @FXML
    private Button btnConfirmar;

    @FXML
    private Button btnCancelar;


    private Entrada entrada;

    DateTime dataFinal;

    LocalDateTime dataFinalTemp;

    ObservableList<Entrada> tabEntrada = FXCollections.observableArrayList();

    public static SaidaController saidaController;

    public static SaidaController getSaidaController() {
        return saidaController;
    }

    public void setSaidaController(SaidaController saidaController) {
        this.saidaController = saidaController;
    }

    AutoCompletionBinding<Entrada> bindCfopEnt = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setSaidaController(this);
       initDropShadow();
        labelSize();
        getHour();
        txtformatPlaca();
        autoComplete(0);
        Platform.runLater(()->{
            if(Index.getUsuario().getTipo().equals("admin")){
                menupopup();
                txtPlacaConsult.requestFocus();
            }
        });

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

    private Float removeMask(String valor) {
        return Float.valueOf(valor.equals("")? "0.00" : valor.replace("R$", "")
                .replace(".", "")
                .replace(",", ".")
                .replace("%","")
                .replace(" ",""));
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


    public void autoComplete(int v){
        txtPlacaConsult.requestFocus();
        atualizarListaComplete();
        if(v==1){
        bindCfopEnt.dispose();
        }
        bindCfopEnt = TextFields.bindAutoCompletion(txtPlacaConsult, SuggestionProvider.create(tabEntrada) );
        bindCfopEnt.setOnAutoCompleted(event -> {
            txtPlacaConsult.setText(String.valueOf(event.getCompletion().getPlaca()));
            txtData.setText(event.getCompletion().getData().substring(0,10));
            txtHora.setText(event.getCompletion().getData().substring(11,19));
            txtPlaca.setText(event.getCompletion().getPlaca());
            txtCategoria.setText(event.getCompletion().getCategoria().getNome());
            entrada = event.getCompletion();
            lblPermanencia.setText(calculaTempo(event.getCompletion().getData()));
            lblValor.setText(monetario(Double.parseDouble(calculaPreco(event.getCompletion().getData(),event.getCompletion().getCategoria()))));
            lblValor.requestFocus();
        });
    }

    public void atualizarListaComplete(){
        try {
            tabEntrada.clear();
            tabEntrada.addAll(new EntradaDAO().buscaEntradas());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void getHour(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        Timeline clock2 = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            lblHora.setText(LocalDateTime.now().format(formatter));
        }), new KeyFrame(Duration.seconds(1))
        );

        clock2.setCycleCount(Animation.INDEFINITE);
        clock2.play();

    }

    private void labelSize(){
        try {
            double width = Dimensions.getWidth();
            double heigth = Dimensions.getHeight()/2;
            double prefSum = anchorPrincipal.getPrefHeight() + anchorPrincipal.getPrefWidth();
            LabelFormater.resizeComponents(width, heigth, prefSum,lblDataEntrada,lblHoraEntrada,lblCategoria,lblPlaca,lblPer,lblPermanencia,lblVal,lblValor,txtData,txtHora,txtPlaca,txtCategoria,txtPlacaConsult );
        }catch (Exception e){
        }
    }

    private void menupopup() {
        final ContextMenu contextMenu = new ContextMenu();
        MenuItem zerar = new MenuItem("ZERAR");
        zerar.setStyle("-fx-font-size: 20px;");
        contextMenu.getItems().addAll(zerar);

        zerar.setOnAction(event -> {
            lblValor.setText("R$ 0,00");
        });


        lblValor.setContextMenu(contextMenu);

    }

    private String  calculaTempo(String horas){
        DateTime dataFinal = new DateTime();
        DateTime dataInicio = new DateTime(horas);

        Seconds s = Seconds.secondsBetween(dataInicio,dataFinal);
        int contador = s.getSeconds();

        int seg  =  contador %60;
        int min  =  contador /60;
        int hora =  min      /60;
        min     %=  60;

       return String.format("%02d:%02d:%02d",hora,min,seg);
    }

    private String  calculaPreco(String horas, Categoria categoria){
         dataFinal = new DateTime();
         dataFinalTemp = LocalDateTime.now();
        DateTime dataInicio = new DateTime(horas);

        Minutes m = Minutes.minutesBetween(dataInicio,dataFinal);
        int total = m.getMinutes();
        int hours = 0;

        //Ordenar o array por tipo
        Collections.sort(categoria.getListaCodBarras());
        int ultimahora = categoria.getListaCodBarras().size()*60;
        if(total < categoria.getTolerancia()){
            return "0.00";
        }
        else if(total > categoria.getTolerancia() && total < 75){
            return String.valueOf(categoria.getListaCodBarras().get(0).getValor());
        }
        else if(total >= ultimahora){
            return String.valueOf(categoria.getListaCodBarras().get(categoria.getListaCodBarras().size()-1).getValor());
        }else{
            while (total >59){
            hours++;
            total-=60;
            }
            String valorFinal = "";
            for(int i=0; i< categoria.getListaCodBarras().size();i++){
                if(categoria.getListaCodBarras().get(i).getHora() == hours){
                    if(total >15){
                        valorFinal = String.valueOf(categoria.getListaCodBarras().get(i+1).getValor());
                        break;
                    }else {
                        valorFinal = String.valueOf(categoria.getListaCodBarras().get(i).getValor());
                        break;
                    }
                }
            }

            return valorFinal;
        }
    }

    public String monetario(double value){
        DecimalFormat df = new DecimalFormat("##,###,###,##0.00", new DecimalFormatSymbols(new Locale("pt", "BR")));
        df.setMinimumFractionDigits(2);
        df.setParseBigDecimal (true);
        return "R$ "+df.format(value);
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

    public void clear() {
    entrada = null;
    dataFinal = null;
    txtCategoria.setText("");
    txtPlaca.setText("");
    txtHora.setText("");
    txtData.setText("");
    txtPlacaConsult.clear();
    lblValor.setText("");
    lblPermanencia.setText("");
    txtPlacaConsult.requestFocus();
    }

    public void btnConfirm(ActionEvent actionEvent) {
        if(ValidationFields.checkEmptyFields(txtPlaca,txtData,txtHora,txtCategoria)){
            if(! entrada.isFinalizada()){
                entrada.setFinalizada(true);
                Saida saida = new Saida(entrada,
                        String.valueOf(dataFinal),
                        removeMask(lblValor.getText()),
                        lblPermanencia.getText());
                new SaidaDAO().salvar(saida);

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.US);

                String strLocalDate = entrada.getData();
                LocalDateTime localDate = LocalDateTime.parse(strLocalDate, formatter);
                String strDateSaida = String.valueOf(dataFinalTemp);
                LocalDateTime dateSaida = LocalDateTime.parse(strDateSaida, formatter);

                String dt = DateTimeFormatter.ofPattern("dd-MM-yyyy").format(localDate);
                String hr = DateTimeFormatter.ofPattern("HH:mm:ss").format(localDate);
                String hrS = DateTimeFormatter.ofPattern("HH:mm:ss").format(dateSaida);

                Configuracao c = new ConfiguracaoDAO().getConfig();

                SaidaPrint sai = new SaidaPrint();
                sai.setNomeEmpresa(c.getEmpresa());
                sai.setCnpj("CNPJ: "+c.getCnpj());
                sai.setEndereco(c.getRua()+", "+c.getBairro()+", N: "+c.getNumero());
                sai.setDataEntrada("Data: "+dt);
                sai.setHoraEntrada("Hora entrada: "+hr);
                sai.setHoraSaida("Hora Saida: "+hrS);
                sai.setPlaca("Placa: "+entrada.getPlaca());
                sai.setTelefone("Telefone: "+c.getTelefone());
                sai.setValPago("Valor pago: "+lblValor.getText());
                sai.setTempoPermanencia("Tempo de Permanência: "+lblPermanencia.getText());

                List<SaidaPrint> lista = new ArrayList<>();
                lista.add(sai);
                try {
                    new ImprimirJasper().imprimirSaida(lista);
                } catch (Exception e) {
                    alert("Erro!","Erro na tentativa de impressão!", "erro: "+e.getMessage()+", "+e.getCause()+", "+ e+", "+e.getStackTrace());
                }
                Toast.makeText((Stage)pane.getScene().getWindow(),
                        "Confirmado!",
                        2000,500,500, pane.getWidth(),pane.getHeight());
                clear();
                autoComplete(1);
            }
        }
    }

    private void alert(String title, String header, String text){
        Alert dialogoInfo = new Alert(Alert.AlertType.INFORMATION);
        dialogoInfo.setTitle(title);
        dialogoInfo.setHeaderText(header);
        dialogoInfo.setContentText(text);
        dialogoInfo.showAndWait();
    }
}
