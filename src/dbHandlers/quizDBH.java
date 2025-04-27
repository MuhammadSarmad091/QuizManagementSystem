package dbHandlers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import businessLayer.Answer;
import businessLayer.Question;
import businessLayer.Quiz;
import businessLayer.Student;
import businessLayer.Submission;
import businessLayer.Submission_Quiz;

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
            conn.setAutoCommit(false); // Start transaction

            // Step 1: Insert quiz and retrieve generated quizNo
            String quizSql = "INSERT INTO Quiz (classCode, quizType, deadLine, totalMarks, quizName) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement psQuiz = conn.prepareStatement(quizSql, Statement.RETURN_GENERATED_KEYS);
            psQuiz.setString(1, classCode);
            psQuiz.setString(2, q.getType());
            psQuiz.setTimestamp(3, Timestamp.valueOf(q.getDeadLine()));
            psQuiz.setFloat(4, q.getTotalMarks());
            psQuiz.setString(5, q.getName());
            psQuiz.executeUpdate();

            ResultSet rsKeys = psQuiz.getGeneratedKeys();
            int quizNo = -1;
            if (rsKeys.next()) {
                quizNo = rsKeys.getInt(1);
            }
            rsKeys.close();
            psQuiz.close();

            // Step 2: Insert questions
            if (quizNo != -1 && q.getQuestions() != null) {
                String qSql = "INSERT INTO Question (quizNo, questionNo, statementt, optA, optB, optC, optD, correctOption, questionType, marks) " +
                              "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement psQ = conn.prepareStatement(qSql);

                for (Question question : q.getQuestions()) {
                    psQ.setInt(1, quizNo);
                    psQ.setInt(2, question.getQuestionNo());
                    psQ.setString(3, question.getStatement());
                    psQ.setString(4, question.getOptA());
                    psQ.setString(5, question.getOptB());
                    psQ.setString(6, question.getOptC());
                    psQ.setString(7, question.getOptD());
                    psQ.setString(8, question.getCorrectAnswer());
                    psQ.setString(9, question.getType());
                    psQ.setFloat(10, question.getMarks());

                    psQ.addBatch();
                }

                psQ.executeBatch();
                psQ.close();
            }

            conn.commit(); // Commit transaction
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (conn != null) conn.rollback(); // Roll back if error occurs
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); }
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

            // Step 1: Fetch submission details
            String sqlSubmission = "SELECT quizNo, studentUserName, submissionDateTime, submissionStatus, totalMarksObtained " +
                                   "FROM Submission WHERE submissionNo = ?";
            PreparedStatement psSubmission = conn.prepareStatement(sqlSubmission);
            psSubmission.setInt(1, submissionNo);
            ResultSet rsSubmission = psSubmission.executeQuery();

            if (rsSubmission.next()) {
                s = new Submission();
                s.setSubmissionNo(submissionNo);
                s.setQuizNo(rsSubmission.getInt("quizNo"));
                s.setStudent(new Student(rsSubmission.getString("studentUserName"), "", "student"));
                s.setSubmissionDateTime(rsSubmission.getTimestamp("submissionDateTime").toLocalDateTime());
                s.setStatus(rsSubmission.getString("submissionStatus"));
                s.setTotalMarksObtained(rsSubmission.getFloat("totalMarksObtained"));
            }

            rsSubmission.close();
            psSubmission.close();

            // Step 2: Fetch answers only if submission exists
            if (s != null) {
                String sqlAnswers = "SELECT questionNo, answer, marksObtained FROM Answer WHERE submissionNo = ?";
                PreparedStatement psAnswers = conn.prepareStatement(sqlAnswers);
                psAnswers.setInt(1, submissionNo);
                ResultSet rsAnswers = psAnswers.executeQuery();

                while (rsAnswers.next()) {
                    Answer a = new Answer();
                    a.setQuestionNumber(rsAnswers.getInt("questionNo"));
                    a.setAnswer(rsAnswers.getString("answer"));
                    a.setMarksObtained(rsAnswers.getFloat("marksObtained"));
                    s.addAnswer(a);
                }

                rsAnswers.close();
                psAnswers.close();
            }

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
                int quizNo = s.getQuizNo(); // TODO: Replace with s.getQuizNo() if available.
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
    public Quiz getQuiz(int quizNo) {
        Quiz quiz = null;
        Connection conn = null;

        try {
            conn = dbManager.connect();

            // Step 1: Fetch quiz details
            String sqlQuiz = "SELECT quizName, quizType, deadLine, totalMarks FROM Quiz WHERE quizNo = ?";
            PreparedStatement psQuiz = conn.prepareStatement(sqlQuiz);
            psQuiz.setInt(1, quizNo);
            ResultSet rsQuiz = psQuiz.executeQuery();

            if (rsQuiz.next()) {
                quiz = new Quiz();
                quiz.setQuizNo(quizNo);
                quiz.setName(rsQuiz.getString("quizName"));
                quiz.setType(rsQuiz.getString("quizType"));
                quiz.setDeadLine(rsQuiz.getTimestamp("deadLine").toLocalDateTime());
                quiz.setTotalMarks(rsQuiz.getFloat("totalMarks"));
            }

            rsQuiz.close();
            psQuiz.close();

            // Step 2: Fetch questions if quiz exists
            if (quiz != null) {
                String sqlQuestions = "SELECT questionNo, questionType, statementt, optA, optB, optC, optD, correctOption, marks " +
                                      "FROM Question WHERE quizNo = ? ORDER BY questionNo";
                PreparedStatement psQ = conn.prepareStatement(sqlQuestions);
                psQ.setInt(1, quizNo);
                ResultSet rsQ = psQ.executeQuery();

                while (rsQ.next()) {
                    Question q = new Question();
                    q.setQuestionNo(rsQ.getInt("questionNo"));
                    q.setType(rsQ.getString("questionType"));
                    q.setStatement(rsQ.getString("statementt"));
                    q.setOptA(rsQ.getString("optA"));
                    q.setOptB(rsQ.getString("optB"));
                    q.setOptC(rsQ.getString("optC"));
                    q.setOptD(rsQ.getString("optD"));
                    q.setCorrectAnswer(rsQ.getString("correctOption"));
                    q.setMarks(rsQ.getFloat("marks"));

                    quiz.getQuestions().add(q);
                }

                rsQ.close();
                psQ.close();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }

        return quiz;
    }
    
    public List<Submission_Quiz> getSubmission_Quizzes(String studentUsername, String classCode) {
        List<Submission_Quiz> result = new ArrayList<>();
        String sql =
          "SELECT q.quizNo, q.quizName, q.quizType, q.deadLine, q.totalMarks,\n" +
          "       s.submissionStatus, s.totalMarksObtained\n" +
          "  FROM Quiz q\n" +
          "  LEFT JOIN Submission s\n" +
          "    ON q.quizNo = s.quizNo\n" +
          "   AND s.studentUserName = ?\n" +
          " WHERE q.classCode = ?";
        try (Connection conn = dbManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, studentUsername);
            ps.setString(2, classCode);
            try (ResultSet rs = ps.executeQuery()) {
                Date now = new Date();
                while (rs.next()) {
                    int quizNo      = rs.getInt("quizNo");
                    String name     = rs.getString("quizName");
                    String type     = rs.getString("quizType");
                    Date due        = new Date(rs.getTimestamp("deadLine").getTime());
                    int totalMarks  = Math.round(rs.getFloat("totalMarks"));
                    String subStatus = rs.getString("submissionStatus");
                    // may be NULL if no submission
                    Float subMarks = rs.getObject("totalMarksObtained") != null
                                     ? rs.getFloat("totalMarksObtained")
                                     : null;

                    Integer marksGiven;
                    String status;
                    if (subStatus != null) {
                        // there is a submission row
                        status     = subStatus;
                        marksGiven = Math.round(subMarks);
                    } else {
                        // no submission row
                        if (!now.after(due)) {
                            status     = "Assigned";
                            marksGiven = 0;
                        } else {
                            status     = "Missing";
                            marksGiven = 0;
                        }
                    }

                    Submission_Quiz sq = new Submission_Quiz(
                        quizNo,
                        name,
                        type,
                        due,
                        totalMarks,
                        marksGiven,
                        status
                    );
                    result.add(sq);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
    
    public int getSubmissionNo(int quizNo, String studentUsername) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = dbManager.connect();
            String query = "SELECT submissionNo FROM Submission WHERE quizNo = ? AND studentUserName = ?";
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, quizNo);
            stmt.setString(2, studentUsername);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("submissionNo");
            } else {
                return 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public int initializeSubmission(Submission s) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int submissionNo = 0;

        try {
            conn = dbManager.connect();

            String insertQuery = "INSERT INTO Submission (quizNo, studentUserName, submissionDateTime, submissionStatus, totalMarksObtained) " +
                                 "VALUES (?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, s.getQuizNo());
            stmt.setString(2, s.getStudent().getUsername());
            stmt.setTimestamp(3, Timestamp.valueOf(s.getSubmissionDateTime()));
            stmt.setString(4, s.getStatus());
            stmt.setFloat(5, s.getTotalMarksObtained());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating submission failed, no rows affected.");
            }

            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                submissionNo = rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return submissionNo;
    }



}