package com.example.MPMT.repository;
import com.example.MPMT.model.ProjectRole;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRoleRepository extends JpaRepository<ProjectRole, Long>{
    List<ProjectRole> findByProjectIdAndUserId(Long projectId, Long userId);
}