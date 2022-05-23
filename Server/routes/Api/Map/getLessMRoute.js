const request = require('request')
const {bus_location} = require('./nightBusData');
var polyline = require('@mapbox/polyline');
const { route } = require('../..');

var routeData = [];

const getLessMRoute = async (startLong, startLati, endLong, endLati) => {
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
    console.log(startLocation,endLocation);

    //환승가능한지 조회
    isTransfer(startLocation, endLocation);

    //osrm 호출 부분 (정류장 마다 호출해서 조회)
    const timer = ms => new Promise(res => setTimeout(res, ms));
    // console.log(startLocation[0][5] - endLocation[0][5]);
    //버스1개만을 타고갈때 + 시작위치 - 끝 위치 일경우 노선을 -- 해주면서 조회 한 후 비동기적으로 osrm호출
    if (startLocation[0][5] - endLocation[0][5] > 0) {
        for(var i = startLocation[0][5]; i >= endLocation[0][5]; i --) {
            location = bus_location.filter(data => data.station_num == i && data.name == startLocation[0][0]);
            nextLocation = bus_location.filter(data => data.station_num == i - 1 && data.name == endLocation[0][0]);
            await connectOSRM(location[0].longitude, location[0].latitude, nextLocation[0].longitude, nextLocation[0].latitude);
            await timer(600);
        }
    }
    //인코딩 된 값을 합쳐서 다시 디코딩 해서 경로를 만듬 
    var routeValue = [];
    for(var i = 0; i < routeData.length; i++) {
        for (var j = 0; j < routeData[i].length; j++) {
            routeValue.push(routeData[i][j])
        }
    }
    //만들어진 버스 루트
    var busRoute = polyline.encode(routeValue);
    console.log(busRoute);
}

//osrm과 연동해서 경로 데이터 불러오기
const connectOSRM = async(sLong, sLati, eLong, eLati) =>{
    var url = 'http://3.82.223.178/route/v1/driving/';
    var queryParams =  sLong + "," + sLati + ";" + eLong + "," + eLati;

    request({
    url: url + queryParams,
    method: 'GET'
    }, function (error, response, body) {
    console.log(url + queryParams);
    console.log('Status', response.statusCode);
    // console.log('Headers', JSON.stringify(response.headers));
    var route = JSON.parse(body);
    routeData.push(polyline.decode(route.routes[0].geometry));
    });
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