package edu.eci.userService.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.eci.userService.enums.AcademicLevel;
import edu.eci.userService.enums.ProfessorType;
import edu.eci.userService.enums.SchoolRelation;

public class UserDTO {

    private long id;
    private String name;
    private String email;
    private LocalDate birthDate;
    @JsonAlias({"relationship", "schoolRelation"})
    private SchoolRelation schoolRelation;
    private AcademicLevel academicLevel;
    private ProfessorType professorType;
    private String academicProgram;
    private int semester;
    private String identificationType;
    private Long identificationNumber;
    private Long phone;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    public UserDTO() {
    }

    public UserDTO(long id, String name, String email, LocalDate birthDate, SchoolRelation schoolRelation,
            AcademicLevel academicLevel, ProfessorType professorType, String academicProgram, int semester,
            String identificationType, Long identificationNumber, Long phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.birthDate = birthDate;
        this.schoolRelation = schoolRelation;
        this.academicLevel = academicLevel;
        this.professorType = professorType;
        this.academicProgram = academicProgram;
        this.semester = semester;
        this.identificationType = identificationType;
        this.identificationNumber = identificationNumber;
        this.phone = phone;
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

    public SchoolRelation getSchoolRelation() {
        return schoolRelation;
    }

    public AcademicLevel getAcademicLevel() {
        return academicLevel;
    }

    public ProfessorType getProfessorType() {
        return professorType;
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

    public void setSchoolRelation(SchoolRelation schoolRelation) {
        this.schoolRelation = schoolRelation;
    }

    public void setAcademicLevel(AcademicLevel academicLevel) {
        this.academicLevel = academicLevel;
    }

    public void setProfessorType(ProfessorType professorType) {
        this.professorType = professorType;
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

    public void setPassword(String password) {
        this.password = password;
    }

}
