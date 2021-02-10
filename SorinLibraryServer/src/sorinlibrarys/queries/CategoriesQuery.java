/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sorinlibrarys.queries;

import sorinlibrarys.entities.Categories;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import sorinlibrarys.entities.Authors;

/**
 *
 * @author Flagster
 */
public class CategoriesQuery {
    EntityManager em;
    EntityManagerFactory emf;

    public CategoriesQuery() {
        
        emf = Persistence.createEntityManagerFactory("SorinLibraryServerPU");
        em = emf.createEntityManager();
        
        em.getTransaction().begin();
    }
    
    public List<Categories> listAllCategories() {
        return em.createNamedQuery("Categories.findAll",Categories.class).getResultList();
    }
    
    public Categories insertCategories(String name){
      try {
        Categories rs = new Categories();
        rs.setCategory(name);
        em.persist(rs);
        em.getTransaction().commit();
        
        return rs;
      }
      catch(Exception e) {
        System.out.println(e.toString());
        return null;
      } 
    }
}
