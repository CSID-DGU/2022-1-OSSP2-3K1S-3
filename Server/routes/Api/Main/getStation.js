const request = require('request')
const {bikeStationArr} = require('./bikeStation');
const {bus_location} = require("../Map/nightBusData");
const busServiceKey = 't5N%2FFqNynkqx15wlqztjL4KgGPu4RnntvpSnvcC2Jx6Czvtphqeg8sXDPp%2BdMI48sTmABuKEDErpcqzM8dRZxw%3D%3D'

//공공데이터 포털에서 받은 내 서비스키 
var parse = require('json-parse');

const getStartBusData = (latitude, longitude, distance) => {
    //내 출발지 좌표로 부터의 주변 버스정류장
    const startPointBusStation = bus_location.filter(data => data.longitude <= (longitude + distance) && data.longitude >= (longitude - distance) && data.latitude <= (latitude + distance) && data.latitude >= (latitude - distance));
    return startPointBusStation;
}
//따릉이 스테이션 정보 불러오기
const getStation = (userLatitude, userLongitude, callback) => {     
    const distance = 0.005; 
    var bikeData = bikeStationArr.filter(data => data.longitude <= (userLongitude + distance) && data.longitude >= (userLongitude - distance) && data.latitude <= (userLatitude + distance) && data.latitude >= (userLatitude - distance));
    var busData = JSON;
    var busDesc = [];

    busDesc = getStartBusData(userLatitude, userLongitude, 0.005);

    callback(undefined,{   
        bikeStation: bikeData,
        busStation: busDesc
    });
        
}

module.exports = getStation;