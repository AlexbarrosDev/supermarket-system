package com.alexdev.domain.entities;

import com.alexdev.domain.enums.SaleStatus;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "tb_sale")
public class Sale implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp // Insere a data manualmente quando a entidade é salva.
    private Instant saleDate;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private SaleStatus status =  SaleStatus.FINALIZED;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL)
    private List<ItemSold> items = new ArrayList<>();

    // Constructor
    public Sale(Client client) {
        this.client = client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setItems(List<ItemSold> items) {
        this.items = items;

        items.forEach(item -> item.setSale(this));
    } // metodo personalizado!

    public BigDecimal getTotal() {

        return items.stream()
                .map(ItemSold::getSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
