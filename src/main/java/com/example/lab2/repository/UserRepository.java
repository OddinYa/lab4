package com.example.lab2.repository;

import com.example.lab2.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    @Query("SELECT MAX(u.id) FROM User u")
    Optional<Integer> findMaxId();
}
