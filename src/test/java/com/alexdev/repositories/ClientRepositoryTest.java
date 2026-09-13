package com.alexdev.repositories;

import com.alexdev.domain.address.Address;
import com.alexdev.domain.client.Client;
import com.alexdev.domain.client.ClientStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class ClientRepositoryTest {

    @Autowired
    ClientRepository clientRepository;

    @Test
    void shouldReturnTrueWhenClientExistsByCpf() {

        // Arrange
        Client client = createClient();

        clientRepository.save(client);

        // Act
        boolean result = clientRepository.existsByCpf("16206516881");

        // Assert
        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenClientNotExistsByCpf() {

        // Act
        boolean result = clientRepository.existsByCpf("91718578857");

        // Assert
        assertFalse(result);
    }
    
    private Client createClient() {

        Client client = new Client();
        client.setName("Client1");
        client.setCpf("16206516881");
        client.setStatus(ClientStatus.ACTIVE);

        Address address1 = new Address();

        client.setAddress(address1);
        
        return  client; 
    }
}