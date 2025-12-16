package Models;

import Database.MySqlDb;
import Util.Response;
import java.sql.*;
import java.util.ArrayList;

public class Rooms {

    private int id;
    private String room_name;
    private String room_details;
    private Double rent;
    private int building_id;
    private String created_at;

    public Rooms() {
    }

    public Rooms(String room_name, String room_details, double rent, int building_id) {
        this.room_name = room_name;
        this.room_details = room_details;
        this.rent = rent;
        this.building_id = building_id;
    }

    // ==========================
    // CREATE BUILDING
    // ==========================
    public Response create() {
        String sql = "INSERT INTO `rooms` (`room_name`, `room_details`, `rent`, `building_id`) VALUES (?, ?, ?, ?)";
        try {
            MySqlDb db = new MySqlDb();
            PreparedStatement stmt = db.conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, this.room_name);
            stmt.setString(2, this.room_details);
            stmt.setDouble(3, this.rent);
            stmt.setInt(4, this.building_id);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    this.id = rs.getInt(1);
                }
                return new Response(true, "Building created successfully!");
            }
            return new Response(false, "Unknown error");

        } catch (SQLException e) {
            return new Response(false, e.getMessage());
        }
    }

    // ==========================
    // UPDATE BUILDING
    // ==========================
    public Response update() {
        String sql = "UPDATE rooms SET room_name=?, room_details=?, rent=?, building_id=? WHERE id=?";
        try {
            MySqlDb db = new MySqlDb();
            PreparedStatement stmt = db.conn.prepareStatement(sql);

            stmt.setString(1, this.room_name);
            stmt.setString(2, this.room_details);
            stmt.setDouble(3, this.rent);
            stmt.setInt(4, this.building_id);
            stmt.setInt(5, this.id);

            int updated = stmt.executeUpdate();
            if (updated > 0) {
                return new Response(true, "Building updated successfully!");
            } else {
                return new Response(false, "No record updated.");
            }

        } catch (SQLException e) {
            return new Response(false, e.getMessage());
        }
    }

    // ==========================
    // DELETE BUILDING
    // ==========================
    public Response delete() {
        String sql = "DELETE FROM rooms WHERE id=?";
        try {
            MySqlDb db = new MySqlDb();
            PreparedStatement stmt = db.conn.prepareStatement(sql);
            stmt.setInt(1, this.id);

            int deleted = stmt.executeUpdate();
            if (deleted > 0) {
                return new Response(true, "Building deleted successfully!");
            } else {
                return new Response(false, "No record deleted.");
            }

        } catch (SQLException e) {
            return new Response(false, e.getMessage());
        }
    }

    // ==========================
    // FIND BUILDING BY ID
    // ==========================
    public static Rooms find(int roomId) {
        String sql = "SELECT * FROM rooms WHERE id=?";
        Rooms rooms = null;

        try {
            MySqlDb db = new MySqlDb();
            PreparedStatement stmt = db.conn.prepareStatement(sql);
            stmt.setInt(1, roomId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                rooms = new Rooms();
                rooms.id = rs.getInt("id");
                rooms.room_name = rs.getString("room_name");
                rooms.room_details = rs.getString("room_details");
                rooms.rent = rs.getDouble("rent");
                rooms.building_id = rs.getInt("building_id");
            }

        } catch (SQLException e) {
            System.out.println("Find failed: " + e.getMessage());
        }

        return rooms;
    }

    // ==========================
// GET BUILDINGS BY USER ID
// ==========================
    public static ArrayList<Rooms> byBuildingId(int buildingID) {
        String sql = "SELECT * FROM rooms WHERE building_id=?";
        ArrayList<Rooms> list = new ArrayList<>();

        try {
            MySqlDb db = new MySqlDb();
            PreparedStatement stmt = db.conn.prepareStatement(sql);
            stmt.setInt(1, buildingID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Rooms rooms = new Rooms();
                rooms.id = rs.getInt("id");
                rooms.room_name = rs.getString("room_name");
                rooms.room_details = rs.getString("room_details");
                rooms.rent = rs.getDouble("rent");
                rooms.building_id = rs.getInt("building_id");
                rooms.created_at = rs.getString("created_at");
                list.add(rooms);
            }

        } catch (SQLException e) {
            System.out.println("Fetch error: " + e.getMessage());
        }

        return list;
    }

    // ==========================
    // GET ALL ROOMS
    // ==========================
    public static ArrayList<Rooms> all() {
        String sql = "SELECT * FROM rooms";
        ArrayList<Rooms> list = new ArrayList<>();

        try {
            MySqlDb db = new MySqlDb();
            PreparedStatement stmt = db.conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Rooms b = new Rooms();
                b.id = rs.getInt("id");
                b.room_name = rs.getString("room_name");
                b.room_details = rs.getString("room_details");
                b.rent = rs.getDouble("rent");
                b.building_id = rs.getInt("building_id");
                b.created_at = rs.getString("created_at");
                list.add(b);
            }

        } catch (SQLException e) {
            System.out.println("Fetch error: " + e.getMessage());
        }

        return list;
    }

    // ==========================
    // GETTERS & SETTERS
    // ==========================
    public int getId() {
        return id;
    }

    public String getRoomName() {
        return room_name;
    }

    public void setBuildingName(String room_name) {
        this.room_name = room_name;
    }

    public String getRoomDetails() {
        return room_details;
    }

    public void setRoomDetails(String room_details) {
        this.room_details = room_details;
    }

    public double getRent() {
        return rent;
    }

    public void setRent(double rent) {
        this.rent = rent;
    }

    public int getBuildingId() {
        return building_id;
    }

    public String getCreatedAt() {
        return created_at;
    }

}
