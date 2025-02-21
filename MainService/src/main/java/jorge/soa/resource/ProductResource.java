package jorge.soa.resource;

import jorge.soa.domain.Product;
import jorge.soa.service.ProductService;
import jakarta.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.faulttolerance.Timeout;

/**
 * REST controller for managing Product.
 */
@Path("/api/product")
public class ProductResource {

    private static final Logger LOG = Logger.getLogger(ProductResource.class.getName());

    @Inject
    private ProductService productService;


    private static final String ENTITY_NAME = "product";

    /**
     * POST : Create a new product.
     *
     * @param product the product to create
     * @return the Response with status 201 (Created) and with body the
     * new product, or with status 400 (Bad Request) if the product has already
     * an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createProduct(Product product) throws URISyntaxException {
        LOG.log(Level.FINE, "REST request to save Product : {}", product);
        productService.create(product);
        return HeaderUtil.createEntityCreationAlert(Response.created(new URI("/resources/api/product/" + product.getProductID())),
                ENTITY_NAME, product.getProductID().toString())
                .entity(product).build();
    }

    /**
     * PUT : Updates an existing product.
     *
     * @param product the product to update
     * @return the Response with status 200 (OK) and with body the updated product,
     * or with status 400 (Bad Request) if the product is not valid,
     * or with status 500 (Internal Server Error) if the product couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateProduct(Product product) throws URISyntaxException {
        LOG.log(Level.FINE, "REST request to update Product : {}", product);
        productService.edit(product);
        return HeaderUtil.createEntityUpdateAlert(Response.ok(), ENTITY_NAME, product.getProductID().toString())
                .entity(product).build();
    }

    /**
     * GET : get all the products.
     
     * @return the Response with status 200 (OK) and the list of products in body
     
     */
    
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Timeout
    public List<Product> getAllProducts() {
        LOG.log(Level.FINE, "REST request to get all Products");
        List<Product> products = productService.findAll();
        return products;
    }

    /**
     * GET /:productID : get the "productID" product.
     *
     * @param productID the productID of the product to retrieve
     * @return the Response with status 200 (OK) and with body the product, or with status 404 (Not Found)
     */
    
    
    @GET
    @Path("/{productID}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProduct(@PathParam("productID") Integer productID) {
        LOG.log(Level.FINE, "REST request to get Product : {}", productID);
        Product product = productService.find(productID);
        return Optional.ofNullable(product)
                .map(res -> Response.status(Response.Status.OK).entity(product).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    /**
     * DELETE /:productID : remove the "productID" product.
     * 
     * @param productID the productID of the product to delete
     * @return the Response with status 200 (OK)
     */
    
    
    @DELETE
    @Path("/{productID}")
    public Response removeProduct(@PathParam("productID") Integer productID) {
        LOG.log(Level.FINE, "REST request to delete Product : {}", productID);
        productService.remove(productService.find(productID));
        return HeaderUtil.createEntityDeletionAlert(Response.ok(), ENTITY_NAME, productID.toString()).build();
    }

}
