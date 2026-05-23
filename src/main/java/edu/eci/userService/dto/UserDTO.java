package edu.eci.userService.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

import edu.eci.userService.enums.UserRoleEnum;

public class UserDTO {

    private long id;
    private String name;
    private String email;
    private LocalDate birthDate;
    private UserRoleEnum role;
    private String relationship;
    private String academicProgram;
    private int semester;
    private String identificationType;
    private Long identificationNumber;
    private Long phone;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    public UserDTO() {
    }

    public UserDTO(long id, String name, String email, LocalDate birthDate, UserRoleEnum role, String relationship,
            String academicProgram, int semester, String identificationType, Long identificationNumber, Long phone,
            String systemRole) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.birthDate = birthDate;
        this.role = role;
        this.relationship = relationship;
        this.academicProgram = academicProgram;
        this.semester = semester;
        this.identificationType = identificationType;
        this.identificationNumber = identificationNumber;
        this.phone = phone;
        this.systemRole = systemRole;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public UserRoleEnum getRole() {
        return role;
    }

    public String getRelationship() {
        return relationship;
    }

    public String getAcademicProgram() {
        return academicProgram;
    }

    public int getSemester() {
        return semester;
    }

    public String getIdentificationType() {
        return identificationType;
    }

    public Long getIdentificationNumber() {
        return identificationNumber;
    }

    public Long getPhone() {
        return phone;
    }

    public String getSystemRole() {
        return systemRole;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public void setRole(UserRoleEnum role) {
        this.role = role;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public void setAcademicProgram(String academicProgram) {
        this.academicProgram = academicProgram;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public void setIdentificationType(String identificationType) {
        this.identificationType = identificationType;
    }

    public void setIdentificationNumber(Long identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    public void setPhone(Long phone) {
        this.phone = phone;
    }

    public void setSystemRole(String systemRole) {
        this.systemRole = systemRole;
    }

}
