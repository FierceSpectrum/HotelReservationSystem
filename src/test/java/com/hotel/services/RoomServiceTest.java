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
    public void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);

        // Arrange
        roomService = new RoomService(databaseConnection);

        // Arrange
        when(databaseConnection.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(any())).thenReturn(preparedStatement);
        when(connection.prepareStatement(any(), anyInt())).thenReturn(preparedStatement);
    }

    @Test
    public void testRegisterRoom_Success() throws SQLException {
        // Arrange
        when(preparedStatement.executeUpdate()).thenReturn(1);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(1);
        Room room = new Room(0, "Single", 100.0, true);

        // Act
        roomService.registerRoom(room);

        // Assert
        assertEquals(1, room.getId());
        verify(preparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void testRegisterRoom_Failure() throws SQLException {
        // Arrange
        when(preparedStatement.executeUpdate()).thenThrow(new SQLException("Error al ejecutar la actualización"));
        Room room = new Room(0, "Single", 100.0, true);

        // Act
        roomService.registerRoom(room);

        // Assert
        assertEquals(0, room.getId());
        verify(preparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void testGetRoom_Success() throws SQLException {
        // Arrange
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getString("type")).thenReturn("Single");
        when(resultSet.getDouble("price")).thenReturn(100.0);
        when(resultSet.getBoolean("available")).thenReturn(true);

        // Act
        Room foundRoom = roomService.getRoom(1);

        // Assert
        assertNotNull(foundRoom);
        assertEquals(foundRoom.getId(), 1);
        assertEquals(foundRoom.getType(), "Single");
        verify(preparedStatement, times(1)).executeQuery();
    }

    @Test
    public void testGetRoom_Failure() throws SQLException {
        // Arrange
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        // Act
        Room foundRoom = roomService.getRoom(1);

        // Assert
        assertNull(foundRoom);
        verify(preparedStatement, times(1)).executeQuery();
    }

    @Test
    public void testSearchRooms_Success() throws SQLException {
        // Arrange
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getString("type")).thenReturn("Single");
        when(resultSet.getDouble("price")).thenReturn(100.0);
        when(resultSet.getBoolean("available")).thenReturn(true);

        // Act
        List<Room> rooms = roomService.searchRooms("Single", null, null);

        // Assert
        assertFalse(rooms.isEmpty());
        assertEquals(rooms.size(), 1);
        verify(preparedStatement, times(1)).executeQuery();
    }

    @Test
    public void testSearchRooms_Failure() throws SQLException {
        // Arrange
        when(preparedStatement.executeQuery()).thenThrow(new SQLException("Error al ejecutar la consulta"));

        // Act
        List<Room> rooms = roomService.searchRooms("Single", null, null);

        // Assert
        assertTrue(rooms.isEmpty());
        verify(preparedStatement, times(1)).executeQuery();
    }

    @Test
    public void testUpdateRoom_Success() throws SQLException {
        // Arrange
        when(preparedStatement.executeUpdate()).thenReturn(1);
        Room room = new Room(1, "Double", 150.0, false);

        // Act
        roomService.updateRoom(room);

        // Assert
        verify(preparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void testUpdateRoom_Failure() throws SQLException {
        // Arrange
        when(preparedStatement.executeUpdate()).thenThrow(new SQLException("Error al ejecutar la actualización"));
        Room room = new Room(1, "Double", 150.0, false);

        // Act
        roomService.updateRoom(room);

        // Assert
        verify(preparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void testDeleteRoom_Success() throws SQLException {
        // Arrange
        when(preparedStatement.executeUpdate()).thenReturn(1);

        // Act
        roomService.deleteRoom(1);

        // Assert
        verify(preparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void testDeleteRoom_Failure() throws SQLException {
        // Arrange
        when(preparedStatement.executeUpdate()).thenThrow(new SQLException("Error al ejecutar la eliminación"));

        // Act
        roomService.deleteRoom(1);

        // Assert
        verify(preparedStatement, times(1)).executeUpdate();
    }
}
