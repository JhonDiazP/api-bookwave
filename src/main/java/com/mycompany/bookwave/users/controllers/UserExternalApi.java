package com.mycompany.bookwave.users.controllers;

import java.util.Map;
import org.springframework.http.ResponseEntity;

import com.mycompany.bookwave.common.DTOs.MemberShipCardDTO;
import com.mycompany.bookwave.common.DTOs.UserDTO;
import com.mycompany.bookwave.users.entities.User;

public interface UserExternalApi {
    ResponseEntity<Map<String,Object>> updateUser(UserDTO userDTO, String id);
    ResponseEntity<Map<String,Object>> updateMemberShipCard(MemberShipCardDTO memberShipCardDTO);
}
