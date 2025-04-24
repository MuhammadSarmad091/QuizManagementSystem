package controllers;

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
}