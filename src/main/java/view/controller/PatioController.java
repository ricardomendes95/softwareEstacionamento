package view.controller;

import com.softpark.dao.CategoriaDAO;
import com.softpark.dao.ConfiguracaoDAO;
import com.softpark.dao.EntradaDAO;
import com.softpark.impressaoJasper.ImprimirJasper;
import com.softpark.impressaoJasper.model.EntradaPrint;
import com.softpark.impressaoJasper.model.PatioPrint;
import com.softpark.model.Configuracao;
import com.softpark.model.Entrada;
import com.softpark.model.Saida;
import com.softpark.util.Dimensions;
import com.softpark.util.LabelFormater;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
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
import org.joda.time.Minutes;
import org.joda.time.Seconds;

import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
        MenuItem atualizar = new MenuItem("Atualizar");
        MenuItem imprimir = new MenuItem("Imprimir");
        atualizar.setStyle("-fx-font-size: 20px;");
        imprimir.setStyle("-fx-font-size: 20px;");
        contextMenu.getItems().addAll(atualizar,imprimir);

        atualizar.setOnAction(event -> {
            tableEstacione.refresh();
        });

        imprimir.setOnAction(event -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.US);
            String strLocalDate = tableEstacione.getSelectionModel().getSelectedItem().getData();
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
            ent.setPlaca("Placa: "+tableEstacione.getSelectionModel().getSelectedItem().getPlaca());
            ent.setTelefone("Telefone: "+c.getTelefone());
            ent.setValPrimeiraHora("Valor da primeira Hora: "+
                    monetario(String.valueOf(tableEstacione.getSelectionModel().
                            getSelectedItem().getCategoria().getListaCodBarras()
                            .get(0).getValor())));
            List<EntradaPrint> lista = new ArrayList<>();
            lista.add(ent);
            try {
                new ImprimirJasper().imprimirEntrada(lista);
            } catch (Exception e) {
                alert("Erro!","Erro na tentativa de impressão!",
                        "erro: "+e.getMessage()+", "+e.getCause()+", "+ e+", "+
                                e.getStackTrace());
            }
        });
        tableEstacione.setContextMenu(contextMenu);
    }



    public String monetario(String value){
        DecimalFormat df = new DecimalFormat("##,###,###,##0.00", new DecimalFormatSymbols(new Locale("pt", "BR")));
        df.setMinimumFractionDigits(2);
        df.setParseBigDecimal (true);
        return "R$ "+df.format(Double.valueOf(value));
    }
    public void btnImprimir(ActionEvent actionEvent) {
        List<PatioPrint> list = new ArrayList<>();
        Configuracao c = new ConfiguracaoDAO().getConfig();
        tableEstacione.getItems().forEach(i ->{
            list.add(new PatioPrint(c.getEmpresa(),
                    "CNPJ: "+c.getCnpj(),
                    c.getRua()+", "+c.getBairro()+", N: "+c.getNumero(),
                    "Telefone: "+c.getTelefone(),
                    i.getPlaca(),
                    i.getCategoria().getNome(),
                    String.valueOf(tableEstacione.getItems().size())));
        });

        try {
            new ImprimirJasper().imprimirPatio(list);
        } catch (Exception e) {
            alert("Erro!","Erro na tentativa de impressão!", "erro: "+e.getMessage()+", "+e.getCause()+", "+ e+", "+e.getStackTrace());
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
