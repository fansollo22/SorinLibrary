/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sorinlibraryc.models;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import org.json.JSONObject;

/**
 *
 * @author Flagster
 */
public class Books implements Serializable {

    private Collection<Reservations> reservationsCollection;
    private Integer id;
    private String name;
    private Integer quantity;
    private String language;
    private int pages;
    private Date launchDate;
    private String img;
    private String bookAuthorsCollection;
    private String bookCategoriesCollection;

    public Books() {
    }

    public Books(Integer id) {
        this.id = id;
    }

    public Books(Integer id, String name, String language, int pages, Date launchDate) {
        this.id = id;
        this.name = name;
        this.language = language;
        this.pages = pages;
        this.launchDate = launchDate;
    }
    
    public Books(JSONObject obj) {
        this.id = Integer.parseInt(obj.get("id").toString());
        this.name = obj.get("name").toString();
        this.language = obj.get("language").toString();
        this.quantity = Integer.parseInt(obj.get("quantity").toString());
        this.pages = Integer.parseInt(obj.get("pages").toString());
        String d = obj.get("launchDate").toString();
        DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
        Date dt;
        try{
           dt = formatter.parse(d); 
        }
        catch (Exception ex)
        {
            dt = new Date();
        }
        
        this.launchDate = dt;
        if(obj.has("img"))
            this.img = obj.get("img").toString();
        this.bookAuthorsCollection = obj.get("bookAuthorsCollection").toString();
        this.bookCategoriesCollection = obj.get("bookCategoriesCollection").toString();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public Date getLaunchDate() {
        return launchDate;
    }

    public void setLaunchDate(Date launchDate) {
        this.launchDate = launchDate;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getBookAuthorsCollection() {
        return bookAuthorsCollection;
    }

    public void setBookAuthorsCollection(String bookAuthorsCollection) {
        this.bookAuthorsCollection = bookAuthorsCollection;
    }

    public String getBookCategoriesCollection() {
        return bookCategoriesCollection;
    }

    public void setBookCategoriesCollection(String bookCategoriesCollection) {
        this.bookCategoriesCollection = bookCategoriesCollection;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Books)) {
            return false;
        }
        Books other = (Books) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entities.Books[ id=" + id + " ]";
    }
    
    public String getReservationsCollection() {
        return "";
    }

    public void setReservationsCollection(Collection<Reservations> reservationsCollection) {
        this.reservationsCollection = reservationsCollection;
    }
    
}
