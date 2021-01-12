/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sorinilie;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Flagster
 */
public class ClientClass {

    private static String ip = "localhost";
    
    private static int port = 666;
    
    public ClientClass() throws UnknownHostException, IOException, ClassNotFoundException, InterruptedException{
        
        InetAddress host = InetAddress.getByName(ip);
        
        Socket socket = null;
        ObjectOutputStream oos = null;
        Integer i = 0;
        while(true)
        {
            System.out.println("Connecting to server...");
            socket = new Socket(host.getHostName(), port);
            System.out.println("Connected");
            oos = new ObjectOutputStream(socket.getOutputStream());
            JSONObject obj = new JSONObject();
            obj.put("command",i.toString());
            i++;
            System.out.println(obj.toString());
            oos.writeObject(obj.toString());
            oos.close();
            Thread.sleep(100);
        }
    }
}
