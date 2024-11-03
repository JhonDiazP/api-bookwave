package com.mycompany.bookwave.users.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mycompany.bookwave.users.entities.MemberShipCard;

public interface MemberShipCardRepository extends JpaRepository<MemberShipCard, String> {
    
}
