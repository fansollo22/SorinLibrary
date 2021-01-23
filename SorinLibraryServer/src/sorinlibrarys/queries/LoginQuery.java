/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sorinlibrarys.queries;

import sorinlibrarys.entities.Users;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Flagster
 */
public class LoginQuery {
    EntityManager em;
    EntityManagerFactory emf;

    public LoginQuery() {
        
        emf = Persistence.createEntityManagerFactory("SorinLibraryServerPU");
        em = emf.createEntityManager();
        
        em.getTransaction().begin();
    }
    
    public List<Users> listLogin() {
        return em.createNamedQuery("Users.findAll",Users.class).getResultList();
    }
    
    public Users getUserById(Integer userID){
        return em.createNamedQuery("Users.findById",Users.class).setParameter("id", userID).getSingleResult();
    }
}
