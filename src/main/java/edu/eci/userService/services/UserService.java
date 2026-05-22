package edu.eci.userService.services;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import edu.eci.userService.dto.UserDTO;
import edu.eci.userService.entities.UserEntity;
import edu.eci.userService.exceptions.InvalidCredentialsException;
import edu.eci.userService.mappers.UserMapper;
import edu.eci.userService.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
            UserMapper userMapper,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserDTO> getAllUsers() {
        List<UserDTO> userDTO = new ArrayList<>();
        for (UserEntity entity : userRepository.findAll()) {
            userDTO.add(userMapper.toDTO(entity));
        }
        return userDTO;
    }

    public UserDTO getUserById(Long id) {
        UserDTO dto = new UserDTO();
        dto = userMapper.toDTO(userRepository.findById(id).orElse(null));
        return dto;
    }

    public UserDTO createUser(UserDTO userDTO) {
        if (userDTO == null) {
            throw new IllegalArgumentException("Request is required");
        }
        UserEntity entity = new UserEntity();
        entity.setName(userDTO.getName());
        entity.setEmail(userDTO.getEmail());
        entity.setBirthDate(userDTO.getBirthDate());
        entity.setSchoolRelation(userDTO.getSchoolRelation());
        entity.setAcademicLevel(userDTO.getAcademicLevel());
        entity.setProfessorType(userDTO.getProfessorType());
        entity.setAcademicProgram(userDTO.getAcademicProgram());
        entity.setSemester(userDTO.getSemester());
        entity.setIdentificationType(userDTO.getIdentificationType());
        entity.setIdentificationNumber(userDTO.getIdentificationNumber());
        entity.setPhone(userDTO.getPhone() != null ? userDTO.getPhone() : 0L);

        String rawPassword = userDTO.getPassword();
        if (rawPassword == null || rawPassword.isBlank()) {
            throw new IllegalArgumentException("Password is required");
        }
        entity.setPassword(passwordEncoder.encode(rawPassword));
        return userMapper.toDTO(userRepository.save(entity));
    }

    public UserDTO updateUser(Long id, UserDTO userDTO) {
        UserEntity entitie = userRepository.findById(id).orElse(null);
        if (entitie == null) {
            throw new NoSuchElementException("No user found with ID: " + id);
        }
        entitie.setName(userDTO.getName());
        entitie.setEmail(userDTO.getEmail());
        entitie.setBirthDate(userDTO.getBirthDate());
        entitie.setSchoolRelation(userDTO.getSchoolRelation());
        entitie.setAcademicLevel(userDTO.getAcademicLevel());
        entitie.setProfessorType(userDTO.getProfessorType());
        entitie.setAcademicProgram(userDTO.getAcademicProgram());
        entitie.setSemester(userDTO.getSemester());
        entitie.setIdentificationType(userDTO.getIdentificationType());
        entitie.setIdentificationNumber(userDTO.getIdentificationNumber());
        entitie.setPhone(userDTO.getPhone());
        if (userDTO.getPassword() != null && !userDTO.getPassword().isBlank()) {
            entitie.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
        return userMapper.toDTO(userRepository.save(entitie));
    }

    public UserDTO authenticate(String email, String rawPassword) {
        if (email == null || email.isBlank() || rawPassword == null || rawPassword.isBlank()) {
            throw new IllegalArgumentException("Email and password are required");
        }
        UserEntity entity = userRepository.findByEmail(email);
        if (entity == null || !passwordEncoder.matches(rawPassword, entity.getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }
        return userMapper.toDTO(entity);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new NoSuchElementException("No user found with ID: " + id);
        }
        userRepository.deleteById(id);
    }
}
