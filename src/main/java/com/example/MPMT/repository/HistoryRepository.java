package com.example.MPMT.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.MPMT.model.History;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long> {
    List<History> findByTaskId(Long taskId);
}