/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sorinlibrary;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import org.json.JSONObject;

/**
 *
 * @author Flagster
 */
public class SorinLibraryServer {

    private static ServerSocket server;
    
    private static int portNumber = 666;
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        try{
            server = new ServerSocket(portNumber);
        }
        catch (IOException e){
            e.printStackTrace();
            System.out.println("Server error");
        }
        
        while(true){
            System.out.println("Waiting for the client request");
            //creating socket and waiting for client connection
            Socket socket = server.accept();
            //read from socket to ObjectInputStream object
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            //convert ObjectInputStream object to String
            String message = (String) ois.readObject();
            System.out.println("Message Received: " + decodeMessage(message));
            //create ObjectOutputStream object
            ois.close();
            socket.close();
        }
    }
    
    private static String decodeMessage(String message)
    {
        JSONObject obj = new JSONObject(message);
        return obj.getString("command");
    }
}
