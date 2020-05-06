package com.wzh.secondshop.controllers;

import com.wzh.secondshop.models.Good;
import com.wzh.secondshop.models.Order;
import com.wzh.secondshop.models.User;
import com.wzh.secondshop.services.GoodService;
import com.wzh.secondshop.services.OrderService;
import com.wzh.secondshop.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
public class OrderController {
	private final GoodService goodService;
	private final OrderService orderService;
	private final UserService userService;

	@Autowired
	public OrderController(GoodService goodService, OrderService orderService, UserService userService) {
		this.goodService = goodService;
		this.orderService = orderService;
		this.userService = userService;
	}

	@RequestMapping(value = "/user/orderInfo", method = RequestMethod.GET)
	public String getOrderInfo(ModelMap model,
			@RequestParam(value = "orderId", required = false) Integer orderId,
			@RequestParam(value = "orderPrice", required = false) Integer orderPrice,
			HttpSession session) {
		User sessionUser = (User) session.getAttribute("user");
		if (sessionUser == null) {
			return "redirect:/";
		}
		Order orderInfo = orderService.getOrderById(orderId);
		List<Order> orders = orderService.getOtherOrderByCustomerId(
				sessionUser.getId(), orderId);
		//判断租赁物品是否到期
		if(orderInfo.getOrderType() != 0) {
			try {
				long newTime = new Date().getTime();
				long creditTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(orderInfo.getCreditEndDate()).getTime();
				if(newTime > creditTime) {
					//更新订单状态为完成
					orderService.updateStatus(3, orderInfo.getId());
					orderInfo.setStatusId(3);
					//更新物品状态
					goodService.updateGoodStatusId(1, orderInfo.getGoodId());
					//退还押金
					userService.rechargeToCount((int)goodService.getGoodById(orderInfo.getGoodId()).getPrise(), orderInfo.getCustomerId());
					User user = (User)session.getAttribute("user");
					user.setCount(user.getCount()+(int)goodService.getGoodById(orderInfo.getGoodId()).getPrise());
					session.setAttribute("user", user);
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		model.addAttribute("orderInfo", orderInfo);
		model.addAttribute("orders", orders);
		model.addAttribute("orderPrice", orderPrice);
		return "user/orderInfo";
	}

	@RequestMapping(value = "/user/sellerInfo", method = RequestMethod.GET)
	public String getSellerInfo(ModelMap model,
			@RequestParam(value = "orderId", required = false) Integer orderId,
			HttpSession session) {
		User sessionUser = (User) session.getAttribute("user");
		if (sessionUser == null) {
			return "redirect:/";
		}
		Order orderInfo = orderService.getOrderById(orderId);
		List<Order> orders = orderService.getOtherOrderBySellerId(
				sessionUser.getId(), orderId);
		model.addAttribute("orderInfo", orderInfo);
		model.addAttribute("orders", orders);
		System.out.println("sellerInfo.size:" + orders.size());
		return "user/sellerInfo";
	}

	@RequestMapping(value = "/user/order/delete/{orderId}", method = RequestMethod.GET)
	public ResponseEntity deleteOrderById(@PathVariable Integer orderId) {
		Boolean success;
		Boolean success1;
		success1 = goodService.updateGoodStatusId(1, orderService.getOrderById(orderId).getGoodId()) > 0;
		if(!success1) return ResponseEntity.ok(success1);
		success = orderService.deleteOrderById(orderId) > 0;
		return ResponseEntity.ok(success);
	}

	@RequestMapping(value = "/user/sellerOrder/delete/{orderId}&{goodId}", method = RequestMethod.GET)
	public ResponseEntity deleteSellerOrderById(@PathVariable Integer orderId,
			@PathVariable Integer goodId) {
		Boolean success;
		success = goodService.updateGoodStatusId(1, goodId) > 0;
		if (success) {
			success = orderService.deleteOrderById(orderId) > 0;
		}
		return ResponseEntity.ok(success);
	}

	@RequestMapping(value = "/user/order/update/status/{orderId}&{statusId}", method = RequestMethod.GET)
	public ResponseEntity updateOrderStatus(@PathVariable Integer orderId,
			@PathVariable Integer statusId,HttpSession session) {
		Order order1 = orderService.getOrderById(orderId);
		User user = userService.getUserById(order1.getCustomerId());
		if(order1.getGoodName().startsWith("积分")) {
			if(user.getGrade() < order1.getMoney()) {
				return ResponseEntity.ok(false);
			}
			orderService.updateGrade(-order1.getMoney(),user.getId());
		}else {
			if(user.getCount() < order1.getMoney()) {
				return ResponseEntity.ok(false);
			}
			userService.rechargeToCount(-order1.getMoney(), user.getId());
			orderService.updateGrade(10,user.getId());
		}
		User userSe = ((User)session.getAttribute("user"));
		userSe.setCount(user.getCount()-order1.getMoney());
		session.setAttribute("user", userSe);
		Boolean success = orderService.updateStatus(statusId, orderId) > 0;
		
		if (success) {
			Order order = orderService.getOrderById(orderId);
			return ResponseEntity.ok(order);
		}
		return ResponseEntity.ok(success);
	}

	@RequestMapping(value = "/user/order/create", method = RequestMethod.POST)
	public ResponseEntity createOrder(@RequestBody Order order) {
		Boolean success = orderService.insertOrder(order) > 0;
		if (success) {
			if(!order.getGoodName().startsWith("积分")) {
				success = goodService.updateGoodStatusId(0, order.getGoodId()) > 0;
			}
			if (success) {
				return ResponseEntity.ok(order.getId());
			} else {
				orderService.deleteOrderById(order.getId());
				return ResponseEntity.ok(success);
			}
		}
		return ResponseEntity.ok(success);
	}
	
	
	/**
	 * 评分
	 */
	@RequestMapping(value = "/user/grade/{score}&{orderid}", method = RequestMethod.GET)
	public ResponseEntity gradeOrder(@PathVariable Integer score,@PathVariable Integer orderid,
			HttpSession session) {
		orderService.changOrderScore(score,orderid);
		Order order = orderService.getOrderById(orderid);
		Good good = goodService.getGoodById(order.getGoodId());
		if(score == 1) {
			//评价过低,减少信用分
			userService.changGrade(-1,good.getUserId());
		}else if(score == 5) {
			//评价满分,增加信用分
			userService.changGrade(1,good.getUserId());
		}else {
			//其他评分不处理
		}
		return ResponseEntity.ok(true);
	}
	
	
	/**
	 * 退租
	 */
	@RequestMapping(value = "/user/exitCredit/{orderid}", method = RequestMethod.GET)
	public ResponseEntity exitCreditOrder(@PathVariable Integer orderid,HttpSession session) {
		//退还押金
		Boolean success;
		Order order = orderService.getOrderById(orderid);
		Good goods = goodService.getGoodById(order.getGoodId());
		success = userService.rechargeToCount((int) goods.getPrise(), order.getCustomerId());
		User user = (User)session.getAttribute("user");
		user.setCount(user.getCount()+(int)goods.getPrise());
		
		if(!success) return ResponseEntity.ok(!success);
		//修改物品状态
		success = goodService.updateGoodStatusId(1, goods.getId()) > 0;
		if(!success) return ResponseEntity.ok(!success);
		//修改订单状态
		success = orderService.updateStatus(3, orderid) > 0;
		session.setAttribute("user", user);
		return ResponseEntity.ok(success);
	}
	
	/**
	 * 续租
	 */
	@RequestMapping(value = "/user/continueCredit/{month}&{orderid}", method = RequestMethod.GET)
	public ResponseEntity continueCreditOrder(@PathVariable Integer orderid,
			@PathVariable Integer month,
			HttpSession session) {
		Order order = orderService.getOrderById(orderid);
		Good goods = goodService.getGoodById(order.getGoodId());
		User user = userService.getUserById(order.getCustomerId());
		//根据续租时间计算应扣金额
		int continueMoney = (int)(goods.getPrise()/10) * month;
		if(user.getCount() < continueMoney) return ResponseEntity.ok(false);
		user.setCount(user.getCount() - continueMoney);
		session.setAttribute("user", user);
		//修改租赁到期时间
		String creditEndDate = order.getCreditEndDate();
		Calendar calendar = Calendar.getInstance();
		try {
			calendar.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(creditEndDate));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		calendar.add(Calendar.MONTH, month);
		order.setCreditEndDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime()));
		boolean success = orderService.updateOrderCreditEndDate(order) > 0;
		return ResponseEntity.ok(success);
	}
	
	/**
	 * 创建租赁订单
	 * @param order
	 * @return
	 */
	@RequestMapping(value = "/user/order/createCredit/{credittime}", method = RequestMethod.POST)
	public ResponseEntity createCreditOrder(@RequestBody Order order,
			@PathVariable Integer credittime) {
		int orderTime = credittime;
		//计算订单金额
		int money = order.getMoney();
		money = (money*orderTime + 10*money)/10; 
		order.setMoney(money);
		//查看账户余额是否充足
		User user = userService.getUserById(order.getCustomerId());
		if(user.getCount() < money) {
			return ResponseEntity.ok(false);
		}
		//userService.rechargeToCount(-money, user.getId());
		
		//设置租赁结束时间
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.MONTH, orderTime);
		Date creditEndTime = calendar.getTime();
		String creditEndTimeS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(creditEndTime);
		System.out.println(creditEndTimeS);
		order.setCreditEndDate(creditEndTimeS);
		order.setOrderType(credittime);
		Boolean success = orderService.insertCreditOrder(order) > 0;
		if (success) {
			success = goodService.updateGoodStatusId(0, order.getGoodId()) > 0;
			if (success) {
				return ResponseEntity.ok(order.getId());
			} else {
				orderService.deleteOrderById(order.getId());
				return ResponseEntity.ok(success);
			}
		}
		return ResponseEntity.ok(success);
	}

	@RequestMapping(value = "/user/order/allOrder", method = RequestMethod.GET)
	public ResponseEntity getAllOrders() {
		List<Order> orderList = orderService.getOrderList();
		return ResponseEntity.ok(orderList);
	}
}
