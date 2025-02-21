package jorge.soa.service;

import jakarta.enterprise.context.Dependent;
import jakarta.persistence.EntityManager;
import jakarta.inject.Inject;
import jorge.soa.domain.Inventory;


@Dependent
public class InventoryService extends AbstractService<Inventory, Integer> {

    @Inject
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public InventoryService() {
        super(Inventory.class);
    }
    
}
