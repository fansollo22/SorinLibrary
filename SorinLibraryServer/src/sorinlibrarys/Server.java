/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sorinlibrarys;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import org.json.JSONObject;

/**
 *
 * @author Flagster
 */
public class Server {
    private static ServerSocket server;
    private static Socket socket;
    private static int portNumber = 666;
    
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        try{
            server = new ServerSocket(portNumber);
        }
        catch (IOException e){
            e.printStackTrace();
            System.out.println("Server error");
        }
        
        while(true){
            try{
                socket = server.accept();
                ServerThread st=new ServerThread(socket);
                st.start();

            }

            catch(Exception e){
                e.printStackTrace();
                System.out.println("Connection Error");

            }
        }
    }
}
