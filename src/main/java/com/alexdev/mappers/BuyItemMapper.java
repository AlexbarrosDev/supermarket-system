package com.alexdev.mappers;

import com.alexdev.dto.response.BuyItemDTO;
import com.alexdev.entities.BuyItem;

public class BuyItemMapper {

    public static BuyItemDTO entityToDTO(BuyItem buyItem) {
        if (buyItem == null) {
            return null;
        }
        return new BuyItemDTO(buyItem.getId(),
                ProductMapper.entityToDTO(buyItem.getProduct()),
                buyItem.getQuantity(),
                buyItem.getUnitPrice(),
                buyItem.getSubTotal());
    }
}
