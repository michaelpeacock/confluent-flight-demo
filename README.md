# Confluent Live Flight Demo


## Description

This project provides a demo of Kafka capabilities including Connect, Kafka Streams, and ksqlDB using live flight data from [OpenSky Network](https://opensky-network.org/). Global flights are displayed by default. A dashboard of information built using Kafka Streams for aggregation is displayed at the top left. You can also draw a geofence using the polygon tool to display a filtered view of flights within the geofence region.

This project has several dependencies including:

  * [KSQLGeo](https://github.com/wlaforest/KSQLGeo) - geospatial UDFs for ksqlDB
  * [KafkaGeoDemo](https://github.com/wlaforest/KafkaGeoDemo) - geo demo used for installing ksqlDB UDFs
  * [kafka-connect-opensky](https://github.com/nbuesing/kafka-connect-opensky) - OpenSky Network Kafka Connector
  * [Kafka Flight Demo Streams](https://github.com/michaelpeacock/kafka-flight-demo-streams) - Kafka Streams applications for transformation and dashboard
  * [Cesium](https://github.com/CesiumGS/cesium) - WebGL geospatial toolkit


## Requirements
  * Confluent Platform 5.5
  * Java 11 or higher

## Steps to Run the Demo
  1. Follow the instructions in the kafka-connect-opensky repo to build the full version and then update the connect plug-in path to include it prior to starting the confluent services.
  2. Follow the instructions KSQLGeo repo to install the ksqlDB UDFs
  3. run `confluent local services start`
  4. run `start.sh` script in the scripts folder
  5. In a web browers, go to http://localhost:8080 

## Main Dashboard
![alt text](https://github.com/michaelpeacock/flight-demo-web-app/raw/main/src/main/resources/static/images/global-flights.png "Global Flights")

## Filtered Geofences
  1. Uncheck the Global Flights option
  2. Select the Filtered Flights option
  3. Draw a polygon around the region to filter flights - click for each point, double click to close the polygon

![alt text](https://github.com/michaelpeacock/flight-demo-web-app/raw/main/src/main/resources/static/images/filtered-flghts1.png "Filtered Philly Flights")
![alt text](https://github.com/michaelpeacock/flight-demo-web-app/raw/main/src/main/resources/static/images/filtered-flights2.png "Filtered Florida Flights")
