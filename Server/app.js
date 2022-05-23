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
const getLessMRoute = require('./routes/Api/Map/getLessMRoute')
const db = require("./module/db_connect");
const conn = db.conn();

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


//경로탐색
app.get('/Api/route/lessmoney', (req, res) => {
  console.log("[call lessmoney Api]");

  const startLong = req.body.sLong;
  const startLati = req.body.sLati;

  const endLong = req.body.eLong;
  const endLati = req.body.eLati;

  getLessMRoute(startLong, startLati, endLong, endLati);    
})

// 추천 비추천 데이터 API
app.post('/Api/Recommend/good', (req, res) => { // 요청시 추천 데이터 값이 갱신된다.
  const route_id = req.body.id; // 경로에 대한 키 값
  const good1 = req.body.good1? 1:0; // true or false
  const good2 = req.body.good2? 1:0; // true or false
  const good3 = req.body.good3? 1:0; // true or false
  const good4 = req.body.good4? 1:0; // true or false
  const good5 = req.body.good5; // 문자열

  conn.query('SELECT * FROM recommend WHERE route_id=?', [route_id], function(err, recommend, fields){
    if(err){ // 새로 값을 생성해준다.
      console.log("존재하지 않습니다."); 
      conn.query('INSERT INTO recommend VALUES(?, ?, ?, ?, ? ,?, ?)',[route_id, route_id, good1, good2, good3, good4, good5], (err, result) => {
        if(err) throw err;
          
        console.log('success');
        conn.end(); // DB 접속 종료
        res.end();
      })
    }else{ // 있으므로 값을 갱신해준다.
      const sql = 'UPDATE recommend SET good1 = good1 + ?, good2 = good2 + ?, good3 = good3 + ?, good4 = good4 + ?, good5 = good5 + ?';
      conn.query(sql, [good1, good2, good3, good4, good5], (err, results) => {
        if (err) throw err;
        conn.end(); 
        res.end();
      }) 
    }
  })
})


app.post('/Api/Recommend/bad', (req, res) => { // 요청시 비추천 데이터 값이 갱신된다..
  const route_id = req.body.id; // 경로에 대한 키 값
  const bad1 = req.body.bad1? 1:0; // true or false
  const bad2 = req.body.bad2? 1:0; // true or false
  const bad3 = req.body.bad3? 1:0; // true or false
  const bad4 = req.body.bad4? 1:0; // true or false
  const bad5 = req.body.bad5; // 문자열

  conn.query('SELECT * FROM recommend WHERE route_id=?', [route_id], function(err, recommend, fields){
    if(err){ // 새로 값을 생성해준다.
      console.log("존재하지 않습니다.");
      conn.query('INSERT INTO not_recommend VALUES(?, ?, ?, ?, ?, ? ,?, ?)',[route_id, route_id, route_id, bad1, bad2, bad3, bad4, bad5], (err, result) => {
        if(err) throw err;
          
        console.log('success');
        conn.end(); // DB 접속 종료
        res.end();
      })
    }else{ // 있으므로 값을 갱신해준다.
      const sql = 'UPDATE not_recommend SET bad1 = bad1 + ?, bad2 = bad2 + ?, bad3 = bad3 + ?, bad4 = bad4 + ?, bad5 = bad5 + ?';
      conn.query(sql, [bad1, bad2, bad3, bad4, bad5], (err, results) => {
        if (err) throw err;
        conn.end(); 
        res.end();
      }) 
    }
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
