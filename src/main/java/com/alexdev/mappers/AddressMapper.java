package com.alexdev.mappers;

import com.alexdev.domain.address.Address;
import com.alexdev.dtos.request.address.AddressCreateDTO;
import com.alexdev.dtos.request.address.AddressUpdateDTO;
import com.alexdev.dtos.response.address.AddressDetailsDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    Address addressCreateDTOToAddressEntity(AddressCreateDTO addressCreateDTO);

    Address addressUpdateDTOToAddressEntity(AddressUpdateDTO addressUpdateDTO);

    AddressDetailsDTO addressEntityToAddressDetailsDTO(Address address);

    List<AddressDetailsDTO> addressEntityListToAddressDetailsDTOList(List<Address> addresses);
}
