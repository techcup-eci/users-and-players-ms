package edu.eci.userService.dto;

import edu.eci.userService.model.enums.PlayingPosition;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * Request body for creating a sport profile.
 */
public class CreateSportProfileRequest {

    @NotNull(message = "Playing position must not be null")
    private PlayingPosition position;

    @Min(value = 1, message = "Jersey number must be at least 1")
    @Max(value = 99, message = "Jersey number must be at most 99")
    private int jerseyNumber;

    private String photoUrl;

    public PlayingPosition getPosition() { return position; }
    public int getJerseyNumber() { return jerseyNumber; }
    public String getPhotoUrl() { return photoUrl; }

    public void setPosition(PlayingPosition position) { this.position = position; }
    public void setJerseyNumber(int jerseyNumber) { this.jerseyNumber = jerseyNumber; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }
}
