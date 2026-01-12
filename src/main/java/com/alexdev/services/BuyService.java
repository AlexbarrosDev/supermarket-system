package com.alexdev.services;

import com.alexdev.dto.request.BuyCreateDTO;
import com.alexdev.dto.request.BuyItemCreateDTO;
import com.alexdev.dto.response.BuyDTO;
import com.alexdev.entities.Buy;
import com.alexdev.entities.BuyItem;
import com.alexdev.entities.Client;
import com.alexdev.entities.Product;
import com.alexdev.mappers.BuyMapper;
import com.alexdev.repositories.BuyItemRepository;
import com.alexdev.repositories.BuyRepository;
import com.alexdev.repositories.ClientRepository;
import com.alexdev.repositories.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class BuyService {

    private final BuyRepository buyRepository;
    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;

    public BuyService(BuyRepository buyRepository,
                      ClientRepository clientRepository,
                      ProductRepository productRepository) {
        this.buyRepository = buyRepository;
        this.clientRepository = clientRepository;
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<BuyDTO> findAll() {
       return buyRepository.findAll()
               .stream()
               .map(BuyMapper::entityToDTO)
               .toList();
    }

    @Transactional(readOnly = true)
    public BuyDTO findById(Long id) {

        Buy entity = buyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Buy not found"));
        return BuyMapper.entityToDTO(entity);
    }

    @Transactional
    public BuyDTO create(BuyCreateDTO buyCreateDTO) {

        Client client = clientRepository.findById(buyCreateDTO.ClientId())
                .orElseThrow(() -> new IllegalArgumentException("Client not found"));

        Buy buy = new Buy();
        buy.setDate(Instant.now());
        buy.setClient(client);

        List<BuyItem> buyItems = new ArrayList<>();

        for (BuyItemCreateDTO x : buyCreateDTO.BuyItems()) {

            Product product =  productRepository.findById(x.productId())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found"));

            BuyItem buyItem = new BuyItem(buy,
                    product,
                    x.quantity(),
                    product.getPrice());

            buyItems.add(buyItem);
        }

        buy.setItems(buyItems);

        return BuyMapper.entityToDTO(buyRepository.save(buy));
    }

}
