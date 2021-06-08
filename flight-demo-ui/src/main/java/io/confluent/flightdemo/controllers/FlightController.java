package io.confluent.flightdemo.controllers;

import io.confluent.flightdemo.models.FilteredFlightModel;
import io.confluent.flightdemo.models.FlightModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;


@Controller
public class FlightController {
	@Autowired
    private SimpMessagingTemplate template;

	private List<FlightModel> flightList = new ArrayList<>();
	private List<FilteredFlightModel> filteredFlightList = new ArrayList<>();

	@KafkaListener(topics = "flight-data", containerFactory = "flightKafkaListenerContainerFactory")
	public void consumeFlightData(FlightModel flight) {
		flightList.add(flight);

		if (flightList.size() > 1000) {
			System.out.println("sending flight data");
			this.template.convertAndSend("/topic/flight-data", flightList);
			flightList.clear();
		}
	}

	@KafkaListener(topics = "filtered_flights", containerFactory = "filteredFlightKafkaListenerContainerFactory")
	public void consumeFilteredFlight(FilteredFlightModel flight) {
		System.out.println("filtered flight id: " + flight.getId());
		filteredFlightList.add(flight);

		if (filteredFlightList.size() > 500) {
			System.out.println("sending filtered flight data");
			this.template.convertAndSend("/topic/filtered-flight-data", filteredFlightList);
			filteredFlightList.clear();
		}
	}

}
