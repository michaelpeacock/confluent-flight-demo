package io.confluent.flightdemo.controllers;

import io.confluent.flightdemo.models.DashboardModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

@Controller
public class DashboardController {
	private Map<String, DashboardModel> dashboardList = new HashMap<>();

	@Autowired
	private SimpMessagingTemplate template;

	@KafkaListener(topics = "dashboard-data", containerFactory = "dashboardKafkaListenerContainerFactory")
	public void consumeDashboardData(DashboardModel dashboard) {
		System.out.println("received dashboard data");
		dashboardList.put(dashboard.getDashboardTitle(), dashboard);
		this.template.convertAndSend("/topic/dashboard-data", dashboard);
	}

	@MessageMapping("/getDashboardData")
	public void requestDashboardData(@Payload String request) throws Exception {
		dashboardList.forEach((k, dashboard) -> {
			this.template.convertAndSend("/topic/dashboard-data", dashboard);
		});
	}
}
