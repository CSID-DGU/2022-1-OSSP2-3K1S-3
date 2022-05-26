searchRoute = require('./getLessMRoute');
const { route } = require('../..');
const {bus_location} = require('./nightBusData');

var startData = [];
var allroute = [];

//1. 내주변 정류장에 시작위치 에서 끝위치 까지 가는 버스존재
    //2. 내주변 정류장은 있는데 끝까지 한번에 가는 버스가 없을때 // -> 시작위치에서 종료위치까지 가는 버스노선이 출발하는 정류장까지 따릉이 or 택시로 보내고 거기서 끝버스 까지 이동
    //3. 내주변 정류장 없고 끝 버스 없음 -> 주변에 버스 정류장이없어서 택시타라 
    //4. 내주변 정류장 없는데 끝버스 있음 -> 종료위치 주변에 버정이 있을때 -> 시작위치에서 종료위치까지 가는 버스노선이 출발하는 정류장까지 따릉이 or 택시로 보내고 거기서 끝버스 까지 이동
    
async function pushData(sLong, sLati, sName, eLong, eLati, eName, type) {
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
        console.log(routeData);
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
    return (calculateRoute_1(sLong, sLati, sName, eLong, eLati, eName));
    
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
           return x[4] - y[4];
       });
       var startOriginLocation = startLocation;
       startLocation = setBus(startBusName, startLocation);
       return startLocation;
}
function calculTaxi(startLong, startLati, sName, endLong, endLati, eName) {
    //서울시내 최대 속도로 돌았을때, 분당 800미터 가능
    return {type: "taxi", time: getDistance(startLong, startLati, endLong, endLati) / 300, cost: calcMoney(startLong, startLati, endLong, endLati), route: [sName ,eName], recommend: 500};
    }
function calculateRoute_1(startLong, startLati, sName, endLong, endLati, eName) {
       //주변에 있는 버스정류장 조회
       var startBusData = getStartBusData(startLati, startLong, 0.01);
       var endBusData = getEndBusData(endLati, endLong, 0.01);
       if (startBusData.length == 0) {
           for (var i = 2; i < 15; i ++ ){
                startBusData = getStartBusData(startLati, startLong, i * 0.01);
                if (startBusData.length > 0) {
                    break;
                }
            }
       }
       if (endBusData.length == 0) {
            for (var i = 0; i < 15; i ++ ){
                endBusData = getEndBusData(endLati, endLong, i * 0.01);
                if (endBusData.length > 0) {
                    break;
                }
            }
       }

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
                data.station_num,
                data.station_name]);
       
       var startLocation = startBusNum.sort(function(x,y){
           return x[4] - y[4];
       });
       var startOriginLocation = startLocation;
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
                data.station_num,
                data.station_name]);
        
       var endLocation = endBusNum.sort(function(x,y){
           return x[4] - y[4];
       });
       var endOriginLocation = endLocation;
       endLocation = setBus(endBusName, endLocation);
       start = startLocation;
       end = endLocation;

       //일치하는 정류장이 있는지 조회
       if (isCorrectStation(startLocation, endLocation, startLong, startLati)) {
        console.log("일치함");
        return {type: "bus", time: calcBusTime(
            Math.abs(startLocation[0][5] - endLocation[0][5])) + 
            calcWalkingTime(getDistance(startLati,startLong, startLocation[0][2],startLocation[0][3]) +
            calcWalkingTime(getDistance(endLati, endLong, endLocation[0][2], endLocation[0][3]))
            ), cost: 2200, route: [sName, startLocation[0][6] + "("+ startLocation[0][0] +"번 버스)",endLocation[0][6] ,eName], recommend: 500};
        }
       else {
        console.log("일치 안함");
           //시작위치 주변은 있는데, 종료위치에는 없을때 
           var case2Location = calculateRoute_2(startLong, startLati, endLong, endLati, endOriginLocation);
           return {type: "bus", time: calcBusTime(Math.abs(case2Location[0][0][5] - case2Location[0][0][5]) + 
           calcWalkingTime(getDistance(startLati,startLong, case2Location[0][0][2],case2Location[0][0][3]) +
            calcWalkingTime(getDistance(endLati, endLong, case2Location[1][0][2], case2Location[1][0][3]))
            )), cost: 2200 + calcMoney(startLong, startLati, case2Location[0][0][3],case2Location[0][0][2]), route: [sName, case2Location[0][0][6] + "("+ case2Location[0][0][0] +"번 버스)", case2Location[1][0][6] ,eName], recommend: 500};
       }
}

//2. 내주변 정류장은 있는데 끝까지 한번에 가는 버스가 없을때 // -> 시작위치에서 종료위치까지 가는 버스노선이 출발하는 정류장까지 따릉이 or 택시로 보내고 거기서 끝버스 까지 이동
function calculateRoute_2(sLong, sLati, eLong, eLati, eStation) {
    var newEndLoacation = searchNearStation(eLong, eLati, eStation);
    if (newEndLoacation == false) {
    }
    else {
    var endLocation = userLocateStaion(eLong, eLati, newEndLoacation);
    var newStartLocation = searchNearStation(sLong, sLati, endLocation);
    var startLocation = userLocateStaion(sLong, sLati, newStartLocation);
    //endLocation배열과 startLocation배열이 시작위치와 끝 위치
    return [startLocation, endLocation];
    }
}

//주변 15키로미터까지의 정류장 재검색
function searchNearStation(long, lati, stationNum){
    for(var j = 1; j <= 15; j ++){
        var oneK = getStartBusData(lati, long, j * 0.01)
        for (var i = 0; i < stationNum.length; i ++ ){
            var oneKSearch = oneK.filter(data => data.name == stationNum[i][0]);
        }
        if (oneKSearch.length > 0) {
            return oneKSearch;
        }
    }
}
function calcMoney(sLong, sLati, endLong, endLati) {
    var distance = getDistance(sLati, sLong, endLati, endLong);
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
//이거 지금 겹치는 정류장 2개 이상일때 해결해야됨
function isCorrectStation(sloc, eloc, sLong, sLati) {
    if (sloc.length == 0) {
        var endBus = bus_location.filter(data => data.name === eloc[0][0]);
        var start = searchNearStation(sLong, sLati, eloc);
        var startBus = bus_location.filter(data => data.name == start[0].name);
        for(var i = 0; i < startBus.length; i++){
            transfer = startBus.filter(data => data.node_id == endBus[i].node_id);
        }        
            if (transfer.length > 0) {
                startData = [startBus]
                return true;
            }
            else {
                return false;
        }

    }
    else {

    var startBus = bus_location.filter(data => data.name === sloc[0][0]);
    var endBus = bus_location.filter(data => data.name === eloc[0][0]);

    for(var i = 0; i < startBus.length; i++){
    transfer = endBus.filter(data => data.node_id == startBus[i].node_id)
    }

    if (transfer.length > 0) {
        return true;
    }
    else {
        return false;
    }
    }
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


module.exports = main;