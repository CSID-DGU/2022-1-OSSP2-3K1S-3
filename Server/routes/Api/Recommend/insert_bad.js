
const db = require("../../../module/db_connect");

exports.bad = function insert_bad(route_id, bad1, bad2, bad3, bad4, bad) {
    
    const conn = db.conn();
    conn.query('SELECT * FROM not_recommend WHERE route_id=?', [route_id], function(err, not_recommend, fields){
        if(err) throw err;
          if(not_recommend[0] == null){ // 기존 경로에 대한 route_id가 존재하지 않을 때
            conn.query('INSERT INTO not_recommend VALUES(?, ?, ?, ?, ? ,?)',[route_id, route_id, bad1, bad2, bad3, bad4], (err, result) => {
              if(err) throw err;
              conn.query('INSERT INTO notre_string VALUES(?, ?, ?)',[1, route_id, bad], (err, result) => {
                if(err) throw err;
                conn.end(); // DB 접속 종료
              })        
              return;
            })
          }else{ // 기존 경로에 대한 route_id가 존재할 때
            const sql1 = 'UPDATE not_recommend SET bad1 = bad1 + ?, bad2 = bad2 + ?, bad3 = bad3 + ?, bad4 = bad4 + ? WHERE route_id = ?';
            const sql2 = 'INSERT INTO notre_string VALUES(?, ?, ?)';
            conn.query(sql1, [bad1, bad2, bad3, bad4, route_id], (err, results) => {
              if (err) throw err;
              conn.query('SELECT * FROM notre_string WHERE route_id = ?', [route_id], (err, results) =>{
                if (err) throw err;
              a = results[results.length - 1].string_id; // 문자열에 갱신할 값을 가져온다.
              conn.query(sql2, [a + 1, route_id, bad], (err, results) => {
                if(err) throw err;
                conn.end(); 
              
              })
              })
              return;
          }) 
        }
      })
}