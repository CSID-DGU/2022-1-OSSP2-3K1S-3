// database 연결 구조
const mysql = require("mysql2/promise");
require("dotenv").config(); // .env 하기 위한 dotenv 라이브러리 

const db = async () => {
    let connection = await mysql.createConnection({
        host: process.env.host,
        user: process.env.user,
        password: process.env.password,
        database: process.env.database
    })
}

// module.exports.conn = function(){
//     const conn = db.createConnection({
//         host: process.env.host,
//         user: process.env.user,
//         password: process.env.password,
//         database: process.env.database
//     });
//     conn.connect(function(err){
//         if(err){
//             console.error('에러 connect:' + err.stack);
//             return;
//         }
//         console.log("Mysql DB Connect완료! ID : " + conn.threadId);
        
//     });
//     return conn;
   
// }


module.exports = db;