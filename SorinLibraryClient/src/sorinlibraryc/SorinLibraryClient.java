/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sorinlibraryc;

import java.io.IOException;
import java.net.UnknownHostException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sorinlibraryc.controllers.LoginController;

/**
 *
 * @author Flagster
 */
public class SorinLibraryClient extends Application {
    
    Client c = null;
    ErrorHandling eh = null;
    
    @Override
    public void start(Stage stage) throws Exception{
        eh = new ErrorHandling();
        try {
            c = new Client();
        } catch (UnknownHostException ex) {
            eh.showError("Eroare necunoscuta !", "Aplicatia a intampinat o eroare la pornire !");
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/sorinlibraryc/views/LoginView.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setTitle("Login - SorinLibrary");
        stage.setScene(scene);
        LoginController controller = loader.getController();
        controller.passConnection(this.c);
        stage.show(); 
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, UnknownHostException, ClassNotFoundException, InterruptedException {
        launch(args);
    }
    
}
