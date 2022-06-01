var reco = require("./reco_sum");
var not_reco = require("./not_reco_sum");

exports.detail1 = async function test(id) {
    a= await reco.reco_sum(id);  // Grace가 출력된다.
    
    console.log("추천 받아온 값",a);
    return a;
}

exports.detail2 = async function test1(id) {
    a= await not_reco.not_reco_sum(id);  // Grace가 출력된다.
    
    console.log("비추천 받아온 값",a);
    return a;
}


// exports.detail = async function test3(id) {
//     a = await test(id);
//     test4(a, id);
//     async function test4(a, id) {
//         b = await test1(id);
//         console.log("미친놈 1, 2",a, b);
//         c = [a[0], a[1], a[2], a[3]]; // 추천 값 sorting
//         a.sort(function(a, b){
//             return b-a;
//         })
//         d = [b[0], b[1], b[2], b[3]]; // 비추천 값 sorting
//         d.sort(function(a,b){
//             return b-a;
//         })

//         if(a[4].length >= 2){
//             a1 = a[4][0];
//             a2 = a[4][1];
//         }
//         else if(a[4].length == 1){
//             a1 = a[4][0];
//             a2 = null;
//         }
//         else{
//             a1 = null;
//             a2 = null;
//         }

//         if(b[4].length >= 2){
//             x1 = b[4][0];
//             x2 = b[4][1];
//         }
//         else if(b[4].length == 1){
//             x1 = b[4][0];
//             x2 = null;
//         }
//         else{
//             x1 = null;
//             x2 = null;
//         }
//         final = [c[0], c[1], a1, a2, d[0], d[1],x1, x2]

//         console.log("good1:",c[0],"good2:",c[1],"good3:",a1,"good4:",a2,"bad1:",d[0],"bad2:",d[1],"bad3:",x1,"bad4:",x2);

//         return final;
        
//     }
// }
