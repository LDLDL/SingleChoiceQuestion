package com.example.exam.repository;

import com.example.exam.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StudentRepository extends JpaRepository<Student, Long> {
    @Query(value="select * from student where name=?1 and ps=?2", nativeQuery = true)
    public Student findStudentByNameAndPwd(String name, String pwd);
}
