package com.hotel.models;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class RoomTest {
    private Room room;

    @BeforeMethod
    void setUp() {
        room = new Room(1, "Single", 100.0, true);
    }

    @Test
    void testRoomConstructor() {
        assertEquals(1, room.getId());
        assertEquals("Single", room.getType());
        assertEquals(100.0, room.getPrice(), 0.01);
        assertTrue(room.isAvailable());
    }

    @Test
    void testRoomSetters() {
        room.setId(2);
        room.setType("Double");
        room.setPrice(150.0);
        room.setAvailable(false);

        assertEquals(2, room.getId());
        assertEquals("Double", room.getType());
        assertEquals(150.0, room.getPrice(), 0.01);
        assertFalse(room.isAvailable());
    }
}
