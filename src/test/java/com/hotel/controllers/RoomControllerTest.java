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
        roomController = new RoomController(roomService);
    }

    @Test
    public void testRegisterRoom() {
        // Arrange
        Room room = new Room(1, "Single", 100.0, true);
        doNothing().when(roomService).registerRoom(room);

        // Act
        roomController.registerRoom("Single", 100.0, true);

        // Assert
        verify(roomService, times(1)).registerRoom(any(Room.class));
    }

    @Test
    public void testsearchRooms() {
        // Arrange
        List<Room> rooms = Arrays.asList(
            new Room(1, "Single", 100.0, true),
            new Room(2, "Single", 150.0, false)
        );
        when(roomService.searchRooms("Single", null, null)).thenReturn(rooms);

        // Act
        List<Room> result = roomController.findRoomByType("Single", null, null);

        // Assert
        assertEquals(result.size(), 2);
    }

    @Test
    public void testsearchAllRooms() {
        // Arrange
        List<Room> rooms = Arrays.asList(
            new Room(1, "Single", 100.0, true),
            new Room(2, "Double", 150.0, true)
        );
        when(roomService.searchRooms(null, null, null)).thenReturn(rooms);

        // Act
        List<Room> result = roomController.getAllRooms();

        // Assert
        assertEquals(result.size(), 2);
    }
}