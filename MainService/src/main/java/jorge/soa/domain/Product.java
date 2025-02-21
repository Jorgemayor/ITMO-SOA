package jorge.soa.domain;

import jakarta.persistence.*;
import java.util.List;
import jakarta.json.bind.annotation.JsonbTransient;

@NamedQueries({
    @NamedQuery(name = "Product.findByProductID", query = "SELECT e FROM Product e WHERE e.productID = :productID"),
    @NamedQuery(name = "Product.findByName", query = "SELECT e FROM Product e WHERE e.name = :name"),
    @NamedQuery(name = "Product.findByPrice", query = "SELECT e FROM Product e WHERE e.price = :price")
})
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer productID;

    private String name;

    private Float price;

    @JsonbTransient
    @OneToMany(mappedBy = "product")
    private List<Inventory> inventories;

    // Getters and setters

    public Integer getProductID() {
        return productID;
    }

    public void setProductID(Integer productID) {
        this.productID = productID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public List<Inventory> getInventories() {
        return inventories;
    }

    public void setInventories(List<Inventory> inventories) {
        this.inventories = inventories;
    }

}
