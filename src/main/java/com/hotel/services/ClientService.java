package com.hotel.services;

import com.hotel.models.Client;
import java.util.ArrayList;
import java.util.List;

public class ClientService {
    private List<Client> clients = new ArrayList<>();

    // Registers a new client in the system
    public void registerClient(Client client) {
        clients.add(client);
    }

    // Retrieves a client by their ID
    public Client getClient(int clientId) {
        return clients.stream()
                .filter(c -> c.getId() == clientId)
                .findFirst()
                .orElse(null);
    }
}
