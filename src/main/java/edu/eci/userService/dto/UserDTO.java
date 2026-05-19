package edu.eci.userService.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import edu.eci.userService.enums.UserRole;
import edu.eci.userService.enums.UserStatus;
import edu.eci.userService.enums.SchoolRelation;
import edu.eci.userService.enums.IdentificationType;
import edu.eci.userService.enums.AcademicLevel;

public class UserDTO {

    private Long id;
    private String fullName;
    private String email;
    private LocalDate birthDate;
    private String phone;
    private IdentificationType identificationType;
    private String identificationNumber;
    private SchoolRelation schoolRelation;
    private AcademicLevel academicLevel;
    private Integer semester;
    private String academicProgram;
    private String professionalChair;
    private String plantType;
    private UserRole role;
    private UserStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public UserDTO() {
    }

    public UserDTO(Long id, String fullName, String email, LocalDate birthDate, String phone,
            IdentificationType identificationType, String identificationNumber,
            SchoolRelation schoolRelation, AcademicLevel academicLevel, Integer semester,
            String academicProgram, String professionalChair, String plantType,
            UserRole role, UserStatus status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.birthDate = birthDate;
        this.phone = phone;
        this.identificationType = identificationType;
        this.identificationNumber = identificationNumber;
        this.schoolRelation = schoolRelation;
        this.academicLevel = academicLevel;
        this.semester = semester;
        this.academicProgram = academicProgram;
        this.professionalChair = professionalChair;
        this.plantType = plantType;
        this.role = role;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public IdentificationType getIdentificationType() {
        return identificationType;
    }

    public void setIdentificationType(IdentificationType identificationType) {
        this.identificationType = identificationType;
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    public SchoolRelation getSchoolRelation() {
        return schoolRelation;
    }

    public void setSchoolRelation(SchoolRelation schoolRelation) {
        this.schoolRelation = schoolRelation;
    }

    public AcademicLevel getAcademicLevel() {
        return academicLevel;
    }

    public void setAcademicLevel(AcademicLevel academicLevel) {
        this.academicLevel = academicLevel;
    }

    public Integer getSemester() {
        return semester;
    }

    public void setSemester(Integer semester) {
        this.semester = semester;
    }

    public String getAcademicProgram() {
        return academicProgram;
    }

    public void setAcademicProgram(String academicProgram) {
        this.academicProgram = academicProgram;
    }

    public String getProfessionalChair() {
        return professionalChair;
    }

    public void setProfessionalChair(String professionalChair) {
        this.professionalChair = professionalChair;
    }

    public String getPlantType() {
        return plantType;
    }

    public void setPlantType(String plantType) {
        this.plantType = plantType;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
