echo "Creating topics..."
kafka-topics --bootstrap-server localhost:9092 --create --topic flights_raw
kafka-topics --bootstrap-server localhost:9092 --create --topic flights
kafka-topics --bootstrap-server localhost:9092 --create --topic dashboard-data
kafka-topics --bootstrap-server localhost:9092 --create --topic flight-data
kafka-topics --bootstrap-server localhost:9092 --create --topic filtered_flights

echo "Creating opensky connector..."
curl localhost:8083/connectors -X POST -H "Content-Type: application/json" -d @opensky-connect.json

echo "Creating streams via ksqlDB..."
ksql < scripts/ksql/createFlightsStream.sql
ksql < scripts/ksql/createGeoFenceStream.sql
ksql < scripts/ksql/createFilteredFlightsStream.sql