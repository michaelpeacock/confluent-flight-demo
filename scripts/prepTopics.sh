kafka-topics --bootstrap-server localhost:9092 --create --topic flights_raw
kafka-topics --bootstrap-server localhost:9092 --create --topic flights
kafka-topics --bootstrap-server localhost:9092 --create --topic dashboard-data
kafka-topics --bootstrap-server localhost:9092 --create --topic flight-data
kafka-topics --bootstrap-server localhost:9092 --create --topic filtered_flights
