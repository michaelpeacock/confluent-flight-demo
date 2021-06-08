package io.confluent.flightdemo.models;

import java.util.ArrayList;
import java.util.List;

public class FlightListModel {
    private List<FlightModel> flightList = new ArrayList<>();

    public List<FlightModel> getFlightList() {
        return flightList;
    }

    public void setFlightList(List<FlightModel> flightList) {
        this.flightList = flightList;
    }

    
 
}
