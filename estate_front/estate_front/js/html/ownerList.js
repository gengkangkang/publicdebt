
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
$(function(){
    //这里查询所有业主列表信息
    $.ajax({
        url:"/api/blockchain/queryUserInfo.do",
        type:"get",
        data:{},
        dataType:"json",
        success:function(data){
            console.log("data=="+JSON.stringify(data));
            if(data.data){
                var tbody =  $("#ownerList");
                $("#ownerList").children().remove();
                $.each(data.data,function(index,owner){
                    var tr = $('<tr>'+
                    '<td><a href="owner.html?ownerId='+owner.idnumber+'">'+owner.name+'</a></td>'+
                    '<td>'+idTypeMap[owner.idtype]+'</td>'+
                    '<td>'+owner.idnumber+'</td>'+
                    '<td>'+PROPERTYPAYINFOMap[owner.propertypayinfo]+'</td>'+
                    '<td>'+owner.level+'</td>'+
                    '<td>'+owner.hashnum+'</td>'+
                    '</tr>');
                    tbody.append(tr);
                });
            }else{
                alert("暂无业主信息");
            }
        }
    });
    var owners = [{
        id:"1",
        name:"胡文飞",
        idNumber:"37078119870218637X",
        idType:"00",
        houseId:"1",
        carId:"1",
        PROPERTYPAYINFO:"2",
        level:"8.9",
        remark:"这个人非常好"
    },
        {
            id:"2",
            name:"胡文飞",
            idNumber:"37078119870218637X",
            idType:"00",
            houseId:"1",
            carId:"1",
            PROPERTYPAYINFO:"2",
            level:"8.9",
            remark:"这个人非常好"
        },
        {
            id:"3",
            name:"胡文飞",
            idNumber:"37078119870218637X",
            idType:"00",
            houseId:"1",
            carId:"1",
            PROPERTYPAYINFO:"2",
            level:"8.9",
            remark:"这个人非常好"
        }];

});