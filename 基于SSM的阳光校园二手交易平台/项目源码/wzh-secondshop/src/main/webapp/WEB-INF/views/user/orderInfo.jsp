<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>订单信息</title>
<link rel="stylesheet"
	href="<c:url value="/statics/bootstrap-3.3.0/css/bootstrap.css"/>">
<link rel="stylesheet"
	href="<c:url value="/statics/jquery-ui-1.12.1/jquery-ui.css"/>">
<link rel="stylesheet"
	href="<c:url value="/statics/jquery-ui-1.12.1/jquery-ui.theme.css"/>">
<link rel="stylesheet" href="<c:url value="/statics/css/style.css"/>">
<script src="<c:url value="/statics/jquery-1.12.4/jquery-1.12.4.js"/>"></script>
</head>
<jsp:include page="../home/header.jsp" />

<div class="container"
	style="position: relative; transform: translate(0, 0)">
	<div class="col-md-9" style="float: left">
		<div class="col-md-12 r1"
			style="background-color: #e4e4e4; height: 40px">
			<div class="col-md-6" style="margin-top: 5px">
				<B style="color: #c4c4c4; font-size: 20px">订单</B>
			</div>
		</div>

		<div class="col-md-12 r2"
			style="background-color: #f9f9f9; padding-top: 5px; padding-bottom: 30px; padding-left: 120px; padding-right: 120px; margin-bottom: 20px">
			<div class="col-md-12" align="center" style="margin-bottom: 5px">
				<h3>订单信息明细</h3>
			</div>
			<div class="col-md-12" style="background-color: #ffffff">
				<div class="col-md-12"
					style="margin-bottom: 10px; margin-top: 20px;">
					<div class="col-md-12"
						style="padding: 30px; background-color: #f2f2f2">
						<div class="col-md-5"
							style="padding-left: 60px; padding-top: 20px">
							<p style="color: #666666; height: 30px">
								<B>订单号</B>
							</p>
							<p style="color: #666666; height: 30px">
								<B>订单类型</B>
							</p>
							<p style="color: #666666; height: 30px">
								<B>物品名称</B>
							</p>
							<p style="color: #666666; height: 30px">
								<B>卖家</B>
							</p>
							<p style="color: #666666; height: 30px">
								<B>买家</B>
							</p>
							<p style="color: #666666; height: 30px">
								<B>订单状态</B>
							</p>
							<p style="color: red; height: 30px">
								<B>订单价格</B>
							</p>
							<p style="color: #666666; height: 30px">
								<B>交易时间</B>
							</p>
							<p style="color: #666666; height: 30px; display: ${orderInfo.orderType==0?"":"none"}">
								<B>结束时间</B>
							</p>
							<p style="color: #666666; height: 30px; display: ${orderInfo.orderType==0?"none":""}">
								<B>到期时间</B>
							</p>
						</div>

						<div id="order" class="col-md-7"
							style="padding-left: 0px; padding-top: 20px">
							<p style="color: #666666; height: 30px">
								<B>${orderInfo.id}</B>
							</p>
							<p style="color: #666666; height: 30px">
								<B>${orderInfo.orderType==0?"购买物品":"租赁物品(到期自动退还押金)"}</B>
							</p>
							<p style="color: #666666; height: 30px">
								<B>${orderInfo.goodName}</B>
							</p>
							<p style="color: #666666; height: 30px">
								<B>${orderInfo.seller}</B>
							</p>
							<p style="color: #666666; height: 30px">
								<B>${orderInfo.customer}</B>
							</p>
							<p style="color: #666666; height: 30px">
								<B>${orderInfo.statusId == 2 ? "交易中":"交易完成"}</B>
							</p>
								<p style="color: red; height: 30px">
								<B>${orderInfo.money}<span style="color:#777;font-size:12px;display: ${orderInfo.orderType == 0 ? "
							none":""}">(物品押金+租期*月租金)</span></B>
							</p> 
							<p style="color: #666666; height: 30px">
								<B>${orderInfo.submitDate}</B>
							</p>
							<p style="color: #666666; height: 30px; display: ${orderInfo.orderType==0?"":"none"}">
								<B>${orderInfo.endDate}</B>
							</p>
							<p style="color: #666666; height: 30px; display: ${orderInfo.orderType==0?"none":""}">
								<B>${orderInfo.creditEndDate}</B>
							</p>
						</div>
					</div>
					<div class="col-md-12" style="margin-top: 20px">
						<button class="btn btn-success" style="display: ${orderInfo.statusId == 2 ? "
							":"none"}" onclick="deleteOrder()" id="credit">撤销订单</button>
						<button id="success" class="btn btn-primary pull-right"
							style="display: ${orderInfo.statusId == 2 ? "
							":"none"};margin-right:8px" onclick="selectValue()">完成交易</button>
						<button id="noSuccess" class="btn btn-primary pull-right"
							style="display: ${orderInfo.statusId == 3 ? "
							":"none"}" disabled="true">交易已完成</button>
							<button  onclick="grade()" id="noSuccess1" class="btn btn-primary pull-right"
								style="display: ${orderInfo.statusId == 3 && orderInfo.score == 0 ? "
								":"none"};margin-right:12px;" >对卖家评分
								</button>
						<button disabled="true" id="noSuccess1" class="btn btn-primary pull-right"
							style="display: ${orderInfo.statusId == 3 && orderInfo.score != 0 ? "
							":"none"};margin-right:12px;" >
							已评分,分值: ${orderInfo.score}</button>
						<button onclick="exitCredit()" id="success1" class="btn btn-primary pull-right"
							style="display: ${orderInfo.statusId == 4 ? "
							":"none"}" >我要退租</button>
						<button onclick="continueCredit()" id="success2" class="btn btn-primary pull-right"
							style="display: ${orderInfo.statusId == 4 ? "
							":"none"}; margin-right:12px" >我要续租</button>	
					</div>
				</div>
			</div>
		</div>
	</div>

	<div id="re-bar" class="col-md-3 r"
		style="background-color: #f9f9f9; padding-bottom: 15px;">
		<div class="col-md-12 r"
			style="text-align: center; margin-top: 10px; background-color: #e4e4e4; padding: 5px">
			<B style="font-size: 20px; color: #a2a2a2">其他订单</B>
		</div>

		<div class="col-md-12 r"
			style="background-color: #ffffff; margin-top: 15px; overflow-y: auto; height: 500px; padding: 5px">
			<c:choose>
				<c:when test="${orders.size() == 0}">
					<div class="col-md-12" align="center"
						style="background-color: #ffffff; margin-top: 50px;">
						<img src="<c:url value="/statics/image/logo/yihan.png"/>"
							width="95%" ; height="170px;">
						<p style="color: #c4c4c4; font-size: 16px">抱歉，暂无其他订单！</p>
					</div>
				</c:when>
				
				<c:otherwise>
					<c:forEach var="order" items="${orders}">
						<a href="/wzh-secondshop/user/orderInfo?orderId=${order.id}">
							<div class="col-md-12 rev"
								style="padding: 10px; margin-bottom: 5px;">
								<p>${order.submitDate}</p>
								<p>${order.goodName}</p>
							</div>
						</a>
					</c:forEach>
				</c:otherwise>
			</c:choose>
		</div>
	</div>
