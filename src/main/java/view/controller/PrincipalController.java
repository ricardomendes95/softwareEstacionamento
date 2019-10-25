package view.controller;



import com.jfoenix.controls.JFXDrawer;
import com.softpark.Index;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class PrincipalController implements Initializable {
    @FXML
    private AnchorPane anchorPrincipal;

    @FXML
    private Button minimize;

    @FXML
    private Button close;

    @FXML
    private JFXHamburger menu;

    @FXML
    private JFXDrawer drawer;

    @FXML
    private AnchorPane borderTop;

    @FXML
    private TabPane tabPane;

    public TabPane getTabPane() {
        return tabPane;
    }

    @FXML
    private BorderPane borderPane;

    @FXML
    private MenuItem config;

    @FXML
    private MenuItem categoria;

    @FXML
    private MenuItem usuarios;

    private static Map<String, Tab> openTabs = new HashMap<>();

    public static PrincipalController principalController;

    public static PrincipalController getPrincipalController() {
        return principalController;
    }

    public static void setPrincipalController(PrincipalController principalController) {
        PrincipalController.principalController = principalController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
            setPrincipalController(this);
            menuHamburguer();
       Platform.runLater(()->{
           if(!Index.getUsuario().getTipo().equals("admin")){
               categoria.setDisable(true);
               config.setDisable(true);
               usuarios.setDisable(true);
           }
           openTab("/view/fxml/patio.fxml","Pátio");
       });


    }


    public void closeProgram(ActionEvent actionEvent) { System.exit(0);}

    public void minimize(ActionEvent e) {
            ((Stage)((Button)e.getSource()).getScene().getWindow()).setIconified(true);
    }

    public void openTab(String panel,  String title){
        boolean value= true;

        if(openTabs.containsKey(panel) && value){
            tabPane.getSelectionModel().select(openTabs.get(panel));
        }else {
            try {
                Parent rootPanel = FXMLLoader.load(this.getClass().getResource(panel));
                Tab tab = new Tab(title);
                tab.setContent(rootPanel);
                BorderPane.setAlignment(tabPane, Pos.CENTER);
                borderPane.setCenter(tabPane);
                tabPane.getTabs().add(tab);
                tabPane.getSelectionModel().select(tab);
                openTabs.put(panel, tab);
                tab.setOnClosed(e -> openTabs.remove(panel));
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                showDialog("Erro!","Erro ao abrir Tab!", e1.getMessage(), Alert.AlertType.ERROR);
                e1.printStackTrace();
            }
        }
    }

    public static void showDialog(String title, String header, String message, Alert.AlertType alertType){
        Alert dialogoInfo = new Alert(alertType);
        dialogoInfo.setTitle(title);
        dialogoInfo.setHeaderText(header);
        dialogoInfo.setContentText(message);
        dialogoInfo.showAndWait();
    }

    private void menuHamburguer(){
        try {
            VBox vBox = FXMLLoader.load(getClass().getResource("/view/fxml/vboxMenu.fxml"));
            Platform.runLater(()->{
            vBox.setMaxHeight(anchorPrincipal.getHeight()-borderTop.getHeight());
            });
            //drawer.setSidePane(vBox);
        HamburgerBackArrowBasicTransition transition = new HamburgerBackArrowBasicTransition(menu);
        transition.setRate(-1);
        menu.addEventHandler(MouseEvent.MOUSE_PRESSED, (e) -> {
            transition.setRate(transition.getRate() * -1);
            transition.play();
            if(drawer.isOpened()){
                drawer.close();
                drawer.setSidePane(new VBox()   );
            }else {
                drawer.open();
                vBox.setPadding(new Insets(5,0,0,0));
                drawer.setSidePane(vBox);

            }
        });

            drawer.open();
            vBox.setPadding(new Insets(5,0,0,0));
            drawer.setSidePane(vBox);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void config(ActionEvent actionEvent) {
        principalController.openTab("/view/fxml/cofig.fxml","Configurações");
    }

    public void categoria(ActionEvent actionEvent) {
        principalController.openTab("/view/fxml/categoria.fxml","Categoria");
    }

    public void usuarios(ActionEvent actionEvent) {
        principalController.openTab("/view/fxml/usuarios.fxml","Usuário");
    }

    public void sair(ActionEvent actionEvent) {
        Stage stage = (Stage) anchorPrincipal.getScene().getWindow();
        stage.close();
        try{
        tabPane.getTabs().forEach(e->{
            closeTab(e);
        });}catch (Exception e){

        }
        Index.getIndex().openLogin(Index.getPrimaryStage());
    }

    private void closeTab(Tab tab) {
        EventHandler<Event> handler = tab.getOnClosed();
        if (null != handler) {
            handler.handle(null);
        } else {
            tab.getTabPane().getTabs().remove(tab);
        }
    }
}
