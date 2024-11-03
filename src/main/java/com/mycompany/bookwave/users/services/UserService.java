package com.mycompany.bookwave.users.services;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mycompany.bookwave.common.DTOs.MemberShipCardDTO;
import com.mycompany.bookwave.common.DTOs.UserDTO;
import com.mycompany.bookwave.orders.controllers.OrderInternalApi;
import com.mycompany.bookwave.users.controllers.UserExternalApi;
import com.mycompany.bookwave.users.controllers.UserInternalApi;
import com.mycompany.bookwave.users.entities.Gender;
import com.mycompany.bookwave.users.entities.MemberShipCard;
import com.mycompany.bookwave.users.entities.Municipality;
import com.mycompany.bookwave.users.entities.User;
import com.mycompany.bookwave.users.entities.UserStatus;
import com.mycompany.bookwave.users.repositories.GenderRepository;
import com.mycompany.bookwave.users.repositories.MemberShipCardRepository;
import com.mycompany.bookwave.users.repositories.MunicipalityRepository;
import com.mycompany.bookwave.users.repositories.UserRepository;
import com.mycompany.bookwave.users.repositories.UserStatusRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class UserService implements UserInternalApi, UserExternalApi {
    
    private final UserRepository userRepository;
    private final MunicipalityRepository municipalityRepository;
    private final GenderRepository genderRepository;
    private final UserStatusRepository userStatusRepository;
    private final MemberShipCardRepository memberShipCardRepository;
    private final OrderInternalApi orderInternalApi;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    // private final UserMapper userMapper;

    public UserService( UserRepository userRepository, 
                        MunicipalityRepository municipalityRepository, 
                        GenderRepository genderRepository, 
                        UserStatusRepository userStatusRepository,
                        MemberShipCardRepository memberShipCardRepository,
                        OrderInternalApi orderInternalApi
                        ) {
        this.userRepository = userRepository;
        this.municipalityRepository = municipalityRepository;
        this.genderRepository = genderRepository;
        this.userStatusRepository = userStatusRepository;
        this.memberShipCardRepository = memberShipCardRepository;
        this.orderInternalApi = orderInternalApi;
        // this.userMapper = userMapper;
    }

    @Override
    @Transactional
    public User saveUser(UserDTO userdDto) {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        try {
            UUID userId = UUID.randomUUID();

            User user = new User();
            user.setId(userId.toString());
            user.setUsername(userdDto.username());
            user.setFirstName(userdDto.firstName());
            user.setLastName(userdDto.lastName());
            user.setEmail(userdDto.email());
            user.setPhone(userdDto.phone());
            user.setProfession(userdDto.profession());
            Municipality municipality = municipalityRepository.findById(userdDto.municipality()).orElseThrow(
                () -> new EntityNotFoundException("Municipality not found")
            );
            user.setMunicipality(municipality);
            Gender gender = genderRepository.findById(userdDto.gender()).orElseThrow(
                () -> new EntityNotFoundException("Gender not found")
            );
            user.setGender(gender);
            UserStatus status = userStatusRepository.findById(userdDto.status()).orElseThrow(
                () -> new EntityNotFoundException("User status not found")
            );
            user.setStatus(status);
            user.setPassword(passwordEncoder.encode(userdDto.password()));
    
            User save = userRepository.save(user);
    
            UUID memberShipCardId = UUID.randomUUID();
            LocalDate date = LocalDate.now();
            LocalDate dateExpired = date.plusYears(2);
            MemberShipCard memberShipCard = new MemberShipCard();
            memberShipCard.setId(memberShipCardId.toString());
            memberShipCard.setPassword(userdDto.memberShipCard());
            memberShipCard.setQuota(new BigDecimal(0));
            memberShipCard.setExpirationDate(dateExpired);
            memberShipCard.setUser(user);
            memberShipCardRepository.save(memberShipCard);

            orderInternalApi.createShoppingCard(user);
    
            map.put("status", HttpStatus.CREATED);
            map.put("message", "User created successfully");
            map.put("data", save);
            return save;
        } catch (Exception e) {
            logger.error("Error creating user", e);
            map.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
            map.put("message", "Error creating user");
            map.put("error", e.getMessage());
            return null;
        }
    }

    @Override
    public ResponseEntity<Map<String, Object>> updateUser(UserDTO userDTO, String id) {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        
        try {
            User user = userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("User not found")
            );
            user.setFirstName(userDTO.firstName());
            user.setLastName(userDTO.lastName());
            user.setEmail(userDTO.email());
            user.setPhone(userDTO.phone());
            user.setProfession(userDTO.profession());
            Municipality municipality = municipalityRepository.findById(userDTO.municipality()).orElseThrow(
                () -> new EntityNotFoundException("Municipality not found")
            );
            user.setMunicipality(municipality);
            Gender gender = genderRepository.findById(userDTO.gender()).orElseThrow(
                () -> new EntityNotFoundException("Gender not found")
            );
            user.setGender(gender);
            userRepository.save(user);
            map.put("status", HttpStatus.OK);
            map.put("message", "User updated successfully");
            map.put("data", user);
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (Exception e) {
            map.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
            map.put("message", "Error updating user");
            map.put("error", e.getMessage());
            return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Map<String, Object>> updateMemberShipCard(MemberShipCardDTO memberShipCardDTO) {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        
        try {
            MemberShipCard memberShipCard = memberShipCardRepository.findById(memberShipCardDTO.id()).orElseThrow(
                () -> new EntityNotFoundException("MemberShipCard not found")
            );
            if(!memberShipCardDTO.cardNumber().equals(memberShipCard.getPassword())) {
                map.put("status", HttpStatus.BAD_REQUEST);
                map.put("message", "MemberShipCard number does not match");
                return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
            }
            memberShipCard.setQuota(memberShipCardDTO.quota());
            memberShipCardRepository.save(memberShipCard);
            map.put("status", HttpStatus.OK);
            map.put("message", "MemberShipCard updated successfully");
            map.put("data", memberShipCard);
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (Exception e) {
            map.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
            map.put("message", "Error updating MemberShipCard");
            map.put("error", e.getMessage());
            return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public MemberShipCard findMemberShipCartById(String id) {
        return memberShipCardRepository.findById(id).orElseThrow(
            () -> new EntityNotFoundException("MemberShipCard not found")
        );
    }

    @Override
    public void saveMemberShipCart(MemberShipCard memberShipCard) {
        memberShipCardRepository.save(memberShipCard);
    }

    @Override
    public User getUserById(String userId) {
        return userRepository.findById(userId).orElseThrow(
            () -> new EntityNotFoundException("User not found")
        );
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(
            () -> new EntityNotFoundException("User not found")
        );
    }
}
