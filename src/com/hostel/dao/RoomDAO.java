package com.hostel.dao;

import com.hostel.db.DatabaseConnection;
import com.hostel.model.Room;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO {
    
    public List<Room> getAllRooms() {
        List<Room> rooms = new ArrayList<>();
        String query = "SELECT * FROM rooms";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            if (conn == null) {
                return rooms;
            }
            
            while (rs.next()) {
                Room room = new Room();
                room.setId(rs.getInt("id"));
                room.setRoomNumber(rs.getString("room_number"));
                room.setRoomType(rs.getString("room_type"));
                room.setCapacity(rs.getInt("capacity"));
                room.setCurrentOccupancy(rs.getInt("current_occupancy"));
                room.setPrice(rs.getDouble("price"));
                room.setAvailable(rs.getBoolean("is_available"));
                rooms.add(room);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rooms;
    }
    
    public List<Room> getAvailableRooms() {
        List<Room> rooms = new ArrayList<>();
        String query = "SELECT * FROM rooms WHERE is_available = true";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            if (conn == null) {
                return rooms;
            }
            
            while (rs.next()) {
                Room room = new Room();
                room.setId(rs.getInt("id"));
                room.setRoomNumber(rs.getString("room_number"));
                room.setRoomType(rs.getString("room_type"));
                room.setCapacity(rs.getInt("capacity"));
                room.setCurrentOccupancy(rs.getInt("current_occupancy"));
                room.setPrice(rs.getDouble("price"));
                room.setAvailable(rs.getBoolean("is_available"));
                rooms.add(room);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rooms;
    }
    
    public Room getRoomById(int roomId) {
        String query = "SELECT * FROM rooms WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            if (conn == null) {
                return null;
            }
            
            pstmt.setInt(1, roomId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Room room = new Room();
                room.setId(rs.getInt("id"));
                room.setRoomNumber(rs.getString("room_number"));
                room.setRoomType(rs.getString("room_type"));
                room.setCapacity(rs.getInt("capacity"));
                room.setCurrentOccupancy(rs.getInt("current_occupancy"));
                room.setPrice(rs.getDouble("price"));
                room.setAvailable(rs.getBoolean("is_available"));
                return room;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean allocateRoom(int studentId, int roomId) {
        String query = "UPDATE rooms SET current_occupancy = current_occupancy + 1, " +
                      "is_available = (current_occupancy + 1 < capacity) WHERE id = ?";
        String studentQuery = "UPDATE students SET room_id = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) {
                return false;
            }
            
            conn.setAutoCommit(false);
            
            try (PreparedStatement pstmt = conn.prepareStatement(query);
                 PreparedStatement studentStmt = conn.prepareStatement(studentQuery)) {
                
                // Update room occupancy
                pstmt.setInt(1, roomId);
                int roomRowsAffected = pstmt.executeUpdate();
                
                // Update student's room
                studentStmt.setInt(1, roomId);
                studentStmt.setInt(2, studentId);
                int studentRowsAffected = studentStmt.executeUpdate();
                
                if (roomRowsAffected > 0 && studentRowsAffected > 0) {
                    conn.commit();
                    return true;
                } else {
                    conn.rollback();
                    return false;
                }
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deallocateRoom(int studentId) {
        String query = "UPDATE rooms r " +
                      "JOIN students s ON r.id = s.room_id " +
                      "SET r.current_occupancy = r.current_occupancy - 1, " +
                      "r.is_available = true " +
                      "WHERE s.id = ?";
        String studentQuery = "UPDATE students SET room_id = NULL WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) {
                return false;
            }
            
            conn.setAutoCommit(false);
            
            try (PreparedStatement pstmt = conn.prepareStatement(query);
                 PreparedStatement studentStmt = conn.prepareStatement(studentQuery)) {
                
                // Update room occupancy
                pstmt.setInt(1, studentId);
                int roomRowsAffected = pstmt.executeUpdate();
                
                // Update student's room
                studentStmt.setInt(1, studentId);
                int studentRowsAffected = studentStmt.executeUpdate();
                
                if (roomRowsAffected > 0 && studentRowsAffected > 0) {
                    conn.commit();
                    return true;
                } else {
                    conn.rollback();
                    return false;
                }
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean addRoom(Room room) {
        String query = "INSERT INTO rooms (room_number, room_type, capacity, price) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            if (conn == null) {
                return false;
            }
            
            pstmt.setString(1, room.getRoomNumber());
            pstmt.setString(2, room.getRoomType());
            pstmt.setInt(3, room.getCapacity());
            pstmt.setDouble(4, room.getPrice());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean updateRoom(Room room) {
        String query = "UPDATE rooms SET room_number = ?, room_type = ?, capacity = ?, price = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            if (conn == null) {
                return false;
            }
            
            pstmt.setString(1, room.getRoomNumber());
            pstmt.setString(2, room.getRoomType());
            pstmt.setInt(3, room.getCapacity());
            pstmt.setDouble(4, room.getPrice());
            pstmt.setInt(5, room.getId());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteRoom(int roomId) {
        String query = "DELETE FROM rooms WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            if (conn == null) {
                return false;
            }
            
            pstmt.setInt(1, roomId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
} 