
const db = require("../../../module/db_connect");

exports.good = function insert_good(route_id, good1, good2, good3, good4, good) {
    
    const conn = db.conn();
    conn.query('SELECT * FROM recommend WHERE route_id=?', [route_id], function(err, recommend, fields){
        if(err) throw err;     
        if(recommend[0] == null){
            conn.query('INSERT INTO recommend VALUES(?, ?, ?, ?, ? ,?)',[route_id, route_id, good1, good2, good3, good4], (err, result) => {
              conn.query('INSERT INTO reco_string VALUES(?, ?, ?)',[1, route_id, good], (err, result) => {
                if(err) throw err;
                conn.end(); // DB 접속 종료
              })
              if(err) throw err;
              
              return;
            })
          }else{
            const sql1 = 'UPDATE recommend SET good1 = good1 + ?, good2 = good2 + ?, good3 = good3 + ?, good4 = good4 + ? WHERE route_id = ?';
            const sql2 = 'INSERT INTO reco_string VALUES(?, ?, ?)';
            conn.query(sql1, [good1, good2, good3, good4, route_id], (err, results) => {
              conn.query('SELECT * FROM reco_string WHERE route_id = ?', [route_id], (err, results) =>{
                if (err) throw err;
              a = results[results.length - 1].string_id; // 문자열에 갱신할 값을 가져온다.
              conn.query(sql2, [a + 1, route_id, good], (err, results) => {
                if(err) throw err;
                conn.end(); 
              }) 
            })
              return;
            }) 
          }
      })
}