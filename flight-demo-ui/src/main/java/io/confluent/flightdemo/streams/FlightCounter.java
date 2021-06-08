package io.confluent.flightdemo.streams;

import io.confluent.flightdemo.models.DashboardModel;
import io.confluent.kafka.streams.serdes.avro.GenericAvroSerde;
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
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class FlightCounter extends StreamsInterface {
    final static Logger logger = LogManager.getLogger(FlightCounter.class);

    @Override
    public Properties updateProperties() {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "flight-counter-app");
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, GenericAvroSerde.class);

        return props;
    }

    @Override
    public Topology createTopology() {
        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, Object> flights = builder.stream("flights_raw");
        KTable<String, String> flightCounts = flights
                .selectKey((key, value) -> "total-flights-processed")
                .groupByKey()
                .count()
                .mapValues(new ValueMapper<Long, String>() {
                    @Override
                    public String apply(final Long count) {
                        DashboardModel dashboard = new DashboardModel();
                        dashboard.setDashboardTitle("Total Flights Processed");
                        dashboard.setDashboardValue(count.toString());
                        return dashboard.toJSON();
                    }
                });
        flightCounts.toStream().to("dashboard-data", Produced.with(Serdes.String(), Serdes.String()));

        return builder.build();
    }
}
