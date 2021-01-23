/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sorinlibraryc.models;

import java.io.Serializable;
import java.util.Collection;
import javax.xml.bind.annotation.XmlTransient;
import org.json.JSONObject;

/**
 *
 * @author Flagster
 */
public class Authors implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer id;
    private String name;
    private Collection<BookAuthors> bookAuthorsCollection;

    public Authors() {
    }

    public Authors(Integer id) {
        this.id = id;
    }

    public Authors(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
    
    public Authors(JSONObject obj) {
        this.id = Integer.parseInt(obj.get("id").toString());
        this.name = obj.get("name").toString();
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

    @XmlTransient
    public Collection<BookAuthors> getBookAuthorsCollection() {
        return bookAuthorsCollection;
    }

    public void setBookAuthorsCollection(Collection<BookAuthors> bookAuthorsCollection) {
        this.bookAuthorsCollection = bookAuthorsCollection;
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
        if (!(object instanceof Authors)) {
            return false;
        }
        Authors other = (Authors) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name;
    }
}