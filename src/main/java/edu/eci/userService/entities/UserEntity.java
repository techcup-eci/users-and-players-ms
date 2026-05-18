package edu.eci.userService.entities;

import java.time.LocalDate;

import edu.eci.userService.enums.UserRoleEnum;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

    @Enumerated(EnumType.STRING) // Enum relationes in the dir: enums.UserRoleEnum
    private UserRoleEnum role;

    @Column(nullable = false)
    private String relationship; // Relationship with the university (student, teacher, etc)

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

    @Column
    private String systemRole;  // identity-ms system role: INVITED, PLAYER, CAPTAIN, ORGANIZER, REFEREE, ADMIN

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

    public UserRoleEnum getRole() {
        return role;
    }

    public String getRelationship() {
        return relationship;
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

    public String getSystemRole() {
        return systemRole;
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

    public void setRole(UserRoleEnum role) {
        this.role = role;
    }
    public void setRelationship(String relationship) {
        this.relationship = relationship;
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

    public void setSystemRole(String systemRole) {
        this.systemRole = systemRole;
    }

    public AthleticProfileEntity getAthleticProfile() {
        return athleticProfile;
    }

    public void setAthleticProfile(AthleticProfileEntity athleticProfile) {
        this.athleticProfile = athleticProfile;
    }
}
