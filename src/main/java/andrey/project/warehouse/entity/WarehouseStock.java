package andrey.project.warehouse.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "warehouse_stock")
public class WarehouseStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false, unique = true)
    private Product product;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "WarehouseStock{" +
                "id=" + id +
                ", productId=" + (product != null ? product.getId() : null) +
                ", productName='" + (product != null ? product.getName() : null) + '\'' +
                ", productArticle='" + (product != null ? product.getArticle() : null) + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
