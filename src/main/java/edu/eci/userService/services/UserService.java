package edu.eci.userService.services;

import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;
import edu.eci.userService.repository.UserRepository;
import edu.eci.userService.mappers.UserMapper;
import edu.eci.userService.dto.UserDTO;
import edu.eci.userService.entities.UserEntity;
import edu.eci.userService.enums.UserRole;
import edu.eci.userService.enums.UserStatus;
import edu.eci.userService.enums.SchoolRelation;
import edu.eci.userService.exception.UserNotFoundException;
import edu.eci.userService.validation.ValidationUtils;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserDTO getUserById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toDTO)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    public UserDTO getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(userMapper::toDTO)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    public UserDTO createUser(UserDTO userDTO) {
        // Validaciones
        validateUserDTO(userDTO);

        UserEntity entity = userMapper.toEntity(userDTO);
        entity.setStatus(UserStatus.ACTIVE);
        entity.setRole(UserRole.STUDENT);

        UserEntity savedEntity = userRepository.save(entity);
        return userMapper.toDTO(savedEntity);
    }

    public UserDTO updateUser(Long id, UserDTO userDTO) {
        UserEntity entity = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        // Validaciones
        validateUserDTO(userDTO);

        // Actualizar campos permitidos (no email ni password)
        if (userDTO.getFullName() != null) {
            entity.setFullName(userDTO.getFullName());
        }
        if (userDTO.getBirthDate() != null) {
            entity.setBirthDate(userDTO.getBirthDate());
        }
        if (userDTO.getPhone() != null) {
            entity.setPhone(userDTO.getPhone());
        }
        if (userDTO.getSchoolRelation() != null) {
            entity.setSchoolRelation(userDTO.getSchoolRelation());
        }
        if (userDTO.getAcademicLevel() != null) {
            entity.setAcademicLevel(userDTO.getAcademicLevel());
        }
        if (userDTO.getSemester() != null) {
            entity.setSemester(userDTO.getSemester());
        }
        if (userDTO.getAcademicProgram() != null) {
            entity.setAcademicProgram(userDTO.getAcademicProgram());
        }
        if (userDTO.getProfessionalChair() != null) {
            entity.setProfessionalChair(userDTO.getProfessionalChair());
        }
        if (userDTO.getPlantType() != null) {
            entity.setPlantType(userDTO.getPlantType());
        }

        UserEntity savedEntity = userRepository.save(entity);
        return userMapper.toDTO(savedEntity);
    }

    public void deactivateUser(Long id) {
        UserEntity entity = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        if (entity.getStatus() == UserStatus.INACTIVE) {
            throw new IllegalArgumentException("User is already inactive");
        }

        // TODO: Verificar con Teams Service que no esté en torneo activo
        entity.setStatus(UserStatus.INACTIVE);
        userRepository.save(entity);
    }

    public void convertToOrganizer(Long id) {
        UserEntity entity = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        if (entity.getRole() == UserRole.ORGANIZER) {
            throw new IllegalArgumentException("User is already an organizer");
        }

        entity.setRole(UserRole.ORGANIZER);
        userRepository.save(entity);
    }

    private void validateUserDTO(UserDTO userDTO) {
        // Validar nombre
        if (userDTO.getFullName() != null && !ValidationUtils.isValidName(userDTO.getFullName())) {
            throw new IllegalArgumentException(ValidationUtils.getValidationErrorMessage("name", ""));
        }

        // Validar email
        if (userDTO.getEmail() != null && !ValidationUtils.isValidEmail(userDTO.getEmail())) {
            throw new IllegalArgumentException(ValidationUtils.getValidationErrorMessage("email", ""));
        }

        // Validar edad
        if (userDTO.getBirthDate() != null) {
            int age = Period.between(userDTO.getBirthDate(), LocalDate.now()).getYears();
            if (!ValidationUtils.isValidAge(age)) {
                throw new IllegalArgumentException(ValidationUtils.getValidationErrorMessage("age", ""));
            }
        }

        // Validar lógica condicional de campos
        if (userDTO.getSchoolRelation() != null) {
            validateConditionalFields(userDTO);
        }
    }

    private void validateConditionalFields(UserDTO userDTO) {
        SchoolRelation relation = userDTO.getSchoolRelation();

        switch (relation) {
            case STUDENT:
                if (userDTO.getAcademicProgram() == null || userDTO.getAcademicProgram().isEmpty()) {
                    throw new IllegalArgumentException("Academic program is required for students");
                }
                if (userDTO.getAcademicLevel() == null) {
                    throw new IllegalArgumentException("Academic level is required for students");
                }
                // Semestre solo para pregrado
                if (userDTO.getAcademicLevel().toString().equals("UNDERGRADUATE") &&
                        (userDTO.getSemester() == null || userDTO.getSemester() < 1 || userDTO.getSemester() > 12)) {
                    throw new IllegalArgumentException("Semester must be between 1 and 12 for undergraduates");
                }
                break;

            case PROFESSOR:
                if (userDTO.getProfessionalChair() == null || userDTO.getProfessionalChair().isEmpty()) {
                    throw new IllegalArgumentException("Professional chair is required for professors");
                }
                if (userDTO.getPlantType() == null || userDTO.getPlantType().isEmpty()) {
                    throw new IllegalArgumentException("Plant type is required for professors");
                }
                break;

            case GRADUATE:
            case GUEST:
            case ADMINISTRATIVE_STAFF:
            case FAMILY_MEMBER:
                // No campos adicionales requeridos
                break;

            default:
                break;
        }
    }
}
