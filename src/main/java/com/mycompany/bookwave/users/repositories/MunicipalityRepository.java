package com.mycompany.bookwave.users.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mycompany.bookwave.users.entities.Municipality;

public interface MunicipalityRepository extends JpaRepository<Municipality, Integer> {
    
}
