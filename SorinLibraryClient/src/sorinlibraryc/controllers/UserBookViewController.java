/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sorinlibraryc.controllers;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.imageio.ImageIO;
import org.json.JSONArray;
import org.json.JSONObject;
import sorinlibraryc.Client;
import sorinlibraryc.ErrorHandling;
import sorinlibraryc.models.Books;

/**
 *
 * @author Flagster
 */
public class UserBookViewController implements Initializable {

    Books book;
    
    TableView tb;
    
    @FXML
    private TextField title;
    
    @FXML
    private TextField author;
    
    @FXML
    private TextField launchDate;
    
    @FXML
    private TextField language;
    
    @FXML
    private TextField pages;
    
    @FXML
    private TextArea categories;
    
    @FXML
    private ImageView img;
    
    Client client = null;
    
    ErrorHandling eh = null;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    public void passParameters(Books book, Client c) {
        this.eh = new ErrorHandling();
        
        this.client = c;
        this.book = book;
        this.title.setText(book.getName());
        this.launchDate.setText(book.getLaunchDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString());
        this.author.setText(book.getBookAuthorsCollection());
        this.categories.setText(book.getBookCategoriesCollection());
        this.language.setText(book.getLanguage());
        this.pages.setText(String.valueOf(book.getPages()));
        
        JSONObject params = new JSONObject();
        params.put("bookID", book.getId());
        JSONObject response = new JSONObject(client.sendCommand("getBookImg", params));
        
        if(response.get("error").equals("") && response.get("img").equals(""))
            return;
        
        if(response.get("error").equals(""))
        {
            JSONArray obj = new JSONArray(response.get("img").toString());
            byte[] img = new byte[obj.length()];
            for(int i=0;i<obj.length();i++)
            {
                img[i] = Byte.parseByte(obj.get(i).toString());
            }
            
            InputStream is = new ByteArrayInputStream(img);
            BufferedImage bi = null;
            
            try{
                bi = ImageIO.read(is);
            }
            catch(IOException ex)
            {
                eh.showError("Error opening image file.", "Error opening image file.");
            }
            
            if(bi != null)
            {
                Image image = SwingFXUtils.toFXImage(bi, null);
                this.img.setImage(image);
            }
        }
    }

    public TableView getTb() {
        return tb;
    }

    public void setTb(TableView tb) {
        this.tb = tb;
    }
    
    @FXML
    private void reserveBook(){
        
        JSONObject params = new JSONObject();
        String user = client.getSession();
        
        params.put("user", user);
        params.put("book", book.getId());
        
        JSONObject response = new JSONObject(client.sendCommand("setReservation", params));
        System.out.println(response.toString());
        if(response.has("error") && response.get("error").equals(""))
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success !");
            alert.setHeaderText("Reservarea efectuata cu success !");
            alert.setContentText("Rezervarea ta a fost efectuata cu success ! Puteti sa va prezentati la librarie pentru a ridica cartea.");
            alert.showAndWait();
            tb.refresh();         
        }
        else if(response.has("error"))
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Eroare !");
            alert.setHeaderText("Reservarea ta nu a putut fi finalizata !");
            alert.setContentText(response.get("error").toString());
            alert.showAndWait();
            tb.refresh();
        }
    }
}
