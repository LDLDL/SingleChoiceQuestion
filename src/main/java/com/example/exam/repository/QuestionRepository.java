package com.example.exam.repository;

import com.example.exam.entity.Question;
import com.example.exam.entity.QuestionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, QuestionId> {

    @Query(value = "select * from library order by random() limit ?1", nativeQuery = true)
    public List<Question> getExam(int n);

    public void deleteByQuestion(QuestionId question);
}
