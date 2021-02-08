/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sorinlibrarys.queries;

import java.text.SimpleDateFormat;
import java.util.Date;
import sorinlibrarys.entities.Books;
import sorinlibrarys.entities.Reservations;
import sorinlibrarys.entities.Users;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Flagster
 */
public class ReservationsQuery {
    EntityManager em;
    EntityManagerFactory emf;

    public ReservationsQuery() {
        
        emf = Persistence.createEntityManagerFactory("SorinLibraryServerPU");
        em = emf.createEntityManager();
        
        em.getTransaction().begin();
    }
    
    public List<Reservations> listReservations() {
        return em.createNamedQuery("Reservations.findAll",Reservations.class).getResultList();
    }
    
    public List<Reservations> listReservationsByUserID(Users userID){
        return em.createNamedQuery("Reservations.findByUserID", Reservations.class).setParameter("userId", userID).getResultList();
    }
    
    public List<Reservations> listReservationsByUserANDBook(Users user, Books book){
        return em.createNamedQuery("Reservations.findByUserANDBook", Reservations.class).setParameter("userId", user).setParameter("bookId", book).getResultList();
    }
    
    public boolean insertReservation(Books book, Users user){
      try {
        Reservations rs = new Reservations();
        rs.setBookId(book);
        rs.setUserId(user);
        Date now = new Date();
        rs.setDate(now);
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
