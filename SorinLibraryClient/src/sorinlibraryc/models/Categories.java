/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sorinlibraryc.models;

import java.io.Serializable;
import java.util.Collection;
import org.json.JSONObject;

/**
 *
 * @author Flagster
 */
public class Categories implements Serializable {

    private Integer id;
    private String category;
    private Collection<BookCategories> bookCategoriesCollection;

    public Categories() {
    }

    public Categories(Integer id) {
        this.id = id;
    }

    public Categories(Integer id, String category) {
        this.id = id;
        this.category = category;
    }
    
    public Categories(JSONObject obj) {
        this.id = obj.getInt("id");
        this.category = obj.get("category").toString();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    
    public Collection<BookCategories> getBookCategoriesCollection() {
        return bookCategoriesCollection;
    }

    public void setBookCategoriesCollection(Collection<BookCategories> bookCategoriesCollection) {
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
        if (!(object instanceof Categories)) {
            return false;
        }
        Categories other = (Categories) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return category;
    }
    
}
