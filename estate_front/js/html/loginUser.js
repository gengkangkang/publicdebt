
var validator = $('#loginForm').validate({
    errorElement: 'div',
    errorClass: 'help-block',
    focusInvalid: false,
    rules: {
        user: { required: true},
        password: { required: true}
    },
    messages: {
        user: { required: "请输入用户名"},
        password: { required: "请输入密码"}
    },
    highlight: function (e) {
        $(e).closest('.form-box').removeClass('has-info').addClass('has-error');
    },
    success: function (e) {
        $(e).closest('.form-box').removeClass('has-error');
        $(e).remove();
    },
    errorPlacement: function (error, element) {
        error.insertAfter(element.parent());
    }
});
$("#loginBtn").click(function(){
    if(validator.form()){
        $.cookie("type","custom",{path:"/"});
        location.href = "houseList.html";
    }
});