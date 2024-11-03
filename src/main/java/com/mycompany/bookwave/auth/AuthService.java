package com.mycompany.bookwave.auth;

import java.util.UUID;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.mycompany.bookwave.common.DTOs.UserDTO;
import com.mycompany.bookwave.users.controllers.UserExternalApi;
import com.mycompany.bookwave.users.controllers.UserInternalApi;
import com.mycompany.bookwave.users.entities.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserInternalApi userInternalApi;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        UserDetails user = userInternalApi.getUserByUsername(request.getUsername());
        String token = jwtService.getToken(user);
        return AuthResponse.builder()
                .token(token)
                .build();
    }

    public AuthResponse register(UserDTO userDTO) {

        User user =  userInternalApi.saveUser(userDTO);

        return AuthResponse.builder()
                .token(jwtService.getToken(user))
                .build();
    }

}
