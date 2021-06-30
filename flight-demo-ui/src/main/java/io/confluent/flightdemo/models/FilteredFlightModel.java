package io.confluent.flightdemo.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FilteredFlightModel implements Serializable {
    private String id;
    private String callSign;
    private String originCountry;
    private Long timePosition;
    private Long lastContact;
    private Double latitude;
    private Double longitude;
    private Double barometricAltitude;
    private Boolean onGround;
    private Double velocity;
    private Double heading;
    private Double verticalRate;
    private Double geometricAltitude;
    private String squawk;
    private Boolean specialPurpose;
    private Integer positionSource;
    
    @JsonProperty("A_id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("A_originCountry")
    public String getOriginCountry() {
        return originCountry;
    }

    public void setOriginCountry(String originCountry) {
        this.originCountry = originCountry;
    }

    @JsonProperty("A_latitude")
    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    @JsonProperty("A_longitude")
    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @JsonProperty("A_timePosition")
    public Long getTimePosition() {
        return timePosition;
    }

    public void setTimePosition(Long timePosition) {
        this.timePosition = timePosition;
    }

    @JsonProperty("A_lastContact")
    public Long getLastContact() {
        return lastContact;
    }

    public void setLastContact(Long lastContact) {
        this.lastContact = lastContact;
    }

    @JsonProperty("A_barometricAltitude")
    public Double getBarometricAltitude() {
        return barometricAltitude;
    }

    public void setBarometricAltitude(Double barometricAltitude) {
        this.barometricAltitude = barometricAltitude;
    }

    @JsonProperty("A_onGround")
    public Boolean getOnGround() {
        return onGround;
    }

    public void setOnGround(Boolean onGround) {
        this.onGround = onGround;
    }

    @JsonProperty("A_velocity")
    public Double getVelocity() {
        return velocity;
    }

    public void setVelocity(Double velocity) {
        this.velocity = velocity;
    }

    @JsonProperty("A_verticalRate")
    public Double getVerticalRate() {
        return verticalRate;
    }

    public void setVerticalRate(Double verticalRate) {
        this.verticalRate = verticalRate;
    }

    @JsonProperty("A_geometricAltitude")
    public Double getGeometricAltitude() {
        return geometricAltitude;
    }

    public void setGeometricAltitude(Double geometricAltitude) {
        this.geometricAltitude = geometricAltitude;
    }

    @JsonProperty("A_squawk")
    public String getSquawk() {
        return squawk;
    }

    public void setSquawk(String squawk) {
        this.squawk = squawk;
    }

    @JsonProperty("A_specialPurpose")
    public Boolean getSpecialPurpose() {
        return specialPurpose;
    }

    public void setSpecialPurpose(Boolean specialPurpose) {
        this.specialPurpose = specialPurpose;
    }

    @JsonProperty("A_positionSource")
    public Integer getPositionSource() {
        return positionSource;
    }

    public void setPositionSource(Integer positionSource) {
        this.positionSource = positionSource;
    }

    public String toJSON() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    @JsonProperty("A_callSign")
    public String getCallSign() {
        return callSign;
    }

    public void setCallSign(String callSign) {
        this.callSign = callSign;
    }
 
    @JsonProperty("A_heading")
    public Double getHeading() {
        return heading;
    }

    public void setHeading(Double heading) {
        this.heading = heading;
    }
}
