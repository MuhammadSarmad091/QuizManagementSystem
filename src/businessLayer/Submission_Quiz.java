package businessLayer;

import java.util.Date;

public class Submission_Quiz {
    private int quizNo;
    private String name;
    private String type;
    private Date due;
    private int total_marks;
    private int marks_given;
    private String status;

    public Submission_Quiz() {}

    public Submission_Quiz(int quizNo, String name, String type, Date due, int total_marks, int marks_given, String status) {
        this.quizNo = quizNo;
        this.name = name;
        this.type = type;
        this.due = due;
        this.total_marks = total_marks;
        this.marks_given = marks_given;
        this.status = status;
    }

    public int getQuizNo() {
        return quizNo;
    }

    public void setQuizNo(int quizNo) {
        this.quizNo = quizNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDue() {
        return due;
    }

    public void setDue(Date due) {
        this.due = due;
    }

    public int getTotal_marks() {
        return total_marks;
    }

    public void setTotal_marks(int total_marks) {
        this.total_marks = total_marks;
    }

    public int getMarks_given() {
        return marks_given;
    }

    public void setMarks_given(int marks_given) {
        this.marks_given = marks_given;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
