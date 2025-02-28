package com.hotel.services;

import com.hotel.models.Room;
import com.hotel.utils.DatabaseConnection;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

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

    @BeforeMethod
    void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);

        // Asegurar que el servicio usa el mock de DatabaseConnection
        roomService = new RoomService(databaseConnection);

        // Configurar el comportamiento de los mocks para ambos tipos de
        // prepareStatement
        when(databaseConnection.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(any())).thenReturn(preparedStatement);
        when(connection.prepareStatement(any(), anyInt())).thenReturn(preparedStatement);
    }

    @Test
    void testSaveRoom() throws SQLException {
        // Configurar el mock para executeUpdate y getGeneratedKeys
        when(preparedStatement.executeUpdate()).thenReturn(1);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(1);

        // Crar una habitación
        Room room = new Room(1, "Single", 100.0, true);

        // Guardar la habitación
        roomService.saveRoom(room);

        // Verificar que se asignó el ID
        assertEquals(1, room.getId());

        // Verificar que se llamó a executeUpdate
        verify(preparedStatement, times(1)).executeUpdate();
    }

    @Test
    void testGetAllRooms() throws SQLException {
        // Configurar el mock para executeQuery
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getString("type")).thenReturn("Single");
        when(resultSet.getDouble("price")).thenReturn(100.0);
        when(resultSet.getBoolean("available")).thenReturn(true);

        // Obtener todas las habitaciones
        List<Room> rooms = roomService.getAllRooms();

        // Verificar que la lista no esté vacía
        assertFalse(rooms.isEmpty());
        assertEquals(1, rooms.size());

        // Verificar que se llamó a executeQuery
        verify(preparedStatement, times(1)).executeQuery();
    }

    @Test
    void testUpdateRoom() throws SQLException {
        // Configurar el mock para executeUpdate
        when(preparedStatement.executeUpdate()).thenReturn(1);

        // Crear una habitación
        Room room = new Room(1, "Double", 150.0, false);

        // Actualizar la habitación
        roomService.updateRoom(room);

        // Verificar que se llamó a executeUpdate
        verify(preparedStatement, times(1)).executeUpdate();
    }

    @Test
    void testDeleteRoom() throws SQLException {
        // Configurar el mock para executeUpdate
        when(preparedStatement.executeUpdate()).thenReturn(1);

        // Eliminar la habitación
        roomService.deleteRoom(1);

        // Verificar que se llamó a executeUpdate
        verify(preparedStatement, times(1)).executeUpdate();
    }
}