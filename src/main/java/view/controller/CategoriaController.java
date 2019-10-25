package view.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.softpark.dao.CategoriaDAO;
import com.softpark.dao.PrecosDAO;
import com.softpark.model.Categoria;
import com.softpark.model.Entrada;
import com.softpark.model.Precos;
import com.softpark.util.Dimensions;
import com.softpark.util.LabelFormater;
import com.softpark.util.Toast;
import com.softpark.util.ValidationFields;
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
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class CategoriaController implements Initializable {
    @FXML
    private AnchorPane pane;

    @FXML
    private AnchorPane anchorPincipal;

    @FXML
    private Label lblNome;

    @FXML
    private Label lblHora;

    @FXML
    private Label lblValor;

    @FXML
    private Label lblTolerancia;

    @FXML
    private TextField txtHora;

    @FXML
    private TextField txtNome;

    @FXML
    private JFXButton btnSalvar;

    @FXML
    private JFXButton btnAdd;

    @FXML
    private TextField txtValHora;

    @FXML
    private TextField txtTolerancia;

    @FXML
    private GridPane grid;

    @FXML
    private JFXTreeTableView<Categoria> tableCate;

    private TreeItem<Categoria> categoriaTemp;

    @FXML
    private TableView<Precos> tableHoras;

    @FXML
    private TableColumn<Precos,String>  horaCol;

    @FXML
    private TableColumn<Precos,String>  valorCol;

    @FXML
    private VBox vboxPrecos;

    private Precos precoTemp;

    ObservableList<Precos> precos = FXCollections.observableArrayList();

    ObservableList<Categoria> listCat = FXCollections.observableArrayList();

    Categoria categoria;

    CategoriaDAO categoriaDAO =  new CategoriaDAO();

    int tamannhoList = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initDropShadow();
//        labelSize();
        initTable();
        menupopup();
        Platform.runLater(()->{
            txtHora.setText("1");
        });
        categoria = new Categoria();
        txtValHora.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ENTER){
                btnAdd.fire();
            }
        });

    }
    public String monetario(String value){
        DecimalFormat df = new DecimalFormat("##,###,###,##0.00", new DecimalFormatSymbols(new Locale("pt", "BR")));
        df.setMinimumFractionDigits(2);
        df.setParseBigDecimal (true);
        return "R$ "+df.format(Double.valueOf(value));
    }

    private void initTable(){
        JFXTreeTableColumn<Categoria,String> nomeCol = new JFXTreeTableColumn<Categoria,String>("Categoria");
        tableCate.setStyle("-fx-font-size: 20px;\n" +
                " -fx-background-color: rgb(64,224,208);\n" +
                " -fx-text-fill: WHITE;");
        nomeCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<Categoria,String> param) ->{
             StringProperty nome = new SimpleStringProperty(param.getValue().getValue().getNome());
            return nome;
        });

        nomeCol.setCellFactory((TreeTableColumn<Categoria, String> param) -> {
            TreeTableCell cell = new TreeTableCell<Categoria, String>(){
                @Override
                //by using Number we don't have to parse a String
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    TreeTableRow<Categoria> ttr = getTreeTableRow();
                    if (item == null || empty){
                        setText(null);
                        ttr.setStyle("");
                        setStyle("");
                    } else {
                        ttr.setStyle(!item.equals("")
                                ? "-fx-background-color:lightgreen; -fx-border-color: green;"
                                : "-fx-background-color:pink");
                        setText(item.toString());
//
                    }
                }
            };
            return cell;
        });

        JFXTreeTableColumn<Categoria,String> valFirstHourCol = new JFXTreeTableColumn<Categoria,String>("Valor Primeira Hora");
        valFirstHourCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<Categoria,String> param) ->{
             StringProperty nome = new SimpleStringProperty(monetario(String.valueOf(param.getValue().getValue().getListaCodBarras().get(0).getValor())));
            return nome;
        });

        JFXTreeTableColumn<Categoria,String> valorHoraCol = new JFXTreeTableColumn<Categoria,String>("Valor Última Hora");
        valorHoraCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<Categoria,String> param) ->{
             StringProperty nome = new SimpleStringProperty(monetario(String.valueOf(
                     param.getValue().getValue().getListaCodBarras()
                             .get(param.getValue().getValue().getListaCodBarras().size()-1).getValor())));
            return nome;
        });

        JFXTreeTableColumn<Categoria,String> toleranciaCol = new JFXTreeTableColumn<Categoria,String>("Tolerância");
        toleranciaCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<Categoria,String> param) ->{
             StringProperty nome = new SimpleStringProperty(String.valueOf(param.getValue().getValue().getTolerancia()));
            return nome;
        });

        tableCate.getColumns().addAll(nomeCol,valFirstHourCol,valorHoraCol,toleranciaCol);

        try {
            listCat.addAll(categoriaDAO.listAll());
        } catch (Exception e) {
            e.printStackTrace();
        }
        final TreeItem<Categoria> root = new RecursiveTreeItem<Categoria>(listCat, RecursiveTreeObject::getChildren);
        tableCate.setRoot(root);
        tableCate.setShowRoot(false);


    }

    private void updateTable(){
        listCat.clear();
        try {
            listCat.addAll(categoriaDAO.listAll());
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    @FXML
    public void btnSalvar(ActionEvent actionEvent) {
        System.out.println("clickou salvar");
            if (ValidationFields.checkEmptyFields(txtNome, txtTolerancia)) {

                if(precos.isEmpty()){
                    alert("Atenção!","Impossivel prosseguir!","Adicione valores das horas antes de prosseguir.");
                }else {
                    System.out.println("começando a verificação!");
                    if (categoriaTemp == null) {
                        System.out.println("criando um categoria");
                        categoria.setNome(txtNome.getText());
                        categoria.setTolerancia(Integer.valueOf(txtTolerancia.getText()));
                        categoria.setListaCodBarras(precos);
                        new CategoriaDAO().salvar(categoria);


                    } else {
                        System.out.println("atualizando uma categoria");
                        categoriaTemp.getValue().setNome(txtNome.getText());
                        categoriaTemp.getValue().setTolerancia(Integer.parseInt(txtTolerancia.getText()));
                        Categoria cat = categoriaTemp.getValue();
                        precos.forEach(p ->{
                            boolean v = tableCate.getSelectionModel().getSelectedItem().getValue().getListaCodBarras().stream().anyMatch(x -> x.getId() == p.getId());
                            if(!v){
                                p.setCategoria(cat);
                                new PrecosDAO().salvar(p);
                            }
                        });
                        tableCate.getSelectionModel().getSelectedItem().getValue().getListaCodBarras().forEach(p->{
                            boolean v = precos.stream().anyMatch(x -> x.getId() == p.getId());
                            if(!v){
                                new PrecosDAO().deletePrecos(p.getId());
                            }
                        });
                        new CategoriaDAO().alterar(cat);
                        tableCate.setDisable(false);
                        categoriaTemp = null;
                    }
                Toast.makeText((Stage) pane.getScene().getWindow(), "Salvo!", 2000, 500, 500, pane.getWidth(), pane.getHeight());
                tableCate.refresh();
                clearCampos();
                updateTable();
            }
        }
    }

    private void labelSize(){
        try {
            double width = Dimensions.getWidth();
            double heigth = Dimensions.getHeight()/2;
            double prefSum = anchorPincipal.getPrefHeight() + anchorPincipal.getPrefWidth();
            LabelFormater.resizeComponents(width, heigth, prefSum,lblNome,lblTolerancia,lblHora,lblValor,txtHora,txtTolerancia,txtValHora,txtNome,btnSalvar,btnAdd );
        }catch (Exception e){
        }
    }

    private void alert(String title, String header, String text){
        Alert dialogoInfo = new Alert(Alert.AlertType.INFORMATION);
        dialogoInfo.setTitle(title);
        dialogoInfo.setHeaderText(header);
        dialogoInfo.setContentText(text);
        dialogoInfo.showAndWait();
    }

    public void clearCampos(){
        txtNome.setText("");
        txtTolerancia.setText("7");
        txtValHora.setText("");
        txtHora.setText("1");
        tableHoras.getItems().clear();
        categoria = new Categoria();
    }



    private double removeMask(String valor) {
        return Double.valueOf(valor.equals("")? "0.00" : valor.replace("R$", "")
                .replace(".", "")
                .replace(",", ".")
                .replace("%","")
                .replace(" ",""));
    }

    private void menupopup(){
        final ContextMenu contextMenu = new ContextMenu();
        MenuItem remover = new MenuItem("Remover");
        MenuItem alterar = new MenuItem("Alterar");
        remover.setStyle("-fx-font-size: 20px;");
        alterar.setStyle("-fx-font-size: 20px;");
        contextMenu.getItems().addAll(remover,alterar);

        remover.setOnAction(event -> {
            listCat.remove(tableCate.getSelectionModel().getSelectedItem().getValue());
            new CategoriaDAO().excluir(tableCate.getSelectionModel().getSelectedItem().getValue().getId());
            tableCate.refresh();
        });

        alterar.setOnAction(event -> {
            categoriaTemp = tableCate.getSelectionModel().getSelectedItem();
            txtNome.setText(categoriaTemp.getValue().getNome());
            txtTolerancia.setText(String.valueOf(categoriaTemp.getValue().getTolerancia()));
            precos.addAll(categoriaTemp.getValue().getListaCodBarras());
            txtHora.setText(String.valueOf(precos.size()+1));
            setItensPrecos();
            tableCate.setDisable(true);
            tamannhoList = precos.size();

        });

        tableCate.setContextMenu(contextMenu);

        final ContextMenu contextMenuPreco = new ContextMenu();
        MenuItem removerPreco = new MenuItem("Remover");
        MenuItem alterarPreco = new MenuItem("Alterar");
        remover.setStyle("-fx-font-size: 20px;");
        alterar.setStyle("-fx-font-size: 20px;");
        contextMenuPreco.getItems().addAll(removerPreco,alterarPreco);

        removerPreco.setOnAction(event -> {
            precos.remove(tableHoras.getSelectionModel().getSelectedItem());
//            new PrecosDAO().excluir(tableHoras.getSelectionModel().getSelectedItem().getId());
            tableHoras.refresh();
        });

        alterarPreco.setOnAction(event -> {
            precoTemp = tableHoras.getSelectionModel().getSelectedItem();
            txtHora.setText(String.valueOf(precoTemp.getHora()));
            txtValHora.setText(monetario(String.valueOf(precoTemp.getValor())));
            tableHoras.setDisable(true);

        });

        tableHoras.setContextMenu(contextMenuPreco);
    }

    public void btnAdd(ActionEvent actionEvent) {
        if (ValidationFields.checkEmptyFields(txtHora,txtValHora)){
            if (precoTemp != null){
                precoTemp.setHora(Integer.parseInt(txtHora.getText()));
                precoTemp.setValor(Float.parseFloat(String.valueOf(removeMask(txtValHora.getText()))));
                precos.get(precos.indexOf(tableHoras.getSelectionModel().getSelectedItem())).setValor(precoTemp.getValor());
                precos.get(precos.indexOf(tableHoras.getSelectionModel().getSelectedItem())).setHora(precoTemp.getHora());
                tableHoras.getSelectionModel().getSelectedItem().setValor(precoTemp.getValor());
                tableHoras.getSelectionModel().getSelectedItem().setHora(precoTemp.getHora());
                tableHoras.setDisable(false);
                tableHoras.refresh();
                precoTemp = null;
            }else {
            precos.add(new Precos(Float.parseFloat(String.valueOf(removeMask(txtValHora.getText()))),Integer.parseInt(txtHora.getText()),categoria));
            setItensPrecos();
            txtHora.setText(String.valueOf(tableHoras.getItems().size()+1));
            txtValHora.setText("");
            txtValHora.requestFocus();
            }
        }
    }

    private void setItensPrecos(){
        horaCol.setCellValueFactory(new PropertyValueFactory<Precos,String>("hora"));
        valorCol.setCellValueFactory(new PropertyValueFactory<Precos,String>("valor"));
        tableHoras.setItems(precos);
    }

    private void centerPosition(){

            AnchorPane.setBottomAnchor(grid,((pane.getHeight()*53.7)/100));
//            AnchorPane.setTopAnchor(tableCate,((pane.getHeight()*47.1)/100));
            AnchorPane.setTopAnchor(tableCate,((Dimensions.getHeight()*42.2)/100));
            AnchorPane.setBottomAnchor(tableCate,(Dimensions.getHeight()*4.2)/100);
            AnchorPane.setLeftAnchor(tableCate,((Dimensions.getWidth()*2.95)/100));
            AnchorPane.setRightAnchor(tableCate,((Dimensions.getWidth()*2.95)/100));
//            AnchorPane.setTopAnchor(vboxPrecos,((Dimensions.getHeight()*5.3)/100));
//            AnchorPane.setRightAnchor(vboxPrecos,((Dimensions.getWidth()*2.95)/100));
//            AnchorPane.setBottomAnchor(vboxPrecos,Dimensions.getHeight()/2);
////            AnchorPane.setBottomAnchor(vboxPrecos,0.1);
//            AnchorPane.setLeftAnchor(vboxPrecos,((Dimensions.getWidth()*43.95)/100));
//            AnchorPane.setRightAnchor(splitTrib,((Dimensions.getWidth()*3.66)/100));
//            AnchorPane.setTopAnchor(splitTrib,((Dimensions.getHeight()*13)/100));
//            AnchorPane.setBottomAnchor(splitTrib,((Dimensions.getHeight()*1.3)/100));
            //GridPane.
    }
}
