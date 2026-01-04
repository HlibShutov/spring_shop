package spring.shop.models;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long cartId;
    private BigDecimal cost;
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(name="customerId", nullable = true)
    private Customer customer;

    public Cart() {
        this.cost = BigDecimal.ZERO;
        this.items = new ArrayList<>();
    }

    public Cart(@Nullable Customer customer) {
        this.cost = BigDecimal.ZERO;
        this.items = new ArrayList<>();
        this.customer = customer;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    @Nullable
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(@Nullable Customer customer) {
        this.customer = customer;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<CartItem> items;
}
