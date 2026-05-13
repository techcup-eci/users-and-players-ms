package edu.eci.userService.dto;

import java.time.LocalDate;

import edu.eci.userService.enums.UserRoleEnum;

public class UserDTO {

    private long id;
    private String name;
    private String email;
    private LocalDate birthDate;
    private UserRoleEnum role;
    private String relationShip;
    private String academicProgram;
    private int semester;
    private String identificationType;
    private int identificationNumber;
    private int phone;
    private String password;

    public UserDTO() {
    }

    public UserDTO(long id, String name, String email, LocalDate birthDate, UserRoleEnum role, String relationShip,
            String academicProgram, int semester, String identificationType, int identificationNumber, int phone,
            String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.birthDate = birthDate;
        this.role = role;
        this.relationShip = relationShip;
        this.academicProgram = academicProgram;
        this.semester = semester;
        this.identificationType = identificationType;
        this.identificationNumber = identificationNumber;
        this.phone = phone;
        this.password = password;
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

    public String getRelationShip() {
        return relationShip;
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

    public int getIdentificationNumber() {
        return identificationNumber;
    }

    public int getPhone() {
        return phone;
    }

    public String getPassword() {
        return password;
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

    public void setRelationShip(String relationShip) {
        this.relationShip = relationShip;
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

    public void setIdentificationNumber(int identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
