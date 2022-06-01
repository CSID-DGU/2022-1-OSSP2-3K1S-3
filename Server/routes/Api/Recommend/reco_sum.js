const db = require("../../../module/db_connect");

async function detail_reco_sum(id, len){
    let results = null; // 돌려주는 용도
    var good_sum = [0, 0, 0, 0];
    var good_etc = [];
    for(var i =0; i<=len;i++){
        route_id = id[i];
        results = await new Promise((resolve) => {
            var conn = db.conn();
            conn.query('SELECT * FROM recommend WHERE route_id =?', [route_id], (err, result) => { 
                if(err) throw err;
                // console.log(i, "순서");
                if(result[0] == null){ // 해당 값이 없을 경우 0으로 반환
                    // console.log("추천 해당 값 없음", result[0], route_id);
                   }
                   else{ // 해당 값 추천 상위 2항목과 기타 2항목 추출
                    good_sum[0] += result[0].good1;
                    good_sum[1] += result[0].good2;
                    good_sum[2] += result[0].good3;
                    good_sum[3] += result[0].good4;
                     conn.query('SELECT * FROM reco_string WHERE route_id = ?',[route_id], (err, resultss) =>{
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
    }
    console.log("최종 결과_김민수", good_sum[0], good_sum[1], good_sum[2], good_sum[3], good_etc);
    good_sum.push(good_etc);
    return good_sum;
    
}


exports.reco_sum = async function main(idd) {
    id = idd;
    console.log(id);
    a= await detail_reco_sum(id, id.length);  // Grace가 출력된다.
    console.log("제발",a);
    console.log(a[0],a[4][0], a[4][4]);
    if(a[4][4] == null){
        console.log("값 없다.");
    }
    return a;
}