SET 'auto.offset.reset'='earliest';

CREATE STREAM flights (
    `id` VARCHAR, 
    `callSign` VARCHAR, 
    `originCountry` VARCHAR,
  	`timePosition` DOUBLE,
    `lastContact` DOUBLE,
    `latitude` DOUBLE,
  	`longitude` DOUBLE,
  	`barometricAltitude` DOUBLE,
    `onGround` BOOLEAN,
    `velocity` DOUBLE,
  	`heading` DOUBLE,
    `verticalRate` DOUBLE,
    `geometricAltitude` DOUBLE,
    `squawk` VARCHAR,
    `specialPurpose` BOOLEAN,
    `positionSource` INTEGER
  ) WITH (
    KAFKA_TOPIC='flights_raw',
    PARTITIONS=1,
    VALUE_FORMAT='JSON'
  );

CREATE STREAM FLIGHT_DATA WITH (KAFKA_TOPIC='flight-data', PARTITIONS=1, REPLICAS=1) AS
    SELECT *, 1 UNITY
    FROM FLIGHTS
    EMIT CHANGES;

