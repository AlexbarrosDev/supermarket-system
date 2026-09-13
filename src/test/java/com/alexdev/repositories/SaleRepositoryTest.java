package com.alexdev.repositories;

import com.alexdev.domain.address.Address;
import com.alexdev.domain.category.Category;
import com.alexdev.domain.client.Client;
import com.alexdev.domain.client.ClientStatus;
import com.alexdev.domain.product.ProductStatus;
import com.alexdev.domain.sale.SaleStatus;
import com.alexdev.domain.group.Group;
import com.alexdev.domain.itemSold.ItemSold;
import com.alexdev.domain.product.Product;
import com.alexdev.domain.sale.Sale;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class SaleRepositoryTest {

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void shouldReturnTrueWhenClientIdExists() {

        // Arrange
        Sale sale = createSale();

        Sale savedSale = saleRepository.save(sale);

        // Act
        boolean result = saleRepository.existsByClientId(savedSale.getClient().getId());

        // Assert
        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenProductIdDoesNotExists() {

        // Arrange
        Long anotherId = Long.MAX_VALUE;

        // Act
        boolean result = saleRepository.existsByClientId(anotherId);

        // Assert
        assertFalse(result);
    }

    private Product createProduct() {

        Category category = new Category("Category");
        Group group = new Group("Group");

        categoryRepository.save(category);
        groupRepository.save(group);

        Product product = new Product();
        product.setName("Product");
        product.setCurrentPrice(BigDecimal.valueOf(10));
        product.setCategory(category);
        product.setGroup(group);
        product.setStatus(ProductStatus.ACTIVE);

        return product;
    }

    private Client createClient() {

        Client client = new Client();
        client.setName("Client");
        client.setCpf("51387513877");
        client.setStatus(ClientStatus.ACTIVE);
        client.setAddress(new Address());

        return client;
    }

    private List<ItemSold> createItemSoldList(Product product) {

        ItemSold itemSold = new ItemSold();
        itemSold.setProduct(product);
        itemSold.setQuantity(10);
        itemSold.setUnitPrice(BigDecimal.valueOf(10));

        return List.of(itemSold);
    }

    private Sale createSale() {

        Client client = clientRepository.save(createClient());
        Product product = productRepository.save(createProduct());

        Sale sale = new Sale(client);
        sale.setStatus(SaleStatus.FINALIZED);
        sale.setItems(createItemSoldList(product));

        return sale;
    }
}