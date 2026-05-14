package edu.eci.userService.services;

import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.stereotype.Service;
import edu.eci.userService.repository.UserRepository;
import edu.eci.userService.mappers.UserMapper;
import edu.eci.userService.dto.UserDTO;
import edu.eci.userService.entities.UserEntity;

import java.util.*;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
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
        UserEntity entity = userMapper.toEntity(userDTO);
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
        entitie.setRole(userDTO.getRole());
        entitie.setRelationShip(userDTO.getRelationShip());
        entitie.setAcademicProgram(userDTO.getAcademicProgram());
        entitie.setSemester(userDTO.getSemester());
        entitie.setIdentificationType(userDTO.getIdentificationType());
        entitie.setIdentificationNumber(userDTO.getIdentificationNumber());
        entitie.setPhone(userDTO.getPhone());
        return userMapper.toDTO(userRepository.save(entitie));
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new NoSuchElementException("No user found with ID: " + id);
        }
        userRepository.deleteById(id);
    }
}
