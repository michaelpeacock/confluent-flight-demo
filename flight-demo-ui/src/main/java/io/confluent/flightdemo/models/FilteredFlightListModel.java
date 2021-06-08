package io.confluent.flightdemo.models;

import java.util.ArrayList;
import java.util.List;

public class FilteredFlightListModel {
    private List<FilteredFlightModel> flightList = new ArrayList<>();

    public List<FilteredFlightModel> getFlightList() {
        return flightList;
    }

    public void setFlightList(List<FilteredFlightModel> flightList) {
        this.flightList = flightList;
    }

    
 
}
