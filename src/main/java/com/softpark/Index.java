package com.softpark;

import com.softpark.dao.UsuarioDAO;
import com.softpark.model.Usuario;
import com.softpark.util.Dimensions;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Reflection;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.List;

public class Index extends Application {

    private static Stage stage;

    private void setPrimaryStage(Stage stage) {
        Index.stage = stage;
    }

    public static Stage getPrimaryStage() {
        return Index.stage;
    }

    public static  Usuario usuario;

    public static Usuario getUsuario() {
        return usuario;
    }

    public static void setUsuario(Usuario usuario) {
        Index.usuario = usuario;
    }

    public static Index index;

    public static Index getIndex() {
        return index;
    }

    public void setIndex(Index index) {
        this.index = index;
    }

    private Parent root;

    private Scene scene;

    public static void main(String[] args) {
        launch(args);
    }

    String checkPw;

    @Override
    public void start(Stage primaryStage) throws IOException {
        setIndex(this);
        setPrimaryStage(primaryStage);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setTitle("SoftPark");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/softpark.png")));
        openLogin(primaryStage);

    }

    public  void openLogin(Stage primaryStage){
        try {
            if(new UsuarioDAO().findAll().isEmpty()){
                new UsuarioDAO().salvar(new Usuario("Administrador","admin", "admin123"));
                new UsuarioDAO().salvar(new Usuario("Funcionario","func","123"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        VBox pane = new VBox();
        HBox HBfechar = new HBox();
        HBfechar.setAlignment(Pos.CENTER_RIGHT);
        Button btnSair = new Button("X");
        btnSair.setStyle("-fx-background-color: #FA8072; -fx-font-size: 18px;");
        btnSair.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.exit(0);
            }
        });
        HBfechar.getChildren().add(btnSair);
        HBfechar.setPadding(new Insets(5,5,0,0));

        pane.getChildren().add(HBfechar);

        BorderPane bp = new BorderPane();
        bp.setPadding(new Insets(0,50,50,50));

        //Adding HBox
        HBox hb = new HBox();
        hb.setPadding(new Insets(0,20,20,30));

        //Adding GridPane
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20,20,20,20));
        gridPane.setHgap(5);
        gridPane.setVgap(5);

        //Implementing Nodes for GridPane
        Label lblUserName = new Label("Usuário:");
        final ComboBox<Usuario> txtUserName = new ComboBox<>();

        try {
            List<Usuario> list = new UsuarioDAO().listAll();
            txtUserName.getItems().addAll(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        txtUserName.getSelectionModel().select(0);
        Label lblPassword = new Label("Senha:");
        final PasswordField pf = new PasswordField();

        Button btnLogin = new Button("Login");
        final Label lblMessage = new Label();

        //Adding Nodes to GridPane layout
        gridPane.add(lblUserName, 0, 0);
        gridPane.add(txtUserName, 1, 0);
        gridPane.add(lblPassword, 0, 1);
        gridPane.add(pf, 1, 1);
        gridPane.add(btnLogin, 2, 1);
        gridPane.add(lblMessage, 1, 2);


        //Reflection for gridPane
        Reflection r = new Reflection();
        r.setFraction(0.7f);
        gridPane.setEffect(r);

        //DropShadow effect
        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(5);
        dropShadow.setOffsetY(5);

        //Adding text and DropShadow effect to it
        Image img = new Image("/softpark.png");
        ImageView imgView = new ImageView(img);
        imgView.setFitWidth(60);
        imgView.setFitHeight(60);
        Text text = new Text("SoftPark");
        text.setFont(Font.font("Courier New", FontWeight.BOLD, 28));
        text.setEffect(dropShadow);

        //Adding text to HBox
        hb.setAlignment(Pos.CENTER);
        hb.getChildren().addAll(imgView,text);

        //Add ID's to Nodes
        bp.setId("bp");
        pane.setId("pane");
        gridPane.setId("root");
        btnLogin.setId("btnLogin");
        text.setId("text");

        //Action for btnLogin
        btnLogin.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                //checkUser = txtUserName.getSelectionModel().getSelectedItem().getNome();
                checkPw = pf.getText().toString();
                if(checkPw.equals(txtUserName.getSelectionModel().getSelectedItem().getSenha())){
                    lblMessage.setText("Sucesso!");
                    lblMessage.setTextFill(Color.GREEN);

                    openSystem(new Stage());
                    setUsuario(txtUserName.getSelectionModel().getSelectedItem());
                    primaryStage.close();

                }
                else{
                    lblMessage.setText("Senha Incorreta!");
                    lblMessage.setTextFill(Color.RED);
                }

                pf.setText("");
            }

        });

        pf.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ENTER){
                btnLogin.fire();
            }
        });

        //Add HBox and GridPane layout to BorderPane Layout
        bp.setTop(hb);
        bp.setCenter(gridPane);

        Platform.runLater(()->{
            pf.requestFocus();
        });

        pane.getChildren().add(bp);
        //Adding BorderPane to the scene and loading CSS
        Scene scene = new Scene(pane);
        scene.getStylesheets().add(getClass().getClassLoader().getResource("login.css").toExternalForm());
        primaryStage.setScene(scene);
        //primaryStage.setResizable(false);
        primaryStage.show();
    }

        private void openSystem(Stage primaryStage){
            primaryStage.close();
            primaryStage.setScene(null);
            primaryStage.initStyle(StageStyle.TRANSPARENT);
            primaryStage.setTitle("SoftPark");
            primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/softpark.png")));

            try {
                root = new FXMLLoader().load(getClass().getResource("/view/fxml/principal.fxml"));
            scene = new Scene(root, Dimensions.getWidth(), Dimensions.getHeight()/1.05);//primaryStage.initStyle(StageStyle.TRANSPARENT);
            primaryStage.setScene(scene);
            primaryStage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }


}
