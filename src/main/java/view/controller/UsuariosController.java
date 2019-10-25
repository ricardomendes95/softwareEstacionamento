package view.controller;

import com.softpark.dao.CategoriaDAO;
import com.softpark.dao.EntradaDAO;
import com.softpark.dao.UsuarioDAO;
import com.softpark.model.Categoria;
import com.softpark.model.Entrada;
import com.softpark.model.Usuario;
import com.softpark.util.Dimensions;
import com.softpark.util.LabelFormater;
import com.softpark.util.Toast;
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
import javafx.stage.Stage;
import javafx.util.Duration;
import org.joda.time.DateTime;
import org.joda.time.Seconds;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class UsuariosController implements Initializable {
    @FXML
    private AnchorPane pane;

    @FXML
    private AnchorPane anchorPrincipal;

    @FXML
    private ComboBox<Usuario> cbUsuario;
    
    @FXML
    private TextField txtSenha;
    
    @FXML
    private Label lblSenha;

    @FXML
    private Label lblNome;

    @FXML
    Button btnSalvar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initDropShadow();
        labelSize();
        preencherCombo();
       
    }

    private void preencherCombo(){
        UsuarioDAO cd = new UsuarioDAO();
        try {
            List<Usuario> list = cd.findAll();
            cbUsuario.getItems().addAll(list);

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

    public void btnSalvar(ActionEvent actionEvent) {
        cbUsuario.getSelectionModel().getSelectedItem().setSenha(txtSenha.getText());
        new UsuarioDAO().alterar(cbUsuario.getSelectionModel().getSelectedItem());
        Toast.makeText((Stage)anchorPrincipal.getScene().getWindow(),"Salvo!",2000,500,500, anchorPrincipal.getWidth(),anchorPrincipal.getHeight());

    }

    private void labelSize(){
        try {
            double width = Dimensions.getWidth();
            double heigth = Dimensions.getHeight()/3;
            double prefSum = anchorPrincipal.getPrefHeight() + anchorPrincipal.getPrefWidth();
            LabelFormater.resizeComponents(width, heigth, prefSum,txtSenha,lblSenha,lblNome,btnSalvar );

            double tamPorc= lblNome.getFont().getSize() / prefSum;
            int tam = (int) Math.round((width + heigth) * tamPorc);
            cbUsuario.setStyle("-fx-font-size: "+tam+";");
        }catch (Exception e){
        }
    }
}
