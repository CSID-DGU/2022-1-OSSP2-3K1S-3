const request = require('request');
const s_request = require('sync-request');
const {bus_location} = require('./nightBusData');
const {bikeStationArr} = require('../Main/bikeStation');
var polyline = require('@mapbox/polyline');
const mysql = require("mysql2/promise");
const { map } = require('../../../app');
require("dotenv").config(); // .env 하기 위한 dotenv 라이브러리 



var routeData = [];
var distanceData = [];
var start = [];
var end = [];
var GloballocationArr = [];
var bothStationCorrect = [];
var busRoutef = "";

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
    else {
        const startBusStation = route.bus_start;
        const endBusStation = route.bus_end;

        if (startBusStation - endBusStation > 0) {
            var busStation = bus_location.filter(data => (data.station_num <= route.bus_start) && (data.station_num >= route.bus_end) && data.name == route.bus_num);
            var reversBus = busStation.reverse().map(data => data);
            console.log(busStation);
            //따릉이 포함 경로일때 
            if (route.s_bike_long != 0) {
                connectOSRM(route.s_bike_long, route.s_bike_lati, busStation[0].longitude, busStation[0].latitude);
                
                busStation.map((busStation, index) => connectOSRM(reversBus[index].longitude, reversBus[index].latitude, reversBus[index + 1 >= reversBus.length ? index : index + 1].longitude, reversBus[index + 1 >= reversBus.length ? index : index + 1].latitude));
                const returnRoute = encodePolyline()
                
                returnData.push({route: returnRoute});

                //따릉이
                returnData.push({name: start + "~" + bikeStationArr.filter(data => data.longitude == route.s_bike_long && data.latitude == route.s_bike_lati).name, time: calcWalkingTime(getDistance(route.s_bike_lati, route.s_bike_long, route.start_lati, route.start_long)), cost: 0, type: "walk"});
                returnData.push({name: bikeStationArr.filter(data => data.longitude == route.s_bike_long && data.latitude == route.s_bike_lati).name + "~" + bikeStationArr.filter(data => data.longitude == route.e_bike_long && data.latitude == route.e_bike_lati).name, time: calcuBike(route.s_bike_long, route.s_bike_lati, route.e_bike_long, route.e_bike_lati), cost: 1000, type: "bike"});
                returnData.push({name: bikeStationArr.filter(data => data.longitude == route.e_bike_long && data.latitude == route.e_bike_lati).name + "~" + reversBus[0].name, time: calcWalkingTime(getDistance(route.e_bike_lati, route.e_bike_long, reversBus[0].latitude, reversBus[0].long)), cost: 0, type: "walk"});

                //버스
                returnData.push({name: reversBus[0].station_name + " ~ " + busStation[0].station_name, time: calcBusTime(busStation.length), cost: 2150, type: "bus"});

                //따릉이
                returnData.push({name: busStation[0].station_name + "~" + bikeStationArr.filter(data => data.longitude == route.fs_bike_long && data.latitude == route.fs_bike_lati).name, time: calcWalkingTime(getDistance(route.fs_bike_lati, route.fs_bike_long, busStation[0].latitude, busStation[0].start_long)), cost: 0, type: "walk"});
                returnData.push({name: bikeStationArr.filter(data => data.longitude == route.fs_bike_long && data.latitude == route.fs_bike_lati).name + "~" + bikeStationArr.filter(data => data.longitude == route.fe_bike_long && data.latitude == route.fe_bike_lati).name, time: calcuBike(route.fs_bike_long, route.fs_bike_lati, route.fe_bike_long, route.fe_bike_lati), cost: 1000, type: "bike"});
                returnData.push({name: bikeStationArr.filter(data => data.longitude == route.e_bike_long && data.latitude == route.e_bike_lati).name + "~" + end, time: calcWalkingTime(getDistance(route.e_bike_lati, route.e_bike_long, route.end_lati, route.end_long)), cost: 0, type: "walk"});

                returnData.push({name: end, time: 0, cost: 0, type: "walk"});
            }
        }
        else if (startBusStation - endBusStation < 0) {
            var busStation = bus_location.filter(data => (data.station_num >= route.bus_start) && (data.station_num <= route.bus_end) && data.name == route.bus_num);
            var resultbust = busStation
            //따릉이 포함 경로일때 
            if (route.s_bike_long != 0) {
                connectOSRM(route.s_bike_long, route.s_bike_lati, busStation[0].longitude, busStation[0].latitude);
                
                busStation.map((busStation, index) => connectOSRM(resultbust[index].longitude, resultbust[index].latitude, resultbust[index + 1 >= resultbust.length ? index : index + 1].longitude, resultbust[index + 1 >= resultbust.length ? index : index + 1].latitude));
                const returnRoute = encodePolyline()
                console.log(returnRoute);
            }
            else {

            }
        }

    }



    console.log(route);
    // var data = locationArr.map((request) => sendOSRM(request));
    callback(undefined,{   
        route: returnData
    });
}

function sleep(ms) {
    return new Promise((resolve) => setTimeout(resolve, ms))
  }


//osrm과 연동해서 경로 데이터 불러오기
async function connectOSRM (req) {
    // sLong, sLati, eLong, eLati
    var url = 'http://3.82.223.178/route/v1/driving/';
    // var queryParams =  sLong + "," + sLati + ";" + eLong + "," + eLati;
    var queryParams = req[0].longitude + "," + req[0].latitude + ";" + req[1].longitude + "," + req[1].latitude;
    
    var route = s_request('GET', url + queryParams);
    var jsonRoute = JSON.parse(route.getBody('utf8'));
    routeData.push(polyline.decode(jsonRoute.routes[0].geometry));
    
    distanceData.push(jsonRoute.routes[0].distance);

    return;
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
    var bikeTemp = getDistance(startLati, startLong, endLati, endLong) / 260
    return bikeTemp;
}

async function getDB (routeId){
    const id = routeId;
    try {
        let connection = await mysql.createConnection({
            host: process.env.host,
            user: process.env.user,
            password: process.env.password,
            database: process.env.database
        })
        let [result] = await connection.query('select * from route where id = ' + id);
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