package edu.eci.userService.dto;

public class AthleticProfileDTO {

    private int dorsalNumber;
    private String email;
    private String position;
    private String laterality;
    private String stature;
    private String state;

    public AthleticProfileDTO() {
    }

    public AthleticProfileDTO(int dorsalNumber, String email, String position, String laterality, String stature,
            String state) {
        this.dorsalNumber = dorsalNumber;
        this.email = email;
        this.position = position;
        this.laterality = laterality;
        this.stature = stature;
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

}
