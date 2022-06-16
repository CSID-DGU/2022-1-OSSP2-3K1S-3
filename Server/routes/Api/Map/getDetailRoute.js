const request = require('request');
const s_request = require('sync-request');
const {bus_location} = require('./nightBusData');
const {bikeStationArr} = require('../Main/bikeStation');
var polyline = require('@mapbox/polyline');
const mysql = require("mysql2/promise");
require("dotenv").config(); // .env 하기 위한 dotenv 라이브러리 



var routeData = [];
var distanceData = [];

async function main(routeId, start, end, callback) {
    routeData = [];
    const returnData = [];
    var route = await getDB(routeId);
    sleep(500);

    if (route.bus_num == "taxi"){
        var data = connectOSRM(route.start_long, route.start_lati, route.end_long, route.end_lati);
        var geoData = encodePolyline();

        returnData.push({route: geoData});
        returnData.push({name: start, time: 0, cost: 0, type: "walk"});
        returnData.push({name: start + " ~ " + end, time: distanceData[0] / 300, cost: calcMoney(distanceData[0]), type: "taxi"});
        returnData.push({name: end, time: 0, cost: 0, type: "walk"});
    }
    if (route.bus_num == "walk") {
        var data = connectOSRM(route.start_long, route.start_lati, route.end_long, route.end_lati);
        var geoData = encodePolyline();

        returnData.push({route: geoData});
        returnData.push({name: start, time: 0, cost: 0, type: "walk"});
        returnData.push({name: start + " ~ " + end, time: calcWalkingTime(getDistance(route.start_lati, route.start_long, route.end_lati, route.end_long)), cost: 0, type: "walk"});
        returnData.push({name: end, time: 0, cost: 0, type: "walk"});
    }
    else {
        const startBusStation = route.bus_start;
        const endBusStation = route.bus_end;

        if (startBusStation - endBusStation > 0) {
            var busStation = bus_location.filter(data => (data.station_num <= route.bus_start) && (data.station_num >= route.bus_end) && data.name == route.bus_num);
            var reversBus = busStation.slice(0).reverse().map(data => data);
            //따릉이 포함 경로일때 
            if (route.s_bike_long != 0) {
                connectOSRM(route.start_long, route.start_lati, route.s_bike_long, route.s_bike_lati);
                connectOSRM(route.s_bike_long, route.s_bike_lati, reversBus[0].longitude, reversBus[0].latitude);
                reversBus.map((busStation, index) => connectOSRM(reversBus[index].longitude, reversBus[index].latitude, reversBus[index + 1 > reversBus.length - 1 ? index : index + 1].longitude, reversBus[index + 1 > reversBus.length - 1 ? index : index + 1].latitude));
                connectOSRM(route.fs_bike_long, route.fs_bike_lati, route.fe_bike_long, route.fe_bike_lati);
                connectOSRM(route.fe_bike_long, route.fe_bike_lati, route.end_long, route.end_lati);
                const returnRoute = encodePolyline()
                
                returnData.push({route: returnRoute});

                //따릉이
                returnData.push({name: start + "~" + bikeStationArr.filter(data => data.longitude.toFixed(8) == route.s_bike_long && data.latitude.toFixed(8) == route.s_bike_lati)[0].name + "(따릉이)", time: calcWalkingTime(getDistance(route.s_bike_lati, route.s_bike_long, route.start_lati, route.start_long)), cost: 0, type: "walk"});
                returnData.push({name: bikeStationArr.filter(data => data.longitude.toFixed(8) == route.s_bike_long && data.latitude.toFixed(8) == route.s_bike_lati)[0].name + " ~ " + bikeStationArr.filter(data => data.longitude.toFixed(8) == route.e_bike_long && data.latitude.toFixed(8) == route.e_bike_lati)[0].name, time: calcuBike(route.s_bike_long, route.s_bike_lati, route.e_bike_long, route.e_bike_lati), cost: 1000, type: "bike"});
                returnData.push({name: bikeStationArr.filter(data => data.longitude.toFixed(8) == route.e_bike_long && data.latitude.toFixed(8) == route.e_bike_lati)[0].name + " ~ " + reversBus[0].station_name + "("+reversBus[0].name+")", time: calcWalkingTime(getDistance(route.e_bike_lati, route.e_bike_long, reversBus[0].latitude, reversBus[0].longitude)), cost: 0, type: "walk"});

                //버스
                returnData.push({name: reversBus[0].station_name + " ~ " + busStation[0].station_name, time: calcBusTime(busStation.length), cost: 2150, type: "bus"});

                //따릉이
                returnData.push({name: busStation[0].station_name + "~" + bikeStationArr.filter(data => data.longitude.toFixed(8) == route.fs_bike_long && data.latitude.toFixed(8) == route.fs_bike_lati)[0].name + "(따릉이)", time: calcWalkingTime(getDistance(route.fs_bike_lati, route.fs_bike_long, busStation[0].latitude, busStation[0].longitude)), cost: 0, type: "walk"});
                returnData.push({name: bikeStationArr.filter(data => data.longitude.toFixed(8) == route.fs_bike_long && data.latitude.toFixed(8) == route.fs_bike_lati)[0].name + " ~ " + bikeStationArr.filter(data => data.longitude.toFixed(8) == route.fe_bike_long && data.latitude.toFixed(8) == route.fe_bike_lati)[0].name, time: calcuBike(route.fs_bike_long, route.fs_bike_lati, route.fe_bike_long, route.fe_bike_lati), cost: 1000, type: "bike"});
                returnData.push({name: bikeStationArr.filter(data => data.longitude.toFixed(8) == route.fe_bike_long && data.latitude.toFixed(8) == route.fe_bike_lati)[0].name + "(따릉이) ~ " + end, time: calcWalkingTime(getDistance(route.fe_bike_lati, route.fe_bike_long, route.end_lati, route.end_long)), cost: 0, type: "walk"});

            }
            else {
                //도보로 정류장 이동
                connectOSRM(route.start_long, route.start_lati, reversBus[0].longitude, reversBus[0].latitude);
                //버스
                reversBus.map((busStation, index) => connectOSRM(reversBus[index].longitude, reversBus[index].latitude, reversBus[index + 1 > reversBus.length - 1 ? index : index + 1].longitude, reversBus[index + 1 > reversBus.length - 1 ? index : index + 1].latitude));
                //도착
                connectOSRM(reversBus[reversBus.length - 1].longitude, reversBus[reversBus.length - 1].latitude, route.end_long, route.end_lati);
                
                const returnRoute = encodePolyline()
                returnData.push({route: returnRoute});

                //도보로 정류장
                returnData.push({name: start + "~" + reversBus[0].station_name, time: calcWalkingTime(getDistance(route.start_lati, route.start_long, reversBus[0].latitude, reversBus[0].longitude)), cost: 0, type: "walk"});
                //버스 노선
                returnData.push({name: reversBus[0].station_name + "(" + reversBus[0].name + ")" + "~" + reversBus[reversBus.length - 1].station_name, time: calcBusTime(reversBus.length), cost: 2150, type: "bus"});
                //도보로 목적지
                returnData.push({name: reversBus[reversBus.length - 1].station_name + "~" + end, time: calcWalkingTime(getDistance(reversBus[reversBus.length - 1].latitude, reversBus[reversBus.length - 1].longitude, route.end_lati, route.end_long)), cost: 0, type: "walk"});

            }
        }
        else if (startBusStation - endBusStation < 0) {
            var busStation = bus_location.filter(data => (data.station_num >= route.bus_start) && (data.station_num <= route.bus_end) && data.name == route.bus_num);
            var resultbust = busStation
            //따릉이 포함 경로일때 
            if (route.s_bike_long != 0) {
                connectOSRM(route.s_bike_long.toFixed(8), route.s_bike_lati.toFixed(8), busStation[0].longitude, busStation[0].latitude);
                
                busStation.map((busStation, index) => connectOSRM(resultbust[index].longitude, resultbust[index].latitude, resultbust[index + 1 >= resultbust.length ? index : index + 1].longitude, resultbust[index + 1 >= resultbust.length ? index : index + 1].latitude));
                connectOSRM(route.fs_bike_long, route.fs_bike_lati, route.fe_bike_long, route.fe_bike_lati);
                connectOSRM(route.fe_bike_long, route.fe_bike_lati, route.end_long, route.end_lati);
                const returnRoute = encodePolyline()

                
                returnData.push({route: returnRoute});

                //따릉이
                returnData.push({name: start + "~" + bikeStationArr.filter(data => data.longitude.toFixed(8) == route.s_bike_long && data.latitude.toFixed(8) == route.s_bike_lati)[0].name + "(따릉이)", time: calcWalkingTime(getDistance(route.s_bike_lati, route.s_bike_long, route.start_lati, route.start_long)), cost: 0, type: "walk"});
                returnData.push({name: bikeStationArr.filter(data => data.longitude.toFixed(8) == route.s_bike_long && data.latitude.toFixed(8) == route.s_bike_lati)[0].name + " ~ " + bikeStationArr.filter(data => data.longitude.toFixed(8) == route.e_bike_long && data.latitude.toFixed(8) == route.e_bike_lati)[0].name, time: calcuBike(route.s_bike_long, route.s_bike_lati, route.e_bike_long, route.e_bike_lati), cost: 1000, type: "bike"});
                returnData.push({name: bikeStationArr.filter(data => data.longitude.toFixed(8) == route.e_bike_long && data.latitude.toFixed(8) == route.e_bike_lati)[0].name + " ~ " + busStation[0].station_name + "("+busStation[0].name+")", time: calcWalkingTime(getDistance(route.e_bike_lati, route.e_bike_long, busStation[0].latitude, busStation[0].longitude)), cost: 0, type: "walk"});

                //버스
                returnData.push({name: busStation[0].station_name + " ~ " + busStation[busStation.length - 1].station_name, time: calcBusTime(busStation.length), cost: 2150, type: "bus"});

                console.log(bikeStationArr.filter(data => data.longitude.toFixed(8) == route.fs_bike_long && data.latitude.toFixed(8) == route.fs_bike_lati)[0].name);
                //따릉이
                returnData.push({name: busStation[busStation.length - 1].station_name + "~" + bikeStationArr.filter(data => data.longitude.toFixed(8) == route.fs_bike_long && data.latitude.toFixed(8) == route.fs_bike_lati)[0].name + "(따릉이)", time: calcWalkingTime(getDistance(route.fs_bike_lati, route.fs_bike_long, busStation[busStation.length - 1].latitude, busStation[busStation.length - 1].longitude)), cost: 0, type: "walk"});
                returnData.push({name: bikeStationArr.filter(data => data.longitude.toFixed(8) == route.fs_bike_long && data.latitude.toFixed(8) == route.fs_bike_lati)[0].name + " ~ " + bikeStationArr.filter(data => data.longitude.toFixed(8) == route.fe_bike_long.toFixed(8) && data.latitude == route.fe_bike_lati)[0].name, time: calcuBike(route.fs_bike_long, route.fs_bike_lati, route.fe_bike_long, route.fe_bike_lati), cost: 1000, type: "bike"});
                returnData.push({name: bikeStationArr.filter(data => data.longitude.toFixed(8) == route.fe_bike_long && data.latitude.toFixed(8) == route.fe_bike_lati)[0].name + "(따릉이) ~ " + end, time: calcWalkingTime(getDistance(route.fe_bike_lati, route.fe_bike_long, route.end_lati, route.end_long)), cost: 0, type: "walk"});
            }
            else {
                //도보로 정류장 이동
                connectOSRM(route.start_long, route.start_lati, busStation[0].longitude, busStation[0].latitude);
                //버스
                busStation.map((busStation, index) => connectOSRM(resultbust[index].longitude, resultbust[index].latitude, resultbust[index + 1 > resultbust.length - 1 ? index : index + 1].longitude, resultbust[index + 1 > resultbust.length - 1 ? index : index + 1].latitude));
                //도착
                connectOSRM(busStation[busStation.length - 1].longitude, busStation[busStation.length - 1].latitude, route.end_long, route.end_lati);
                
                const returnRoute = encodePolyline()
                returnData.push({route: returnRoute});

                //도보로 정류장
                returnData.push({name: start + "~" + busStation[0].station_name, time: calcWalkingTime(getDistance(route.start_lati, route.start_long, busStation[0].latitude, busStation[0].longitude)), cost: 0, type: "walk"});
                //버스 노선
                returnData.push({name: busStation[0].station_name + "(" + busStation[0].name + ")" + "~" + busStation[busStation.length - 1].station_name, time: calcBusTime(busStation.length), cost: 2150, type: "bus"});
                //도보로 목적지
                returnData.push({name: busStation[busStation.length - 1].station_name + "~" + end, time: calcWalkingTime(getDistance(busStation[busStation.length - 1].latitude, busStation[busStation.length - 1].longitude, route.end_lati, route.end_long)), cost: 0, type: "walk"});

            }
        }

    }
    // var data = locationArr.map((request) => sendOSRM(request));
    callback(undefined,{   
        route: returnData
    });
}

