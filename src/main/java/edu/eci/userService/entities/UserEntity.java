package edu.eci.userService.entities;

import java.time.LocalDate;

import edu.eci.userService.config.AcademicLevelConverter;
import edu.eci.userService.config.ProfessorTypeConverter;
import edu.eci.userService.config.SchoolRelationConverter;
import edu.eci.userService.enums.AcademicLevel;
import edu.eci.userService.enums.ProfessorType;
import edu.eci.userService.enums.SchoolRelation;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String password;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Convert(converter = SchoolRelationConverter.class)
    @Column(name = "relationship", nullable = false)
    private SchoolRelation schoolRelation;

    @Convert(converter = AcademicLevelConverter.class)
    private AcademicLevel academicLevel;

    @Convert(converter = ProfessorTypeConverter.class)
    private ProfessorType professorType;

    @Column
    private String academicProgram;

    @Column
    private Integer semester;

    @Column(nullable = false)
    private String identificationType;

    @Column(nullable = false)
    private Long identificationNumber;

    @Column(nullable = false)
    private Long phone = 0L;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private AthleticProfileEntity athleticProfile;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
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

    public Integer getSemester() {
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


    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public void setSemester(Integer semester) {
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


    public AthleticProfileEntity getAthleticProfile() {
        return athleticProfile;
    }

    public void setAthleticProfile(AthleticProfileEntity athleticProfile) {
        this.athleticProfile = athleticProfile;
    }
}
