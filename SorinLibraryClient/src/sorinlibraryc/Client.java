/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sorinlibraryc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Flagster
 */
public class Client {

    private static String ip = "localhost";
    
    private static int port = 666;
    
    private String session;
    
    InetAddress address;
    Socket s1 = null;
    String line = "";
    BufferedReader is = null;
    PrintWriter os = null;
    ErrorHandling eh = null;
    
    public Client() throws UnknownHostException
    {
        eh = new ErrorHandling();
        serverConnect();
    }
    
    private void serverConnect() throws UnknownHostException
    {
        address = InetAddress.getByName(ip);

        try 
        {
            s1=new Socket(address, port); // You can use static final constant PORT_NUM
            is=new BufferedReader(new InputStreamReader(s1.getInputStream()));
            os= new PrintWriter(s1.getOutputStream());
        }
        catch (IOException e)
        {
            eh.showError("Conexiunea cu serverul nu a reusit !", "Conexiunea cu serverul nu a avut succes ! Va rugam, verificati conexiunea la internet !");
        }
    }
    
    public String sendCommand(String command, JSONObject params)
    {
        String response = null;
        JSONObject json = new JSONObject();
        params.put("sessionID", session);
        json.put("command",command);
        json.put("params",params);
        try
        {
            line = json.toString();
            os.println(line);
            os.flush();
            response = is.readLine();
        }
        catch(IOException e)
        {
            eh.showError("Conexiunea cu serverul nu a reusit !", "Conexiunea cu serverul nu a avut succes ! Va rugam, verificati conexiunea la internet !");
        }
        
        return response;
    }
    
    public void closeConnection()
    {
        try
        {
            if (is != null)
                is.close(); 

            if(os != null)
                os.close();
            
            if (s1 != null)
                s1.close();
        }
        catch(IOException e)
        {
            eh.showError("Inchiderea conexiunii nu a reusit !", "Inchiderea conexiunii cu serverul nu a reusit !");
        }
    }
    
    public void setSession(String s)
    {
        session = s;
    }
    
    public String getSession()
    {
        return this.session;
    }
}
