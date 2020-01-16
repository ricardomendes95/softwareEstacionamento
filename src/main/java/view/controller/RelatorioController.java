package view.controller;

import com.jfoenix.controls.JFXDatePicker;
import com.softpark.dao.ConfiguracaoDAO;
import com.softpark.dao.EntradaDAO;
import com.softpark.dao.SaidaDAO;
import com.softpark.impressaoJasper.ImprimirJasper;
import com.softpark.impressaoJasper.model.PatioPrint;
import com.softpark.impressaoJasper.model.RelatorioPrint;
import com.softpark.model.Configuracao;
import com.softpark.model.Entrada;
import com.softpark.model.Saida;
import com.softpark.util.Dimensions;
import com.softpark.util.LabelFormater;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.joda.time.DateTime;
import org.joda.time.Seconds;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class RelatorioController implements Initializable {
    @FXML
    private AnchorPane pane;

    @FXML
    private AnchorPane panePrincipal;

    @FXML
    private TableView<Saida> tableEstacione;

    @FXML
    private TableColumn<Saida, String> placaCol;

    @FXML
    private TableColumn<Saida, String> modeloCol;

    @FXML
    private TableColumn<Saida, String> dataHoraEntraCol;

    @FXML
    private TableColumn<Saida, String> dataHoraSaidaCol;

    @FXML
    private TableColumn<Saida, String> tempoCol;

    @FXML
    private TableColumn<Saida, String> valPagoCol;

    @FXML
    private Label lblTotal;

    @FXML
    private Label lblTotalVeic;

    @FXML
    private Label lbl1;

    @FXML
    private Label lbl2;

    @FXML
    private JFXDatePicker dateInicio;

    @FXML
    private JFXDatePicker dateFim;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initDropShadow();
        setItensTable();
        Platform.runLater(()->labelSize());
    }

    private void initDropShadow(){
        DropShadow dropShadow = new DropShadow();
        dropShadow.setBlurType(BlurType.GAUSSIAN);
        dropShadow.setColor(Color.rgb(190,190,190));
        dropShadow.setHeight(5);
        dropShadow.setWidth(4);
        dropShadow.setOffsetX(5);
        dropShadow.setOffsetY(4);
        dropShadow.setSpread(5);
        pane.setEffect(dropShadow);
    }

    private void setItensTable(){
        placaCol.setCellFactory(col -> {
            return new TableCell<Saida, String>() {
                @Override
                public void updateItem(String price, boolean empty) {
                    super.updateItem(price, empty);
                    if (empty) {
                        setText("");
                    } else {
                        setText(getTableView().getItems().get(getIndex()).getEntrada().getPlaca());
                    }
                }
            };
        });

        modeloCol.setCellFactory(col -> {
            return new TableCell<Saida, String>() {
                @Override
                public void updateItem(String price, boolean empty) {
                    super.updateItem(price, empty);
                    if (empty) {
                        setText("");
                    } else {
                        setText(getTableView().getItems().get(getIndex()).getEntrada().getCategoria().getNome());
                    }
                }
            };
        });
        dataHoraEntraCol.setCellFactory(col -> {
            return new TableCell<Saida, String>() {
                @Override
                public void updateItem(String price, boolean empty) {
                    super.updateItem(price, empty);
                    if (empty) {
                        setText("");
                    } else {
                        String data = getTableView().getItems().get(getIndex()).getEntrada().getData();
                        data = data.substring(8,10)+"/"+data.substring(5,7)+"/"+data.substring(0,4)+" "+data.substring(11,19);
                        setText(data);
                    }
                }
            };
        });
        dataHoraSaidaCol.setCellFactory(col -> {
            return new TableCell<Saida, String>() {
                @Override
                public void updateItem(String price, boolean empty) {
                    super.updateItem(price, empty);
                    if (empty) {
                        setText("");
                    } else {
                        String data = getTableView().getItems().get(getIndex()).getDataHora();
                        data = data.substring(8,10)+"/"+data.substring(5,7)+"/"+data.substring(0,4)+" "+data.substring(11,19);
                        setText(data);

                    }
                }
            };
        });


        tempoCol.setCellFactory(col -> {
            return new TableCell<Saida, String>() {
                @Override
                public void updateItem(String price, boolean empty) {
                    super.updateItem(price, empty);
                    if (empty) {
                        setText("");
                    } else {
                        setText(getTableView().getItems().get(getIndex()).getTempoDePermanencia());
                    }
                }
            };
        });

        valPagoCol.setCellFactory(col -> {
            return new TableCell<Saida, String>() {
                @Override
                public void updateItem(String price, boolean empty) {
                    super.updateItem(price, empty);
                    if (empty) {
                        setText("");
                    } else {
                        setText(monetario(getTableView().getItems().get(getIndex()).getValor()));
                    }
                }
            };
        });

    }

    public void updateTable(){
        SaidaDAO saidaDAO = new SaidaDAO();
        ObservableList<Saida> list = FXCollections.observableArrayList();

        list.addAll(saidaDAO.getSaidaBetweenDates(
                String.valueOf(new DateTime(String.valueOf(dateInicio.getValue().atStartOfDay()))),
                String.valueOf(LocalDateTime.of(dateFim.getValue(), LocalTime.MAX))));

        tableEstacione.setItems(list);
    }

    public String monetario(double value){
        DecimalFormat df = new DecimalFormat("##,###,###,##0.00", new DecimalFormatSymbols(new Locale("pt", "BR")));
        df.setMinimumFractionDigits(2);
        df.setParseBigDecimal (true);
        return "R$ "+df.format(value);
    }

    private void sum(){
        double val = tableEstacione.getItems().stream().mapToDouble(p-> p.getValor()==0?0:p.getValor()).sum();
        lblTotal.setText(monetario(val));
        lblTotalVeic.setText(String.valueOf(tableEstacione.getItems().size()));
    }

    public void search(ActionEvent actionEvent) {
        updateTable();
        sum();
    }

    public void btnImprimir(ActionEvent actionEvent) {
        if(tableEstacione.getItems().size() > 0) {
            List<RelatorioPrint> list = new ArrayList<>();
            Configuracao c = new ConfiguracaoDAO().getConfig();
            tableEstacione.getItems().forEach(i -> {
                list.add(new RelatorioPrint(
                        c.getEmpresa(),
                        i.getEntrada().getPlaca(),
                        i.getEntrada().getCategoria().getNome(),
                        String.valueOf(tableEstacione.getItems().size()),
                        lblTotal.getText(),
                        i.getDataHora().substring(0, 10)));
            });
            try {
                new ImprimirJasper().imprimirRelatorio(list);
            } catch (Exception e) {
                alert("Erro!", "Erro na tentativa de impressão!", "erro: " + e.getMessage() + ", " + e.getCause() + ", " + e + ", " + e.getStackTrace());
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

    private void labelSize(){
        try {
            double width = Dimensions.getWidth();
            double heigth = Dimensions.getHeight()/2;
            double prefSum = panePrincipal.getPrefHeight() + panePrincipal.getPrefWidth();
            LabelFormater.resizeComponents(width, heigth, prefSum,lblTotal,lblTotalVeic,lbl1,lbl2 );
        }catch (Exception e){
        }
    }
}
