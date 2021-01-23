/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sorinlibrarys.queries;

import sorinlibrarys.entities.Authors;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Flagster
 */
public class AuthorsQuery {
    EntityManager em;
    EntityManagerFactory emf;

    public AuthorsQuery() {
        
        emf = Persistence.createEntityManagerFactory("SorinLibraryServerPU");
        em = emf.createEntityManager();
        
        em.getTransaction().begin();
    }
    
    public List<Authors> listAllAuthors() {
        return em.createNamedQuery("Authors.findAll",Authors.class).getResultList();
    }
}
