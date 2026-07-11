package com.alexdev.dtos.request.address;

public record AddressCreateDTO(

        String street,
        String number,
        String city,
        String state,
        String zip
)
{}
