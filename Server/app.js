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
