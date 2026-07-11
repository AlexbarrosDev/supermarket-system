package com.alexdev.mappers;

import com.alexdev.domain.entities.Address;
import com.alexdev.dtos.request.address.AddressCreateDTO;
import com.alexdev.dtos.response.address.AddressDetailsDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    Address addressRequestDTOToAddressEntity(AddressCreateDTO addressCreateDTO);

    AddressDetailsDTO addressEntityToAddressDetailsDTO(Address address);

    List<AddressDetailsDTO> addressEntityListToAddressDetailsDTOList(List<Address> addresses);
}
