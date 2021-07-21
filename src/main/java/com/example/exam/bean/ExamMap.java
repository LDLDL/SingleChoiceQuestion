package com.example.exam.bean;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ExamMap {
    private Map<Long, String> exams;

    public ExamMap() {
        this.exams = new HashMap<Long, String>();
    }

    public String getExamAnswer(Long id) {
        return this.exams.get(id);
    }

    public String putExamAnswer(Long id, String answer) {
        return this.exams.put(id, answer);
    }

    public String deleteExamAnswer(Long id) {
        return this.exams.remove(id);
    }
}
