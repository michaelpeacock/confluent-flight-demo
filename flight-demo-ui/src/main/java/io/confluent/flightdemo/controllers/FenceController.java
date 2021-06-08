package io.confluent.flightdemo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
public class FenceController {
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;


	@MessageMapping("/fence-data")
	public void receiveFenceData(@Payload String fence) throws Exception {
		System.out.println("fence data: " + fence);
		kafkaTemplate.send("fence_raw", fence);
	}

}
