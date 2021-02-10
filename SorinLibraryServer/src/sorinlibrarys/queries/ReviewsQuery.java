/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sorinlibrarys.queries;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import sorinlibrarys.entities.Books;
import sorinlibrarys.entities.Reviews;
import sorinlibrarys.entities.Users;

/**
 *
 * @author Flagster
 */
public class ReviewsQuery {
    EntityManager em;
    EntityManagerFactory emf;

    public ReviewsQuery() {
        
        emf = Persistence.createEntityManagerFactory("SorinLibraryServerPU");
        em = emf.createEntityManager();
        
        em.getTransaction().begin();
    }
    
    public List<Reviews> listReviews() {
        return em.createNamedQuery("Reviews.findAll",Reviews.class).getResultList();
    }
    
    public List<Reviews> listReviewsByBook(Books book) {
        return em.createNamedQuery("Reviews.findByBook", Reviews.class).setParameter("bookid", book).getResultList();
    }
    
    public boolean insertReview(Books book, Users user, String review, String rating){
      try {
        Reviews rs = new Reviews();
        Integer rat = Integer.parseInt(rating);
        rs.setBookid(book);
        rs.setRating(rat);
        rs.setReview(review);
        rs.setUserid(user);
        em.persist(rs);
        em.getTransaction().commit();
        
        return true;
      }
      catch(Exception e) {
        System.out.println(e.toString());
        return false;
      } 
    }
}
