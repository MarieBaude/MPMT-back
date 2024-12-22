package com.example.MPMT.repository;

import java.util.List;

import com.example.MPMT.model.Projects;
import com.example.MPMT.model.Users;

import org.springframework.data.jpa.repository.JpaRepository;
public interface ProjectsRepository extends JpaRepository<Projects, Long> {
    List<Projects> findByCreatedByOrProjectRoles_User(Users createdBy, Users user);
}
