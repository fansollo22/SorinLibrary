/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sorinlibrarys;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import sorinlibrarys.entities.Users;

/**
 *
 * @author Flagster
 */
public class ServerThread extends Thread {
    Socket s;
    String line;
    BufferedReader is;
    PrintWriter os;
    
    RequestParser rp;
    private Users session;
    
    public ServerThread(Socket s){
        this.s=s;
        this.rp = new RequestParser(this);
    }

    @Override
    public void run() {
        try
        {
            is= new BufferedReader(new InputStreamReader(s.getInputStream()));
            os= new PrintWriter(s.getOutputStream());

        }
        catch(IOException e)
        {
            System.out.println("IO error in server thread");
        }
        
        try 
        {
            String request = is.readLine();
            line = decodeMessage(request);
            while(true){
                line = decodeMessage(request);
                os.println(line);
                os.flush();
                request = is.readLine();
            }
        } 
        catch (IOException e) 
        {
            line = this.getName(); //reused String line for getting thread name
            System.out.println("IO Error/ Client "+line+" terminated abruptly");
        }
        catch(NullPointerException e)
        {
            line = this.getName(); //reused String line for getting thread name
            System.out.println("Client "+line+" Closed");
        }
        finally
        {    
            try
            {
                if (is != null){
                    is.close(); 
                }
                if(os != null){
                    os.close();
                }
                if (s != null){
                    s.close();
                }
            }
            catch(IOException ie)
            {
                System.out.println("Socket Close Error");
            }
        }//end finally
    }
    
    private String decodeMessage(String message)
    {
        String response = null;
        if(message != null)
            response = rp.parse(message);
        return response;
    }
    
    public Users getSession()
    {
        return this.session;
    }
    
    public void setSession(Users ses)
    {
        this.session = ses;
    }
}
