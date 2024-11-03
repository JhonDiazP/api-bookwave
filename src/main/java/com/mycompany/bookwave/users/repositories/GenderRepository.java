package com.mycompany.bookwave.users.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mycompany.bookwave.users.entities.Gender;

public interface GenderRepository extends JpaRepository<Gender, Integer> {
    
}
