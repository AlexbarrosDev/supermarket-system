package com.alexdev.entities;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Buy implements Serializable {

    private Long id;
    private Instant date;

    private Client client;

    private List<BuyItem> items = new ArrayList<>();

    public Buy() {
    }

    public Buy(Long id, Instant date, Client client) {
        this.id = id;
        this.date = date;
        this.client = client;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Buy buy = (Buy) o;
        return Objects.equals(id, buy.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
