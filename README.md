# Confluent Live Flight Demo


## Description

This project provides a demo of Kafka capabilities including Connect, Kafka Streams, and ksqlDB using live flight data from [OpenSky Network](https://opensky-network.org/). Global flights are displayed by default. A dashboard of information built using Kafka Streams for aggregation is displayed at the top left. You can also draw a geofence using the polygon tool to display a filtered view of flights within the geofence region. Flights update every 10 seconds (occassional failures hitting the OpenSky end point may skip an update).

![alt text](https://github.com/michaelpeacock/flight-demo-web-app/raw/main/src/main/resources/static/images/FlightDemo.png "Flight Demo")

This project has several dependencies including:

  * [KSQLGeo](https://github.com/wlaforest/KSQLGeo) - geospatial UDFs for ksqlDB
  * [KafkaGeoDemo](https://github.com/wlaforest/KafkaGeoDemo) - geo demo used for installing ksqlDB UDFs
  * [kafka-connect-opensky](https://github.com/nbuesing/kafka-connect-opensky) - OpenSky Network Kafka Connector
  * [Kafka Flight Demo Streams](https://github.com/michaelpeacock/kafka-flight-demo-streams) - Kafka Streams applications for transformation and dashboard
  * [Cesium](https://github.com/CesiumGS/cesium) - WebGL geospatial toolkit


## Requirements
  * Confluent Platform 6.0
  * Java 11 or higher
  * Docker

## Steps to Run the Demo
  * `docker-compose up -d` 

## Main Dashboard
![alt text](https://github.com/michaelpeacock/flight-demo-web-app/raw/main/src/main/resources/static/images/global-flights.png "Global Flights")

## Filtered Geofences
  1. Uncheck the Global Flights option
  2. Select the Filtered Flights option
  3. Draw a polygon around the region to filter flights - click for each point, double click to close the polygon

![alt text](https://github.com/michaelpeacock/flight-demo-web-app/raw/main/src/main/resources/static/images/filtered-flghts1.png "Filtered Philly Flights")
![alt text](https://github.com/michaelpeacock/flight-demo-web-app/raw/main/src/main/resources/static/images/filtered-flights2.png "Filtered Florida Flights")

## Create a New Dashboard Item with ksqlDB
Add the following in the C3 ksql editor. This will create a new entry in the dashboard with the count of flights from Ireland.
`CREATE STREAM FLIGHTS_RAW WITH (KAFKA_TOPIC='flights_raw', PARTITIONS=1, REPLICAS=1, VALUE_FORMAT='AVRO');`

`CREATE TABLE IRELAND_FLIGHTS WITH (KAFKA_TOPIC='dashboard-data', PARTITIONS=1, REPLICAS=1, VALUE_FORMAT='JSON') AS SELECT
  'total-ireland-flights' as X,
  'Total Ireland Flight' `dashboardTitle`,
  COUNT(*) `dashboardValue`
FROM FLIGHTS_RAW
WHERE (originCountry = 'Ireland')
GROUP BY 'total-ireland-flights'
EMIT CHANGES;`

And set auto.offset.reset to earliest to get a count from the beginning.

