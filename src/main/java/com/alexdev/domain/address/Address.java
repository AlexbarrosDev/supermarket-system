package com.alexdev.domain.address;

import com.alexdev.domain.client.Client;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "tb_address")
public class Address implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long   id;
    private String street;      // Rua
    private String number;      // numero
    private String city;        // Cidade
    private String state;       // Estado
    private String zip;         // CEP

    @OneToOne(mappedBy = "address")
    private Client client;

    public Address(
                   String street,
                   String city,
                   String number,
                   String state,
                   String zip
    ) {
        this.street = street;
        this.city = city;
        this.number = number;
        this.state = state;
        this.zip = zip;
    }
}
