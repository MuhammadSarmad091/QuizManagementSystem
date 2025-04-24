package dbHandlers;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import businessLayer.Class;
import businessLayer.Student;
import businessLayer.Teacher;
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
        Connection conn = null;
        try {
            conn = dbManager.connect();
            // First check if the teacher is already in the class.
            String checkSql = "SELECT COUNT(*) FROM ClassTeacher WHERE classCode = ? AND teacherUserName = ?";
            PreparedStatement checkPs = conn.prepareStatement(checkSql);
            checkPs.setString(1, classCode);
            checkPs.setString(2, teacherID);
            ResultSet rs = checkPs.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                if (count > 0) {
                    rs.close();
                    checkPs.close();
                    return false;
                }
            }
            rs.close();
            checkPs.close();
            
            // Teacher is not in the class; insert the record.
            String sql = "INSERT INTO ClassTeacher (classCode, teacherUserName) VALUES (?, ?)";
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
                try { 
                    conn.close(); 
                } catch (SQLException ex) { 
                    ex.printStackTrace(); 
                }
            }
        }
    }
    
    public List<Student> getStudentsInTheClass(String classCode) {
        List<Student> students = new ArrayList<>();
        Connection conn = null;
        try {
            conn = dbManager.connect();
            String sql = "SELECT studentUserName, name FROM ClassStudent cs INNER JOIN Userr u ON cs.studentUserName = u.username WHERE cs.classCode = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, classCode);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String username = rs.getString("studentUserName");
                String name = rs.getString("name");
                Student s = new Student(username, "", "student", name);
                students.add(s);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
        return students;
    }
    
    public List<Teacher> getTeachersInTheClass(String classCode) {
        List<Teacher> teachers = new ArrayList<>();
        Connection conn = null;
        try {
            conn = dbManager.connect();
            String sql = "SELECT teacherUserName, name FROM ClassTeacher ct INNER JOIN Userr u ON ct.teacherUserName = u.username WHERE ct.classCode = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, classCode);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String username = rs.getString("teacherUserName");
                String name = rs.getString("name");
                Teacher t = new Teacher(username, "", "teacher", name);
                teachers.add(t);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
        return teachers;
    }

    public boolean removeStudentFromClass(String classCode, String studentUsername) {
        Connection conn = null;
        try {
            conn = dbManager.connect();
            conn.setAutoCommit(false);

            // 1. Delete Answers associated with the student's submissions in quizzes for this class.
            String sqlDeleteAnswers =
                "DELETE FROM Answer " +
                "WHERE submissionNo IN (" +
                "    SELECT s.submissionNo " +
                "    FROM Submission s " +
                "    JOIN Quiz q ON s.quizNo = q.quizNo " +
                "    WHERE q.classCode = ? AND s.studentUserName = ?" +
                ")";
            try (PreparedStatement ps = conn.prepareStatement(sqlDeleteAnswers)) {
                ps.setString(1, classCode);
                ps.setString(2, studentUsername);
                ps.executeUpdate();
            }

            // 2. Delete Submissions for the student in quizzes for this class.
            String sqlDeleteSubmissions =
                "DELETE FROM Submission " +
                "WHERE studentUserName = ? " +
                "  AND quizNo IN (SELECT quizNo FROM Quiz WHERE classCode = ?)";
            try (PreparedStatement ps = conn.prepareStatement(sqlDeleteSubmissions)) {
                ps.setString(1, studentUsername);
                ps.setString(2, classCode);
                ps.executeUpdate();
            }

            // 3. Remove the student from the ClassStudent table.
            String sqlDeleteClassStudent =
                "DELETE FROM ClassStudent " +
                "WHERE classCode = ? AND studentUserName = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlDeleteClassStudent)) {
                ps.setString(1, classCode);
                ps.setString(2, studentUsername);
                ps.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return false;
        } finally {
            if (conn != null) {
                try {
                    // Reset auto-commit before closing the connection.
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean removeTeacherFromClass(String classCode, String teacherUsername) {
        try (Connection conn = dbManager.connect()) {
            String sql = "DELETE FROM ClassTeacher WHERE classCode = ? AND teacherUserName = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, classCode);
                ps.setString(2, teacherUsername);
                int rowsAffected = ps.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean joinClass(String classCode, String studentUserName) {
        Connection conn = null;
        PreparedStatement checkClassStmt = null;
        PreparedStatement checkStudentStmt = null;
        PreparedStatement insertStmt = null;
        ResultSet rs = null;

        try {
            conn = dbManager.connect();

            // Check if the class with this classCode exists
            String checkClassQuery = "SELECT 1 FROM Class WHERE classCode = ?";
            checkClassStmt = conn.prepareStatement(checkClassQuery);
            checkClassStmt.setString(1, classCode);
            rs = checkClassStmt.executeQuery();

            if (!rs.next()) {
                return false; // No such class exists
            }
            rs.close();
            checkClassStmt.close();

            // Check if the student is already in this class
            String checkStudentQuery = "SELECT 1 FROM ClassStudent WHERE classCode = ? AND studentUserName = ?";
            checkStudentStmt = conn.prepareStatement(checkStudentQuery);
            checkStudentStmt.setString(1, classCode);
            checkStudentStmt.setString(2, studentUserName);
            rs = checkStudentStmt.executeQuery();

            if (rs.next()) {
                return false; // Student is already enrolled
            }

            rs.close();
            checkStudentStmt.close();

            // Add student to class
            String insertQuery = "INSERT INTO ClassStudent (classCode, studentUserName) VALUES (?, ?)";
            insertStmt = conn.prepareStatement(insertQuery);
            insertStmt.setString(1, classCode);
            insertStmt.setString(2, studentUserName);
            insertStmt.executeUpdate();

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (rs != null) rs.close();
                if (checkClassStmt != null) checkClassStmt.close();
                if (checkStudentStmt != null) checkStudentStmt.close();
                if (insertStmt != null) insertStmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    
    public boolean leaveClassStudent(String classCode, String studentUsername) {
        Connection conn = null;
        PreparedStatement psDeleteSubs = null;
        PreparedStatement psDeleteClassStudent = null;
        try {
            conn = dbManager.connect();
            conn.setAutoCommit(false);

            String sqlDeleteSubs = 
                "DELETE FROM Submission " +
                "WHERE studentUserName = ? " +
                "  AND quizNo IN (SELECT quizNo FROM Quiz WHERE classCode = ?)";
            psDeleteSubs = conn.prepareStatement(sqlDeleteSubs);
            psDeleteSubs.setString(1, studentUsername);
            psDeleteSubs.setString(2, classCode);
            psDeleteSubs.executeUpdate();

            String sqlDeleteClassStudent = 
                "DELETE FROM ClassStudent WHERE classCode = ? AND studentUserName = ?";
            psDeleteClassStudent = conn.prepareStatement(sqlDeleteClassStudent);
            psDeleteClassStudent.setString(1, classCode);
            psDeleteClassStudent.setString(2, studentUsername);
            int rows = psDeleteClassStudent.executeUpdate();

            conn.commit();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            return false;
        } finally {
            try {
                if (psDeleteSubs != null) psDeleteSubs.close();
                if (psDeleteClassStudent != null) psDeleteClassStudent.close();
                if (conn != null)      conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public List<Class> getClassesStudent(String studentUsername) {
        List<Class> classes = new ArrayList<>();
        String sql = "SELECT c.classCode, c.className, c.classDesc " +
                     "FROM Class c INNER JOIN ClassStudent ct ON c.classCode = ct.classCode " +
                     "WHERE ct.studentUserName = ?";
        try (Connection conn = this.dbManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, studentUsername);
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


    
}
