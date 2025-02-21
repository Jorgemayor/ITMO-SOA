package jorge.soa.service;

import jakarta.enterprise.context.Dependent;
import jakarta.persistence.EntityManager;
import jakarta.inject.Inject;
import jorge.soa.domain.Product;


@Dependent
public class ProductService extends AbstractService<Product, Integer> {

    @Inject
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProductService() {
        super(Product.class);
    }
    
}
