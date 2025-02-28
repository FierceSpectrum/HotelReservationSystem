package com.hotel.controllers;

import com.hotel.models.Client;
import com.hotel.services.ClientService;
import java.util.List;

public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    // Registrar un nuevo cliente
    public void registerClient(String name, String email, String phone) {
        Client client = new Client(0, name, email, phone);
        clientService.saveClient(client);
        System.out.println("Cliente registrado correctamente");
    }

    // Buscar un cliente por ID
    public Client findClientById(int id) {
        return clientService.findClientById(id);
    }

    // Obtener todos los clientes
    public List<Client> getAllClients() {
        return clientService.getAllClients();
    }
}
