searchRoute = require('./getLessMRoute');
const { route } = require('../..');
const {bus_location} = require('./nightBusData');
const {bikeStationArr} = require('../Main/bikeStation');

var startData = [];
var allroute = [];

//1. 내주변 정류장에 시작위치 에서 끝위치 까지 가는 버스존재
    //2. 내주변 정류장은 있는데 끝까지 한번에 가는 버스가 없을때 // -> 시작위치에서 종료위치까지 가는 버스노선이 출발하는 정류장까지 따릉이 or 택시로 보내고 거기서 끝버스 까지 이동
    //3. 내주변 정류장 없고 끝 버스 없음 -> 주변에 버스 정류장이없어서 택시타라 
    //4. 내주변 정류장 없는데 끝버스 있음 -> 종료위치 주변에 버정이 있을때 -> 시작위치에서 종료위치까지 가는 버스노선이 출발하는 정류장까지 따릉이 or 택시로 보내고 거기서 끝버스 까지 이동
    
async function pushData(sLong, sLati, sName, eLong, eLati, eName, type) {
    //버스에 대한 정보
    allroute.push(getbusData(sLong, sLati, sName, eLong, eLati, eName));
    allroute.push(calculTaxi(sLong, sLati, sName, eLong, eLati, eName));
    return;
}
//type에 따라 낮은가격순/인기순/낮은시간순
function main(sLong, sLati, sName, eLong, eLati, eName, type, callback) {
    switch (type) {
    case "lessMoney":
        allroute = [];
        pushData(sLong, sLati, sName, eLong, eLati, eName, type);
        //금액순으로 정렬
        var routeData = allroute.sort(function(x,y){
            return x.cost - y.cost;
        });
        callback(undefined,{   
            routeData: routeData
        });
        return;

    case "recommend":
        allroute = [];
        pushData(sLong, sLati, sName, eLong, eLati, eName, type);
        //추천순으로 정렬
        var routeData = allroute.sort(function(x,y){
            return x.recommend - y.recommend;
        });

        callback(undefined,{   
            routeData: routeData
        });
        return;

    case "lessTime":
        allroute = [];
        pushData(sLong, sLati, sName, eLong, eLati, eName, type);
        //시간순으로 정렬
        var routeData = allroute.sort(function(x,y){
            return x.time - y.time;
        });

        callback(undefined,{   
            routeData: routeData
        });
        return;

    }
}

function getbusData(sLong, sLati, sName, eLong, eLati, eName) {
    console.log(calcBusRoute(sLong, sLati, sName, eLong, eLati, eName));
    return (calcBusRoute(sLong, sLati, sName, eLong, eLati, eName));
    
}

/*버스 경로 탐색*/
function calcBusRoute(sLong, sLati, sName, eLong, eLati, eName) {
    var startPoint = [];
    var endPoint = [];
    for(var i = 2; i < 15; i++) {
        startPoint = getStartBusData(sLati, sLong, i * 0.01);
        endPoint = getEndBusData(eLati, eLong, i * 0.01);

        if(startPoint.length > 0 && endPoint.length > 0) {
            break;
        }
    }
    //시작 데이터 정제
    var startBusNum = startPoint.filter(data => [data.name, 
        data.node_id, 
        data.latitude, 
        data.longitude, 
        getDistance(sLati, sLong, data.latitude, data.longitude),
        data.stationNum]).map(data =>[
             data.name, 
             data.node_id, 
             data.latitude, 
             data.longitude, 
             getDistance(sLati, sLong, data.latitude, data.longitude),
             data.station_num,
             data.station_name]);
    //종료 데이터 정제
    var endBusNum = endPoint.filter(data => [data.name, 
        data.node_id, 
        data.latitude, 
        data.longitude, 
        getDistance(eLati, eLong, data.latitude, data.longitude),
        data.stationNum]).map(data =>[
             data.name, 
             data.node_id, 
             data.latitude, 
             data.longitude, 
             getDistance(eLati, eLong, data.latitude, data.longitude),
             data.station_num,
             data.station_name]);

    //시작 위치 버스 이름
    var startBusName = startPoint.filter(data => data.name).map(data => data.name);
    startBusName = startBusName.filter((v,i) => startBusName.indexOf(v) === i);
    var sBusName = startPoint.filter(data => data.name).map(data => data.name);
    sBusName = sBusName.filter((v,i) => sBusName.indexOf(v) === i);

    //종료 위치 버스 이름
    var endBusName = endPoint.filter(data => data.name).map(data => data.name);
    endBusName = endBusName.filter((v,i) => endBusName.indexOf(v) === i);
    var eBusName = endPoint.filter(data => data.name).map(data => data.name);
    eBusName = eBusName.filter((v,i) => eBusName.indexOf(v) === i);

    var startLocation = startBusNum.sort(function(x,y){
        return x[4] - y[4]
    });
    startLocation = setBus(startBusName, startLocation);

    var endLocation = endBusNum.sort(function(x,y){
        return x[4] - y[4]
    });
    endLocation = setBus(endBusName, endLocation);
    var routeData = isLocate(startLocation,endLocation,sBusName,eBusName);
    return routeData;
}

