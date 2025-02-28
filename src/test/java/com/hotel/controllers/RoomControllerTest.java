package com.hotel.controllers;

import com.hotel.models.Room;
import com.hotel.services.RoomService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

public class RoomControllerTest {

    @Mock
    private RoomService roomService;

    @InjectMocks
    private RoomController roomController;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterRoom() {
        // Arrange
        Room room = new Room(1, "Single", 100.0, true);
        doNothing().when(roomService).saveRoom(room);

        // Act
        roomController.registerRoom("Single", 100.0, true);

        // Assert
        verify(roomService, times(1)).saveRoom(any(Room.class));
    }

    @Test
    public void testFindRoomsByType() {
        // Arrange
        List<Room> rooms = Arrays.asList(
            new Room(1, "Single", 100.0, true),
            new Room(2, "Double", 150.0, true)
        );
        when(roomService.findRoomsByType("Single")).thenReturn(rooms);

        // Act
        List<Room> result = roomController.findRoomByType("Single");

        // Assert
        assertEquals(result.size(), 2);
    }

    @Test
    public void testGetAllRooms() {
        // Arrange
        List<Room> rooms = Arrays.asList(
            new Room(1, "Single", 100.0, true),
            new Room(2, "Double", 150.0, true)
        );
        when(roomService.getAllRooms()).thenReturn(rooms);

        // Act
        List<Room> result = roomController.getAllRooms();

        // Assert
        assertEquals(result.size(), 2);
    }
}