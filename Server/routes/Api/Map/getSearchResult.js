searchRoute = require('./getLessMRoute');
const { route } = require('../..');
const {bus_location} = require('./nightBusData');
const {bikeStationArr} = require('../Main/bikeStation');
const mysql = require("mysql2/promise");

var startData = [];
var allroute = [];
var routeID = 0;
var returndata = [];

function sleep(ms) {
    return new Promise((resolve) => setTimeout(resolve, ms))
  }

async function pushData(sLong, sLati, sName, eLong, eLati, eName, type) {
    //버스에 대한 정보
    allroute.push(await calcBusRoute(sLong, sLati, sName, eLong, eLati, eName));
}
//type에 따라 낮은가격순/인기순/낮은시간순
async function main(sLong, sLati, sName, eLong, eLati, eName, type, callback) {
    console.log(sLong, sLati, sName, eLong, eLati, eName, type);
    switch (type) {
    case "lessMoney":
        returndata = [];
        pushData(sLong, sLati, sName, eLong, eLati, eName, type);
        //금액순으로 정렬
        await sleep(5500);
        var routeData = returndata.sort(function(x,y){
            return x.cost - y.cost;
        });
        callback(undefined,{   
            routeData: routeData
        });
        return;

    case "recommend":
        returndata = [];
        pushData(sLong, sLati, sName, eLong, eLati, eName, type);
        await sleep(5500);

        //추천순으로 정렬
        var routeData = returndata.sort(function(x,y){
            return x.recommend - y.recommend;
        });

        callback(undefined,{   
            routeData: routeData
        });
        return;

    case "lessTime":
        returndata = [];
        pushData(sLong, sLati, sName, eLong, eLati, eName, type);
        //시간순으로 정렬
        await sleep(5500);

        var routeData = returndata.sort(function(x,y){
            return x.time - y.time;
        });

        callback(undefined,{   
            routeData: routeData
        });
        return;

    }
}

