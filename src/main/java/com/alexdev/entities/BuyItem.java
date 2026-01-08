package com.alexdev.entities;

import java.io.Serializable;
import java.util.Objects;

public class BuyItem implements Serializable {

    private Long id;

    private Buy buy;

    private Product product;

    private Integer quantity;

    private Double unitPrice;

    public BuyItem() {
    }

    public BuyItem(Buy buy, Product product, Integer quantity, Double unitPrice) {
        this.buy = buy;
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public Long getId() {
        return id;
    }

    public Buy getBuy() {
        return buy;
    }

    public void setBuy(Buy buy) {
        this.buy = buy;
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

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BuyItem buyItem = (BuyItem) o;
        return Objects.equals(id, buyItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
