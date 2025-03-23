package businessLayer;

import java.util.ArrayList;
import java.util.List;

public class Class {
    private String classCode;
    private String className;
    private String classDescription;
    private List<Student> students;
    private List<Quiz> quizzes;
    private List<Teacher> teachers;
    private List<Submission> submissions;

    public Class() {
        this.students = new ArrayList<>();
        this.quizzes = new ArrayList<>();
        this.teachers = new ArrayList<>();
        this.submissions = new ArrayList<>();
    }

    public Class(String classCode, String className, String classDescription) {
        this.classCode = classCode;
        this.className = className;
        this.classDescription = classDescription;
        this.students = new ArrayList<>();
        this.quizzes = new ArrayList<>();
        this.teachers = new ArrayList<>();
        this.submissions = new ArrayList<>();
    }

    // Getters and Setters
    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassDescription() {
        return classDescription;
    }

    public void setClassDescription(String classDescription) {
        this.classDescription = classDescription;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public List<Quiz> getQuizzes() {
        return quizzes;
    }

    public void setQuizzes(List<Quiz> quizzes) {
        this.quizzes = quizzes;
    }

    public List<Teacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<Teacher> teachers) {
        this.teachers = teachers;
    }

    public List<Submission> getSubmissions() {
        return submissions;
    }

    public void setSubmissions(List<Submission> submissions) {
        this.submissions = submissions;
    }
}