/*버스 경로 탐색*/
async function calcBusRoute(sLong, sLati, sName, eLong, eLati, eName, callback) {
    console.log("경로 계산 실행");
    var startPoint = [];
    var endPoint = [];
    for(var i = 10; i < 15; i++) {
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

    console.log(sBusName, eBusName);
    var startLocation = startBusNum.sort(function(x,y){
        return x[4] - y[4];
    });
    startLocation = setBus(startBusName, startLocation);

    var endLocation = endBusNum.sort(function(x,y){
        return x[4] - y[4];
    });
    endLocation = setBus(endBusName, endLocation);
    var routeData = isLocate(startLocation,endLocation,sBusName,eBusName);
    const RD = routeData;

    //시작 위치로 부터 따릉이 경로
    var bikeRoute = [];
    for(var i = 0; i < routeData.length; i++) {
        var temp = calculateBicycle(sLong, sLati, sName, routeData[i][0][3], routeData[i][0][2], eName);
        bikeRoute.push(temp);
    }
    var eBikeRoute = [];
    //도착 위치로 부터 따릉이 경로
    for(var i = 0; i < routeData.length; i++) {
        var temp = calculateBicycle(routeData[i][1][3], routeData[i][1][2], sName, eLong, eLati,eName);
        eBikeRoute.push(temp);
    }

    const data = await Promise.all(routeData.map((routeData, index) => callbackToPush(sLong, sLati, eLong, eLati, RD, bikeRoute, eBikeRoute, index, sName, eName)));
    const data2 = await Promise.all(routeData.map((routeData, index) => callbackNoBikeToPush(sLong, sLati, eLong, eLati,RD, index, sName, eName)));

    returndata.push(await calculTaxi(sLong, sLati, sName, eLong, eLati, eName));

    return [data, data2];
}

async function callbackNoBikeToPush(sLong, sLati, eLong, eLati,routeData, index, sName, eName){
    var timeData = calcWalkingTime(getDistance(sLati, sLong, routeData[index][0][2], routeData[index][0][3])) + 
        calcBusTime(Math.abs(routeData[index][0][5] - routeData[index][1][5]))+
        calcWalkingTime(getDistance(routeData[index][1][2], routeData[index][1][3], eLati, eLong));
        var priceData = 2150;
        var esBus = routeData[index][0][6] + "(" + routeData[index][0][0] +"번 버스) -> " + " " + routeData[index][1][6] ;
        var reco = await getRecommendData(sLong, sLati, eLong, eLati);


        var id = await updateRouteTable(sLong, sLati, eLong, eLati, routeData[index][0][0], routeData[index][0][5], routeData[index][1][5], 0, 0, 0, 0, 0, 0, 0, 0);
        returndata.push({routeID: id, type: "bus", time: timeData, cost: priceData, route: [sName, esBus, eName], recommend: reco});
        return await id;
}
async function callbackToPush (sLong, sLati, eLong, eLati,routeData, bikeRoute, eBikeRoute, index, sName, eName){

    var priceData = 2150 + 2000;
    var esBus = routeData[index][0][6] + "(" + routeData[index][0][0] +"번 버스) -> " + " " + routeData[index][1][6] ;
    var reco = await getRecommendData(sLong, sLati, eLong, eLati);
    var timeData = calcuBike(bikeRoute[index][0][0].longitude, bikeRoute[index][0][0].latitude, bikeRoute[index][1][0].longitude, bikeRoute[index][1][0].latitude) +
    calcBusTime(Math.abs(routeData[index][0][5] - routeData[index][1][5]));

    var id = await updateRouteTable(sLong, sLati, eLong, eLati, routeData[index][0][0], routeData[index][0][5], routeData[index][1][5], bikeRoute[index][0][0].longitude, bikeRoute[index][0][0].latitude, bikeRoute[index][1][0].longitude, bikeRoute[index][1][0].latitude, eBikeRoute[index][0][0].longitude, eBikeRoute[index][0][0].latitude, eBikeRoute[index][1][0].longitude, eBikeRoute[index][1][0].latitude);
    returndata.push({routeID: id, type: "bus", time: timeData, cost: priceData, route: [sName, bikeRoute[index][0][0].name + "(따릉이)", bikeRoute[index][1][0].name + "(따릉이)", esBus,  eBikeRoute[index][0][0].name + "(따릉이)", eBikeRoute[index][1][0].name + "(따릉이)", eName], recommend: reco})

    return await id;
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

async function calculTaxi(startLong, startLati, sName, endLong, endLati, eName) {
    //서울시내 최대 속도로 돌았을때, 분당 800미터 가능
    var reco = await getRecommendData(startLong, startLati, endLong, endLati);
    var id = await updateRouteTable(startLong, startLati, endLong, endLati,'taxi', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
    return {routeID: id, type: "taxi", time: getDistance(startLong, startLati, endLong, endLati) / 300, cost: calcMoney(startLong, startLati, endLong, endLati), route: [sName ,eName], recommend: reco};
}

function calcuBike(startLong, startLati, endLong, endLati) {
    var bikeTemp = getDistance(startLati, startLong, endLati, endLong) / 260
    return bikeTemp;
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
         for (var i = 2; i < 15; i ++ ){
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
        endNearBike.push([endBikeData[i], getDistance(endLati, endLong, endBikeData[i].latitude, endBikeData[i].longitude)]);
    }

    var sBikeLocation = startNearBike.sort(function(x,y){
        return x[1] - y[1];
    });
    var eBikeLocation = endNearBike.sort(function(x,y){
        return x[1] - y[1];
    });

    return [sBikeLocation[0], eBikeLocation[0]];
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

async function getRecommendData(sLong, sLati, eLong, eLati) {
  const input_start_lati = sLati; // 입력받은 시작 위도
  const input_start_long = sLong; // 입력받은 시작 경도
  const input_end_lati = eLati; // 입력받은 끝 위도
  const input_end_long = eLong; // 입력받은 끝 경도

  var resultSum = 0
  var route_id = [];
  // 반경 시작과 끝에 대한 id를 뽑아낸다.
  const sql = 'SELECT ( 6371 * acos ( cos ( radians(start_lati) ) * cos( radians(?) ) * cos( radians(start_long) - radians(?) ) + sin ( radians(start_lati) ) * sin( radians(?) ))) AS distance1, ( 6371 * acos ( cos ( radians(end_lati) ) * cos( radians(?) ) * cos( radians(end_long) - radians(?) ) + sin ( radians(end_lati) ) * sin( radians(?) ))) AS distance2, id FROM route HAVING (distance1 <= 0.1 AND distance2 <= 0.1) ORDER BY id'
  try {
    let connection = await mysql.createConnection({
        host: process.env.host,
        user: process.env.user,
        password: process.env.password,
        database: process.env.database
    })

    let [result] = await connection.query(sql, [input_start_lati, input_start_long, input_start_lati, input_end_lati, input_end_long, input_end_lati]);
    if (result.length == 0) {
        return resultSum;
    }
  
    // return await result;

} catch (error) {
    console.log(error);
}

}

async function updateRouteTable (sLong, sLati, eLong, eLati, busNum, busStart, busEnd, sBikeLong, sBikeLati, eBikeLong, eBikelati, fsBikeLong, fsBikeLati, feBikeLong, feBikeLati, callback){

    const start_long = sLong;
    const start_lati = sLati;
    const end_long = eLong;
    const end_lati = eLati;
    const bus_num = busNum;
    const bus_start = busStart;
    const bus_end = busEnd;
    const s_bike_long = sBikeLong;
    const s_bike_lati = sBikeLati;
    const e_bike_long = eBikeLong;
    const e_bike_lati = eBikelati;
    try {
        let connection = await mysql.createConnection({
            host: process.env.host,
            user: process.env.user,
            password: process.env.password,
            database: process.env.database
        })

        let [result] = await connection.query('INSERT INTO route (start_long, end_long, start_lati, end_lati, bus_start , bus_end, s_bike_long, s_bike_lati, e_bike_long, e_bike_lati, bus_num, fs_bike_long, fs_bike_lati, fe_bike_long, fe_bike_lati) VALUES ('+ start_long + "," + end_long+ "," +start_lati+ "," +end_lati+ "," +bus_start+ "," +bus_end+ "," +s_bike_long+ "," +s_bike_lati+ "," + e_bike_long+ "," + e_bike_lati+ ",'" + bus_num+"',"+ fsBikeLong + "," + fsBikeLati + ","+feBikeLong + "," + feBikeLati + ")");
        connection.end();
        return await result.insertId;

    } catch (error) {
        console.log(error);
    }
} 

module.exports = main;