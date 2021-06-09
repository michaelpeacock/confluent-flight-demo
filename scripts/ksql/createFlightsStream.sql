SET 'auto.offset.reset'='earliest';

CREATE STREAM flights (
    `id` VARCHAR, 
    `callSign` VARCHAR, 
    `originCountry` VARCHAR,
  	`updateTime` DOUBLE,
  	`latitude` DOUBLE,
  	`longitude` DOUBLE,
  	`altitude` DOUBLE,
  	`speed` DOUBLE,
  	`heading` DOUBLE
  ) WITH (
    KAFKA_TOPIC='flights',
    PARTITIONS=1,
    VALUE_FORMAT='JSON'
  );

CREATE STREAM FLIGHT_DATA WITH (KAFKA_TOPIC='flight-data', PARTITIONS=1, REPLICAS=1) AS
    SELECT *, 1 UNITY
    FROM FLIGHTS
    EMIT CHANGES;

