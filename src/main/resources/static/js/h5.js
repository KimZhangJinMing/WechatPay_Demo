$(function () {
    $("#createOrder").click(function () {
        $.ajax({
            "url":"https://qp-shore.gentingcruises.com/WechatPay_Kim/order",
            "type":"GET",
            "data":{
                "number":$("#number").val().trim()
            },
            "dataType":"json",
            success: function (result) {
                console.log(result);
                if(result.code == 200) {
                    window.location.href = result.data;
                }else {
                    alert(result.message)
                }
            }

        })
    })
})