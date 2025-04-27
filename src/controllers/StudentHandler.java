package controllers;

import java.time.LocalDateTime;
import java.util.List;

import businessLayer.*;
import businessLayer.Class;
import dbHandlers.*;

public class StudentHandler 
{
    private User student;
    private Class currentClass;
    private Quiz currentQuiz;
    private Submission currentSubmission;
	public User getStudent() {
		return student;
	}
	public void setStudent(User student) {
		this.student = student;
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
	public void setCurrentSubmission(Submission currentSubmission) {
		this.currentSubmission = currentSubmission;
	}
	
	//Business layer functions
	public boolean joinClass(String classCode)
	{
		return classDBH.getDBH().joinClass(classCode, this.student.getUsername());
	}
	
	public boolean leaveClass()
	{
		return classDBH.getDBH().leaveClassStudent(this.currentClass.getClassCode(), this.student.getUsername());
	}
	
	public List<Class> getClasses()
	{
		if(this.student == null)
			return null;
		return classDBH.getDBH().getClassesStudent(this.student.getUsername());
	}
	
	 public List<Submission_Quiz> getSubmission_Quizzes()
	 {
		 return quizDBH.getDBH().getSubmission_Quizzes(this.student.getUsername(), this.currentClass.getClassCode());
	 }
	 
	 public void startQuiz(int quizNo)
	 {
		 this.currentQuiz = quizDBH.getDBH().getQuiz(quizNo);
		 this.currentSubmission = new Submission();
		 this.currentSubmission.setQuizNo(quizNo);
		 this.currentSubmission.setSubmissionDateTime(LocalDateTime.now());
		 this.currentSubmission.setStatus("Not Graded");
		 Student s = new Student();
		 s.setUsername(this.getStudent().getUsername());
		 this.currentSubmission.setStudent(s);
		 int submNo = quizDBH.getDBH().initializeSubmission(currentSubmission);
		 this.currentSubmission.setSubmissionNo(submNo);
	 }
	 
	 public void viewSubmission(int quizNo)
	 {
		 this.currentQuiz = quizDBH.getDBH().getQuiz(quizNo);
		 int submNo = quizDBH.getDBH().getSubmissionNo(quizNo, this.student.getUsername());
		 this.currentSubmission = quizDBH.getDBH().getSubmission(submNo);	 
	 }
	 
	 public void saveSubmission()
	 {
		 quizDBH.getDBH().saveSubmission(currentSubmission);
	 }
	
}