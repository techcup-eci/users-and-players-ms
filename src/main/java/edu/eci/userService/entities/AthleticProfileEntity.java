package edu.eci.userService.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "athletic_profiles")
public class AthleticProfileEntity {

    @Id
    private Long id;

    @Column(nullable = false)
    private Integer dorsalNumber;

    @Column(nullable = false)
    private String nickName;

    @OneToOne
    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String position;

    @Column(nullable = false)
    private String laterality;

    @Column(nullable = false)
    private String stature;

    @Column(nullable = false)
    private String state;

    @OneToOne
    @MapsId // Link the ID of this entity to the ID of the UserEntity
    @JoinColumn(name = "user_id") // Database column name
    private UserEntity user; // We're replacing Long with the Entity class

    public void setDorsalNumber(Integer dorsalNumber) {
        this.dorsalNumber = dorsalNumber;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setLaterality(String laterality) {
        this.laterality = laterality;
    }

    public void setStature(String stature) {
        this.stature = stature;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getDorsalNumber() {
        return dorsalNumber;
    }

    public String getNickName() {
        return nickName;
    }

    public UserEntity getUser() {
        return user;
    }

    public String getPosition() {
        return position;
    }

    public String getLaterality() {
        return laterality;
    }

    public String getStature() {
        return stature;
    }

    public String getState() {
        return state;
    }

}
