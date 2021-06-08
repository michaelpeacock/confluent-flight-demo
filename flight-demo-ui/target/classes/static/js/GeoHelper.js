function createGeoJSON(type, coordinates) {
    var coords = [];
    var positions = [];
    console.log("num of coords:" + coordinates.length);
    for (i = 0; i < coordinates.length; i++) {
        // get value in radians
        var cartographic = Cesium.Cartographic.fromCartesian(coordinates[i]);
        
        // convert to degrees
        var lat = Cesium.Math.toDegrees(cartographic.latitude);
        var lon = Cesium.Math.toDegrees(cartographic.longitude);

        positions.push([lon, lat]);
   }

   // end the polygon with the first points
   var cartographic1 = Cesium.Cartographic.fromCartesian(coordinates[0]);
   var lat1 = Cesium.Math.toDegrees(cartographic1.latitude);
   var lon1 = Cesium.Math.toDegrees(cartographic1.longitude);
   positions.push([lon1, lat1]);
   
   // add all points to the array
   coords.push(positions);

   var geojson = {
        "type": "Feature",
        "properties": {},
        "geometry": {
            "type": type,
            "coordinates": coords
        },
        
   };
   geojson._raw_data = JSON.stringify(geojson);
   console.log("geojson: %O", geojson);
   return geojson;
}

/*
{
    "TYPE": "Feature",
    "properties": {},
    "GEOMETRY": {
      "type": "Polygon",
      "coordinates": "[[[-77.256203, 38.866165], [-77.256203, 38.945625], [-77.134315, 38.945625], [-77.134315, 38.866165], [-77.256203, 38.866165]]]"
    },
    "_RAW_DATA": "{\"type\":\"Feature\",\"properties\":{},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[-77.256203,38.866165],[-77.256203,38.945625],[-77.134315,38.945625],[-77.134315,38.866165],[-77.256203,38.866165]]]}}",
    "UNITY": 1
  }
  */