function sleep(ms) {
    return new Promise((resolve) => setTimeout(resolve, ms))
  }


async function connectOSRM (sLong, sLati, eLong, eLati) {
    // sLong, sLati, eLong, eLati
    var url = 'http://3.82.223.178/route/v1/driving/';
    var queryParams =  sLong + "," + sLati + ";" + eLong + "," + eLati;
    // var queryParams = req[0].longitude + "," + req[0].latitude + ";" + req[1].longitude + "," + req[1].latitude;
    
    var route = s_request('GET', url + queryParams);
    var jsonRoute = JSON.parse(route.getBody('utf8'));
    routeData.push(polyline.decode(jsonRoute.routes[0].geometry));

    
    distanceData.push(jsonRoute.routes[0].distance);

    return;
}


function encodePolyline(){
    var routeValue = [];
    for(var i = 0; i < routeData.length; i++) {
        for (var j = 0; j < routeData[i].length; j++) {
            routeValue.push(routeData[i][j])
        }
    }
    //만들어진 버스 루트
    var busRoute = polyline.encode(routeValue);
    busRoutef = busRoute;
    //인코딩 된 값을 합쳐서 다시 디코딩 해서 경로를 만듬 
    return busRoute;
}

function callBusDistance(arr) {
    var dis = 0.0;
    for(var i = 1; i < arr.length - 2; i++) {
        dis += arr[i]
    }
    return dis;
}
  
