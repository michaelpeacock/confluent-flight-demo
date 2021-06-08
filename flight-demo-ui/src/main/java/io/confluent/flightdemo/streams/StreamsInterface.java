package io.confluent.flightdemo.streams;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.Topology;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.Properties;

public abstract class StreamsInterface {
    @Autowired
    Properties streamsProperties;

    @PostConstruct
    public void startStream() {
        Properties props = updateProperties();
        if (props != null) {
            streamsProperties.putAll(props);
        }

        KafkaStreams streams = new KafkaStreams(createTopology(), streamsProperties);

        streams.cleanUp();
        streams.start();

        // shutdown hook to correctly close the streams application
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    }

    public Properties updateProperties() {
        return null;
    }

    public abstract Topology createTopology();
}
