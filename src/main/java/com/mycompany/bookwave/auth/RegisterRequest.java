package com.mycompany.bookwave.auth;

import com.mycompany.bookwave.users.entities.Gender;
import com.mycompany.bookwave.users.entities.Municipality;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    String username;
    String first_name; 
    String last_name;
    Municipality municipality;
    Gender gender;
    String email;
    String phone;
    String password;
}
