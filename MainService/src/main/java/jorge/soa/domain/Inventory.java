package jorge.soa.domain;

import jakarta.persistence.*;

@NamedQueries({
    @NamedQuery(name = "Inventory.findByInventoryID", query = "SELECT e FROM Inventory e WHERE e.inventoryID = :inventoryID"),
    @NamedQuery(name = "Inventory.findByQuantity", query = "SELECT e FROM Inventory e WHERE e.quantity = :quantity"),
    @NamedQuery(name = "Inventory.findByLocation", query = "SELECT e FROM Inventory e WHERE e.location = :location")
})
@Entity
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer inventoryID;

    private Integer quantity;

    private String location;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    // Getters and setters

    public Integer getInventoryID() {
        return inventoryID;
    }

    public void setInventoryID(Integer inventoryID) {
        this.inventoryID = inventoryID;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

}
