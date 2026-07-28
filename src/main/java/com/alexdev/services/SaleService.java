package com.alexdev.services;

import com.alexdev.domain.entities.Client;
import com.alexdev.domain.enums.SaleStatus;
import com.alexdev.dtos.request.sale.ItemSoldCreateDTO;
import com.alexdev.dtos.request.sale.SaleCreateDTO;
import com.alexdev.dtos.request.sale.SaleStatusUpdateDTO;
import com.alexdev.dtos.response.sale.SaleDetailsDTO;
import com.alexdev.domain.entities.ItemSold;
import com.alexdev.domain.entities.Product;
import com.alexdev.domain.entities.Sale;
import com.alexdev.dtos.response.sale.SaleSummaryDTO;
import com.alexdev.exceptions.BusinessException;
import com.alexdev.exceptions.ResourceNotFoundException;
import com.alexdev.mappers.SaleMapper;
import com.alexdev.repositories.ClientRepository;
import com.alexdev.repositories.ProductRepository;
import com.alexdev.repositories.SaleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SaleService {

    private final SaleRepository saleRepository;

    private final ClientRepository clientRepository;

    private final ProductRepository productRepository;

    private final SaleMapper saleMapper;

    @Transactional(readOnly = true)
    public List<SaleSummaryDTO> findAllSale() {

        List<Sale> saleList = saleRepository.findAll();

        if (saleList.isEmpty()) {
            throw new ResourceNotFoundException("Sale not found");
        }
        
        return saleMapper
                .saleEntityListToSaleSummaryDTOList(saleList);
    }

    @Transactional(readOnly = true)
    public SaleDetailsDTO findSaleById(Long saleId) {

        Sale sale = saleRepository
                .findById(saleId)
                .orElseThrow(()
                -> new ResourceNotFoundException("Sale not found"));

        return saleMapper.saleEntityToSaleDetailsDTO(sale);
    }

    @Transactional
    public SaleDetailsDTO createSale(SaleCreateDTO sale) {

        return buildSale(sale);
    }

    @Transactional
    public SaleDetailsDTO updateStatus(Long id, SaleStatusUpdateDTO status) {

        Sale sale = saleRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sale not found"));

        if (sale.getStatus() == SaleStatus.CANCELED) {
            throw new BusinessException("This sale has already been cancelled."); // Esta venda já esta cancelada.
        }

        sale.setStatus(status.status());
        return saleMapper.saleEntityToSaleDetailsDTO(sale);
    }

    @Transactional
    public void deleteSaleById(Long saleId) {

        Sale sale = saleRepository
                .findById(saleId)
                .orElseThrow(() -> new ResourceNotFoundException("Sale not found"));

        if (sale.getStatus() != SaleStatus.CANCELED) {

            throw new BusinessException("Only canceled sales can be deleted.");
        }

        saleRepository.delete(sale);
    }

    private SaleDetailsDTO buildSale(SaleCreateDTO sale) {

        Sale entity = saleMapper.saleCreateDTOToSaleEntity(sale);

        entity.setClient(getClientOrThrow(sale.clientId()));

        entity.setItems(createItems(sale.items()));

        return saleMapper
                .saleEntityToSaleDetailsDTO(saleRepository.save(entity));
    }

    private Client getClientOrThrow(Long id) {

        return clientRepository
                .findById(id)
                .orElseThrow(()
                        -> new ResourceNotFoundException("Client not found with id " + id));
    }

    private Product getProductOrThrow(Long id) {

        return productRepository
                .findById(id)
                .orElseThrow(()
                        -> new ResourceNotFoundException("Product not found with id " + id));
    }

    private List<ItemSold> createItems(List<ItemSoldCreateDTO> itemsDto) {

        List<ItemSold> items = new ArrayList<>();

        for (ItemSoldCreateDTO dto : itemsDto) {
            items.add(createItem(dto));
        }

        return items;
    }

    private ItemSold createItem(ItemSoldCreateDTO itemDto) {

        Product product = getProductOrThrow(itemDto.productId());

        ItemSold item =  new ItemSold();
        item.setProduct(product);
        item.setQuantity(itemDto.quantity());
        item.setUnitPrice(product.getCurrentPrice());

        return item;
    }
}
