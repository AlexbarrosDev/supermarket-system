package com.alexdev.config;

import com.alexdev.entities.*;

import com.alexdev.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.Instant;
import java.util.List;

@Configuration
@Profile("test")
public class TestConfig implements CommandLineRunner {

    MarkRepository markRepository;
    CategoryRepository categoryRepository;
    ProductRepository productRepository;
    ClientRepository clientRepository;
    BuyRepository buyRepository;

    public TestConfig(MarkRepository markRepository,
                      CategoryRepository categoryRepository,
                      ProductRepository productRepository,
                      ClientRepository clientRepository,
                      BuyRepository buyRepository) {
        this.markRepository = markRepository;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.clientRepository = clientRepository;
        this.buyRepository = buyRepository;
    }

    @Override
    public void run(String ... args) throws Exception {

        Client client1 = new Client("Alex", "50586644521");
        Client client2 = new Client("Giovanna", "72989244220");

        clientRepository.saveAll(List.of(client1, client2));

        //
        Mark mark1 = new Mark("Omo");
        Mark mark2 = new Mark("Ype");

        markRepository.saveAll(List.of(mark1, mark2));

        //
        Category category1 = new Category("Produtos de limpeza");

        categoryRepository.save(category1);

        //
        Product product1 = new Product("Sabão em pó", 12.99, category1, mark1);
        Product product2 = new Product("Sabão em pó", 13.99, category1, mark2);
        Product product3 = new Product("Detergente", 3.99, category1, mark2);

        productRepository.saveAll(List.of(product1, product2, product3));

        //
        Buy buy1 = new Buy(Instant.now(), client1);
        Buy buy2 = new Buy(Instant.now(), client2);

        //
        BuyItem buyItem1 = new BuyItem(buy1, product1, 1, product1.getPrice());
        BuyItem buyItem2 = new BuyItem(buy1, product2, 1, product2.getPrice());
        BuyItem buyItem3 = new BuyItem(buy2, product3, 1, product3.getPrice());
        BuyItem buyItem4 = new BuyItem(buy2, product2, 1, product2.getPrice());

        buy1.getItems().addAll(List.of(buyItem1, buyItem2));
        buy2.getItems().addAll(List.of(buyItem3, buyItem4));

        buyRepository.saveAll(List.of(buy1, buy2));
    }
}
