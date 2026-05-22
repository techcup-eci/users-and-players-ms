package edu.eci.userService.dto;

public class AthleticProfileDTO {

    private int dorsalNumber;
    private Long id;
    private UserDTO user; // ← era UserEntity, ahora UserDTO (no expone password)
    private String email;
    private String nickName;
    private String position;
    private String laterality;
    private String stature;
    private String state;

    public AthleticProfileDTO() {
    }

    public AthleticProfileDTO(int dorsalNumber, Long id, UserDTO user, String nickName, String position,
            String laterality, String stature, String state) {
        this.dorsalNumber = dorsalNumber;
        this.user = user;
        this.id = id;
        this.nickName = nickName;
        this.position = position;
        this.laterality = laterality;
        this.stature = stature;
        this.state = state;
    }

    public int getDorsalNumber() {
        return dorsalNumber;
    }

    public Long getId() {
        return id;
    }

    public UserDTO getUser() {
        return user;
    }

    public String getEmail() {
        return email;
    }

    public String getNickName() {
        return nickName;
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

    public void setId(Long id) {
        this.id = id;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
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