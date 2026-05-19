package edu.eci.userService.dto;

import java.time.LocalDateTime;
import edu.eci.userService.enums.LateralityType;
import edu.eci.userService.enums.ProfileStatus;

public class AthleticProfileDTO {

    private Long id;
    private Long userId;
    private Integer dorsalNumber;
    private String position;
    private LateralityType laterality;
    private Integer stature;
    private ProfileStatus status;
    private String photoUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public AthleticProfileDTO() {
    }

    public AthleticProfileDTO(Long id, Long userId, Integer dorsalNumber, String position,
            LateralityType laterality, Integer stature, ProfileStatus status,
            String photoUrl, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.dorsalNumber = dorsalNumber;
        this.position = position;
        this.laterality = laterality;
        this.stature = stature;
        this.status = status;
        this.photoUrl = photoUrl;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getDorsalNumber() {
        return dorsalNumber;
    }

    public void setDorsalNumber(Integer dorsalNumber) {
        this.dorsalNumber = dorsalNumber;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public LateralityType getLaterality() {
        return laterality;
    }

    public void setLaterality(LateralityType laterality) {
        this.laterality = laterality;
    }

    public Integer getStature() {
        return stature;
    }

    public void setStature(Integer stature) {
        this.stature = stature;
    }

    public ProfileStatus getStatus() {
        return status;
    }

    public void setStatus(ProfileStatus status) {
        this.status = status;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
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
