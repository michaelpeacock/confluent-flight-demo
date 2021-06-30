var globalFlightsData = new Cesium.CustomDataSource('global-flight-data');
viewer.dataSources.add(globalFlightsData);

var filteredFlightsData = new Cesium.CustomDataSource('filtered-flight-data');
viewer.dataSources.add(filteredFlightsData);
filteredFlightsData.show = false;

function handleFlightUpdate(flight_list) {
    console.log("handle flight update: flight count " + flight_list.length);
    flight_list.forEach(flight => {
        var entity = globalFlightsData.entities.getOrCreateEntity(flight.id);
        var position = Cesium.Cartesian3.fromDegrees(flight.longitude, flight.latitude, flight.barometricAltitude);
        var heading = Cesium.Math.toRadians(0);
        var pitch = Cesium.Math.toRadians(15.0);
        var roll = Cesium.Math.toRadians(0.0);
        var orientation = Cesium.Transforms.headingPitchRollQuaternion(position, new Cesium.HeadingPitchRoll(heading, pitch, roll));


        entity.description = buildDescription(flight);
        entity.position = position;
        entity.orientation = orientation;
        //entity.point = { pixelSize: 10, color: Cesium.Color.RED };
        entity.billboard = {
          color : Cesium.Color.BLACK.withAlpha(0.5),
          rotation : Cesium.Math.toRadians(-flight.heading),
          image : 'images/airplane-4-64.png',
          width : 32,
          height : 32
        };
        /*
          const pointEntity = viewer.entities.add({
          description: `ID: ${flight.id} data point at (${flight.longitude}, ${flight.latitude})`,
          position: Cesium.Cartesian3.fromDegrees(flight.longitude, flight.latitude, flight.altitude),
          point: { pixelSize: 10, color: Cesium.Color.RED }
          });
      */

      });
}

function buildDescription(flight) {
  var description = "\
    <p> \
      ID: " + flight.id + "<br> \
      Country: " + flight.originCountry + "<br><br> \
      Lat/Long: " + flight.latitude + " / " + flight.longitude + " <br> \
      Alitude: " + flight.barometricAltitude + " <br> \
      Heading: " + flight.heading + "<br> \
      Speed: " + flight.velocity + " <br><br> \
      Last Update: " + flight.timePosition + " <br> \
      </p>";
  return description;
}

function handleFilteredFlightUpdate(flight_list) {
  console.log("handle filtered flight update: flight count");
  flight_list.forEach(flight => {
    var entity = filteredFlightsData.entities.getOrCreateEntity(flight.A_id);
    var position = Cesium.Cartesian3.fromDegrees(flight.A_longitude, flight.A_latitude, flight.A_barometricAltitude);
    var heading = Cesium.Math.toRadians(0);
    var pitch = Cesium.Math.toRadians(15.0);
    var roll = Cesium.Math.toRadians(0.0);
    var orientation = Cesium.Transforms.headingPitchRollQuaternion(position, new Cesium.HeadingPitchRoll(heading, pitch, roll));

    
    entity.description = buildFilteredDescription(flight);
    entity.position = position;
    entity.orientation = orientation; 
    //entity.point = { pixelSize: 10, color: Cesium.Color.RED };
    entity.billboard = {
      color : Cesium.Color.BLACK.withAlpha(0.5),
      rotation : Cesium.Math.toRadians(-flight.A_heading),
      image : 'images/airplane-4-64.png',
      width : 32,
      height : 32
    };
    /*
      const pointEntity = viewer.entities.add({
      description: `ID: ${flight.id} data point at (${flight.longitude}, ${flight.latitude})`,
      position: Cesium.Cartesian3.fromDegrees(flight.longitude, flight.latitude, flight.altitude),
      point: { pixelSize: 10, color: Cesium.Color.RED }
      });
  */

    });

}

function buildFilteredDescription(flight) {
var description = "\
  <p> \
    ID: " + flight.A_id + "<br> \
    Country: " + flight.A_originCountry + "<br><br> \
    Lat/Long: " + flight.A_latitude + " / " + flight.A_longitude + " <br> \
    Alitude: " + flight.A_barometricAltitude + " <br> \
    Heading: " + flight.A_heading + "<br> \
    Speed: " + flight.A_velocity + " <br><br> \
    Last Update: " + flight.A_timePosition + " <br> \
    </p>";
return description;
}

function handleGlobalClick(cb) {
  globalFlightsData.show = !globalFlightsData.show;
}

function handleFilteredClick(cb) {
  filteredFlightsData.show = !filteredFlightsData.show;
}