function isLocate(startData, endData, startName, endName) {
    var response = [];
    for(var i = 0; i < startName.length; i++) {
        for(var j = 0; j < endName.length; j++) {
            //버스 방향이 일치할때
            if (startName[i] == endName[j] && typeof(startData[i]) != "undefined" && typeof(endData[j]) != "undefined") {
                response.push([startData[i], endData[j]]);
            }
        }
    }
    return response;

}



function userLocateStaion(startLong, startLati, startBusData) {   
       //위치 주변 버스정류장에서 탈 수 있는 버스 조회
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
                data.station_num,
                data.station_name]);
       
       var startLocation = startBusNum.sort(function(x,y){
           return x[4] - y[4]
       });
       var startOriginLocation = startLocation;
       startLocation = setBus(startBusName, startLocation);
       return startLocation;
}

function calculTaxi(startLong, startLati, sName, endLong, endLati, eName) {
    //서울시내 최대 속도로 돌았을때, 분당 800미터 가능
    return {type: "taxi", time: getDistance(startLong, startLati, endLong, endLati) / 300, cost: calcMoney(startLong, startLati, endLong, endLati), route: [sName ,eName], recommend: 500};
}

function calculateBicycle(startLong, startLati, sName, endLong, endLati, eName) {
    var startBikeData = getStartBikeData(startLati, startLong, 0.01);
    var endBikeData = getEndBikeData(endLati, endLong, 0.01);
    if (startBikeData.length == 0) {
        for (var i = 2; i < 15; i ++ ){
            startBikeData = getStartBikeData(startLati, startLong, i * 0.01);
             if (startBikeData.length > 0) {
                 break;
             }
         }
    }
    if (endBikeData.length == 0) {
         for (var i = 0; i < 15; i ++ ){
            endBikeData = getEndBikeData(endLati, endLong, i * 0.01);
             if (endBikeData.length > 0) {
                 break;
             }
         }
    }
    var startNearBike = [];
    var endNearBike = [];
    for(var i = 0; i < startBikeData.length; i++) {
        startNearBike.push([startBikeData[i], getDistance(startLati, startLong, startBikeData[i].latitude, startBikeData[i].longitude)]);
    }
    for(var i = 0; i < endBikeData.length; i++) {
        endNearBike.push([endBikeData[i], getDistance(startLati, startLong, endBikeData[i].latitude, endBikeData[i].longitude)]);
    }

    var startLocation = startNearBike.sort(function(x,y){
        return x[1] - y[1];
    });
    var endlocation = endNearBike.sort(function(x,y){
        return x[1] - y[1];
    });

    // console.log(startLocation[0], endlocation[0], "start,end");
}


function calcBusTime(stationNum) {
    return stationNum * 1.5;
}
function calcWalkingTime(distance) {
    return distance / 60.0;
}

//현위치에서 가장 적게 걸리는 버스 정류장 조회
const setBus = (arr1, arr2) => {
    var sol = [];
    for(var i = 0; i < arr2.length; i++){
        if (arr1.length == 0) {
            break;
        }
        if (arr1[0] === arr2[i][0]) {
            sol.push(arr2[i]);
            arr1.shift();
            arr2.shift();
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
const getStartBusData = (latitude, longitude, distance) => {
    //내 출발지 좌표로 부터의 주변 버스정류장
    const startPointBusStation = bus_location.filter(data => data.longitude <= (longitude + distance) && data.longitude >= (longitude - distance) && data.latitude <= (latitude + distance) && data.latitude >= (latitude - distance));
    return startPointBusStation;
}


const getEndBusData = (latitude, longitude, distance) => {
    //내 종착지 좌표로 주변 버스 정류장 조회
    const endPointBusStation = bus_location.filter(data => data.longitude <= (longitude + distance) && data.longitude >= (longitude - distance) && data.latitude <= (latitude + distance) && data.latitude >= (latitude - distance));
    return endPointBusStation;
}

const getStartBikeData = (latitude, longitude, distance) => {
    //내 출발지 좌표로 부터의 주변 버스정류장
    const startPointBikeStation = bikeStationArr.filter(data => data.longitude <= (longitude + distance) && data.longitude >= (longitude - distance) && data.latitude <= (latitude + distance) && data.latitude >= (latitude - distance));
    return startPointBikeStation;
}


const getEndBikeData = (latitude, longitude, distance) => {
    //내 종착지 좌표로 주변 버스 정류장 조회
    const endPointBikeStation = bikeStationArr.filter(data => data.longitude <= (longitude + distance) && data.longitude >= (longitude - distance) && data.latitude <= (latitude + distance) && data.latitude >= (latitude - distance));
    return endPointBikeStation;
}

module.exports = main;