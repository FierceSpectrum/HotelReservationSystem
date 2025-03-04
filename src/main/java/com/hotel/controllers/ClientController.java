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
        clientService.registerClient(client);
        System.out.println("Cliente registrado correctamente con ID: " + client.getId());
    }

    // Buscar un cliente por ID
    public Client findClientById(int id) {
        Client client = clientService.getClient(id);
        if (client == null) {
            System.out.println("Cliente no encontrado");
        }
        System.out.println("Cliente encontrado: " + client.getName());
        return client;
    }

    // Obtener todos los clientes
    public List<Client> getAllClients() {
        List<Client> clients = clientService.getAllClients();
        System.out.println("Clientes encontrados: " + clients.size());
        return clients;
    }

    // Buscar un cliente por correo electrónico
    public Client findClientByEmail(String email) {
        Client client = clientService.getClientByEmail(email);
        if (client == null) {
            System.out.println("Cliente no encontrado");
        }
        return client;
    }
}
