package jorge.soa.resource;

import jorge.soa.domain.Inventory;
import jorge.soa.service.InventoryService;
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
 * REST controller for managing Inventory.
 */
@Path("/api/inventory")
public class InventoryResource {

    private static final Logger LOG = Logger.getLogger(InventoryResource.class.getName());

    @Inject
    private InventoryService inventoryService;

    @Inject
    private ProductService productService;

    private static final String ENTITY_NAME = "inventory";

    /**
     * POST : Create a new inventory.
     *
     * @param inventory the inventory to create
     * @return the Response with status 201 (Created) and with body the
     * new inventory, or with status 400 (Bad Request) if the inventory has already
     * an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createInventory(Inventory inventory) throws URISyntaxException {
        LOG.log(Level.FINE, "REST request to save Inventory : {}", inventory);
        if (inventory.getProduct() != null && inventory.getProduct().getProductID() != null) {
            inventory.setProduct(productService.find(inventory.getProduct().getProductID()));
        } else {
            inventory.setProduct(null);
        }
        inventoryService.create(inventory);
        return HeaderUtil.createEntityCreationAlert(Response.created(new URI("/resources/api/inventory/" + inventory.getInventoryID())),
                ENTITY_NAME, inventory.getInventoryID().toString())
                .entity(inventory).build();
    }

    /**
     * PUT : Updates an existing inventory.
     *
     * @param inventory the inventory to update
     * @return the Response with status 200 (OK) and with body the updated inventory,
     * or with status 400 (Bad Request) if the inventory is not valid,
     * or with status 500 (Internal Server Error) if the inventory couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateInventory(Inventory inventory) throws URISyntaxException {
        LOG.log(Level.FINE, "REST request to update Inventory : {}", inventory);
        if (inventory.getProduct() != null && inventory.getProduct().getProductID() != null) {
            inventory.setProduct(productService.find(inventory.getProduct().getProductID()));
        } else {
            inventory.setProduct(null);
        }
        inventoryService.edit(inventory);
        return HeaderUtil.createEntityUpdateAlert(Response.ok(), ENTITY_NAME, inventory.getInventoryID().toString())
                .entity(inventory).build();
    }

    /**
     * GET : get all the inventories.
     
     * @return the Response with status 200 (OK) and the list of inventories in body
     
     */
    
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Timeout
    public List<Inventory> getAllInventories() {
        LOG.log(Level.FINE, "REST request to get all Inventories");
        List<Inventory> inventories = inventoryService.findAll();
        return inventories;
    }

    /**
     * GET /:inventoryID : get the "inventoryID" inventory.
     *
     * @param inventoryID the inventoryID of the inventory to retrieve
     * @return the Response with status 200 (OK) and with body the inventory, or with status 404 (Not Found)
     */
    
    
    @GET
    @Path("/{inventoryID}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getInventory(@PathParam("inventoryID") Integer inventoryID) {
        LOG.log(Level.FINE, "REST request to get Inventory : {}", inventoryID);
        Inventory inventory = inventoryService.find(inventoryID);
        return Optional.ofNullable(inventory)
                .map(res -> Response.status(Response.Status.OK).entity(inventory).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    /**
     * DELETE /:inventoryID : remove the "inventoryID" inventory.
     * 
     * @param inventoryID the inventoryID of the inventory to delete
     * @return the Response with status 200 (OK)
     */
    
    
    @DELETE
    @Path("/{inventoryID}")
    public Response removeInventory(@PathParam("inventoryID") Integer inventoryID) {
        LOG.log(Level.FINE, "REST request to delete Inventory : {}", inventoryID);
        inventoryService.remove(inventoryService.find(inventoryID));
        return HeaderUtil.createEntityDeletionAlert(Response.ok(), ENTITY_NAME, inventoryID.toString()).build();
    }

}
