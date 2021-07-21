package com.example.exam.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name="library")
public class Question {
    @EmbeddedId
    @AttributeOverrides(
            @AttributeOverride(name = "question", column = @Column(name="question"))
    )
    private QuestionId question;

    @Column(name="A")
    private String A;
    @Column(name="B")
    private String B;
    @Column(name="C")
    private String C;
    @Column(name="D")
    private String D;
    private String answer;
    private String answer_id;

    public Question(String question, String a, String b, String c, String d, String answer, String answer_id) {
        this.question = new QuestionId(question);
        this.A = a;
        this.B = b;
        this.C = c;
        this.D = d;
        this.answer = answer;
        this.answer_id = answer_id;
    }

    public Question() {

    }

    public String getQuestion() {
        return question.getQuestion();
    }

    public void setQuestion(String question) {
        this.question.setQuestion(question);
    }

    public String getA() {
        return A;
    }

    public void setA(String a) {
        A = a;
    }

    public String getB() {
        return B;
    }

    public void setB(String b) {
        B = b;
    }

    public String getC() {
        return C;
    }

    public void setC(String c) {
        C = c;
    }

    public String getD() {
        return D;
    }

    public void setD(String d) {
        D = d;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAnswer_id() {
        return answer_id;
    }

    public void setAnswer_id(String answer_id) {
        this.answer_id = answer_id;
    }
}
