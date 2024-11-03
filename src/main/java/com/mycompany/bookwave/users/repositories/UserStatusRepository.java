package com.mycompany.bookwave.users.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mycompany.bookwave.users.entities.UserStatus;

public interface UserStatusRepository extends JpaRepository<UserStatus, Integer> {

    
}
