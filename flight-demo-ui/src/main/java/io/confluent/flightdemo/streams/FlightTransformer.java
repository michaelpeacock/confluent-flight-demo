package io.confluent.flightdemo.streams;

import io.confluent.flightdemo.models.FlightModel;
import io.confluent.kafka.streams.serdes.avro.GenericAvroSerde;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.ValueMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Properties;

import static io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG;

@Component
public class FlightTransformer extends StreamsInterface {
    final static Logger logger = LogManager.getLogger(FlightTransformer.class);

    @Override
    public Properties updateProperties() {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "flight-model-app");
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, GenericAvroSerde.class);

        return props;
    }

    @Override
    public Topology createTopology() {
        StreamsBuilder builder = new StreamsBuilder();

        KStream<String, GenericRecord> flights = builder.stream("flights_raw");
        KStream<String, String> flightModel = flights.mapValues(record -> createFlightModel(record).toJSON());
        flightModel.to("flights", Produced.with(Serdes.String(), Serdes.String()));

        return builder.build();
    }

    public FlightModel createFlightModel(GenericRecord record) {
        System.out.println(record.toString());
        FlightModel flight = new FlightModel();

        if (record.get("id") != null) {
            flight.setId(record.get("id").toString());
        }

        if (record.get("callsign") != null) {
            flight.setCallSign(record.get("callsign").toString());
        }

        if (record.get("originCountry") != null) {
            flight.setOriginCountry(record.get("originCountry").toString());
        }

        if (record.get("timePosition") != null) {
            flight.setUpdateTime(Long.valueOf(record.get("timePosition").toString()));
        }

        if (record.get("location") != null) {
            flight.setLatitude(Double.valueOf(((GenericRecord) record.get("location")).get("lat").toString()));
            flight.setLongitude(Double.valueOf(((GenericRecord) record.get("location")).get("lon").toString()));
        }

        if (record.get("geometricAltitude") != null) {
            flight.setAltitude(Double.valueOf(record.get("geometricAltitude").toString()));
        }

        if (record.get("velocity") != null) {
            flight.setSpeed(Double.valueOf(record.get("velocity").toString()));
        }

        if (record.get("heading") != null) {
            flight.setHeading(Double.valueOf(record.get("heading").toString()));
        }

        return flight;
    }

}
