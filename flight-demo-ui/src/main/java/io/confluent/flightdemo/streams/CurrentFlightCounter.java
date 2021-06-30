package io.confluent.flightdemo.streams;

import io.confluent.flightdemo.models.DashboardModel;
import io.confluent.flightdemo.models.FlightModel;
import io.confluent.kafka.streams.serdes.avro.GenericAvroSerde;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.*;

import static io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG;

@Component
public class CurrentFlightCounter extends StreamsInterface {
    final static Logger logger = LogManager.getLogger(CurrentFlightCounter.class);

    @Override
    public Properties updateProperties() {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "current-flight-counter-app");

        return props;
    }

    @Override
    public Topology createTopology() {
        final Serde<FlightModel> flightSerde = FlightModel.getJsonSerde();
        Set<String> uniqueFlightsList = new HashSet<String>();
        StreamsBuilder builder = new StreamsBuilder();

        KStream<String, FlightModel> flights = builder.stream("flights_raw", Consumed.with(Serdes.String(), flightSerde));
        KTable<String, String> uniquesFlightCountsByKey = flights
                .selectKey((key, flight) -> {
                    uniqueFlightsList.add(flight.getId());
                    return "unique-flight-counts";
                })
                .groupByKey()
                .count()
                .mapValues(new ValueMapper<Long, String>() {
                    @Override
                    public String apply(final Long count) {
                        DashboardModel dashboard = new DashboardModel();
                        dashboard.setDashboardTitle("Current Flights Tracked");
                        dashboard.setDashboardValue(String.valueOf(uniqueFlightsList.size()));
                        return dashboard.toJSON();
                    }
                });
        uniquesFlightCountsByKey.toStream().to("dashboard-data", Produced.with(Serdes.String(), Serdes.String()));

        return builder.build();
    }
}
