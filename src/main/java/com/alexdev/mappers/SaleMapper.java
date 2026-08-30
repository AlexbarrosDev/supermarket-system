package com.alexdev.mappers;

import com.alexdev.dtos.request.sale.SaleCreateDTO;
import com.alexdev.dtos.response.sale.SaleDetailsDTO;
import com.alexdev.domain.sale.Sale;
import com.alexdev.dtos.response.sale.SaleSummaryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {
                ClientMapper.class
        }
)
public interface SaleMapper {

    Sale saleCreateDTOToSaleEntity(SaleCreateDTO saleCreateDTO);

    @Mapping(target = "total", source = "total")
    SaleDetailsDTO saleEntityToSaleDetailsDTO(Sale saleEntity);

    List<Sale> saleCreateDTOListToSaleEntityList(List<SaleCreateDTO> sales);

    List<SaleDetailsDTO> saleEntityListToSaleDetailsDTOList(List<Sale> sales);

    List<SaleSummaryDTO> saleEntityListToSaleSummaryDTOList(List<Sale> sales);
}
