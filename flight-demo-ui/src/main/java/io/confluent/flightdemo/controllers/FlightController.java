package io.confluent.flightdemo.controllers;

import io.confluent.flightdemo.models.FilteredFlightModel;
import io.confluent.flightdemo.models.FlightModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
public class FlightController {
	@Autowired
    private SimpMessagingTemplate template;

	private Map<String, FlightModel> flightList = new HashMap<>();
	private Map<String, FlightModel> updatedFlightList = new HashMap<>();
	private Map<String, FilteredFlightModel> filteredFlightList = new HashMap<>();
	private Map<String, FilteredFlightModel> updatedFilteredFlightList = new HashMap<>();

	@PostConstruct
	public void periodic () {
		//		FlightModel flight = new FlightModel();
		//		flight.setId("test");
		//		flight.setLatitude(39.9837234);
		//		flight.setLongitude(-74.8271081);
		//		flightList.put("test", flight);


		System.out.println("starting thread");
		new Thread(() -> {
			try {
				while(true) {
					sendBufferedFlights();
					sendBufferedFilteredFlights();

					Thread.sleep(2000);
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}).start();
	}

	@KafkaListener(topics = "flight-data", containerFactory = "flightKafkaListenerContainerFactory")
	public void consumeFlightData(FlightModel flight) {
		flightList.put(flight.getId(), flight);

		synchronized (this) {
			updatedFlightList.put(flight.getId(), flight);
		}
	}

	@MessageMapping("/getFlights")
	public void requestFlightData(@Payload String request) throws Exception {
		synchronized (this) {
			updatedFlightList.putAll(flightList);
			updatedFilteredFlightList.putAll(filteredFlightList);
		}
	}

	private void sendBufferedFlights() {
		List<FlightModel> flightsToSend = new ArrayList<>();
		System.out.println("sendBufferedFlights size " + updatedFlightList.size());
		if (updatedFlightList.size() > 0) {
			synchronized (this) {
				updatedFlightList.forEach((key, value) -> {
					flightsToSend.add(value);

					if (flightsToSend.size() > 1000) {
						System.out.println("sending a block of flight data - " + flightsToSend.size());
						this.template.convertAndSend("/topic/flight-data", flightsToSend);
						try {
							Thread.sleep(50);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						flightsToSend.clear();
					}
				});

				//send the remaining buffered flights
				System.out.println("sending remaining flight data - " + flightsToSend.size());
				this.template.convertAndSend("/topic/flight-data", flightsToSend);
				flightsToSend.clear();

				updatedFlightList.clear();
			}
		}
	}

	@KafkaListener(topics = "filtered_flights", containerFactory = "filteredFlightKafkaListenerContainerFactory")
	public void consumeFilteredFlight(FilteredFlightModel flight) {
		synchronized (this) {
			filteredFlightList.put(flight.getId(), flight);
			updatedFilteredFlightList.put(flight.getId(), flight);
		}
	}

	private void sendBufferedFilteredFlights() {
		List<FilteredFlightModel> filteredFlightsToSend = new ArrayList<>();

		if (updatedFilteredFlightList.size() > 0) {
			synchronized (this) {
				updatedFilteredFlightList.forEach((key, value) -> {
					filteredFlightsToSend.add(value);

					if (filteredFlightsToSend.size() > 1000) {
						System.out.println("sending a block of filtered flight data - " + filteredFlightsToSend.size());
						this.template.convertAndSend("/topic/filtered-flight-data", filteredFlightsToSend);
						try {
							Thread.sleep(50);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						filteredFlightsToSend.clear();
					}
				});

				//send the remaining buffered flights
				System.out.println("sending remaining filtered flight data - " + filteredFlightsToSend.size());
				this.template.convertAndSend("/topic/filtered-flight-data", filteredFlightsToSend);
				filteredFlightsToSend.clear();

				updatedFilteredFlightList.clear();
			}
		}
	}
}
