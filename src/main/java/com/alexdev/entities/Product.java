package com.alexdev.entities;

import java.io.Serializable;
import java.util.Objects;

public class Product implements Serializable {

    private Long id;
    private String name;
    private Double price;
    private Category category;
    private Mark mark;

    public Product() {
    }

    public Product(String name, Double price, Category category, Mark mark) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.mark = mark;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Mark getMark() {
        return mark;
    }

    public void setMark(Mark mark) {
        this.mark = mark;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
