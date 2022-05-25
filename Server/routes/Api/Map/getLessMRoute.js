const request = require('request');
const s_request = require('sync-request');
const {bus_location} = require('./nightBusData');
var polyline = require('@mapbox/polyline');
const { route } = require('../..');
const { json } = require('express/lib/response');

var routeData = [];
var distanceData = [];
var start = [];
var end = [];
var GloballocationArr = [];
var bothStationCorrect = [];
var busRoutef = "";


function getLessMRoute (startLong, startLati, endLong, endLati, callback){
    routeData = [];
    distanceData = [];
    bothStationCorrect = [];
    getOneRoute(startLong, startLati, endLong, endLati, "all");

    //둘다 정류장이 존재할때 
    bothStationCorrect = [{"route": busRoutef}]
    for(var i = 0; i < GloballocationArr.length; i++) {
        if (i == 0) {
            bothStationCorrect.push({"name": "출발지", "time": calcWalkingTime(distanceData[i]), "cost": 0, "type": "walk"});
        }
        if (i == 1) {
            bothStationCorrect.push({"name": GloballocationArr[i][0].station_name, "time": calcBusTime(GloballocationArr.length - 3), "cost": 2250, "type": "bus"});
        }
        if (i == GloballocationArr.length - 1) {
            bothStationCorrect.push({"name": "목적지", "time": calcWalkingTime(distanceData[i]), "cost": 0, "type": "walk"});
        }
    }

    //1. 내주변 정류장에 시작위치 에서 끝위치 까지 가는 버스존재
    //2. 내주변 정류장은 있는데 끝까지 한번에 가는 버스가 없을때 // -> 시작위치에서 종료위치까지 가는 버스노선이 출발하는 정류장까지 따릉이 or 택시로 보내고 거기서 끝버스 까지 이동
    //3. 내주변 정류장 없고 끝 버스 없음 -> 사고발생 -> 주변에 버스 정류장이없어서 택시타라 
    //4. 내주변 정류장 없는데 끝버스 있음 -> 종료위치 주변에 버정이 있을때 -> 시작위치에서 종료위치까지 가는 버스노선이 출발하는 정류장까지 따릉이 or 택시로 보내고 거기서 끝버스 까지 이동
    

    callback(undefined, {
        bothStationCorrect: bothStationCorrect
    })

}
async function calcRouteData(startLong, startLati, endLong, endLati) {
    return await sendOSRM(startLong, startLati, endLong, endLati); 
}
function callBusDistance(arr) {
    var dis = 0.0;
    for(var i = 1; i < arr.length - 2; i++) {
        dis += arr[i]
    }
    return dis;
}