</div>

<jsp:include page="../home/footer.jsp" />

<script>
    $(function () {
        $(window).scroll(function () {
            if ($(document).scrollTop() >= 20) {
                $("#re-bar").css({
                    "position": "fixed",
                    "top": 50 + $(document).scrollTop() + "px",
                    "width": 285,
                    "right": 15
                });
            } else {
                $("#re-bar").css({
                    "position": "fixed",
                    "top": 70 + "px",
                    "width": 285,
                    "right": 15
                });
            }
        })
    });

    
    function deleteOrder() {
        $.getJSON("/wzh-secondshop/user/order/delete/" + ${orderInfo.id}, function (data) {
            if (data == true){
                alert("订单删除成功！");
                $(window).attr('location','/wzh-secondshop/user/userProfile');
            } else {
                alert("订单删除失败！");
            }
        })
    }

    function selectValue() {
        $.getJSON("/wzh-secondshop/user/order/update/status/" + ${orderInfo.id} + "&"+${orderInfo.orderType==0?'3':'4'}, function (data) {
            if (data == false){
                alert("操作失败!请确保余额或积分充足");
            } else {
            	if(${orderInfo.orderType==0?'3':'4'} == '3') {
	                document.getElementById("success").style.display = "none";
	                document.getElementById("noSuccess").style.display = "";
	                document.getElementById("order").innerHTML = "<p style=\"color: #666666; height: 30px\"><B>" + data.id + "</B></p>" +
	                    "<p style=\"color: #666666; height: 30px\"><B>购买物品</B></p>" +
	                    "<p style=\"color: #666666; height: 30px\"><B>" + data.goodName + "</B></p>" +
	                    "<p style=\"color: #666666; height: 30px\"><B>" + data.seller + "</B></p>" +
	                    "<p style=\"color: #666666; height: 30px\"><B>" + data.customer + "</B></p>" +
	                    "<p style=\"color: #666666; height: 30px\"><B>${data.statusId == 2 ? '交易中':'交易完成' }</B></p>" +
	                    "<p style=\"color: #666666; height: 30px\"><B>" + data.money + "</B></p>" +
	                    "<p style=\"color: #666666; height: 30px\"><B>" + data.submitDate + "</B></p>" +
	                    "<p style=\"color: #666666; height: 30px\"><B>" + data.endDate + "</B></p>";
            		
            	}else {
	                document.getElementById("success").style.display = "none";
	                document.getElementById("success1").style.display = "";
	                document.getElementById("success2").style.display = "";
	                document.getElementById("credit").style.display = "none";
            		 document.getElementById("order").innerHTML = "<p style=\"color: #666666; height: 30px\"><B>" + data.id + "</B></p>" +
                     "<p style=\"color: #666666; height: 30px\"><B>租赁物品</B></p>" +
                     "<p style=\"color: #666666; height: 30px\"><B>" + data.goodName + "</B></p>" +
                     "<p style=\"color: #666666; height: 30px\"><B>" + data.seller + "</B></p>" +
                     "<p style=\"color: #666666; height: 30px\"><B>" + data.customer + "</B></p>" +
                     "<p style=\"color: #666666; height: 30px\"><B>${data.statusId == 2 ? '交易中':'交易完成' }</B></p>" +
                     "<p style=\"color: #666666; height: 30px\"><B>" + data.money + "</B><span style='color:#777;font-size:12px'>(物品押金+租期*月租金)</span></p>" +
                     "<p style=\"color: #666666; height: 30px\"><B>" + data.submitDate + "</B></p>" +
                     "<p style=\"color: #666666; height: 30px\"><B>" + data.endDate + "</B></p>";
            	}
            }
        })
    }
    
    function exitCredit() {
        if (confirm("您确定要退还该物品吗?提前退还只退回押金")) { 
        	$.get("/wzh-secondshop/user/exitCredit/"+${orderInfo.id}, function (data) {
	            if (!data){
	                alert("退租失败");
	            } else {
	            	alert("退租成功，押金已退回您的账户.");  
					location.reload(); 
	            }	
			})
        }  
    }
    
    function continueCredit() {
    	var month=prompt("请输入您要续租的时间,单位(月) ","0");
        if(month < 0) {
        	alert("请输入正确的日期!");
        }else {
        	$.get("/wzh-secondshop/user/continueCredit/"+month+"&"+${orderInfo.id}, function (data) {
	            if (!data){
	                alert("续租失败,请确保账户余额充足");
	            } else {
					alert("续租成功!");
					location.reload(); 
	            }	
			})
        }
    }
    
    
    
    function grade() {
    	var score=prompt("请输入您对卖家及物品使用评分,1分最低,5分最高 ","5");
    	if(score < 1) {
    		alert("请输入1-5之间的数值");
    	}else if(score > 5) {
    		alert("请输入1-5之间的数值");
    	}else {
    		$.get("/wzh-secondshop/user/grade/"+score + "&" + ${orderInfo.id}, function (data) {
	            if (!data){
	                alert("评分失败!");
	            } else {
					alert("评分成功!");
					location.reload(); 
	            }	
			})
    	}
    }
</script>
<script src="<c:url value="/statics/bootstrap-3.3.0/js/bootstrap.js"/>"></script>
<script src="<c:url value="/statics/jquery-ui-1.12.1/jquery-ui.js"/>"></script>
<script
	src="<c:url value="/statics/jquery-ui-1.12.1/datepicker-zh-CN.js"/>"></script>
</body>
</html>