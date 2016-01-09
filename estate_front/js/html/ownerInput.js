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
if(true){param.ownerId
    //这里查询业主信息
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
    $("#name").val(owner.name);
    $("#idType").val(owner.idType);
    $("#idTypeDis").html(idTypeMap[owner.idType]);
    $("#idNumber").val(owner.idNumber);
    $("#house").val(owner.house.HOURCENO);
    $("#houseDis").html(owner.house.HOURCEADDR);
    $("#carId").html(owner.car.CARNO);
    $("#carDis").html(owner.car.CARNO);
    $("#recharge").val(owner.PROPERTYPAYINFO);
    $("#rechargeDis").html(PROPERTYPAYINFOMap[owner.PROPERTYPAYINFO]);
    $("#remark").val(owner.remark);
}

$("#save").click(function(){
    //这里保存业主数据
    alert("保存成功");
});
$("#idTypeUl").find("a").click(function(){
    var $this = $(this);
    var value = $this.data("value");
    var text = $this.text();
    $("#idType").val(value);
    $("#idTypeDis").html(text);
});

$("#houseUl").find("a").click(function(){
    var $this = $(this);
    var value = $this.data("value");
    var text = $this.text();
    $("#house").val(value);
    $("#houseDis").html(text);
});
$("#carUl").find("a").click(function(){
    var $this = $(this);
    var value = $this.data("value");
    var text = $this.text();
    $("#carId").val(value);
    $("#carDis").html(text);
});
