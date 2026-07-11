package com.alexdev.dtos.response.address;

public record AddressDetailsDTO(

        Long id,
        String street,
        String number,
        String city,
        String state,
        String zip
) {}
