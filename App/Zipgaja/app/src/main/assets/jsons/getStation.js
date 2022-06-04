const request = require('request')
const bike = require('./bikeStation');
const busServiceKey = 't5N%2FFqNynkqx15wlqztjL4KgGPu4RnntvpSnvcC2Jx6Czvtphqeg8sXDPp%2BdMI48sTmABuKEDErpcqzM8dRZxw%3D%3D'

//공공데이터 포털에서 받은 내 서비스키
var parse = require('json-parse');

//따릉이 스테이션 정보 불러오기
const getStation = (userLatitude, userLongitude, callback) => {
    const bikeData = bike.bikeStationArr.filter(data => data.longitude <= (userLongitude + 0.005) && data.longitude >= (userLongitude - 0.005) && data.latitude <= (userLatitude + 0.005) && data.latitude >= (userLatitude - 0.005));
    var busData = JSON;
    var busDesc = [];

    var url = 'http://ws.bus.go.kr/api/rest/stationinfo/getStationByPos';
    var queryParams = '?serviceKey=' + busServiceKey ; /* Service Key*/
    queryParams += '&tmX=' + encodeURIComponent(userLongitude); /* */
    queryParams += '&tmY=' + encodeURIComponent(userLatitude); /* */
    queryParams += '&radius=' + encodeURIComponent(500); /* */
    queryParams += '&resultType=' + encodeURIComponent('json'); /* */

    request({
    url: url + queryParams,
    method: 'GET'
    }, function (error, response, body) {
    // console.log(url + queryParams)
    console.log('Status', response.statusCode);
    // console.log('Headers', JSON.stringify(response.headers));
    busData = JSON.parse(body);
    busDesc = busData.msgBody.itemList;

    callback(undefined,{
        bikeStation: bikeData,
        busStation: busDesc
    });
    });

}

module.exports = getStation;