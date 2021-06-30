package io.confluent.flightdemo.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;
import io.confluent.kafka.serializers.KafkaJsonDeserializer;
import io.confluent.kafka.serializers.KafkaJsonSerializer;

import java.util.HashMap;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FlightModel {
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCallSign() {
        return callSign;
    }

    public void setCallSign(String callSign) {
        this.callSign = callSign;
    }

    public String getOriginCountry() {
        return originCountry;
    }

    public void setOriginCountry(String originCountry) {
        this.originCountry = originCountry;
    }

    public Long getTimePosition() {
        return timePosition;
    }

    public void setTimePosition(Long timePosition) {
        this.timePosition = timePosition;
    }

    public Long getLastContact() {
        return lastContact;
    }

    public void setLastContact(Long lastContact) {
        this.lastContact = lastContact;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getBarometricAltitude() {
        return barometricAltitude;
    }

    public void setBarometricAltitude(Double barometricAltitude) {
        this.barometricAltitude = barometricAltitude;
    }

    public Boolean getOnGround() {
        return onGround;
    }

    public void setOnGround(Boolean onGround) {
        this.onGround = onGround;
    }

    public Double getVelocity() {
        return velocity;
    }

    public void setVelocity(Double velocity) {
        this.velocity = velocity;
    }

    public Double getHeading() {
        return heading;
    }

    public void setHeading(Double heading) {
        this.heading = heading;
    }

    public Double getVerticalRate() {
        return verticalRate;
    }

    public void setVerticalRate(Double verticalRate) {
        this.verticalRate = verticalRate;
    }

    public Double getGeometricAltitude() {
        return geometricAltitude;
    }

    public void setGeometricAltitude(Double geometricAltitude) {
        this.geometricAltitude = geometricAltitude;
    }

    public String getSquawk() {
        return squawk;
    }

    public void setSquawk(String squawk) {
        this.squawk = squawk;
    }

    public Boolean getSpecialPurpose() {
        return specialPurpose;
    }

    public void setSpecialPurpose(Boolean specialPurpose) {
        this.specialPurpose = specialPurpose;
    }

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

    public static Serde<FlightModel> getJsonSerde(){
        Map<String, Object> serdeProps = new HashMap<>();
        serdeProps.put("json.value.type", FlightModel.class);
        final Serializer<FlightModel> detectionSer = new KafkaJsonSerializer<>();
        detectionSer.configure(serdeProps, false);

        final Deserializer<FlightModel> detectionDes = new KafkaJsonDeserializer<>();
        detectionDes.configure(serdeProps, false);
        return Serdes.serdeFrom(detectionSer, detectionDes);
    }

}
