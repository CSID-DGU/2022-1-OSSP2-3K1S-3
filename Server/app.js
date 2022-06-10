var createError = require('http-errors');
var express = require('express');
var path = require('path');
var cookieParser = require('cookie-parser');
var logger = require('morgan');

var indexRouter = require('./routes/index');
var usersRouter = require('./routes/users');
const res = require('express/lib/response');
const router = require('./routes/index');
const getStation = require('./routes/Api/Main/getStation');
const getDetailRoute = require('./routes/Api/Map/getDetailRoute');
const getSearchResult = require('./routes/Api/Map/getSearchResult');
const db = require("./module/db_connect");
var details = require("./routes/Api/Recommend/sumofsum");
var insert_bad = require("./routes/Api/Recommend/insert_bad");
var insert_good = require("./routes/Api/Recommend/insert_good");
var app = express();

// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'jade');

app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

app.use('/', indexRouter);
app.use('/users', usersRouter);

//메인 뷰 따릉이 및 버스 정류장 제공 API
app.post('/Api/Main/getStation', (req, res) => {
  console.log("[call getStation Api]");

  const userLongitude = req.body.nameValuePairs.long;
  const userLatitude = req.body.nameValuePairs.lati;

  console.log("log :: ", userLatitude, userLongitude);

  getStation(userLatitude, userLongitude,(error, {bikeStation, busStation} = {}) => {
    if (error) {
      console.log("Error to bike");
      return res.send({error})
    }
    res.json({status: res.statusCode, data: [{bike: bikeStation, bus: busStation}]});
    // console.log(station);
  })
})

//경로 검색결과 리스트 조회
app.post('/Api/route/searchList', (req, res) => {
  console.log("[call searchList Api]");

  const startLong = req.body.nameValuePairs.sLong;
  const startLati = req.body.nameValuePairs.sLati;
  const startName = req.body.nameValuePairs.sName;

  const endLong = req.body.nameValuePairs.eLong;
  const endLati = req.body.nameValuePairs.eLati;
  const endName = req.body.nameValuePairs.eName;

  const type = req.body.nameValuePairs.type;
  console.log("log :: ", startLong, startLati, startName, endLong, endLati, endName ,type);

  getSearchResult(startLong, startLati, startName, endLong, endLati, endName ,type, (error, {routeData} = {}) => {
    if(error) {
      console.log("Error to searchResult");
      return res.send({error});
    }
    res.json({status: res.statusCode, data: [{route: routeData}]})
  });
});

//경로탐색
app.post('/Api/route/detailRoute', (req, res) => {
  console.log("[call detailRoute Api]");

  const id = req.body.id;
  const start = req.body.start;
  const end = req.body.end;
  
  console.log("log :: ",id, start, end);

  getDetailRoute(id, start, end,(error, {route}) => {
    if (error) {
      console.log('error');
      return res.send({error});
    }
    res.json({status: res.statusCode, data: route});
  });    
})

// 추천 비추천 데이터 API
app.post('/Api/Recommend/good', (req, res) => { // 요청시 추천 데이터 값이 갱신된다.
  
  console.log("[Recommend good requestData]", req.body.id, req.body.good1, req.body.good2, req.body.good3, req.body.good4, req.body.good);

  const conn = db.conn();
  const route_id = req.body.id; // 경로에 대한 키 값
  const good1 = (req.body.good1 == "true")? 1:0; // true or false 문자열로 받아옴
  const good2 = (req.body.good2 == "true")? 1:0; // true or false
  const good3 = (req.body.good3 == "true")? 1:0; // true or false
  const good4 = (req.body.good4 == "true")? 1:0; // true or false
  const good = req.body.good; // 문자열

  insert_good.good(route_id, good1, good2, good3, good4, good);
  res.end();
})

app.post('/Api/Recommend/bad', (req, res) => { // 요청시 비추천 데이터 값이 갱신된다..
  
  console.log("[Recommend bad requestData]", req.body.id, req.body.bad1, req.body.bad2, req.body.bad3, req.body.bad4, req.body.bad);

  const route_id = req.body.id; // 경로에 대한 키 값 
  const bad1 = (req.body.bad1 == "true")? 1:0; // true or false 문자열로 받아옴
  const bad2 = (req.body.bad2 == "true")? 1:0; // true or false
  const bad3 = (req.body.bad3 == "true")? 1:0; // true or false
  const bad4 = (req.body.bad4 == "true")? 1:0; // true or false
  const bad = req.body.bad; // 문자열

  insert_bad.bad(route_id, bad1,bad2,bad3,bad4,bad);
  res.end();
})

