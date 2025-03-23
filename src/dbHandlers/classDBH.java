package dbHandlers;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import businessLayer.Class;
public class classDBH {
    private static classDBH instance;
    private DBManager dbManager;

    
    private classDBH() 
    {
		this.dbManager=DBManager.getDBManager();
    }
    
    public static classDBH getDBH() {
        if (instance == null) {
            instance = new classDBH();
        }
        return instance;
    }
    
    public String generateClassCode() {
        String code = null;
        boolean isUnique = false;
        while (!isUnique) {
            code = generateRandomCode(10);
            // Check if this code exists in the database.
            String sql = "SELECT COUNT(*) FROM Class WHERE classCode = ?";
            try (Connection conn = this.dbManager.connect();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, code);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        int count = rs.getInt(1);
                        if (count == 0) {
                            isUnique = true;
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                // Optionally handle exception (retry or break)
            }
        }
        return code;
    }
    
    // Private helper method to generate a random alphanumeric string of a given length.
    private String generateRandomCode(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
    
    // Creates a new Class record in the database.
    public boolean createClass(Class c) {
        // Generate a class code if not provided.
        if (c.getClassCode() == null || c.getClassCode().isEmpty()) {
            c.setClassCode(generateClassCode());
        }
        String sql = "INSERT INTO Class (classCode, className, classDesc) VALUES (?, ?, ?)";
        try (Connection conn = this.dbManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getClassCode());
            ps.setString(2, c.getClassName());
            ps.setString(3, c.getClassDescription());
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Retrieves the list of classes associated with a teacher.
    public List<Class> getClasses(String teacherUsername) {
        List<Class> classes = new ArrayList<>();
        String sql = "SELECT c.classCode, c.className, c.classDesc " +
                     "FROM Class c INNER JOIN ClassTeacher ct ON c.classCode = ct.classCode " +
                     "WHERE ct.teacherUserName = ?";
        try (Connection conn = this.dbManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, teacherUsername);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String classCode = rs.getString("classCode");
                    String className = rs.getString("className");
                    String classDesc = rs.getString("classDesc");
                    Class cls = new Class(classCode, className, classDesc);
                    classes.add(cls);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return classes;
    }
    
    // Removes a class from the database.
    public boolean removeClass(String classCode) {
        String sql = "DELETE FROM Class WHERE classCode = ?";
        try (Connection conn = this.dbManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, classCode);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean addTeacherInClass(String teacherID, String classCode) {
        String sql = "INSERT INTO ClassTeacher (classCode, teacherUserName) VALUES (?, ?)";
        Connection conn = null;
        try {
            conn = dbManager.connect();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, classCode);
            ps.setString(2, teacherID);
            int rows = ps.executeUpdate();
            ps.close();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
        }
    }
    
}
