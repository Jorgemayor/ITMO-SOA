package jorge.soa.service.producer;

import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

/**
 * Producer for injectable EntityManager
 *
 */
@RequestScoped
public class EntityManagerProducer {

    @PersistenceContext(unitName = "MyApplicationPU")
    private EntityManager em;

    @Produces
    public EntityManager getEntityManager(){
        return em;
    }

}