package com.example.exam.repository;

import com.example.exam.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    @Query(value="select * from admin where name=?1 and ps=?2", nativeQuery = true)
    public Admin findAdminByNameAndPwd(String name, String pwd);
}
