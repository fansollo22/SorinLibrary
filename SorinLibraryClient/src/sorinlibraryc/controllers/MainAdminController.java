/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sorinlibraryc.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import sorinlibraryc.Client;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableRow;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;
import sorinlibraryc.ErrorHandling;
import sorinlibraryc.models.Authors;
import sorinlibraryc.models.Books;
import sorinlibraryc.models.Categories;
import sorinlibraryc.models.Reservations;

/**
 *
 * @author Flagster
 */
public class MainAdminController implements Initializable {
    
    @FXML 
    private TableView<Books> tabView;
    
    @FXML
    private TableColumn<Books, Integer> colID;
    
    @FXML
    private TableColumn<Books, String> colName;
    
    @FXML
    private TableColumn<Books, String> colAuthors;
    
    @FXML
    private TableColumn<Books, String> colCategories;
    
    @FXML
    private ComboBox catFilter;
    
    @FXML
    private ComboBox autFilter;
    
    @FXML
    private TextField nameFilter;
    
    Client client = null;
    
    ErrorHandling eh;
    
    JSONArray notifications = null;
    
    MainAdminController thisController;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        initColumns();
        initRowEvent();
    }
    
    @FXML
    public void filterBooks()
    {
        JSONObject params = new JSONObject();
        if(!catFilter.getSelectionModel().isEmpty())
        {
            Categories c = (Categories)catFilter.getValue();
            params.put("category", c.getId());
        }
            
        if(!autFilter.getSelectionModel().isEmpty())
        {
            Authors a = (Authors)autFilter.getValue();
            params.put("author", a.getId());
        }
        
        if(!nameFilter.getText().trim().isEmpty())
            params.put("name", nameFilter.getText());
        
        JSONObject response = new JSONObject(client.sendCommand("getBooks", params));
        JSONArray a = new JSONArray(response.get("books").toString());
        ObservableList<Books> data = FXCollections.observableArrayList();
        for(int i = 0; i < a.length(); i++)
        {
              JSONObject obj = a.getJSONObject(i);
              Books b = new Books(obj);
              data.add(b);
        }
        tabView.setItems(data);
    }
    
    @FXML
    public void resetFilters()
    {
        JSONObject params = new JSONObject();
        JSONObject response = new JSONObject(client.sendCommand("getBooks", params));
        JSONArray a = new JSONArray(response.get("books").toString());
        ObservableList<Books> data = FXCollections.observableArrayList();
        for(int i = 0; i < a.length(); i++)
        {
              JSONObject obj = a.getJSONObject(i);
              Books b = new Books(obj);
              data.add(b);
        }
        tabView.setItems(data); 
        
        nameFilter.setText("");
        catFilter.valueProperty().set(null);
        autFilter.valueProperty().set(null);
    }
    
    private void showNotifications()
    {
        if(!this.notifications.isEmpty())
        {
            for(Integer i = 0;i<notifications.length();i++)
            {
                String s = notifications.getString(i);
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Atentie !");
                alert.setHeaderText(s);
                alert.showAndWait();
            }
        }
    }
    
    private void initColumns()
    {
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAuthors.setCellValueFactory(new PropertyValueFactory<>("bookAuthorsCollection"));
        colCategories.setCellValueFactory(new PropertyValueFactory<>("bookCategoriesCollection"));
    }
    
    private void initTable()
    {
        JSONObject params = new JSONObject();
        JSONObject response = new JSONObject(client.sendCommand("initBookView", params));
        JSONArray books = new JSONArray(response.get("books").toString());
        ObservableList<Books> dataBooks = FXCollections.observableArrayList();
        for(int i = 0; i < books.length(); i++)
        {
              JSONObject obj = books.getJSONObject(i);
              Books b = new Books(obj);
              dataBooks.add(b);
        }
        tabView.setItems(dataBooks);
        
        JSONArray authors = new JSONArray(response.get("authors").toString());
        ObservableList<Authors> dataAuth = FXCollections.observableArrayList();
        for(int i = 0; i < authors.length(); i++)
        {
            JSONObject obj = authors.getJSONObject(i);
            Authors auth = new Authors(obj);
            dataAuth.add(auth);
        }
        autFilter.setItems(dataAuth);
        
        JSONArray categories = new JSONArray(response.get("categories").toString());
        ObservableList<Categories> dataCat = FXCollections.observableArrayList();
        for(int i = 0; i < categories.length(); i++)
        {
            JSONObject obj = categories.getJSONObject(i);
            Categories cat = new Categories(obj);
            dataCat.add(cat);
        }
        catFilter.setItems(dataCat);
    }
    
    private void initRowEvent()
    {
        tabView.setRowFactory( tv -> {
            TableRow<Books> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    try {
                        Books rowData = row.getItem();
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/sorinlibraryc/views/AdminBookView.fxml"));
                        Parent parent = loader.load();
                        AdminBookViewController controller = loader.getController();
                        controller.passParameters(rowData, client, controller);
                        controller.setTb(tabView);
                        Scene scene = new Scene(parent);
                        Stage stage = new Stage();
                        stage.setTitle(rowData.getName());
                        stage.setScene(scene);
                        stage.show();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        eh.showError("Eroare afisare carte !", "O eroare a fost intampinata in incercarea de a afisa cartea !");
                    }
                    
                }
            });
            return row ;
        });
    }
    
    @FXML
    private void addBookView() throws IOException
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/sorinlibraryc/views/AddBookView.fxml"));
        Parent parent = loader.load();
        AddBookViewController controller = loader.getController();
        controller.passParameters(client, thisController);
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setTitle("Adauga carte");
        stage.setScene(scene);
        stage.show();
    }
    
    public void passConnection(Client c, JSONArray j, MainAdminController ctrl)
    {
        client = c;
        notifications = j;
        thisController = ctrl; 
        showNotifications();
        initTable();
        eh = new ErrorHandling();
    }
}
