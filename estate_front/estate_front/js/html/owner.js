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
var param = getUrlParam();
$(function(){
    console.log('param.ownerId=='+param.ownerId)
    if(param.ownerId){//
        //这个需要查业主信息
        $.ajax({
            url:"/api/blockchain/queryUserInfoByID.do",
            type:"get",
            data:{
                IDNUMBER:param.ownerId
            },
            dataType:"json",
            success:function(data){
                console.log("data=="+JSON.stringify(data));
                if(data.data[0]){
                    var owner = data.data[0];
                    $("#name").html(owner.name);
                    $("#idType").html(idTypeMap[owner.idtype]);
                    $("#idNumber").html(owner.idnumber);
                    $("#houseInfo").html(owner.hourseid);
                    $("#houseInfo").click(function(){
                        location.href = "house.html?houseId="+owner.hourseid;
                    });
                    $("#carInfo").html(owner.carid);
                    $("#carInfo").click(function(){
                        location.href = "carinfo.html?carId="+owner.carid;
                    });
                    $("#recharge").html(PROPERTYPAYINFOMap[owner.propertypayinfo]);
                    $("#level").html(owner.level);
                    $("#remark").html(owner.hashnum);
                }else{
                    alert("暂无业主信息");
                }
            }
        });
    }
});

$("#modify").click(function(){
    location.href = "ownerInput.html?ownerId="+param.ownerId;
});