package com.mycompany.bookwave.users.controllers;

import com.mycompany.bookwave.common.DTOs.UserDTO;
import com.mycompany.bookwave.users.entities.MemberShipCard;
import com.mycompany.bookwave.users.entities.User;

public interface UserInternalApi {
    MemberShipCard findMemberShipCartById(String memberShipCart);
    void saveMemberShipCart(MemberShipCard memberShipCart);
    User getUserById(String userId);
    User saveUser(UserDTO userDTO);
    User getUserByUsername(String username);
}
