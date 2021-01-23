/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sorinlibrarys.queries;

import sorinlibrarys.entities.Authors;
import sorinlibrarys.entities.Books;
import sorinlibrarys.entities.Categories;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Flagster
 */
public class BooksQuery {
    EntityManager em;
    EntityManagerFactory emf;

    public BooksQuery() {
        
        emf = Persistence.createEntityManagerFactory("SorinLibraryServerPU");
        em = emf.createEntityManager();
        
        em.getTransaction().begin();
    }
    
    public Books getBookByID(int ID) {
       return em.createNamedQuery("Books.findById",Books.class).setParameter("id", ID).getSingleResult();
    }
    
    public List<Books> listAllBooks() {
        return em.createNamedQuery("Books.findAll",Books.class).getResultList();
    }
    
    public void reduceBookStock(Books b) {
        b.setQuantity(b.getQuantity()-1);
        em.merge(b);
        em.getTransaction().commit();
    }
    
    public List<Books> bookFiltering(String name, Integer cat, Integer auth){
        String query = "";
        System.out.println(name=="");
        if((name == "" || name.trim().isEmpty() || name == null) && (cat != null || auth != null))
             query += "SELECT b FROM Books b WHERE";
        else if(cat == null && auth == null)
        {
            if(name != "" && !name.trim().isEmpty() && name != null)
                query += "SELECT b FROM Books b WHERE b.name LIKE '%"+name+"%'";
            else  
                query += "SELECT b FROM Books b";
        }  
        else
             query += "SELECT b FROM Books b WHERE b.name LIKE '%"+name+"%' OR";
       
        if(cat != null)
            query += " b.id IN (SELECT bc.bookId.id FROM BookCategories bc WHERE bc.categoryId.id ="+String.valueOf(cat)+")";
        
        if(auth != null && cat != null)
            query += " AND b.id IN (SELECT ba.bookId.id FROM BookAuthors ba WHERE ba.authorId.id ="+String.valueOf(auth)+")";
        else if(auth != null)
            query += " b.id IN (SELECT ba.bookId.id FROM BookAuthors ba WHERE ba.authorId.id ="+String.valueOf(auth)+")";
        System.out.println(query);
        return em.createQuery(query, Books.class).getResultList();
    }
}
