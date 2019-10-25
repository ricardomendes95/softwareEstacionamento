package view.controller;

import com.softpark.dao.CategoriaDAO;
import com.softpark.dao.EntradaDAO;
import com.softpark.model.Entrada;
import com.softpark.model.Saida;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
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
import org.joda.time.Minutes;
import org.joda.time.Seconds;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class PatioController implements Initializable {
    @FXML
    private AnchorPane pane;

    @FXML
    private TableView<Entrada> tableEstacione;

    @FXML
    private TableColumn<Entrada, String> placaCol;

    @FXML
    private TableColumn<Entrada, String> modeloCol;

    @FXML
    private TableColumn<Entrada, String> entradaCol;

    @FXML
    private TableColumn<Entrada, String> tempoCol;

    public static PatioController patioController;

    public static PatioController getPatioController() {
        return patioController;
    }

    public void setPatioController(PatioController patioController) {
        this.patioController = patioController;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setPatioController(this);
        initDropShadow();
        setItensTable();
        menupopup();
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
        placaCol.setCellValueFactory(new PropertyValueFactory<Entrada,String>("placa"));
        modeloCol.setCellFactory(col ->
                new TableCell<Entrada, String>() {
                    @Override
                    public void updateItem(String price, boolean empty) {
                        super.updateItem(price, empty);
                        if (empty) {
                            setText("");
                        } else {
                            setText(getTableView().getItems().get(getIndex()).getCategoria().getNome());
                        }
                    }
                });
        entradaCol.setCellFactory(col ->
                new TableCell<Entrada, String>() {
                    @Override
                    public void updateItem(String price, boolean empty) {
                        super.updateItem(price, empty);
                        if (empty) {
                            setText("");
                        } else {
                            setText(getTableView().getItems().get(getIndex()).getData().substring(11,19));
                        }
                    }
                });


        tempoCol.setCellFactory(col -> new TableCell<Entrada,String>(){
            @Override
            public void updateItem(String price, boolean empty){
                super.updateItem(price,empty);
                if (empty){
                    setText("");
                }else{
                    StringProperty hora = new SimpleStringProperty("");
                    DateTime dataFinal = new DateTime();
                    DateTime dataInicio = new DateTime(getTableView().getItems().get(getIndex()).getData());

                    Seconds s = Seconds.secondsBetween(dataInicio,dataFinal);
                    int total = s.getSeconds();

                    try {
                        cronos(total,hora);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
                        setText(hora.getValue());
                    }), new KeyFrame(Duration.seconds(1))
                    );
                    clock.setCycleCount(Animation.INDEFINITE);
                    clock.play();
                }
            }
        });

                updateTable();

    }

    public void cronos(int cont,StringProperty e ) throws InterruptedException {


        StringProperty horas = new SimpleStringProperty("");
        Task<Void> task = new Task<Void>() {
            int contador = cont;
            @Override
            protected Void call() throws Exception {
                while(!isCancelled()){
                    contador++;
                    int seg  =  contador %60;
                    int min  =  contador /60;
                    int hora =  min      /60;
                    min     %=  60;

                    updateMessage(String.format("%02d:%02d:%02d",hora,min,seg));
                    Thread.sleep(1000);
                }

                return null;
            }
        };
        e.bind(task.messageProperty());
        new Thread(task).start();
    }

    public void updateTable(){
        ObservableList<Entrada> list = FXCollections.observableArrayList();
        list.addAll(new EntradaDAO().buscaEntradas());
        tableEstacione.setItems(list);
        tableEstacione.refresh();
    }

    private void menupopup() {
        final ContextMenu contextMenu = new ContextMenu();
        MenuItem remover = new MenuItem("Atualizar");
        remover.setStyle("-fx-font-size: 20px;");
        contextMenu.getItems().addAll(remover);

        remover.setOnAction(event -> {
            tableEstacione.refresh();
        });
        tableEstacione.setContextMenu(contextMenu);
    }
}
