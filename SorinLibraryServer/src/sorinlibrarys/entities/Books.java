/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sorinlibrarys.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Flagster
 */
@Entity
@Table(name = "books")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Books.findAll", query = "SELECT b FROM Books b")
    , @NamedQuery(name = "Books.findById", query = "SELECT b FROM Books b WHERE b.id = :id")
    , @NamedQuery(name = "Books.findByName", query = "SELECT b FROM Books b WHERE b.name = :name")
    , @NamedQuery(name = "Books.findByQuantity", query = "SELECT b FROM Books b WHERE b.quantity = :quantity")
    , @NamedQuery(name = "Books.findByLanguage", query = "SELECT b FROM Books b WHERE b.language = :language")
    , @NamedQuery(name = "Books.findByPages", query = "SELECT b FROM Books b WHERE b.pages = :pages")
    , @NamedQuery(name = "Books.findByLaunchDate", query = "SELECT b FROM Books b WHERE b.launchDate = :launchDate")
    , @NamedQuery(name = "Books.findByImg", query = "SELECT b FROM Books b WHERE b.img = :img")})
public class Books implements Serializable {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "bookId")
    private Collection<Reservations> reservationsCollection;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Column(name = "quantity")
    private Integer quantity;
    @Basic(optional = false)
    @Column(name = "language")
    private String language;
    @Basic(optional = false)
    @Column(name = "pages")
    private int pages;
    @Basic(optional = false)
    @Column(name = "launch_date")
    @Temporal(TemporalType.DATE)
    private Date launchDate;
    @Column(name = "img")
    private String img;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "bookId", fetch=FetchType.EAGER)
    private Collection<BookAuthors> bookAuthorsCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "bookId", fetch=FetchType.EAGER)
    private Collection<BookCategories> bookCategoriesCollection;

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

    @XmlTransient
    public String getBookAuthorsCollection() {
        
        String s = "";
        int counter = 0;
        
        for (BookAuthors ba : bookAuthorsCollection) {
            Authors a = ba.getAuthorId();
            if(counter == 0)
                s += a.getName();
            else
                s += ","+a.getName();
            counter++;
        }
        if(s == "")
            s = "Nici-un autor setat.";
        return s;
    }

    public void setBookAuthorsCollection(Collection<BookAuthors> bookAuthorsCollection) {
        this.bookAuthorsCollection = bookAuthorsCollection;
    }

    @XmlTransient
    public String getBookCategoriesCollection() {
        String s = "";
        int counter = 0;
        
        for (BookCategories bc : bookCategoriesCollection) {
            Categories a = bc.getCategoryId();
            if(counter == 0)
                s += a.getCategory();
            else
                s += ","+a.getCategory();
            counter++;
        }
        if(s == "")
            s = "Nici-o categorie setata.";
        return s;
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

    @XmlTransient
    public String getReservationsCollection() {
        return "";
    }

    public void setReservationsCollection(Collection<Reservations> reservationsCollection) {
        this.reservationsCollection = reservationsCollection;
    } 
}
