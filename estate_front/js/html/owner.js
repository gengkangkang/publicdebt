var idTypeMap = {
    "00":"身份证",
    "01":"回乡证",
    "02":"台胞证",
    "03":"护照",
    "04":"其他"
};
var PROPERTYPAYINFOMap = {
    "1":"优",
    "2":"良",
    "3":"差"
};
function getUrlParam () {
    var param = {};
    var href = location.href;
    var paramStr = href.substr(href.indexOf("?") + 1);
    if (paramStr) {
        var paramArray = paramStr.split("&");
        $(paramArray).each(function (index, keyValueStr) {
            var field = keyValueStr.split("=");
            param[field[0]] = field[1];
        });
        return param;
    } else {
        return null;
    }
}
var param = getUrlParam;
$(function(){
    if(true){//param.ownerId
        //这个需要查业主信息
        var owner = {
                id:"1",
                name:"胡文飞",
                idNumber:"37078119870218637X",
                idType:"00",
                house:{
                    id:"1",
                    HOURCENO:"1",
                    HOURCEAREA:"100平",
                    HOURCEADDR:"北京昌平36A8单元901",
                    HOURCERDATE:"2007年7月15日",
                    REMARK:"新房"
                },
                car:{
                    id:"1",
                    CARNO:"京N39087",
                    CARBRAND:"奥迪",
                    CARPRICE:"50万",
                    BUYDATE:"2013年7月15日",
                    REMARK:"好车"
                },
                PROPERTYPAYINFO:"2",
                level:"8.9",
                remark:"这个人非常好"
            }
        $("#name").html(owner.name);
        $("#idType").html(idTypeMap[owner.idType]);
        $("#idNumber").html(owner.idNumber);
        $("#houseInfo").html(owner.house.HOURCEADDR);
        $("#carInfo").html(owner.car.CARBRAND);
        $("#recharge").html(PROPERTYPAYINFOMap[owner.PROPERTYPAYINFO]);
        $("#level").html(owner.level);
        $("#remark").html(owner.remark);
    }
});

$("#modify").click(function(){
    location.href = "ownerInput.html?ownerId="+param.ownerId;
});