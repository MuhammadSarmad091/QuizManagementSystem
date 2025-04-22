package controllers;

import java.util.List;

import businessLayer.*;
import businessLayer.Class;
import dbHandlers.*;

public class TeacherHandler {
    private User teacher;
    private Class currentClass;
    private Quiz currentQuiz;
    private Submission currentSubmission;

    public TeacherHandler() {}

    public TeacherHandler(Teacher teacher, Class currentClass, Quiz currentQuiz, Submission currentSubmission) {
        this.teacher = teacher;
        this.currentClass = currentClass;
        this.currentQuiz = currentQuiz;
        this.currentSubmission = currentSubmission;
    }

    // Getters and Setters
    public User getTeacher() {
        return teacher;
    }

    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }

    public Class getCurrentClass() {
        return currentClass;
    }

    public void setCurrentClass(Class currentClass) {
        this.currentClass = currentClass;
    }

    public Quiz getCurrentQuiz() {
        return currentQuiz;
    }

    public void setCurrentQuiz(Quiz currentQuiz) {
        this.currentQuiz = currentQuiz;
    }

    public Submission getCurrentSubmission() {
        return currentSubmission;
    }

    public void setCurrentSubmission(Submission currentSubmission) 
    {
        this.currentSubmission = currentSubmission;
    }
    
    public String generateClassCode()
    {
    	return classDBH.getDBH().generateClassCode();
    }
    
    //Functions
    public void createClass(String classCode, String className, String classDescription) {
        Class c = new Class(classCode, className, classDescription);
        classDBH.getDBH().createClass(c);
    }
    public List<Class> getClasses(String teacherUsername) {
        return classDBH.getDBH().getClasses(teacherUsername);
    }
    
    public void removeClass(String classCode) {
        classDBH.getDBH().removeClass(classCode);
    }
    
    public List<Quiz> getQuizzes(String classCode) {
        return quizDBH.getDBH().getQuizzes(classCode);
    }
    
    public void createQuiz(String type) {
        this.currentQuiz = new Quiz();
        this.currentQuiz.setType(type);
    }
    
    public void addQuestion(String statement, String type, String optA, String optB, String optC, String optD, String correctAnswer, float marks) {
        if (this.currentQuiz != null) {
            Question q = new Question();
            q.setStatement(statement);
            q.setType(type);
            q.setOptA(optA);
            q.setOptB(optB);
            q.setOptC(optC);
            q.setOptD(optD);
            q.setCorrectAnswer(correctAnswer);
            q.setMarks(marks);
            this.currentQuiz.addQuestion(q);
        }
    }
    public void removeQuestion(int qNo) {
        if (this.currentQuiz != null) {
            this.currentQuiz.removeQuestion(qNo);
        }
    }
    
    public void saveQuiz() {
        if (this.currentQuiz != null) {
            quizDBH.getDBH().createQuiz(this.currentQuiz,this.currentClass.getClassCode());
        }
    }
    
    public List<Submission> getSubmissions(int quizNo, String ccode) {
        return quizDBH.getDBH().getSubmissions(quizNo, ccode);
    }
    
    public void openSubmission(int submissionNo) {
        this.currentSubmission = quizDBH.getDBH().getSubmission(submissionNo);
    }
    
    public boolean updateMarks(int qNo, float marks) {
        if (this.currentSubmission != null) {
            return this.currentSubmission.updateMarks(qNo, marks);
        }
        return false;
    }
    
    public void saveSubmission(int[] Qnos, float[] Marks) {
        if (this.currentSubmission != null) {
            for (int i = 0; i < Qnos.length; i++) {
                this.currentSubmission.updateMarks(Qnos[i], Marks[i]);
            }
            this.currentSubmission.setStatus("Graded");
            quizDBH.getDBH().saveSubmission(this.currentSubmission);
        }
    }
    
    public boolean addTeacherInClass(String teacherID, String classCode)
    {
    	return classDBH.getDBH().addTeacherInClass(teacherID, classCode);
    }
    
    public List<Student> getStudentsInTheClass(String classCode)
    {
    	return classDBH.getDBH().getStudentsInTheClass(classCode);
    }
    
    public List<Teacher> getTeachersInTheClass(String classCode)
    {
    	return classDBH.getDBH().getTeachersInTheClass(classCode);
    }
    
    public boolean removeStudentFromClass(String classCode, String studentUsername)
    {
    	return classDBH.getDBH().removeStudentFromClass(classCode, studentUsername);
    }
    public boolean removeTeacherFromClass(String classCode, String teacherUsername)
    {
    	if(teacherUsername.equalsIgnoreCase(this.teacher.getUsername()))
    	{
    		return false;
    	}
    	return classDBH.getDBH().removeTeacherFromClass(classCode, teacherUsername);
    }
    
    public List<Teacher> getAllTeachers()
    {
    	return userDBH.getDBH().getAllTeachers();
    }
    public List<Teacher> getTeacherByName(String name)
    {
    	return userDBH.getDBH().getTeacherByName(name);
    }
}