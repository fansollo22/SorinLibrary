/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sorinlibraryc.controllers;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import org.json.JSONObject;
import sorinlibraryc.Client;
import sorinlibraryc.models.Books;

/**
 * FXML Controller class
 *
 * @author Flagster
 */
public class AddBookViewController implements Initializable {

    @FXML
    TextField bookTitle;
    
    @FXML
    TextField language;
    
    @FXML
    TextField pages;
    
    @FXML
    DatePicker launchDate;
    
    @FXML
    TextArea authors;
    
    @FXML
    TextArea categories;
    
    @FXML
    ImageView img;
    
    Client client;
    
    URI imgURI = null;
    
    String imgExt = null;
    
    MainAdminController ctrl;
       
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        pages.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, 
                String newValue) {
                if (!newValue.matches("\\d*")) {
                    pages.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }
    
    @FXML
    private void uploadCover()
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        FileChooser.ExtensionFilter fileExtensions = new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg");
        fileChooser.getExtensionFilters().add(fileExtensions);
        File file = fileChooser.showOpenDialog(categories.getScene().getWindow());
        if(file != null)
        {
            imgURI = file.toURI();
            imgExt = getFileExtension(file);
            Image image = new Image(file.toURI().toString());
            img.setImage(image);
        }
    }
    
    @FXML
    private void sendBook()
    {
        JSONObject params = new JSONObject();
        
        if(bookTitle.getText().trim().equals("") || language.getText().trim().equals("") || pages.getText().trim().equals(""))
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Eroare !");
            alert.setHeaderText("Campurile nu pot fi goale !");
            alert.setContentText("");
            alert.showAndWait();
            return;
        }
        
        JSONObject b = new JSONObject();
        b.put("name", bookTitle.getText());
        b.put("language", language.getText());
        b.put("pages", Integer.parseInt(pages.getText()));
        b.put("launchDate", java.sql.Date.valueOf(launchDate.getValue()));
        
        if(imgURI != null)
        {
            BufferedImage bi = null;
            try
            {
               bi = ImageIO.read(new File(imgURI));
            }
            catch (IOException ex)
            {
                params.put("img", "");
            }

            if(bi != null)
            {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                try
                {
                    ImageIO.write(bi, imgExt, baos);
                }
                catch (IOException ex)
                {
                    params.put("img", "");
                }

                byte[] bytes = baos.toByteArray();
                if(bytes.length > 0)
                {
                    params.put("img", bytes);
                    params.put("ext", imgExt);
                }
            }
        }

        params.put("book",b);
        params.put("authors", authors.getText());
        params.put("categories", categories.getText());
        JSONObject response = new JSONObject(client.sendCommand("setBook", params));
        if(response.get("error").equals("") && !response.has("warning"))
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success !");
            alert.setHeaderText("Cartea a fost adaugata cu succes !");
            alert.setContentText("Cartea impreuna cu toate datele acesteia au fost salvate cu success in baza de date !");
            alert.showAndWait();
            ctrl.filterBooks();
            Stage oldStage = (Stage) img.getScene().getWindow();
            oldStage.close();
        }
        else if(response.has("warning"))
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Atentie !");
            alert.setHeaderText("Coperta cartii nu poate fi inserata !");
            alert.setContentText(response.getString("warning"));
            alert.showAndWait();
            ctrl.filterBooks();
            Stage oldStage = (Stage) img.getScene().getWindow();
            oldStage.close();
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Eroare !");
            alert.setHeaderText("Cartea nu a putut fi adaugata !");
            alert.setContentText(response.getString("error"));
            alert.showAndWait();
        }
    }
    
    
    public void passParameters(Client c, MainAdminController ctrl){
        this.client = c;
        this.ctrl = ctrl;
    }
    
    private String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) 
        {
            return ""; // empty extension
        }
        return name.substring(lastIndexOf+1);
    }
}
