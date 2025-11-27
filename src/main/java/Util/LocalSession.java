package Util;
import com.google.gson.Gson;
import java.io.*;
import Models.Users;

public class LocalSession {

    private static final String FILE = "session.json";
    private static final Gson gson = new Gson();

    // Save logged-in user to local JSON
    public static void saveUser(Users user) {
        try (FileWriter writer = new FileWriter(FILE)) {
            gson.toJson(user, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load user from JSON (for auto-login)
    public static Users loadUser() {
        try (FileReader reader = new FileReader(FILE)) {
            return gson.fromJson(reader, Users.class);
        } catch (IOException e) {
            return null;
        }
    }

    // Clear session
    public static void clear() {
        File file = new File(FILE);
        if (file.exists()) file.delete();
    }
}
