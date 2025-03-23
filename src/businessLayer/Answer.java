package businessLayer;

public class Answer {
    private int questionNumber;
    private String answer;
    private float marksObtained;

    public Answer() {
    }

    public Answer(int questionNumber, String answer, float marksObtained) {
        this.questionNumber = questionNumber;
        this.answer = answer;
        this.marksObtained = marksObtained;
    }

    // Getters and Setters
    public int getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(int questionNumber) {
        this.questionNumber = questionNumber;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public float getMarksObtained() {
        return marksObtained;
    }

    public void setMarksObtained(float marksObtained) {
        this.marksObtained = marksObtained;
    }
}