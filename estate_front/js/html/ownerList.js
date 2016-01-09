
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
    var tbody =  $("#ownerList");
$("#ownerList").children().remove();
    $.each(owners,function(index,owner){
        var tr = $('<tr>'+
        '<td><a href="owner.html?ownerId="'+owner.id+'>'+owner.name+'</a></td>'+
        '<td>'+idTypeMap[owner.idType]+'</td>'+
        '<td>'+owner.idNumber+'</td>'+
        '<td>'+PROPERTYPAYINFOMap[owner.PROPERTYPAYINFO]+'</td>'+
        '<td>'+owner.level+'</td>'+
        '</tr>');
        tbody.append(tr);
    });
});