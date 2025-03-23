package businessLayer;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

public class Quiz {
    private int quizNo;
    private String type;
    private String name;
    private List<Question> questions;
    private LocalDateTime deadLine;
    private float totalMarks;

    public Quiz() {
        this.questions = new ArrayList<>();
        this.totalMarks = 0;
    }

    public Quiz(int quizNo, String type, LocalDateTime deadLine) {
        this.quizNo = quizNo;
        this.type = type;
        this.deadLine = deadLine;
        this.questions = new ArrayList<>();
        this.totalMarks = 0;
    }

    public int getQuizNo() {
        return quizNo;
    }

    public void setQuizNo(int quizNo) {
        this.quizNo = quizNo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public LocalDateTime getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(LocalDateTime deadLine) {
        this.deadLine = deadLine;
    }

    public float getTotalMarks() {
        return totalMarks;
    }

    public void setTotalMarks(float totalMarks) {
        this.totalMarks = totalMarks;
    }

    public int findMaxQuestionNo() {
        int maxNo = 0;
        for (Question q : questions) {
            if (q.getQuestionNo() > maxNo) {
                maxNo = q.getQuestionNo();
            }
        }
        return maxNo;
    }

    public boolean addQuestion(Question q) {
        int maxNo = findMaxQuestionNo();
        q.setQuestionNo(maxNo + 1);
        totalMarks += q.getMarks();
        return questions.add(q);
    }

    public boolean removeQuestion(int qNo) {
        Question toRemove = null;
        for (Question q : questions) {
            if (q.getQuestionNo() == qNo) {
                toRemove = q;
                break;
            }
        }
        if (toRemove != null) {
            totalMarks -= toRemove.getMarks();
            questions.remove(toRemove);
            // Reassign question numbers
            for (int i = 0; i < questions.size(); i++) {
                questions.get(i).setQuestionNo(i + 1);
            }
            return true;
        }
        return false;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
