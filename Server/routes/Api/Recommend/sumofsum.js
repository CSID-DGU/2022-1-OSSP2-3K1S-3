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
