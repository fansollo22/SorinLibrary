/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sorinlibraryc.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import org.json.JSONObject;
import sorinlibraryc.Client;
import sorinlibraryc.models.Books;

/**
 * FXML Controller class
 *
 * @author Flagster
 */
public class AddReviewViewController implements Initializable {

    @FXML
    private ComboBox<String> rating;
    
    @FXML
    private TextArea review;
    
    @FXML
    private Label message;
    
    String[] intArray = new String[]{ "1","2","3","4","5" };
    
    AdminBookViewController ctrl;
    
    UserBookViewController ctrl2;
    
    Client client;
    
    Books book;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        rating.getItems().addAll(intArray);
        review.setTextFormatter(new TextFormatter<String>(change -> 
            change.getControlNewText().length() <= 300 ? change : null));
        review.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                if(newValue.length() > 300)
                    newValue = oldValue;
                
                message.setText("Max "+(300-newValue.length())+" characters.");
            }
        });
    }
    
    public void passParameters(Books b,Client c, AdminBookViewController ctrl, UserBookViewController ctrl2)
    {
        this.client = c;
        this.book = b;
        this.ctrl = ctrl;
        this.ctrl2 = ctrl2;
    }
    
    public void publishReview()
    {
        JSONObject params = new JSONObject();
        params.put("book",book.getId());
        params.put("user", client.getSession());
        String givenRating;
        if(rating.getSelectionModel().isEmpty())
            givenRating = rating.getValue();
        else
            givenRating = "5";
        params.put("rating", givenRating);
        params.put("review", review.getText());
        params.put("sessionID", client.getSession());
        JSONObject response = new JSONObject(client.sendCommand("setReview", params));
        
        if(response.has("error") && response.get("error").equals(""))
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success !");
            alert.setHeaderText("Recenzia postata cu success !");
            alert.setContentText("Recenzia ta a fost postata cu success ! Multumim !");
            alert.showAndWait();
            if(ctrl != null)
                ctrl.getReviews();
            else
                ctrl2.getReviews();
        }
        else if(response.has("error"))
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Eroare !");
            alert.setHeaderText("Recenzia ta nu a putut fi postata !");
            alert.setContentText(response.get("error").toString());
            alert.showAndWait();
        }
        
        Stage oldStage = (Stage) message.getScene().getWindow();
        oldStage.close();
    }
    
}
