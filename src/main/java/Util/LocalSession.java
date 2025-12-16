package Util;

import Models.Users;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.*;

public class LocalSession {

    private static final String FILE = "session.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private static Users currentUser = null; // <-- ADD THIS

    // Save logged-in user to JSON + memory
    public static void saveUser(Users user) {
        currentUser = user; // <-- Store in memory

        try (FileWriter writer = new FileWriter(FILE)) {
            gson.toJson(user, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load user from JSON (only once)
    public static Users loadUser() {
        if (currentUser != null) {
            return currentUser;
        }

        try (FileReader reader = new FileReader(FILE)) {
            currentUser = gson.fromJson(reader, Users.class);
            return currentUser;
        } catch (IOException e) {
            return null;
        }
    }

    // Clear session
    public static void clear() {
        currentUser = null;
        File f = new File(FILE);
        if (f.exists()) {
            f.delete();
        }
    }

    // GLOBAL ACCESS
    public static Users user() {
        return loadUser();
    }
}
