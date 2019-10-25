package view.controller;

import com.jfoenix.controls.JFXDatePicker;
import com.softpark.dao.EntradaDAO;
import com.softpark.dao.SaidaDAO;
import com.softpark.model.Entrada;
import com.softpark.model.Saida;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

public class RelatorioController implements Initializable {
    @FXML
    private AnchorPane pane;

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
    private JFXDatePicker dateInicio;

    @FXML
    private JFXDatePicker dateFim;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initDropShadow();
        setItensTable();

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
    }

    public void search(ActionEvent actionEvent) {
        updateTable();
        sum();
    }
}