// API 추천, 비추천 상세보기 상위 2개 항목과 상위 기타항목
app.post('/Api/Detail', (req, res) => {
  
 console.log("[Detail id requestData]", req.body.nameValuePairs.id);

 async function test3(id) {
  a = await details.detail1(id); // 추천 데이터 합산하기
  test4(a, id);
  async function test4(a, id) {
      b = await details.detail2(id); // 비추천 데이터 합산하기
      check1 = [a[0], a[1], a[2], a[3]];
      c = [a[0], a[1], a[2], a[3]]; // 합산된 추천 값 sorting
      c.sort(function(a, b){
          return b-a;
      })

      check2 = [b[0], b[1], b[2], b[3]];
      d = [b[0], b[1], b[2], b[3]]; // 합산된 비추천 값 sorting
      d.sort(function(a,b){
          return b-a;
      })

      if(a[4].length >= 2){ // 합산된 추천 값 기타 항목 추리기
          a1 = a[4][0];
          a2 = a[4][1];
      }
      else if(a[4].length == 1){
          a1 = a[4][0];
          a2 = "";
      }
      else{
          a1 = "";
          a2 = "";
      }

      if(b[4].length >= 2){ // 합산된 비추천 값 기타 항목 추리기
          x1 = b[4][0];
          x2 = b[4][1];
      }
      else if(b[4].length == 1){
          x1 = b[4][0];
          x2 = "";
      }
      else{
          x1 = "";
          x2 = "";
      }

      var status = res.statusCode

      num1 = check1.indexOf(c[0]);
      check1[num1] = -1; // 중복된 인덱스 방지
      num2 = check1.indexOf(c[1]);
      num3 = check2.indexOf(d[0]);
      check2[num3] = -1; // 중복된 인덱스 방지
      num4 = check2.indexOf(d[1]);

    
      good_check1 = details.detail3(num1); 
      good_check2 = details.detail3(num2); 
      bad_check1 = details.detail4(num3); 
      bad_check2 = details.detail4(num4); 
      
      res.json({status: status, data: {good1: c[0], good2: c[1], good_check1: good_check1, good_check2: good_check2, good3: a1, good4: a2, bad1: d[0], bad2: d[1], bad_check1: bad_check1, bad_check2: bad_check2, bad3: x1, bad4: x2}});
  }
}
test3(req.body.nameValuePairs.id);

})

// API 추천 수
// 삭제 및 수정할 예정 
app.get('/Api/reco_number', (req, res) => {
  
  input_start_lati; // 입력받은 시작 위도
  input_start_long; // 입력받은 시작 경도
  input_end_lati; // 입력받은 끝 위도
  input_end_long; // 입력받은 끝 경도
  // 반경 시작과 끝에 대한 id를 뽑아낸다.
  const sql = 'SELECT ( 6371 * acos ( cos ( radians(start_lati) ) * cos( radians(?) ) * cos( radians(start_long) - radians(?) ) + sin ( radians(start_lati) ) * sin( radians(?) ))) AS distance1, ( 6371 * acos ( cos ( radians(end_lati) ) * cos( radians(?) ) * cos( radians(end_long) - radians(?) ) + sin ( radians(end_lati) ) * sin( radians(?) ))) AS distance2, id FROM route HAVING (distance1 <= 0.3 AND distance2 <= 0.3) ORDER BY id'

  var good_sum = 0;
  const conn = db.conn();
  conn.query(sql, [input_start_lati, input_start_long, input_start_lati, input_end_lati, input_end_long, input_end_lati], (err, results) => {
    for(i=0; i<results.length; i++){ // 경로 id 뽑아내기
      route_id = results[i].id;
      conn.query('SELECT * FROM recommend WHERE route_id =?', [route_id], (err, result) => {
        if(err) throw err;
        if(result[0] == null){ // 해당 값 없으면 0
          good_sum += 0;
        }
        else{
          good_sum += result[0].good1 + result[0].good2 + result[0].good3 + result[0].good4;
        }
      })
    }
    conn.end();
    res.json({sum: good_sum});
    // res.end();
  })
})

// catch 404 and forward to error handler
app.use(function(req, res, next) {
  next(createError(404));
});

// error handler
app.use(function(err, req, res, next) {
  // set locals, only providing error in development
  res.locals.message = err.message;
  res.locals.error = req.app.get('env') === 'development' ? err : {};

  // render the error page
  res.status(err.status || 500);
  res.render('error');
});

module.exports = app;
