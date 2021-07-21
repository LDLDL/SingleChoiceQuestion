package com.example.exam.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class QuestionId implements Serializable {
    private String question;

    public QuestionId(String question){
        this.question = question;
    }

    public QuestionId() {

    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
