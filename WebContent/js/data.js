var line3 = [
    { id: 89, name: "林场", line: 7 },
    { id: 90, name: "星火站", line: 7 },
    { id: 91, name: "东大成贤学院", line: 7 },
    { id: 73, name: "泰冯", line: 7 },
    { id: 92, name: "天润城", line: 7 },
    { id: 93, name: "柳洲东路", line: 7 },
    { id: 94, name: "上元门", line: 7 },
    { id: 95, name: "五塘广场", line: 7 },
    { id: 96, name: "小市", line: 7 },
    { id: 14, name: "南京站", line: 7 },
    { id: 97, name: "新庄", line: 7 },
    { id: 98, name: "鸡鸣寺", line: 7 },
    { id: 99, name: "浮桥", line: 7 },
    { id: 26, name: "大行宫", line: 7 },
    { id: 100, name: "常府街", line: 7 },
    { id: 101, name: "夫子庙", line: 7 },
    { id: 102, name: "武定门", line: 7 },
    { id: 103, name: "雨花门", line: 7 },
    { id: 104, name: "卡子门", line: 7 },
    { id: 105, name: "大明路", line: 7 },
    { id: 106, name: "明城大道", line: 7 },
    { id: 44, name: "南京南站", line: 7 },
    { id: 107, name: "宏运大道", line: 7 },
    { id: 108, name: "胜太西路", line: 7 },
    { id: 109, name: "天元西路", line: 7 },
    { id: 110, name: "九龙湖", line: 7 },
    { id: 111, name: "诚信大道", line: 7 },
    { id: 112, name: "东大九龙湖校区", line: 7 },
    { id: 113, name: "秣周东路", line: 7 }
];

var line10 = [
    { id: 64, name: "雨山路", line: 4 },
    { id: 63, name: "文德路", line: 4 },
    { id: 62, name: "龙华路", line: 4 },
    { id: 61, name: "南京工业大学", line: 4 },
    { id: 60, name: "浦口大道", line: 4 },
    { id: 59, name: "临江路", line: 4 },
    { id: 58, name: "江心洲", line: 4 },
    { id: 57, name: "绿博园", line: 4 },
    { id: 56, name: "梦都大道", line: 4 },
    { id: 1, name: "奥体中心", line: 4 },
    { id: 2, name: "元通", line: 4 },
    { id: 3, name: "中胜", line: 4 },
    { id: 4, name: "小行", line: 4 },
    { id: 5, name: "安德门", line: 4 }
];

var lines = [
    { id: 7, name: "3号线", stations: line3 },
    { id: 4, name: "10号线", stations: line10 }
];
var SUFF_DEVICE_COLOR = "00",SUFF_ARROW_IN = "01",SUFF_ARROW_OUT = "02",
SUFF_ARROW_pause = "03",SUFF_ARROW_Maintain = "04";
var POST_CODE = 1,GATE_CODE = 2,TVM_CODE = 3;
var _DEVICE_TYPE = ["post","gate","tvm"];