function calcMoney(distance) {
    var expay = 0;
    if (distance > 2000) {        
        expay = (distance - 2000) % 132;
        expay * 120;
        return distance + expay;
    }
    else {
        return 4600;
    }

}
//걸어서 가는 시간 계산 (분수)
function calcWalkingTime(distance) {
    return distance / 60.0;
}

function calcBusTime(stationNum) {
    return stationNum * 1.5;
}

function calcuBike(startLong, startLati, endLong, endLati) {
    var bikeTemp = getDistance(startLati, startLong, endLati, endLong) / 160 + 5;
    return bikeTemp;
}

async function getDB (routeId){
    const id = routeId;
    let connection = await mysql.createConnection({
        host: process.env.host,
        user: process.env.user,
        password: process.env.password,
        database: process.env.database
    })
    try {
        let [result] = await connection.query('select * from route where id = ' + id);
        connection.end();
        return await result[0];

    } catch (error) {
        console.log(error);
    }
} 

const getDistance = (lat1, lon1, lat2, lon2) => {
    if ((lat1 == lat2) && (lon1 == lon2))
        return 0;

    var radLat1 = Math.PI * lat1 / 180;
    var radLat2 = Math.PI * lat2 / 180;
    var theta = lon1 - lon2;
    var radTheta = Math.PI * theta / 180;
    var dist = Math.sin(radLat1) * Math.sin(radLat2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.cos(radTheta);
    if (dist > 1)
        dist = 1;

    dist = Math.acos(dist);
    dist = dist * 180 / Math.PI;
    dist = dist * 60 * 1.1515 * 1.609344 * 1000;
    if (dist < 100) dist = Math.round(dist / 10) * 10;
    else dist = Math.round(dist / 100) * 100;

    return dist;
}

module.exports = main;