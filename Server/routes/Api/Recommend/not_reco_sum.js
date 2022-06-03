const db = require("../../../module/db_connect");

// var app = express();

async function detail_not_reco_sum(busN){
    let results = null; // 돌려주는 용도
    var bad_sum = [0, 0, 0, 0];
    var bad_etc = [];
        results = await new Promise((resolve) => {
            var conn = db.conn();
            conn.query('select sum(bad1) as b1, sum(bad2) as b2, sum(bad3) as b3, sum(bad4) as b4, id  from route, not_recommend where bus_num = ? and not_recommend.route_id = route.id', [busN], (err, result) => { 
                if(err) throw err;
                // console.log(i, "순서");
                if(result[0] == null){ // 해당 값이 없을 경우 0으로 반환
                    // console.log("추천 해당 값 없음", result[0], route_id);
                   }
                   else{ // 해당 값 추천 상위 2항목과 기타 2항목 추출
                    bad_sum[0] += result[0].b1;
                    bad_sum[1] += result[0].b2;
                    bad_sum[2] += result[0].b3;
                    bad_sum[3] += result[0].b4;
                     conn.query('select * from notre_string, route where route.bus_num = ? and notre_string.route_id = route.id',[busN], (err, resultss) =>{
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
    bad_sum.push(bad_etc);
    return bad_sum;
    
}


exports.not_reco_sum = async function main(busN) {
    a= await detail_not_reco_sum(busN);
    return a;
}

// module.exports = getStation;