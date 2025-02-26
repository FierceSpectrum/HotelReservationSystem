package com.hotel.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ClientTest {
    private Client client;

    @BeforeEach
    void setUp() {
        client = new Client(1, "John Doe", "john@example.com", "123456789");
    }

    @Test
    void testClientConstructor() {
        assertEquals(1, client.getId());
        assertEquals("John Doe", client.getName());
        assertEquals("john@example.com", client.getEmail());
        assertEquals("123456789", client.getPhone());
    }

    @Test
    void testClientSetters() {
        client.setId(2);
        client.setName("Jane Doe");
        client.setEmail("jane@example.com");
        client.setPhone("987654321");

        assertEquals(2, client.getId());
        assertEquals("Jane Doe", client.getName());
        assertEquals("jane@example.com", client.getEmail());
        assertEquals("987654321", client.getPhone());
    }
}
