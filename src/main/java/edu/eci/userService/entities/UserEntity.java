package edu.eci.userService.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import java.time.LocalDate;
import edu.eci.userService.enums.UserRoleEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.CascadeType;

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
    private Integer identificationNumber;

    @Column(nullable = false)
    private Integer phone;

    @Column(nullable = false)
    private String password;

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

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public UserRoleEnum getRole() {
        return role;
    }

    public String getRelationShip() {
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

    public Integer getIdentificationNumber() {
        return identificationNumber;
    }

    public Integer getPhone() {
        return phone;
    }

    public String getPassword() {
        return password;
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

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public void setRole(UserRoleEnum role) {
        this.role = role;
    }
w
    public void setRelationShip(String relationship) {
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

    public void setIdentificationNumber(Integer identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    public void setPhone(Integer phone) {
        this.phone = phone;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
