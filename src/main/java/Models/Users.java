package Models;

import Database.MySqlDb;
import Util.LocalSession;
import Util.Response;
import java.sql.*;
import java.util.ArrayList;
import org.mindrot.jbcrypt.BCrypt;

public class Users {

    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String password;
    private String userType;
    private String createdAt;

    public Users() {
    }

    public Users(String firstName, String lastName, String email, String username, String password, String userType) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.password = password;
        this.userType = userType;
    }

    // ==========================
    // LOGIN USER (BCrypt)
    // ==========================
    public static Response login(String username, String password) {
        String sql = "SELECT * FROM users WHERE username=?";

        try {
            MySqlDb db = new MySqlDb();
            PreparedStatement stmt = db.conn.prepareStatement(sql);
            stmt.setString(1, username);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {

                String storedHash = rs.getString("password");

                // Compare hashed password using BCrypt
                if (BCrypt.checkpw(password, storedHash)) {

                    // Successful login
                    Users user = new Users();
                    user.id = rs.getInt("id");
                    user.firstName = rs.getString("first_name");
                    user.lastName = rs.getString("last_name");
                    user.username = rs.getString("username");
                    user.userType = rs.getString("user_type");
                    user.email = rs.getString("email");
                    user.createdAt = rs.getString("created_at");

                    // Save user to session
                    LocalSession.saveUser(user);

                    return new Response(true, "Login successful!");

                } else {
                    return new Response(false, "Incorrect password!");
                }

            } else {
                return new Response(false, "Username not found!");
            }

        } catch (SQLException e) {
            return new Response(false, "Database error: " + e.getMessage());
        }
    }

    // ==========================
    // PASSWORD HASHING (BCrypt)
    // ==========================
    private static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    // ==========================
    // CREATE USER
    // ==========================
    public Response create() {
        String sql = "INSERT INTO users (first_name, last_name, email, username, password, user_type) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            MySqlDb db = new MySqlDb();
            PreparedStatement stmt = db.conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            String hashedPassword = hashPassword(this.password);

            stmt.setString(1, this.firstName);
            stmt.setString(2, this.lastName);
            stmt.setString(3, this.email);
            stmt.setString(4, this.username);
            stmt.setString(5, hashedPassword);
            stmt.setString(6, this.userType);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    this.id = rs.getInt(1);
                }
                return new Response(true, "User created successfully!");
            }

            return new Response(false, "Unknown error");

        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate entry")) {
                return new Response(false, "Username already exists!");
            }
            return new Response(false, e.getMessage());
        }
    }

    // ==========================
    // UPDATE USER
    // ==========================
    public Response update() {
        String sql = "UPDATE users SET first_name=?, last_name=?, email=?, username=?, password=?, user_type=? WHERE id=?";
        try {
            MySqlDb db = new MySqlDb();
            PreparedStatement stmt = db.conn.prepareStatement(sql);

            String hashedPassword = hashPassword(this.password);

            stmt.setString(1, this.firstName);
            stmt.setString(2, this.lastName);
            stmt.setString(3, this.email);
            stmt.setString(4, this.username);
            stmt.setString(5, hashedPassword);
            stmt.setString(6, this.userType);
            stmt.setInt(7, this.id);

            int updated = stmt.executeUpdate();
            if (updated > 0) {
                return new Response(true, "User updated successfully!");
            } else {
                return new Response(false, "No record updated.");
            }

        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate entry")) {
                return new Response(false, "Username already exists!");
            }
            return new Response(false, e.getMessage());
        }
    }

    // ==========================
    // DELETE USER
    // ==========================
    public Response delete() {
        String sql = "DELETE FROM users WHERE id=?";
        try {
            MySqlDb db = new MySqlDb();
            PreparedStatement stmt = db.conn.prepareStatement(sql);
            stmt.setInt(1, this.id);

            int deleted = stmt.executeUpdate();
            if (deleted > 0) {
                return new Response(true, "User deleted successfully!");
            } else {
                return new Response(false, "No record deleted.");
            }

        } catch (SQLException e) {
            return new Response(false, e.getMessage());
        }
    }

    // ==========================
    // FIND USER BY ID
    // ==========================
    public static Users find(int userId) {
        String sql = "SELECT * FROM users WHERE id=?";
        Users user = null;

        try {
            MySqlDb db = new MySqlDb();
            PreparedStatement stmt = db.conn.prepareStatement(sql);
            stmt.setInt(1, userId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                user = new Users();
                user.id = rs.getInt("id");
                user.firstName = rs.getString("first_name");
                user.lastName = rs.getString("last_name");
                user.email = rs.getString("email");
                user.username = rs.getString("username");
                user.password = rs.getString("password");
                user.userType = rs.getString("user_type");
                user.createdAt = rs.getString("created_at");
            }

        } catch (SQLException e) {
            System.out.println("Find failed: " + e.getMessage());
        }

        return user;
    }

    // ==========================
    // GET ALL USERS
    // ==========================
    public static ArrayList<Users> all() {
        String sql = "SELECT * FROM users";
        ArrayList<Users> list = new ArrayList<>();

        try {
            MySqlDb db = new MySqlDb();
            PreparedStatement stmt = db.conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Users u = new Users();
                u.id = rs.getInt("id");
                u.firstName = rs.getString("first_name");
                u.lastName = rs.getString("last_name");
                u.email = rs.getString("email");
                u.username = rs.getString("username");
                u.userType = rs.getString("user_type");
                u.createdAt = rs.getString("created_at");
                list.add(u);
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "Users{id=" + id + ", username=" + username + "}";
    }
}
