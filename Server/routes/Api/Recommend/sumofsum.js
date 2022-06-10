var reco = require("./reco_sum");
var not_reco = require("./not_reco_sum");

exports.detail1 = async function test(id) {
    a= await reco.reco_sum(id);  // Grace가 출력된다.
    
    // console.log("추천 받아온 값",a);
    return a;
}

exports.detail2 = async function test1(id) {
    a= await not_reco.not_reco_sum(id);  // Grace가 출력된다.
    
    // console.log("비추천 받아온 값",a);
    return a;
}


exports.detail3 = function caseInGood(val){
    var answer ="";
    switch (val){
        case 0:
            answer = "돈이 예상보다 적게 들어요";
            break;
        case 1:
            answer = "예상 도착 시간과 비슷해요";
            break;
        case 2:
            answer = "도보 구간이 적어요";
            break;
        case 3:
            answer = "정류장 대기시간이 짧아요";
            break;
    }
    return answer;
}


exports.detail4 = function caseInBad(val){
    var answer ="";
    switch (val){
        case 0:
            answer ="돈이 예상보다 많이 들어요";
            break;
        case 1:
            answer ="예상 도착 시간보다 늦어요"
            break;
        case 2:
            answer ="도보 구간이 많아요";
            break;
        case 3:
            answer ="정류장 대기시간이 길어요";
            break;
    }
    return answer;
}
