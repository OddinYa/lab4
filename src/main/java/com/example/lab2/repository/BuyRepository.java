package com.example.lab2.repository;

import com.example.lab2.model.entity.Buy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BuyRepository extends JpaRepository<Buy,Integer> {

    @Query("SELECT MAX(b.id) FROM Buy b")
    Optional<Integer> findMaxId();
}

