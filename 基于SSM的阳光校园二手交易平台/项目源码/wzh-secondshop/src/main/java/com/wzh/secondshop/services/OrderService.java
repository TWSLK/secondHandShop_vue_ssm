package com.wzh.secondshop.services;

import com.wzh.secondshop.mappers.OrderMapper;
import com.wzh.secondshop.mappers.UserMapper;
import com.wzh.secondshop.models.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderMapper orderMapper;
    
    @Autowired
    private UserMapper userMapper;

    @Transactional
    public List<Order> getOrderByCustomerId(Integer customerId){
        return orderMapper.getOrderByCustomerId(customerId);
    }

    @Transactional
    public List<Order> getOrderList(){
        return orderMapper.getOrderList();
    }

    @Transactional
    public List<Order> getOrderBySellerId(Integer sellerId){
        return orderMapper.getOrderBySellerId(sellerId);
    }

    @Transactional
    public List<Order> getOtherOrderByCustomerId(Integer customerId, Integer orderId){
        return orderMapper.getOtherOrderByCustomerId(customerId, orderId);
    }

    @Transactional
    public List<Order> getOtherOrderBySellerId(Integer sellerId, Integer orderId){
        return orderMapper.getOtherOrderBySellerId(sellerId, orderId);
    }

    @Transactional
    public Order getOrderById(Integer orderId){
        return orderMapper.getOrderById(orderId);
    }
    @Transactional
    public int updateOrderCreditEndDate(Order order){
        return orderMapper.updateOrderCreditEndDate(order.getId(),order.getCreditEndDate());
    }
    

    @Transactional
    public int updateStatus(Integer statusId, Integer orderId){
        return orderMapper.updateStatus(statusId, orderId);
    }
    
    @Transactional
    public int updateGrade(Integer money,Integer userid){
    	
        return userMapper.upGrade(money,userid);
    }

    @Transactional
    public int deleteOrderById(Integer orderId){
        return orderMapper.deleteOrderById(orderId);
    }
    @Transactional
    public int insertOrder(Order order){
        return orderMapper.insertOrder(order);
    }
    @Transactional
    public int insertCreditOrder(Order order){
        return orderMapper.insertCreditOrder(order);
    }
    @Transactional
	public int changOrderScore(Integer score, Integer orderId) {
    	return orderMapper.changOrderScore(score,orderId);
	}

}
