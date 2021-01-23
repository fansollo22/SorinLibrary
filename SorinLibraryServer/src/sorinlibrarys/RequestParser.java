/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sorinlibrarys;

import java.io.File;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;
import javax.imageio.ImageIO;
import org.json.JSONArray;
import org.json.JSONObject;
import sorinlibrarys.entities.Authors;
import sorinlibrarys.entities.Books;
import sorinlibrarys.entities.Categories;
import sorinlibrarys.entities.Users;
import sorinlibrarys.queries.AuthorsQuery;
import sorinlibrarys.queries.BooksQuery;
import sorinlibrarys.queries.CategoriesQuery;
import sorinlibrarys.queries.LoginQuery;

/**
 *
 * @author Flagster
 */
public class RequestParser {
    
    private ServerThread sh;
    
    public RequestParser(ServerThread s)
    {
        sh = s;
    }
    
    public String parse(String req)
    {
        JSONObject obj = new JSONObject(req);
        String command = obj.getString("command");
        JSONObject params = obj.getJSONObject("params");
        
        switch(command) 
        { 
            case "login": 
                return loginUser(params);
            case "getBooks": 
                return getBooks(params); 
            case "getBookImg": 
                return getBookImage(params);
            case "initBookView": 
                return initBookView();
            default: 
                return null; 
        }
    }
    
    private String loginUser(JSONObject params)
    {
        String user = params.getString("username");
        String pass = params.getString("password");
        
        System.out.println(user);
        System.out.println(pass);
        
        LoginQuery lq = new LoginQuery();
        List<Users> listLogin = lq.listLogin();
        
        JSONObject response = new JSONObject();
        
        for (Users u : listLogin) {
            if(user.equals(u.getUsername()))
            {
                if(pass.equals(u.getPassword()))
                {
                    if(u.getIsAdmin() == 1)
                    {
                        response.put("userRole", "admin");
                        response.put("error", "");
                        sh.setSession(u.getUsername());
                        break;
                    }
                    else
                    {
                        response.put("userRole", "user");
                        response.put("error", "");
                        sh.setSession(u.getUsername());
                        break;
                    }
                }
                else
                {
                   response.put("userRole", "");
                   response.put("error", "Incorrect password.");
                }
            }
            else
            {
                response.put("userRole", "");
                response.put("error", "Incorrect username.");
            }
        }
        
        return response.toString();
    }
    
    private String getBooks(JSONObject params)
    {
        String response = "";
        BooksQuery bq = new BooksQuery();
        
//        if(!params.get("session").equals(sh.getSession())){
//            return "";
//        }
//        
//        params.remove("session");
        
        if(params.isEmpty())
        {
            JSONObject obj = new JSONObject();
            List<Books> lb = bq.listAllBooks();
            JSONArray ja = new JSONArray(lb);
            obj.put("books", ja);
            response = obj.toString();
        }
        
        return response;
    }
    
    private String getBookImage(JSONObject params)
    {
        JSONObject response = new JSONObject();
        
        BooksQuery bq = new BooksQuery();
        Books b = bq.getBookByID(params.getInt("bookID"));
        if(b.getImg() == null)
        {
            response.put("error","");
            response.put("img","");
            return response.toString();
        }
        
        BufferedImage bi = null;
        
        try{
           bi = ImageIO.read(new File(b.getImg()));
        }
        catch (IOException ex)
        {
            response.put("img", "");
            response.put("error", "Error opening image file.");
        }

        if(bi != null)
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try{
                ImageIO.write(bi, "jpg", baos);
            }
            catch (IOException ex)
            {
                response.put("img", "");
                response.put("error", "Error opening image file.");
            }
            
            byte[] bytes = baos.toByteArray();
            if(bytes.length > 0)
            {
                response.put("img", bytes);
                response.put("error", "");
            }
        }
        
        return response.toString();
    }
    
    private String initBookView()
    {
        String response = "";
        BooksQuery bq = new BooksQuery();
        
        JSONObject obj = new JSONObject();
        List<Books> lb = bq.listAllBooks();
        JSONArray ja = new JSONArray(lb);
        obj.put("books", ja);
        
        AuthorsQuery aq = new AuthorsQuery();
        List<Authors> la = aq.listAllAuthors();
        ja = new JSONArray(la);
        obj.put("authors", ja);
        
        CategoriesQuery cq = new CategoriesQuery();
        List<Categories> lc = cq.listAllCategories();
        ja = new JSONArray(lc);
        obj.put("categories", ja);
        
        response = obj.toString();
        return response;
    }
}
