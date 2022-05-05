const request = require('request')
const BikeserviceKey = '42496c485370696238316479436577'
//공공데이터 포털에서 받은 내 서비스키 

var parse = require('json-parse');
// const { route } = require('.');
// const app = require('../app');
// const { router, render } = require('../app');

//따릉이 스테이션 정보 불러오기
const getBikeStation = (callback) => {      

    console.log("따릉이 호출 완료");            //진입햇는지 확인용
    
    var url = 'http://openapi.seoul.go.kr:8088';
    var queryParams = '/' + BikeserviceKey; /* Service Key*/
    queryParams += '/' + encodeURIComponent('json'); /* */
    queryParams += '/' + encodeURIComponent('bikeList'); /* */
    queryParams += '/' + encodeURIComponent('1001'); /* */
    queryParams += '/' + encodeURIComponent('2000'); /* */

request({
    url: url + queryParams,
    method: 'GET'
}, function (error, response, body) {
    console.log(url + queryParams)
    console.log('Status', response.statusCode);
    // console.log('Headers', JSON.stringify(response.headers));
    console.log('Reponse received', body);

        callback(undefined,{    //body를 air이름으로 만들어서 index.js에 보내준다
            bike:body
        })
        });
        
}

module.exports = getBikeStation;