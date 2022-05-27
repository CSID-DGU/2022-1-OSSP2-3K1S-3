const db = require("./module/db_connect");

const conn = db.conn();



// API 추천 수 

  
    // 스타트 경도와 위도에 대해서 쿼리를 돌리고 추천 수 뽑아내기
    // 해당하는 추천 수의 를 뽀아준다.
var good_sum = 0;

console.log('성공');
// const route_id = req.body.id; // 경로 id에 대한 추천 상세보기

const sql = 'SELECT ( 6371 * acos ( cos ( radians(start_lati) ) * cos( radians(37.56726109866003) ) * cos( radians(start_long) - radians(127.1903672271528) ) + sin ( radians(start_lati) ) * sin( radians(37.56726109866003) ))) AS distance, id FROM route HAVING distance < 10 ORDER BY distance LIMIT 0 , 10;'
conn.query('sql', (err, result) => {

  if(err) throw err;
  console.log("성공")
//   if(result[0] == null){ // 해당 값 없으면 0
//     good_sum = 0;
//   }
//   else{
//     good_sum = result[0].good1 + result[0].good2 + result[0].good3 + result[0].good4;
//   }
//   conn.end();
//   res.json({sum: good_sum});
// res.end();
})
   
  