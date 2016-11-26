/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carnetelectronique;


import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.swing.JOptionPane;


 

public class ContactJPAController {
    
    public ContactJPAController( EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    private EntityManagerFactory emf = null;
 
    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
 
    public void create(Contact personNew)  throws  Exception {
          EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(personNew);
            em.getTransaction().commit();
        } catch (Exception ex) {
            System.out.println("carnetelectronique.ContactJPAController.create() error!!");
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }//fin create
    public void edit(Contact personNew) throws  Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            personNew = em.merge(personNew);
            em.getTransaction().commit();
        } 
        catch (Exception ex) 
        {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) 
            {
                int id = (int)personNew.getId();
                if (findPersonNew(id) == null) {
                    throw new Exception("The personNew with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } 
        finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(int id) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Contact personNew;
            try {
                personNew = em.getReference(Contact.class, id);
                personNew.getId();
            } catch (EntityNotFoundException enfe) {
                throw new Exception("The personNew with id " + id + " no longer exists.", enfe);
                
            }
            em.remove(personNew);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Contact> findPersonNewEntities() {
        return findPersonNewEntities(true, -1, -1);
    }

    public List<Contact> findPersonNewEntities(int maxResults, int firstResult) {
        return findPersonNewEntities(false, maxResults, firstResult);
    }

    private List<Contact> findPersonNewEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Contact.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Contact findPersonNew(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Contact.class, id);
        } finally {
            em.close();
        }
    }

    public int getPersonNewCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Contact> rt = cq.from(Contact.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
      



    
