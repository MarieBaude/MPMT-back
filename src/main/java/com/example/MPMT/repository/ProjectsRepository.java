package com.example.MPMT.repository;

import com.example.MPMT.model.Projects;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectsRepository extends JpaRepository<Projects, Long> {
    
}
