/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sorinlibraryc.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;
import sorinlibraryc.Client;

/**
 *
 * @author Flagster
 */
public class LoginController {
    
    @FXML
    private TextField username;
    
    @FXML
    private PasswordField password;
    
    @FXML
    private Label message;
    
    Client client = null;
   
    
    public void passConnection(Client c)
    {
        this.client = c;
    }
    
    @FXML
    public void login()  throws IOException
    {
        JSONObject params = new JSONObject();
        
        if(username.getText().equals("") || password.getText().equals(""))
        {
            message.setText("Username-ul sau parola nu pot fi goale !");
            return;
        }
        
        params.put("username", username.getText());
        params.put("password", password.getText());
        JSONObject response = new JSONObject(client.sendCommand("login", params));
        String error = response.get("error").toString();
        String answer = response.get("userRole").toString();
        JSONArray notifications = new JSONArray(response.get("notifications").toString());
        
        if(error.equals(""))
        {
            client.setSession(username.getText());
            
            if(answer.equals("user"))
            {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/sorinlibraryc/views/MainUser.fxml"));
                Scene scene = new Scene(loader.load());
                Stage stage = new Stage();
                stage.setTitle("User - SorinLibrary");
                stage.setScene(scene);
                MainUserController controller = loader.getController();
                controller.passConnection(this.client, notifications);
                stage.show(); 
                Stage oldStage = (Stage) message.getScene().getWindow();
                oldStage.close();
            }
            else if(answer.equals("admin"))
            {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/sorinlibraryc/views/MainAdmin.fxml"));
                Scene scene = new Scene(loader.load());
                Stage stage = new Stage();
                stage.setTitle("User - SorinLibrary");
                stage.setScene(scene);
                MainAdminController controller = loader.getController();
                controller.passConnection(this.client, notifications);
                stage.show(); 
                Stage oldStage = (Stage) message.getScene().getWindow();
                oldStage.close();
            }
        }
        else
        {
            message.setText(error);
        }
    }
}
