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
if(true){//param.ownerId
    //这里查询业主信息
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
                $("#NAME").val(owner.name);
                $("#IDTYPE").val(owner.idtype);
                $("#idTypeDis").html(idTypeMap[owner.idtype]);
                $("#IDNUMBER").val(owner.idnumber);
                $("#HOURSEID").val(owner.hourseid);
                $("#houseDis").html(owner.hourseid);
                $("#carDis").html(owner.carid);
                $("#PROPERTYPAYINFO").val(owner.propertypayinfo);
                $("#rechargeDis").html(PROPERTYPAYINFOMap[owner.propertypayinfo]);
                //$("#level").html(owner.level);
                //$("#remark").html(owner.remark);
            }else{
                alert("暂无业主信息");
            }
        }
    });
}

$("#save").click(function(){
    //这里保存业主数据
    $.ajax({
        url:"/api/blockchain/saveUserInfo.do",
        type:"post",
        data:$("#transferform").serialize(),
        dataType:"json",
        success:function(data){
            if(data.code=="0000"){
                alert("保存成功");
            }else{
                alert("保存失败");
            }
        }
    });

});
$("#idTypeUl").find("a").click(function(){
    var $this = $(this);
    var value = $this.data("value");
    var text = $this.text();
    $("#IDTYPE").val(value);
    $("#idTypeDis").html(text);
});

$("#houseUl").find("a").click(function(){
    var $this = $(this);
    var value = $this.data("value");
    var text = $this.text();
    $("#HOURSEID").val(value);
    $("#houseDis").html(text);
});
$("#carUl").find("a").click(function(){
    var $this = $(this);
    var value = $this.data("value");
    var text = $this.text();
    $("#CARID").val(value);
    $("#carDis").html(text);
});
$("#rechargeUl").find("a").click(function(){
    var $this = $(this);
    var value = $this.data("value");
    var text = $this.text();
    $("#PROPERTYPAYINFO").val(value);
    $("#rechargeDis").html(text);
});
