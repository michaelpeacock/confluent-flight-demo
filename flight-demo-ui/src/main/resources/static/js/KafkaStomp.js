var stompClient = null;
let dashboardList = new Map();

connect();

function setConnected(connected) {
    $("#dashboard-data").html("");
}

function connect() {
    var socket = new SockJS('/flight-demo-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/dashboard-data', function (dashboard) {
            updateDashboard(JSON.parse(dashboard.body));            
        });
        stompClient.subscribe('/topic/flight-data', function (flight) {
            console.log("receive flight data");
            handleFlightUpdate(JSON.parse(flight.body));
        });
        stompClient.subscribe('/topic/filtered-flight-data', function (flight) {
            handleFilteredFlightUpdate(JSON.parse(flight.body));
        });

        //request the current data
        stompClient.send("/app/getFlights", {}, JSON.stringify("all"));
        stompClient.send("/app/getDashboardData", {}, JSON.stringify("all"));

    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function updateDashboard(message) {
    console.log("message: " + message);
    dashboardList.set(message.dashboardTitle, message.dashboardValue);

    var content = ' ';
    for (let [key, value] of dashboardList) {
        console.log(key + ' = ' + value);
        content += "<tr><td>" + key + ": " + value;
    }
    $("#dashboard-data").html(content);
}



