package edu.eci.userService.dto;

import java.util.Date;

public class AthleticProfileDTO {

    private int dorsalNumber;
    private Date dateOfBirth;
    private String position;
    private String laterality;
    private String stature;
    private String state;

    public AthleticProfileDTO() {
    }

    public AthleticProfileDTO(int dorsalNumber, Date dateOfBrith, String position, String laterality, String stature,
            String state) {
        this.dorsalNumber = dorsalNumber;
        this.dateOfBirth = dateOfBrith;
        this.position = position;
        this.laterality = laterality;
        this.stature = stature;
        this.state = state;
    }

    public int getDorsalNumber() {
        return dorsalNumber;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
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

    public void setDorsalNumber(int dorsalNumber) {
        this.dorsalNumber = dorsalNumber;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
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

}
