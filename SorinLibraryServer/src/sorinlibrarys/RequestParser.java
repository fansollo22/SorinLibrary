/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sorinlibrarys;

import java.io.File;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javafx.collections.ObservableList;
import javax.imageio.ImageIO;
import org.json.JSONArray;
import org.json.JSONObject;
import sorinlibrarys.entities.Authors;
import sorinlibrarys.entities.BookAuthors;
import sorinlibrarys.entities.BookCategories;
import sorinlibrarys.entities.Books;
import sorinlibrarys.entities.Categories;
import sorinlibrarys.entities.Reservations;
import sorinlibrarys.entities.Reviews;
import sorinlibrarys.entities.Users;
import sorinlibrarys.queries.AuthorsQuery;
import sorinlibrarys.queries.BooksQuery;
import sorinlibrarys.queries.CategoriesQuery;
import sorinlibrarys.queries.LoginQuery;
import sorinlibrarys.queries.ReservationsQuery;
import sorinlibrarys.queries.ReviewsQuery;

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
            case "setReservation":
                return setReservation(params);
            case "getAuthors":
                return getAuthors();
            case "getCategories":
                return getCategories();
            case "getBookReservations":
                return getBookReservations(params);
            case "getBookReviews":
                return getBookReviews(params);
            case "setReview":
                return setReview(params);
            case "setBook":
                return setBook(params);
            default: 
                return null; 
        }
    }
    
    private String loginUser(JSONObject params)
    {
        String user = params.getString("username");
        String pass = params.getString("password");
        
        LoginQuery lq = new LoginQuery();
        List<Users> listLogin = lq.listLogin();
        
        JSONObject response = new JSONObject();
        
        for (Users u : listLogin) 
        {
            if(user.equals(u.getUsername()))
            {
                if(pass.equals(u.getPassword()))
                {
                    if(u.getIsAdmin() == 1)
                    {
                        response.put("userRole", "admin");
                        response.put("error", "");
                        response.put("notifications", this.userNotifications(u));
                        sh.setSession(u);
                        break;
                    }
                    else
                    {
                        response.put("userRole", "user");
                        response.put("error", "");
                        response.put("notifications", this.userNotifications(u));
                        sh.setSession(u);
                        break;
                    }
                }
                else
                {
                   response.put("userRole", "");
                   response.put("error", "Incorrect password.");
                   response.put("notifications", "");
                }
            }
            else
            {
                response.put("userRole", "");
                response.put("error", "Incorrect username.");
                response.put("notifications", "");
            }
        }
        
        return response.toString();
    }
    
    private String userNotifications(Users userId)
    {
        JSONArray response = new JSONArray();
        ReservationsQuery rq = new ReservationsQuery();
        
        List<Reservations> lr = rq.listReservationsByUserID(userId);
        
        Date now = new Date();

        for(Reservations r : lr)
        {
            Date reservationDate = r.getDate();
            LocalDateTime localDateTime = reservationDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            localDateTime = localDateTime.plusMonths(1);
            reservationDate = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
            if(reservationDate.before(now))
            {
                String notification = "Termenul de imprumut de O LUNA pentru cartea - "+r.getBookId().getName()+" - a expirat si trebuie returnata catre librarie !";
                response.put(notification);
            }
        }
        
        System.out.println(response.toString());
        
        return response.toString();
    }
    
    private String getBooks(JSONObject params)
    {    
        String response = "";
        BooksQuery bq = new BooksQuery();
        String nameF = "";
        Integer authorF = null;
        Integer categoryF = null;
        
        if(params.has("name"))
            nameF = params.getString("name");
        
        if(params.has("author"))
            authorF = params.getInt("author");
        
        if(params.has("category"))
            categoryF = params.getInt("category");
        
        JSONObject obj = new JSONObject();
        List<Books> lb = bq.bookFiltering(nameF,categoryF,authorF);
        JSONArray ja = new JSONArray(lb);
        obj.put("books", ja);
        response = obj.toString();
        
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
        File f = new File(b.getImg());
        try{
           bi = ImageIO.read(f);
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
                ImageIO.write(bi, getFileExtension(f), baos);
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
    
    private String setReservation(JSONObject params)
    {
        JSONObject response = new JSONObject();
        String checkUser = "";
        Integer bookID = null;
        
        if(params.has("user"))
            checkUser = params.get("user").toString();
        
        if(params.has("book"))
            bookID = params.getInt("book");

        if(!sh.getSession().getUsername().equals(checkUser) || bookID == null)
            return new JSONObject().toString();

        BooksQuery bq = new BooksQuery();
        Books book = bq.getBookByID(bookID);
        
        ReservationsQuery rq = new ReservationsQuery();
        boolean b = rq.insertReservation(book, sh.getSession());
        if(b)
           response.put("error", ""); 
        else
           response.put("error", "Rezervarea ta nu a putut fi finalizata. Daca crezi ca aceasta eroare nu ar trebui sa se intample, contacteaza administratorul.");
        
        return response.toString();
    }
    
    private String getAuthors()
    {
        JSONObject obj = new JSONObject();
        AuthorsQuery aq = new AuthorsQuery();
        List<Authors> la = aq.listAllAuthors();
        
        obj.put("authors", la);
        return obj.toString();
    }
    
    private String getCategories()
    {
        JSONObject obj = new JSONObject();
        CategoriesQuery cq = new CategoriesQuery();
        List<Categories> lc = cq.listAllCategories();
        
        obj.put("categories", lc);
        return obj.toString();
    }
    
    private String getBookReservations(JSONObject params)
    {
        JSONObject response = new JSONObject();
        BooksQuery bq = new BooksQuery();
        Books b = bq.getBookByID(params.getInt("book"));
        
        ReservationsQuery rq = new ReservationsQuery();
        List<Reservations> rs = rq.listReservationsByUserANDBook(sh.getSession(), b);
        response.append("reservations", rs);
        
        return response.toString();
    }
    
    private String getBookReviews(JSONObject params)
    {
        JSONObject response = new JSONObject();
        BooksQuery bq = new BooksQuery();
        Books b = bq.getBookByID(params.getInt("book"));
        
        ReviewsQuery rq = new ReviewsQuery();
        List<Reviews> rs = rq.listReviewsByBook(b);
        
        JSONArray js = new JSONArray(rs);
        response.append("reviews", js);
        
        return response.toString();
    }
    
    private String setReview(JSONObject params)
    {
        if(!params.has("sessionID") || !params.get("sessionID").equals(sh.getSession().getUsername()))
        {
            return new JSONObject().toString();
        }
        
        JSONObject response = new JSONObject();
        String checkUser = "";
        Integer bookID = null;
        
        if(params.has("user"))
            checkUser = params.get("user").toString();
        
        if(params.has("book"))
            bookID = params.getInt("book");

        if(!sh.getSession().getUsername().equals(checkUser) || bookID == null)
            return new JSONObject().toString();

        BooksQuery bq = new BooksQuery();
        Books book = bq.getBookByID(bookID);
        
        ReviewsQuery rq = new ReviewsQuery();
        boolean b = rq.insertReview(book, sh.getSession(), params.getString("review"), params.getString("rating"));
        if(b)
           response.put("error", ""); 
        else
           response.put("error", "Recenzia ta nu a putut fi postata. Daca crezi ca aceasta eroare nu ar trebui sa se intample, contacteaza administratorul.");
        
        return response.toString();
    }
    
    private String setBook(JSONObject params)
    {
        JSONObject response = new JSONObject();
        JSONObject book = new JSONObject(params.get("book").toString());
        BooksQuery bq = new BooksQuery();
        
        List<Books> bookExists = bq.checkIfBookExists(book.getString("name"));
        if(!bookExists.isEmpty())
        {
            response.put("error", "Cartea exista deja in baza de date !");
            return response.toString();
        }
        
        String authors = params.getString("authors");
        String categories = params.getString("categories");
        
        Books b = new Books();
        b.setName(book.getString("name"));
        b.setLanguage(book.getString("language"));
        b.setPages(book.getInt("pages"));
        b.setLaunchDate(java.sql.Date.valueOf(book.getString("launchDate")));
        
        if(params.has("img"))
        {
            JSONArray imgArray = new JSONArray(params.get("img").toString());
            String imgExt = params.getString("ext");
            
            byte[] img = new byte[imgArray.length()];
            for(int i=0;i<imgArray.length();i++)
            {
                img[i] = Byte.parseByte(imgArray.get(i).toString());
            }
            
            File folder = new File(System.getProperty("user.dir")+"\\"+"images"+"\\");
            if(!folder.exists())
            {
                try{
                    folder.createNewFile();
                }
                catch(IOException ex){
                    ex.printStackTrace();
                    response.put("warning", "Nu pot creea folder-ul pentru imagini ! Imaginea nu a fost salvata !");
                }
            }
                
            String fileName = new Timestamp(System.currentTimeMillis()).toString().replace(':', '-');
            String filePath = System.getProperty("user.dir")+"\\images\\"+fileName+"."+imgExt;
            Path path = Paths.get(filePath);
            try{
                File imageObj = new File(fileName);
                imageObj.createNewFile();
                Files.write(path, img);
                b.setImg(filePath);
            }
            catch(IOException ex){
                ex.printStackTrace();
                response.put("warning", "Imaginea nu a putut fi incarcata !");
            }
        }

        List<String> auth = Arrays.asList(authors.split("\\s*,\\s*"));
        AuthorsQuery a = new AuthorsQuery();
        List<Authors> allAuthors = a.listAllAuthors();
        
        List<BookAuthors> ba = new ArrayList();
        for(String s : auth)
        {
            Authors keepAuth = null;
            
            for(Authors aDb : allAuthors)
            {
                if(s.toLowerCase().trim().equals(aDb.getName().toLowerCase().trim()))
                {
                    keepAuth = aDb;
                    break;
                } 
            }
            
            if(keepAuth != null)
            {
                BookAuthors bookAuth = new BookAuthors();
                bookAuth.setAuthorId(keepAuth);
                bookAuth.setBookId(b);
                ba.add(bookAuth);
            }
            else
            {
                Authors aut = a.insertAuthors(s);
                if(aut != null)
                {
                    BookAuthors bookAuth = new BookAuthors();
                    bookAuth.setAuthorId(aut);
                    bookAuth.setBookId(b);
                    ba.add(bookAuth);
                }
            }
        }
        
        List<String> cats = Arrays.asList(categories.split("\\s*,\\s*"));
        CategoriesQuery c = new CategoriesQuery();
        List<Categories> allCategories = c.listAllCategories();
        
        List<BookCategories> ca = new ArrayList();
        for(String s : cats)
        {
            Categories keepCat = null;
            
            for(Categories cDb : allCategories)
            {
                System.out.println(s.toLowerCase().trim() + " = " + cDb.getCategory().toLowerCase().trim());
                if(s.toLowerCase().trim().equals(cDb.getCategory().toLowerCase().trim()))
                {
                    keepCat = cDb;
                    break;
                }
            }
            if(keepCat != null)
            {
                BookCategories bookCat = new BookCategories();
                bookCat.setCategoryId(keepCat);
                bookCat.setBookId(b);
                ca.add(bookCat);
            }
            else
            {
                Categories cat = c.insertCategories(s);
                if(cat != null)
                {
                    BookCategories bookCat = new BookCategories();
                    bookCat.setCategoryId(cat);
                    bookCat.setBookId(b);
                    ca.add(bookCat);
                }     
            }
        }
        
        b.setBookAuthorsCollection(ba);
        b.setBookCategoriesCollection(ca);
        
        if(bq.insertBook(b) != null)
            response.put("error", "");
        else
            response.put("error", "Nu am putut insera cartea in baza de date.");
        
        return response.toString();
    }
    
    private String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return ""; // empty extension
        }
        return name.substring(lastIndexOf+1);
    }
}
