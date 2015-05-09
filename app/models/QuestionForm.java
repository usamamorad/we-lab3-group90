package models;

import at.ac.tuwien.big.we15.lab2.api.Answer;

import java.util.List;

public class QuestionForm {

    private List<Integer> answers;  //chosen answers

    public List<Integer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Integer> answers) {
        this.answers = answers;
    }

    @Override
    public String toString() {
        return "QuestionForm{" +
                "answers=" + answers +
                '}';
    }
}
