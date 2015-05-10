package models;

import java.util.ArrayList;
import java.util.List;

public class QuestionForm {

    private List<Integer> answers = new ArrayList<Integer>();  //chosen answers

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
