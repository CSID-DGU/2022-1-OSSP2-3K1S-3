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
const db = require("./module/db_connect");

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
app.get('/Api/Main/getStation', (req, res) => {
  console.log("[call getStation Api]");

  const userLongitude = req.body.long;
  const userLatitude = req.body.lati;

  getStation(userLatitude, userLongitude,(error, {bikeStation, busStation} = {}) => {
    if (error) {
      console.log("Error to bike");
      return res.send({error})
    }
    res.json({status: res.statusCode, data: [{bike: bikeStation, bus: busStation}]});
    // console.log(station);
  })
})

// 추천 비추천 데이터 API
app.post('/Api/Recommend/good', (req, res) => { // 요청시 추천 데이터 값이 갱신된다.
  const conn = db.conn();
  const route_id = req.body.id; // 경로에 대한 키 값
  const good1 = req.body.good1? 1:0; // true or false
  const good2 = req.body.good2? 1:0; // true or false
  const good3 = req.body.good3? 1:0; // true or false
  const good4 = req.body.good4? 1:0; // true or false
  const good = req.body.good; // 문자열
  conn.query('SELECT * FROM recommend WHERE route_id=?', [route_id], function(err, recommend, fields){
    if(err) throw err;     
    if(recommend[0] == null){
        conn.query('INSERT INTO recommend VALUES(?, ?, ?, ?, ? ,?)',[route_id, route_id, good1, good2, good3, good4], (err, result) => {
          conn.query('INSERT INTO reco_string VALUES(?, ?, ?)',[1, route_id, good], (err, result) => {
            if(err) throw err;
            conn.end(); // DB 접속 종료
          })
          if(err) throw err;
          
          res.end();
        })
      }else{
        const sql1 = 'UPDATE recommend SET good1 = good1 + ?, good2 = good2 + ?, good3 = good3 + ?, good4 = good4 + ?';
        const sql2 = 'INSERT INTO reco_string VALUES(?, ?, ?)';
        conn.query(sql1, [good1, good2, good3, good4], (err, results) => {
          conn.query('SELECT * FROM reco_string WHERE route_id = ?', [route_id], (err, results) =>{
            if (err) throw err;
          a = results[results.length - 1].string_id; // 문자열에 갱신할 값을 가져온다.
          conn.query(sql2, [a + 1, route_id, good], (err, results) => {
            if(err) throw err;
            conn.end(); 
          }) 
        })
          if (err) throw err;
          res.end();
        }) 
      }
  })
})

app.post('/Api/Recommend/bad', (req, res) => { // 요청시 비추천 데이터 값이 갱신된다..
  const conn = db.conn();
  const route_id = req.body.id; // 경로에 대한 키 값
  const bad1 = req.body.bad1? 1:0; // true or false
  const bad2 = req.body.bad2? 1:0; // true or false
  const bad3 = req.body.bad3? 1:0; // true or false
  const bad4 = req.body.bad4? 1:0; // true or false
  const bad = req.body.bad; // 문자열

  conn.query('SELECT * FROM not_recommend WHERE route_id=?', [route_id], function(err, not_recommend, fields){
    if(err) throw err;
      if(not_recommend[0] == null){ // 기존 경로에 대한 route_id가 존재하지 않을 때
        conn.query('INSERT INTO not_recommend VALUES(?, ?, ?, ?, ? ,?)',[route_id, route_id, bad1, bad2, bad3, bad4], (err, result) => {
          if(err) throw err;
          conn.query('INSERT INTO notre_string VALUES(?, ?, ?)',[1, route_id, bad], (err, result) => {
            if(err) throw err;
            conn.end(); // DB 접속 종료
          })        
          res.end();
        })
      }else{ // 기존 경로에 대한 route_id가 존재할 때
        const sql1 = 'UPDATE not_recommend SET bad1 = bad1 + ?, bad2 = bad2 + ?, bad3 = bad3 + ?, bad4 = bad4 + ?';
        const sql2 = 'INSERT INTO notre_string VALUES(?, ?, ?)';
        conn.query(sql1, [bad1, bad2, bad3, bad4], (err, results) => {
          if (err) throw err;
          conn.query('SELECT * FROM notre_string WHERE route_id = ?', [route_id], (err, results) =>{
            if (err) throw err;
          a = results[results.length - 1].string_id; // 문자열에 갱신할 값을 가져온다.
          conn.query(sql2, [a + 1, route_id, bad], (err, results) => {
            if(err) throw err;
            conn.end(); 
          })
          })
        res.end();
      }) 
    }
  })
})

// API 추천, 비추천 상세보기 상위 2개 항목과 상위 기타항목
app.get('/Api/Detail', (req, res) => {

  // 스타트 경도와 위도에 대해서 쿼리를 돌리고 추천 수 뽑아내기 -> 우선 reco에서 해준다.
  // 해당하는 추천 수의 를 뽀아준다. 
  const conn = db.conn();
  const route_id = req.param('id'); // get 호출이므로 body로 받을 수 없다. querystring으로 받기
  // const route_id = req.body.id; // 경로 id에 대한 추천 상세보기
  conn.query('SELECT * FROM recommend WHERE route_id =?', [route_id], (err, result) => { 
    if(err) throw err;
    if(result[0] == null){ // 해당 값이 없을 경우 0으로 반환
      a = 0;
      b = 0;
      a1 = null;
      a2 = null;
    }
    else{ // 해당 값 추천 상위 2항목과 기타 2항목 추출
      c = [result[0].good1, result[0].good2, result[0].good3, result[0].good4];
      c.sort(function(a, b){
        return b - a;
      });
      a = c[0];
      b = c[1];
      conn.query('SELECT * FROM reco_string WHERE route_id = ?',[route_id], (err, results) =>{
        if(err) throw err;
        if(results.length >= 2){
          a1 = results[results.length - 1].good;
          a2 = results[results.length - 2].good;
        }
        else if(results.length == 1){
          a1 = results[results.length - 1].good;
          a2 = null;
        }
        else{
          a1= null;
          a2= null;
        }
      })
    }
    conn.query('SELECT * FROM not_recommend WHERE route_id =?', [route_id], (err, result) => {
      if(err) throw err;
      if(result[0] == null){ // 해당 값이 없을 경우 0으로 반환
        x = 0;
        y = 0;
        x1 = null;
        x2 = null;
        conn.end();
        res.json({good1: a, good2: b, good3: a1, good4: a2, bad1: x, bad2: y, bad3: x1, bad4: x2})
   
      }
      else{ // 해당 값 비추천 상위 2항목과 기타 2항목 추출
        d = [result[0].ba1, result[0].bad2, result[0].bad3, result[0].bad4];
        d.sort(function(a, b){
          return b-a;
        });
        x = d[0];
        y = d[1];
        conn.query('SELECT * FROM notre_string WHERE route_id = ?',[route_id], (err, results) =>{
          if(err) throw err;
          if(results.length >= 2){
            x1 = results[results.length - 1].bad;
            x2 = results[results.length - 2].bad;
          }
          else if(results.length == 1){
            x1 = results[results.length - 1].good;
            x2 = null;
          }
          else{
            x1= null;
            x2= null;
          }
          conn.end();
          res.json({good1: a, good2: b, good3: a1, good4: a2, bad1: x, bad2: y, bad3: x1, bad4: x2})

       })
      }
    })
  })
})

// API 추천 수 
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
