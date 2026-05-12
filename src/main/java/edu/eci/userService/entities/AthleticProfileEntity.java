package edu.eci.userService.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "athletic_profiles")
public class AthleticProfileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int dorsalNumber;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String position;

    @Column(nullable = false)
    private String laterality;

    @Column(nullable = false)
    private String stature;

    @Column(nullable = false)
    private String state;

    public void setDorsalNumber(int dorsalNumber) {
        this.dorsalNumber = dorsalNumber;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public int getDorsalNumber() {
        return dorsalNumber;
    }

    public String getEmail() {
        return email;
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
