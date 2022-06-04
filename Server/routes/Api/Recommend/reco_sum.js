const db = require("../../../module/db_connect");

async function detail_reco_sum(busN){
    let results = null; // 돌려주는 용도
    var good_sum = [0, 0, 0, 0];
    var good_etc = [];
        results = await new Promise((resolve) => {
            var conn = db.conn();
            conn.query('select sum(good1) as sg1, sum(good2) as sg2, sum(good3) as sg3, sum(good4) as sg4, id  from route, recommend where bus_num = ? and recommend.route_id = route.id;', [busN], (err, result) => { 
                if(err) throw err;
                // console.log(i, "순서");
                if(result[0] == null){ // 해당 값이 없을 경우 0으로 반환
                    // console.log("추천 해당 값 없음", result[0], route_id);
                   }
                   else{ // 해당 값 추천 상위 2항목과 기타 2항목 추출
                    good_sum[0] += result[0].sg1;
                    good_sum[1] += result[0].sg2;
                    good_sum[2] += result[0].sg3;
                    good_sum[3] += result[0].sg4;
                     conn.query('select * from reco_string, route where route.bus_num = ? and reco_string.route_id = route.id',[busN], (err, resultss) =>{
                        if(err) throw err;
                        if(resultss.length >= 2){
                          good_etc.push(resultss[resultss.length - 1].good);
                          good_etc.push(resultss[resultss.length - 2].good);
                        }
                        else if(resultss.length == 1){
                          good_etc.push(resultss[resultss.length - 1].good);
                        }
                      })
                   }
                resolve();
                conn.end();
            });
        });
    good_sum.push(good_etc);
    return good_sum;
    
}


exports.reco_sum = async function main(busN) {
    a= await detail_reco_sum(busN); 
    return a;
}