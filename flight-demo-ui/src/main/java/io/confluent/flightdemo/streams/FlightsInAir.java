package io.confluent.flightdemo.streams;

import io.confluent.flightdemo.models.DashboardModel;
import io.confluent.kafka.streams.serdes.avro.GenericAvroSerde;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.ValueMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

import static io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG;

public class FlightsInAir extends StreamsInterface {
    final static Logger logger = LogManager.getLogger(FlightsInAir.class);

    @Override
    public Properties updateProperties() {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "current-flights-in-air-counter-app");
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, GenericAvroSerde.class);

        return props;
    }

    @Override
    public Topology createTopology() {
        Set<String> uniqueFlightsList = new HashSet<String>();
        StreamsBuilder builder = new StreamsBuilder();

        KStream<String, GenericRecord> flights = builder.stream("flights_raw");
        KTable<String, String> uniquesFlightCountsByKey = flights
                .filter((key, value) ->  {return value.get("onGround").toString().matches("false");})
                .selectKey((key, value) -> {
                    uniqueFlightsList.add(value.get("id").toString());
                    return "flights-in-air-counts";
                })
                .groupByKey()
                .count()
                .mapValues(new ValueMapper<Long, String>() {
                    @Override
                    public String apply(final Long count) {
                        DashboardModel dashboard = new DashboardModel();
                        dashboard.setDashboardTitle("Flights In Air");
                        dashboard.setDashboardValue(String.valueOf(uniqueFlightsList.size()));
                        return dashboard.toJSON();
                }
        });
        uniquesFlightCountsByKey.toStream().to("dashboard-data", Produced.with(Serdes.String(), Serdes.String()));

        return builder.build();
    }
}
