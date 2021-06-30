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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Properties;

import static io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG;

//@Component
public class USFlightCounter extends StreamsInterface {
    final static Logger logger = LogManager.getLogger(USFlightCounter.class);

    @Override
    public Properties updateProperties() {
        Properties prop = new Properties();
        prop.put(StreamsConfig.APPLICATION_ID_CONFIG, "us-flight-counter-app");

        return prop;
    }

    @Override
    public Topology createTopology() {
        final Serde<FlightModel> flightSerde = FlightModel.getJsonSerde();
        StreamsBuilder builder = new StreamsBuilder();

        KStream<String, FlightModel> flights = builder.stream("flights_raw", Consumed.with(Serdes.String(), flightSerde));
        KTable<String, String> usFlightCounts = flights
                .filter((key, value) -> {
                    return value.getOriginCountry().matches("United States");
                })
                .selectKey((key, value) -> "total-us-flights")
                .groupByKey()
                .count()
                .mapValues(count -> {
                        DashboardModel dashboard = new DashboardModel();
                        dashboard.setDashboardTitle("Total US Flights");
                        dashboard.setDashboardValue(count.toString());
                        return dashboard.toJSON();
                    });
        usFlightCounts.toStream().to("dashboard-data", Produced.with(Serdes.String(), Serdes.String()));

        return builder.build();
    }

}
