package businessLayer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Submission {
    private int submissionNo;
    private int quizNo;
    private Student student;
    private List<Answer> answers;
    private float totalMarksObtained;
    private String status;
    private LocalDateTime submissionDateTime;

    public Submission() {
        this.answers = new ArrayList<>();
        this.totalMarksObtained = 0;
        this.status = "Not Graded";
    }

    public Submission(int submissionNo, Student student) {
        this.submissionNo = submissionNo;
        this.student = student;
        this.answers = new ArrayList<>();
        this.totalMarksObtained = 0;
        this.status = "Not Graded";
    }

    // Getters and Setters
    public int getSubmissionNo() {
        return submissionNo;
    }

    public void setSubmissionNo(int submissionNo) {
        this.submissionNo = submissionNo;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public float getTotalMarksObtained() {
        return totalMarksObtained;
    }

    public void setTotalMarksObtained(float totalMarksObtained) {
        this.totalMarksObtained = totalMarksObtained;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void addAnswer(Answer a) {
        answers.add(a);
    }

    public boolean updateMarks(int qNo, float marks) {
        for (Answer answer : answers) {
            if (answer.getQuestionNumber() == qNo) {
                answer.setMarksObtained(marks);
                return true;
            }
        }
        return false;
    }

    public void calculateTotalMarks() {
        float total = 0;
        for (Answer answer : answers) {
            total += answer.getMarksObtained();
        }
        this.totalMarksObtained = total;
    }

	public int getQuizNo() {
		return quizNo;
	}

	public void setQuizNo(int quizNo) {
		this.quizNo = quizNo;
	}

	public LocalDateTime getSubmissionDateTime() {
		return submissionDateTime;
	}

	public void setSubmissionDateTime(LocalDateTime submissionDateTime) {
		this.submissionDateTime = submissionDateTime;
	}
}
