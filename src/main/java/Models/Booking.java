package Models;

import Database.MySqlDb;
import java.sql.*;
import java.util.ArrayList;

public class Booking {
    
    private int userId;
    private int roomId;
    private int status;   // 1 = booked, 0 = cancelled, etc.

    // Default constructor
    public Booking() {
    }

    // Constructor with all fields you insert
    public Booking(int userId, int roomId, int status) {
        this.userId = userId;
        this.roomId = roomId;
        this.status = status;
    }

    // Getters and Setters
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public int getRoomId() {
        return roomId;
    }
    
    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }
    
    public int getStatus() {
        return status;
    }
    
    public void setStatus(int status) {
        this.status = status;
    }
    
    @Override
    public String toString() {
        return "Booking{"
                + "userId=" + userId
                + ", roomId=" + roomId
                + ", status=" + status
                + '}';
    }

    // ==========================
    // Database Functions
    // ==========================
    // Store new booking
    public boolean store() {
        String sql = "INSERT INTO booking (user_id, room_id, status) VALUES (?, ?, ?)";
        try {
            MySqlDb db = new MySqlDb();
            PreparedStatement stmt = db.conn.prepareStatement(sql);
            stmt.setInt(1, this.userId);
            stmt.setInt(2, this.roomId);
            stmt.setInt(3, this.status);
            
            int rows = stmt.executeUpdate();
            stmt.close();
            db.conn.close();
            return rows > 0;
        } catch (SQLException e) {
            System.out.println("Insert error: " + e.getMessage());
            return false;
        }
    }

    // Update booking status by booking id
    public static boolean updateById(int id, int status) {
        String sql = "UPDATE booking SET status = ? WHERE id = ?";
        try {
            MySqlDb db = new MySqlDb();
            PreparedStatement stmt = db.conn.prepareStatement(sql);
            stmt.setInt(1, status);
            stmt.setInt(2, id);
            
            int rows = stmt.executeUpdate();
            stmt.close();
            db.conn.close();
            return rows > 0;
        } catch (SQLException e) {
            System.out.println("Update error: " + e.getMessage());
            return false;
        }
    }

    // Toggle booking status by user_id and room_id
    public static boolean updateByRoomIdUserIdStatus(int user_id, int room_id, int currentStatus) {
        String sql = "UPDATE booking SET status = ? WHERE user_id = ? AND room_id = ? AND status= ?";
        try {
            MySqlDb db = new MySqlDb();
            PreparedStatement stmt = db.conn.prepareStatement(sql);

            // Flip the status: if current is 1 → set 0, if current is 0 → set 1
            int newStatus = (currentStatus == 1 ? 0 : 1);
            
            stmt.setInt(1, newStatus);
            stmt.setInt(2, user_id);
            stmt.setInt(3, room_id);
            stmt.setInt(4, currentStatus);
            
            int rows = stmt.executeUpdate();
            
            stmt.close();
            db.conn.close();
            
            return rows > 0; // true if at least one row updated
        } catch (SQLException e) {
            System.out.println("Update error: " + e.getMessage());
            return false;
        }
    }

    // Find bookings by room_id
    public static ArrayList<Booking> findByRoomId(int roomId) {
        ArrayList<Booking> list = new ArrayList<>();
        String sql = "SELECT * FROM booking WHERE room_id = ?";
        try {
            MySqlDb db = new MySqlDb();
            PreparedStatement stmt = db.conn.prepareStatement(sql);
            stmt.setInt(1, roomId);
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Booking b = new Booking(
                        rs.getInt("user_id"),
                        rs.getInt("room_id"),
                        rs.getInt("status")
                );
                list.add(b);
            }
            rs.close();
            stmt.close();
            db.conn.close();
        } catch (SQLException e) {
            System.out.println("Find error: " + e.getMessage());
        }
        return list;
    }

    // Find bookings by status
    public static ArrayList<Booking> findByStatus(int status) {
        ArrayList<Booking> list = new ArrayList<>();
        String sql = "SELECT * FROM booking WHERE status = ?";
        try {
            MySqlDb db = new MySqlDb();
            PreparedStatement stmt = db.conn.prepareStatement(sql);
            stmt.setInt(1, status);
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Booking b = new Booking(
                        rs.getInt("user_id"),
                        rs.getInt("room_id"),
                        rs.getInt("status")
                );
                list.add(b);
            }
            rs.close();
            stmt.close();
            db.conn.close();
        } catch (SQLException e) {
            System.out.println("Find error: " + e.getMessage());
        }
        return list;
    }

    // get rooms by users
    public static ArrayList<Rooms> findByUser(int user_id) {
        ArrayList<Rooms> list = new ArrayList<>();
        String sql = "SELECT * FROM booking WHERE user_id = ?";
        try {
            MySqlDb db = new MySqlDb();
            PreparedStatement stmt = db.conn.prepareStatement(sql);
            stmt.setInt(1, user_id);
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Rooms b = new Rooms().find(rs.getInt("room_id"));
                list.add(b);
            }
            rs.close();
            stmt.close();
            db.conn.close();
        } catch (SQLException e) {
            System.out.println("Find error: " + e.getMessage());
        }
        return list;
    }

    // get rooms by users and status
    public static ArrayList<Rooms> findByUserAndStatus(int user_id, int status) {
        ArrayList<Rooms> list = new ArrayList<>();
        String sql = "SELECT * FROM booking WHERE user_id = ? and status = ?";
        try {
            MySqlDb db = new MySqlDb();
            PreparedStatement stmt = db.conn.prepareStatement(sql);
            stmt.setInt(1, user_id);
            stmt.setInt(2, status);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Rooms b = new Rooms().find(rs.getInt("room_id"));
                list.add(b);
            }
            rs.close();
            stmt.close();
            db.conn.close();
        } catch (SQLException e) {
            System.out.println("Find error: " + e.getMessage());
        }
        return list;
    }
    
    public static ArrayList<RoomDetails> LoadBookedRoomByOwner(int ownerId) {
        ArrayList<RoomDetails> roomdetails = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            MySqlDb db = new MySqlDb();
            conn = db.getConnection(); // however you manage your DB connection

            String sql = "SELECT r.id AS room_id, "
                    + "r.building_id, bk.user_id as customer_user_id, bk.created_at "
                    + "FROM buildings b "
                    + "JOIN rooms r ON r.building_id = b.id "
                    + "JOIN booking bk ON bk.room_id = r.id AND bk.status = 1 "
                    + "JOIN users u ON u.id = bk.user_id "
                    + "WHERE b.user_id = ?";
            
            ps = conn.prepareStatement(sql);
            ps.setInt(1, ownerId);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                Rooms room = new Rooms().find(rs.getInt("room_id"));
                Buildings b = new Buildings().find(rs.getInt("building_id"));
                Users user = new Users().find(rs.getInt("customer_user_id"));
                RoomDetails rd = new RoomDetails();
                
                rd.setBuilding(b);
                rd.setRoom(room);
                rd.setUser(user);
                rd.setCreatedAt(rs.getString("created_at"));
                roomdetails.add(rd);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception ignored) {
            }
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception ignored) {
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception ignored) {
            }
        }
        
        return roomdetails;
    }
}
