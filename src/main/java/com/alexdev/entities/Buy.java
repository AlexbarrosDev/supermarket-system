package com.alexdev.entities;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "tb_buy")
public class Buy implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Instant date;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @OneToMany(mappedBy = "buy", cascade = CascadeType.PERSIST)
    private List<BuyItem> items = new ArrayList<>();

    public Buy() {
    }

    public Buy(Instant date, Client client) {
        this.client = client;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public List<BuyItem> getItems() {
        return items;
    }

    public Double getTotal() {

        Double total = 0.0;

        for (BuyItem x : items) {

            total += x.getSubTotal();
        }
        return total;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Buy buy = (Buy) o;
        return Objects.equals(id, buy.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
