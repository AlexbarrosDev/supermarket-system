package com.alexdev.dtos.request.address;

public record AddressUpdateDTO(

        String street,
        String number,
        String city,
        String state,
        String zip
){}
