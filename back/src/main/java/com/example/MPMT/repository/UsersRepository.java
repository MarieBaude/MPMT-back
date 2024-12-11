package com.example.MPMT.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.MPMT.model.Users;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
}
