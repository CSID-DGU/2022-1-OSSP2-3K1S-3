const db = require("../../../module/db_connect");

// var app = express();

async function detail_not_reco_sum(id, len){
    let results = null; // 돌려주는 용도
    var bad_sum = [0, 0, 0, 0];
    var bad_etc = [];
    for(var i =0; i<=len;i++){
        route_id = id[i];
        results = await new Promise((resolve) => {
            var conn = db.conn();
            conn.query('SELECT * FROM not_recommend WHERE route_id =?', [route_id], (err, result) => { 
                if(err) throw err;
                // console.log(i, "순서");
                if(result[0] == null){ // 해당 값이 없을 경우 0으로 반환
                    // console.log("추천 해당 값 없음", result[0], route_id);
                   }
                   else{ // 해당 값 추천 상위 2항목과 기타 2항목 추출
                    bad_sum[0] += result[0].bad1;
                    bad_sum[1] += result[0].bad2;
                    bad_sum[2] += result[0].bad3;
                    bad_sum[3] += result[0].bad4;
                     conn.query('SELECT * FROM notre_string WHERE route_id = ?',[route_id], (err, resultss) =>{
                        if(err) throw err;
                        if(resultss.length >= 2){
                          bad_etc.push(resultss[resultss.length - 1].bad);
                          bad_etc.push(resultss[resultss.length - 2].bad);
                        }
                        else if(resultss.length == 1){
                          bad_etc.push(resultss[resultss.length - 1].bad);
                        }
                      })
                   }
                resolve();
                conn.end();
            });
        });
    }
    console.log("최종 결과_김민수", bad_sum[0], bad_sum[1], bad_sum[2], bad_sum[3], bad_etc);
    bad_sum.push(bad_etc);
    return bad_sum;
    
}


exports.not_reco_sum = async function main(idd) {
    id = idd;
    console.log(id);
    a= await detail_not_reco_sum(id, id.length);  // Grace가 출력된다.
    console.log("제발",a);
    console.log(a[0],a[4][0], a[4][4]);
    if(a[4][4] == null){
        console.log("값 없다.");
    }
    return a;
}

// module.exports = getStation;