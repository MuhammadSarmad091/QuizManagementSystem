package dbHandlers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import businessLayer.Answer;
import businessLayer.Quiz;
import businessLayer.Student;
import businessLayer.Submission;

public class quizDBH {
    private static quizDBH instance;
    private DBManager dbManager;
    
    private quizDBH() 
    {
		this.dbManager=DBManager.getDBManager();
    }
    
    public static quizDBH getDBH() {
        if (instance == null) {
            instance = new quizDBH();
        }
        return instance;
    }
    
    public List<Quiz> getQuizzes(String classCode) {
        List<Quiz> quizzes = new ArrayList<>();
        Connection conn = null;
        try {
            conn = dbManager.connect();
            // Updated SQL to retrieve quizName as well.
            String sql = "SELECT quizNo, quizType, quizName, deadLine, totalMarks FROM Quiz WHERE classCode = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, classCode);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Quiz quiz = new Quiz();
                quiz.setQuizNo(rs.getInt("quizNo"));
                quiz.setType(rs.getString("quizType"));
                // Set the new name attribute from the database column quizName
                quiz.setName(rs.getString("quizName"));
                Timestamp ts = rs.getTimestamp("deadLine");
                if (ts != null) {
                    quiz.setDeadLine(ts.toLocalDateTime());
                }
                quiz.setTotalMarks(rs.getFloat("totalMarks"));
                quizzes.add(quiz);
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
        return quizzes;
    }


    public void createQuiz(Quiz q, String classCode) {
        Connection conn = null;
        try {
            conn = dbManager.connect();
            String sql = "INSERT INTO Quiz (classCode, quizType, deadLine, totalMarks) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, classCode);
            ps.setString(2, q.getType());
            // Convert LocalDateTime to SQL Timestamp
            ps.setTimestamp(3, Timestamp.valueOf(q.getDeadLine()));
            ps.setFloat(4, q.getTotalMarks());
            int rows = ps.executeUpdate();
            if (rows > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    q.setQuizNo(rs.getInt(1));
                }
                rs.close();
            }
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }

    public List<Submission> getSubmissions(int quizNo, String classCode) {
        List<Submission> submissions = new ArrayList<>();
        Connection conn = null;
        try {
            conn = dbManager.connect();
            String sql = "SELECT cs.studentUserName, s.submissionNo, s.submissionDateTime, s.submissionStatus, s.totalMarksObtained " +
                         "FROM ClassStudent cs " +
                         "LEFT JOIN Submission s ON cs.studentUserName = s.studentUserName AND s.quizNo = ? " +
                         "WHERE cs.classCode = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, quizNo);
            ps.setString(2, classCode);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Submission sub = new Submission();
                String studentUser = rs.getString("studentUserName");
                sub.setStudent(new Student(studentUser, "", "student"));
                int subNo = rs.getInt("submissionNo");
                if (rs.wasNull()) {
                    // No submission record exists for this student.
                    sub.setSubmissionNo(0);
                    sub.setStatus("None");
                    // Set default values for missing submission record.
                    sub.setSubmissionDateTime(null); // or LocalDateTime.now() or LocalDateTime.MIN
                    sub.setTotalMarksObtained(0);
                } else {
                    sub.setSubmissionNo(subNo);
                    sub.setStatus(rs.getString("submissionStatus"));
                    java.sql.Timestamp ts = rs.getTimestamp("submissionDateTime");
                    if (ts != null) {
                        sub.setSubmissionDateTime(ts.toLocalDateTime());
                    } else {
                        sub.setSubmissionDateTime(null); // set to default if desired
                    }
                    sub.setTotalMarksObtained(rs.getFloat("totalMarksObtained"));
                }
                submissions.add(sub);
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
        return submissions;
    }



    public Submission getSubmission(int submissionNo) {
        Submission s = null;
        Connection conn = null;
        try {
            conn = dbManager.connect();
            String sql = "SELECT quizNo, studentUserName, submissionStatus FROM Submission WHERE submissionNo = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, submissionNo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                s = new Submission();
                s.setSubmissionNo(submissionNo);
                s.setStatus(rs.getString("submissionStatus"));
                s.setStudent(new Student(rs.getString("studentUserName"), "", "student"));
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
        return s;
    }

    public void saveSubmission(Submission s) {
        Connection conn = null;
        try {
            conn = dbManager.connect();
            conn.setAutoCommit(false);
            
            boolean exists = false;
            if (s.getSubmissionNo() != 0) {
                String checkSql = "SELECT COUNT(*) FROM Submission WHERE submissionNo = ?";
                PreparedStatement checkPs = conn.prepareStatement(checkSql);
                checkPs.setInt(1, s.getSubmissionNo());
                ResultSet rs = checkPs.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    exists = true;
                }
                rs.close();
                checkPs.close();
            }
            
            if (!exists) {
                // For demonstration, assume quizNo is available. If not, modify Submission to include quizNo.
                int quizNo = 1; // TODO: Replace with s.getQuizNo() if available.
                String insertSql = "INSERT INTO Submission (quizNo, studentUserName, submissionDateTime, submissionStatus, totalMarksObtained) VALUES (?, ?, GETDATE(), ?, ?)";
                PreparedStatement insertPs = conn.prepareStatement(insertSql);
                insertPs.setInt(1, quizNo);
                insertPs.setString(2, s.getStudent().getUsername());
                insertPs.setString(3, s.getStatus());
                insertPs.setFloat(4, s.getTotalMarksObtained());
                int rows = insertPs.executeUpdate();
                if (rows > 0) {
                    ResultSet genKeys = insertPs.getGeneratedKeys();
                    if (genKeys.next()) {
                        s.setSubmissionNo(genKeys.getInt(1));
                    }
                    genKeys.close();
                }
                insertPs.close();
                
                // Insert each answer associated with this submission.
                for (Answer a : s.getAnswers()) {
                    String ansSql = "INSERT INTO Answer (submissionNo, questionNo, answer, marksObtained) VALUES (?, ?, ?, ?)";
                    PreparedStatement ansPs = conn.prepareStatement(ansSql);
                    ansPs.setInt(1, s.getSubmissionNo());
                    ansPs.setInt(2, a.getQuestionNumber());
                    ansPs.setString(3, a.getAnswer());
                    ansPs.setFloat(4, a.getMarksObtained());
                    ansPs.executeUpdate();
                    ansPs.close();
                }
            } else {
                // Update existing submission record.
                String updateSql = "UPDATE Submission SET submissionStatus = ?, totalMarksObtained = ? WHERE submissionNo = ?";
                PreparedStatement updatePs = conn.prepareStatement(updateSql);
                updatePs.setString(1, s.getStatus());
                updatePs.setFloat(2, s.getTotalMarksObtained());
                updatePs.setInt(3, s.getSubmissionNo());
                updatePs.executeUpdate();
                updatePs.close();
                
                // For each answer in the submission, check if it exists and update; if not, insert it.
                for (Answer a : s.getAnswers()) {
                    String checkAnsSql = "SELECT COUNT(*) FROM Answer WHERE submissionNo = ? AND questionNo = ?";
                    PreparedStatement checkAnsPs = conn.prepareStatement(checkAnsSql);
                    checkAnsPs.setInt(1, s.getSubmissionNo());
                    checkAnsPs.setInt(2, a.getQuestionNumber());
                    ResultSet rsAns = checkAnsPs.executeQuery();
                    boolean ansExists = false;
                    if (rsAns.next() && rsAns.getInt(1) > 0) {
                        ansExists = true;
                    }
                    rsAns.close();
                    checkAnsPs.close();
                    
                    if (ansExists) {
                        String updateAnsSql = "UPDATE Answer SET answer = ?, marksObtained = ? WHERE submissionNo = ? AND questionNo = ?";
                        PreparedStatement updateAnsPs = conn.prepareStatement(updateAnsSql);
                        updateAnsPs.setString(1, a.getAnswer());
                        updateAnsPs.setFloat(2, a.getMarksObtained());
                        updateAnsPs.setInt(3, s.getSubmissionNo());
                        updateAnsPs.setInt(4, a.getQuestionNumber());
                        updateAnsPs.executeUpdate();
                        updateAnsPs.close();
                    } else {
                        String insertAnsSql = "INSERT INTO Answer (submissionNo, questionNo, answer, marksObtained) VALUES (?, ?, ?, ?)";
                        PreparedStatement insertAnsPs = conn.prepareStatement(insertAnsSql);
                        insertAnsPs.setInt(1, s.getSubmissionNo());
                        insertAnsPs.setInt(2, a.getQuestionNumber());
                        insertAnsPs.setString(3, a.getAnswer());
                        insertAnsPs.setFloat(4, a.getMarksObtained());
                        insertAnsPs.executeUpdate();
                        insertAnsPs.close();
                    }
                }
            }
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}