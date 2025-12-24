package spring.shop.models;

import jakarta.persistence.*;

@Entity
public class Image {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long image_id;

    @ManyToOne
    @JoinColumn(name="product_id", nullable=false)
    private Product product;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    private String path;
}
