package view.controller;

import com.jfoenix.controls.JFXButton;
import com.softpark.Index;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;

import java.net.URL;
import java.util.ResourceBundle;

public class VboxMenuController implements Initializable {

    @FXML
    private JFXButton entrada;

    @FXML
    private JFXButton saida;

    @FXML
    private JFXButton patio;

    @FXML
    private JFXButton relatorio;

    private PrincipalController principalController;


    public static VboxMenuController vboxMenuController;

    public static VboxMenuController getVboxMenuController() {
        return vboxMenuController;
    }

    public  void setVboxMenuController(VboxMenuController vboxMenuController) {
        this.vboxMenuController = vboxMenuController;
    }

    public JFXButton getEntrada() {
        return entrada;
    }

    public JFXButton getSaida() {
        return saida;
    }

    public JFXButton getPatio() {
        return patio;
    }

    public JFXButton getRelatorio() {
        return relatorio;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setVboxMenuController(this);
        principalController = PrincipalController.getPrincipalController();
        tab();
        Platform.runLater(()->{
        if(!Index.getUsuario().getTipo().equals("admin")){
            relatorio.setDisable(true);
        }
        });
    }

    public void openEntrada(ActionEvent actionEvent) {
        principalController.openTab("/view/fxml/entrada.fxml","Entrada");

    }

    public void openSaida(ActionEvent actionEvent) {
        principalController.openTab("/view/fxml/saida.fxml","Saída");
    }

    public void openPatio(ActionEvent actionEvent) {
        principalController.openTab("/view/fxml/patio.fxml","Pátio");
    }

    public void openRelatorio(ActionEvent actionEvent) {
        principalController.openTab("/view/fxml/relatorio.fxml","Relatório");
    }

    public void alterColor(){
        entrada.setStyle("-fx-background-color: rgb(64,224,208);");
        saida.setStyle("-fx-background-color: rgb(64,224,208);");
        relatorio.setStyle("-fx-background-color: rgb(64,224,208);");
        patio.setStyle("-fx-background-color: rgb(64,224,208);");
    }

    private void tab(){
        PrincipalController.principalController.getTabPane().getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Tab>() {
                    @Override
                    public void changed(ObservableValue<? extends Tab> ov, Tab t, Tab t1) {

                        alterColor();
                        if(t1 != null){
                            switch (t1.getText()){
                                case "Entrada": entrada.setStyle("-fx-background-color: rgb(95,158,160);");
                                break;
                                case "Pátio": patio.setStyle("-fx-background-color: rgb(95,158,160);");
                                                PatioController.getPatioController().updateTable();
                                break;
                                case "Saída": saida.setStyle("-fx-background-color: rgb(95,158,160);");
                                                SaidaController.getSaidaController().autoComplete(1);
                                break;
                                case "Relatório": relatorio.setStyle("-fx-background-color: rgb(95,158,160);");
                            }
                        }

                    }
                }
        );
    }
}
