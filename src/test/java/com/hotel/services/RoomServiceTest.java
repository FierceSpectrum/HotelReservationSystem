package com.hotel.services;

import com.hotel.models.Room;
import com.hotel.utils.DatabaseConnection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class RoomServiceTest {

    @Mock
    private DatabaseConnection databaseConnection;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    @InjectMocks
    private RoomService roomService;

    @BeforeEach
    void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        when(databaseConnection.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
    }

    @Test
    void testSaveRoom_Success() throws SQLException {
        Room room = new Room(1, "Single", 100.0, true);
        roomService.saveRoom(room);

        verify(preparedStatement, times(1)).executeUpdate();
        System.out.println("Prueba de guardar habitación exitosa.");
    }

    @Test
    void testSaveRoom_Failure() throws SQLException {
        Room room = new Room(1, "Single", 100.0, true);
        when(preparedStatement.executeUpdate()).thenThrow(new SQLException("Error de base de datos"));

        assertThrows(SQLException.class, () -> roomService.saveRoom(room));
        System.out.println("Prueba de guardar habitación fallida.");
    }

    @Test
    void testGetAllRooms_Success() throws SQLException {
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getString("type")).thenReturn("Single");
        when(resultSet.getDouble("price")).thenReturn(100.0);
        when(resultSet.getBoolean("available")).thenReturn(true);

        List<Room> rooms = roomService.getAllRooms();
        assertFalse(rooms.isEmpty());
        assertEquals(1, rooms.size());
        System.out.println("Prueba de obtener todas las habitaciones exitosa.");
    }

    @Test
    void testGetAllRooms_Failure() throws SQLException {
        when(preparedStatement.executeQuery()).thenThrow(new SQLException("Error de base de datos"));

        assertThrows(SQLException.class, () -> roomService.getAllRooms());
        System.out.println("Prueba de obtener todas las habitaciones fallida.");
    }

    @Test
    void testUpdateRoom_Success() throws SQLException {
        Room room = new Room(1, "Double", 150.0, false);
        roomService.updateRoom(room);

        verify(preparedStatement, times(1)).executeUpdate();
        System.out.println("Prueba de actualizar habitación exitosa.");
    }

    @Test
    void testUpdateRoom_Failure() throws SQLException {
        Room room = new Room(1, "Double", 150.0, false);
        when(preparedStatement.executeUpdate()).thenThrow(new SQLException("Error de base de datos"));

        assertThrows(SQLException.class, () -> roomService.updateRoom(room));
        System.out.println("Prueba de actualizar habitación fallida.");
    }

    @Test
    void testDeleteRoom_Success() throws SQLException {
        roomService.deleteRoom(1);

        verify(preparedStatement, times(1)).executeUpdate();
        System.out.println("Prueba de eliminar habitación exitosa.");
    }

    @Test
    void testDeleteRoom_Failure() throws SQLException {
        when(preparedStatement.executeUpdate()).thenThrow(new SQLException("Error de base de datos"));

        assertThrows(SQLException.class, () -> roomService.deleteRoom(1));
        System.out.println("Prueba de eliminar habitación fallida.");
    }
}