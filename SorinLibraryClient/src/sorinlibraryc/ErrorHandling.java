/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sorinlibraryc;

import javafx.application.Platform;
import javafx.scene.control.Alert;

/**
 *
 * @author Flagster
 */
public class ErrorHandling {
    
    
    public void showError(String title,String message)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Eroare !");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
        Platform.exit();
        System.exit(0);
    }
}
