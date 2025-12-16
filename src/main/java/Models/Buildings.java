package Models;

import Database.MySqlDb;
import Util.Response;
import java.sql.*;
import java.util.ArrayList;

public class Buildings {

    private int id;
    private String buildingName;
    private String address;
    private String paymentDetails;
    private int userId;
    private String policy;
    private String createdAt;

    public Buildings() {
    }

    public Buildings(String buildingName, String address, String paymentDetails, int userId, String policy) {
        this.buildingName = buildingName;
        this.address = address;
        this.paymentDetails = paymentDetails;
        this.userId = userId;
        this.policy = policy;
    }

    // ==========================
    // CREATE BUILDING
    // ==========================
    public Response create() {
        String sql = "INSERT INTO buildings (building_name, address, payment_details, user_id, policy) VALUES (?, ?, ?, ?, ?)";
        try {
            MySqlDb db = new MySqlDb();
            PreparedStatement stmt = db.conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, this.buildingName);
            stmt.setString(2, this.address);
            stmt.setString(3, this.paymentDetails);
            stmt.setInt(4, this.userId);
            stmt.setString(5, this.policy);

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
        String sql = "UPDATE buildings SET building_name=?, address=?, payment_details=?, user_id=?, policy=? WHERE id=?";
        try {
            MySqlDb db = new MySqlDb();
            PreparedStatement stmt = db.conn.prepareStatement(sql);

            stmt.setString(1, this.buildingName);
            stmt.setString(2, this.address);
            stmt.setString(3, this.paymentDetails);
            stmt.setInt(4, this.userId);
            stmt.setString(5, this.policy);
            stmt.setInt(6, this.id);

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
        String sql = "DELETE FROM buildings WHERE id=?";
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
    public static Buildings find(int buildingId) {
        String sql = "SELECT * FROM buildings WHERE id=?";
        Buildings building = null;

        try {
            MySqlDb db = new MySqlDb();
            PreparedStatement stmt = db.conn.prepareStatement(sql);
            stmt.setInt(1, buildingId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                building = new Buildings();
                building.id = rs.getInt("id");
                building.buildingName = rs.getString("building_name");
                building.address = rs.getString("address");
                building.paymentDetails = rs.getString("payment_details");
                building.userId = rs.getInt("user_id");
                building.policy = rs.getString("policy");
                building.createdAt = rs.getString("created_at");
            }

        } catch (SQLException e) {
            System.out.println("Find failed: " + e.getMessage());
        }

        return building;
    }

    // ==========================
// GET BUILDINGS BY USER ID
// ==========================
    public static ArrayList<Buildings> byUserId(int userId) {
        String sql = "SELECT * FROM buildings WHERE user_id=?";
        ArrayList<Buildings> list = new ArrayList<>();

        try {
            MySqlDb db = new MySqlDb();
            PreparedStatement stmt = db.conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Buildings b = new Buildings();
                b.id = rs.getInt("id");
                b.buildingName = rs.getString("building_name");
                b.address = rs.getString("address");
                b.paymentDetails = rs.getString("payment_details");
                b.userId = rs.getInt("user_id");
                b.policy = rs.getString("policy");
                b.createdAt = rs.getString("created_at");
                list.add(b);
            }

        } catch (SQLException e) {
            System.out.println("Fetch error: " + e.getMessage());
        }

        return list;
    }

    // ==========================
    // GET ALL BUILDINGS
    // ==========================
    public static ArrayList<Buildings> all() {
        String sql = "SELECT * FROM buildings";
        ArrayList<Buildings> list = new ArrayList<>();

        try {
            MySqlDb db = new MySqlDb();
            PreparedStatement stmt = db.conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Buildings b = new Buildings();
                b.id = rs.getInt("id");
                b.buildingName = rs.getString("building_name");
                b.address = rs.getString("address");
                b.paymentDetails = rs.getString("payment_details");
                b.userId = rs.getInt("user_id");
                b.policy = rs.getString("policy");
                b.createdAt = rs.getString("created_at");
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

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPaymentDetails() {
        return paymentDetails;
    }

    public void setPaymentDetails(String paymentDetails) {
        this.paymentDetails = paymentDetails;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPolicy() {
        return policy;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return buildingName; // or buildingName + " (" + id + ")"
    }

}
