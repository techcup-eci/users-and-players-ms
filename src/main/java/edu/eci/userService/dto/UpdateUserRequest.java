package edu.eci.userService.dto;

import edu.eci.userService.model.enums.SchoolRelation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;

/**
 * Request body for updating a user's basic information.
 * Email is NOT updatable — if present in the request body it must be rejected (400).
 */
public class UpdateUserRequest {

    @NotBlank(message = "Full name must not be blank")
    private String fullName;

    private SchoolRelation schoolRelation;
    private String academicProgram;
    private Integer semester;

    /** Must not be present in the request. If included, validation will return 400. */
    @Null(message = "Email cannot be updated through this endpoint")
    private String email;

    public String getFullName() { return fullName; }
    public SchoolRelation getSchoolRelation() { return schoolRelation; }
    public String getAcademicProgram() { return academicProgram; }
    public Integer getSemester() { return semester; }
    public String getEmail() { return email; }

    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setSchoolRelation(SchoolRelation schoolRelation) { this.schoolRelation = schoolRelation; }
    public void setAcademicProgram(String academicProgram) { this.academicProgram = academicProgram; }
    public void setSemester(Integer semester) { this.semester = semester; }
    public void setEmail(String email) { this.email = email; }
}
