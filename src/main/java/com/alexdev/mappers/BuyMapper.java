package com.alexdev.mappers;

import com.alexdev.dto.response.BuyDTO;
import com.alexdev.dto.response.BuyItemDTO;
import com.alexdev.entities.Buy;
import com.alexdev.entities.BuyItem;

import java.util.ArrayList;
import java.util.List;

public class BuyMapper {

    public static BuyDTO entityToDTO(Buy buy) {
        if (buy == null) {
            return null;
        }
        return new BuyDTO(buy.getId(),
                buy.getDate(),
                ClientMapper.entityToDTO(buy.getClient()),
                buyItemDTOList(buy.getItems()),
                buy.getTotal());
    }

    private static List<BuyItemDTO> buyItemDTOList(List<BuyItem> list) {
        List<BuyItemDTO> listDTO = new ArrayList<>();
        for (BuyItem x : list) {
            listDTO.add(BuyItemMapper.entityToDTO(x));
        }
        return listDTO;
    }
}
