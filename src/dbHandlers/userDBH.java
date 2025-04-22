package dbHandlers;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import businessLayer.Class;
import businessLayer.Teacher;
import businessLayer.User;
public class userDBH {
    private static userDBH instance;
    private DBManager dbManager;

    
    private userDBH() 
    {
		this.dbManager=DBManager.getDBManager();
    }
    
    public static userDBH getDBH() {
        if (instance == null) {
            instance = new userDBH();
        }
        return instance;
    }
    
    public User logIn(String username, String password) {
        User user = null;
        Connection conn = null;
        try {
            conn = dbManager.connect();
            String sql = "SELECT username, password, name, userType FROM Userr WHERE username = ? AND password = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                // Create a User object using the retrieved data.
                user = new User(rs.getString("username"), 
                                rs.getString("password"), 
                                rs.getString("userType"),
                                rs.getString("name"));
                // Note: If you need to use the 'name' field, consider extending your User class.
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return user;
    }

    public User signUp(String username, String password, String name, String type) {
        User user = null;
        Connection conn = null;
        try {
            conn = dbManager.connect();
            // Check if the username already exists.
            String checkSql = "SELECT COUNT(*) FROM Userr WHERE username = ?";
            PreparedStatement checkPs = conn.prepareStatement(checkSql);
            checkPs.setString(1, username);
            ResultSet checkRs = checkPs.executeQuery();
            if (checkRs.next() && checkRs.getInt(1) > 0) {
                // Username already exists, so we return null.
                return null;
            }
            checkRs.close();
            checkPs.close();
            
            // Insert the new user.
            String sql = "INSERT INTO Userr (username, password, name, userType) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, name);
            ps.setString(4, type);
            int rows = ps.executeUpdate();
            ps.close();
            
            if (rows > 0) {
                user = new User(username, password, type);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return user;
    }

    public List<Teacher> getAllTeachers() {
        List<Teacher> teachers = new ArrayList<>();
        Connection conn = null;
        try {
            conn = dbManager.connect();
            String sql = "SELECT username, password, name, userType FROM Userr WHERE userType = 'teacher'";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Teacher teacher = new Teacher(
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("userType"),
                        rs.getString("name")
                );
                teachers.add(teacher);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try { 
                    conn.close(); 
                } catch (SQLException e) { 
                    e.printStackTrace(); 
                }
            }
        }
        return teachers;
    }
    
    // Returns a list of teachers whose name matches the provided pattern.
    public List<Teacher> getTeacherByName(String namePattern) {
        List<Teacher> teachers = new ArrayList<>();
        Connection conn = null;
        try {
            conn = dbManager.connect();
            String sql = "SELECT username, password, name, userType FROM Userr WHERE userType = 'teacher' AND name LIKE ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + namePattern + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Teacher teacher = new Teacher(
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("userType"),
                        rs.getString("name")
                );
                teachers.add(teacher);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try { 
                    conn.close(); 
                } catch (SQLException e) { 
                    e.printStackTrace(); 
                }
            }
        }
        return teachers;
    }
}