async function getOneRoute (startLong, startLati, endLong, endLati, type){
    //주변에 있는 버스정류장 조회
    startBusData = getStartBusData(startLati, startLong);
    endBusData = getEndBusData(endLati, endLong);

    //시작 위치 주변 버스정류장에서 탈 수 있는 버스 조회
    startBusName = startBusData.filter(data => data.name).map(data => data.name);
    startBusName = startBusName.filter((v, i)=> startBusName.indexOf(v) === i);

    startBusNum = startBusData.filter(data => [data.name, 
        data.node_id, 
        data.latitude, 
        data.longitude, 
        getDistance(startLati, startLong, data.latitude, data.longitude),
        data.stationNum]).map(data =>[
            data.name, 
            data.node_id, 
            data.latitude, 
            data.longitude, 
            getDistance(startLati, startLong, data.latitude, data.longitude),
        data.station_num]);
    
    var startLocation = startBusNum.sort(function(x,y){
        return x[4] - y[4];
    });
    startLocation = setBus(startBusName, startLocation);


    //도착지 주변 버스정류장에서 탈 수 있는 버스 조회
    endBusName = endBusData.filter(data => data.name).map(data => data.name);
    endBusName = endBusName.filter((v,i) => endBusName.indexOf(v) === i);
    
    endBusNum = endBusData.filter(data => [data.name, 
        data.node_id, 
        data.latitude, 
        data.longitude, 
        getDistance(endLati, endLong, data.latitude, data.longitude),
        data.station_num]).map(data =>[
            data.name, 
            data.node_id, 
            data.latitude, 
            data.longitude, 
            getDistance(endLati, endLong, data.latitude, data.longitude),
            data.station_num]);
    
    var endLocation = endBusNum.sort(function(x,y){
        return x[4] - y[4];
    });
    endLocation = setBus(endBusName, endLocation);
    start = startLocation;
    end = endLocation;
    console.log(startLocation,endLocation);

    //환승가능한지 조회
    isTransfer(startLocation, endLocation);
    if (type == "all") {
        var locationArr = [[{"latitude": startLati, "longitude": startLong}, {"latitude": startLocation[0][2], "longitude": startLocation[0][3]}]];
    }
    else if (type == "bus") {
        var locationArr = [];
    }
    //버스1개만을 타고갈때 + 시작위치 - 끝 위치 일경우 노선을 -- 해주면서 조회 한 후 비동기적으로 osrm호출
    if (startLocation[0][5] - endLocation[0][5] > 0) {
        for(var i = startLocation[0][5]; i >= endLocation[0][5]; i --) {
            location = bus_location.filter(data => data.station_num == i && data.name == startLocation[0][0]);
            nextLocation = bus_location.filter(data => data.station_num == i - 1 && data.name == endLocation[0][0]);
            locationArr.push([location[0], nextLocation[0]]);
        }
        if (type == "all") {
            locationArr.push([{"latitude": endLati, "longitude": endLong}, {"latitude": endLocation[0][2], "longitude": endLocation[0][3]}]);
        }
        GloballocationArr = locationArr;
        console.log(locationArr[0][0].latitude);
        //osrm 호출 부분 (정류장 마다 호출해서 조회)
        var data = locationArr.map((request) => sendOSRM(request));
    };
  
    //인코딩 된 값을 합쳐서 다시 디코딩 해서 경로를 만듬 
    var routeValue = [];
    for(var i = 0; i < routeData.length; i++) {
        for (var j = 0; j < routeData[i].length; j++) {
            routeValue.push(routeData[i][j])
        }
    }
    //만들어진 버스 루트
    var busRoute = polyline.encode(routeValue);
    busRoutef = busRoute;

    return busRoute;
}

async function sendOSRM(req) {
    return await connectOSRM(req);
}

//걸어서 가는 시간 계산 (분수)
function calcWalkingTime(distance) {
    return distance / 60.0;
}

function calcBusTime(stationNum) {
    return stationNum * 1.5;
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

//환승 확인 (구현중)
function isTransfer(start, end){
    var startBus = bus_location.filter(data => data.name === start[0][0]);
    var endBus = bus_location.filter(data => data.name === end[0][0]);

    for(var i = 0; i < startBus.length; i++){
    transfer = endBus.filter(data => data.node_id == startBus[i].node_id)
    }
    // console.log(transfer);

}


//현위치에서 가장 적게 걸리는 버스 정류장 조회
const setBus = (arr1, arr2) => {
    var sol = [];
    for(var i = 0; i < arr2.length; i++){
        if (arr1[0] === arr2[0][0]) {
            sol.push(arr2[0]);
            arr2.shift();
            arr1.shift();
        }
    }
    return sol;

}

//현재 좌표에서 끝 좌표까지 직선의 거리 계산
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

//주변 좌표의 버스정류장 검색
const getStartBusData = (latitude, longitude) => {
    //내 출발지 좌표로 부터의 주변 버스정류장
    const startPointBusStation = bus_location.filter(data => data.longitude <= (longitude + 0.005) && data.longitude >= (longitude - 0.005) && data.latitude <= (latitude + 0.005) && data.latitude >= (latitude - 0.005));
    return startPointBusStation;
}


const getEndBusData = (latitude, longitude) => {
    //내 종착지 좌표로 주변 버스 정류장 조회
    const endPointBusStation = bus_location.filter(data => data.longitude <= (longitude + 0.005) && data.longitude >= (longitude - 0.005) && data.latitude <= (latitude + 0.005) && data.latitude >= (latitude - 0.005));
    return endPointBusStation;
}

module.exports = getLessMRoute;