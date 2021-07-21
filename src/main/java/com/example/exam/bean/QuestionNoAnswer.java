package com.example.exam.bean;

import com.example.exam.entity.Question;

public class QuestionNoAnswer {
    private String question;
    private String A;
    private String B;
    private String C;
    private String D;

    public QuestionNoAnswer(String question, String a, String b, String c, String d) {
        this.question = question;
        A = a;
        B = b;
        C = c;
        D = d;
    }

    public QuestionNoAnswer(Question q){
        question = q.getQuestion();
        A = q.getA();
        B = q.getB();
        C = q.getC();
        D = q.getD();
    }

    public QuestionNoAnswer() {

    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
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
}
