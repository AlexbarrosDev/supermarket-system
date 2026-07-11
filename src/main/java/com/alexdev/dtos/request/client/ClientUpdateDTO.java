package com.alexdev.dtos.request.client;

import com.alexdev.dtos.request.address.AddressUpdateDTO;


public record ClientUpdateDTO(

        String name,
        String phone,
        AddressUpdateDTO address
){}
