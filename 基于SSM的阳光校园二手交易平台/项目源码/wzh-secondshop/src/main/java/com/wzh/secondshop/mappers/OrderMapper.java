package com.wzh.secondshop.mappers;

import com.wzh.secondshop.models.Order;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface OrderMapper {
	@Select("select * from order_table where customer_id = #{customerId} ORDER BY submit_date DESC;")
	List<Order> getOrderByCustomerId(Integer customerId);

	@Select("select * from order_table ORDER BY submit_date DESC;")
	List<Order> getOrderList();

	@Select("select * from order_table where seller_id = #{sellerId} ORDER BY submit_date DESC;")
	List<Order> getOrderBySellerId(Integer sellerId);

	@Select("select * from order_table where id = #{orderId} ORDER BY submit_date DESC;")
	Order getOrderById(Integer orderId);

	@Select("select * from order_table where customer_id = #{0} and id != #{1} ORDER BY submit_date DESC;")
	List<Order> getOtherOrderByCustomerId(Integer customerId, Integer orderId);

	@Select("select * from order_table where seller_id = #{0} and id != #{1} ORDER BY submit_date DESC;")
	List<Order> getOtherOrderBySellerId(Integer sellerId, Integer orderId);

	@Update("update order_table set status_id = #{0}, end_date = now() where id = #{1};")
	int updateStatus(Integer statusId, Integer orderId); 
	
	@Update("update order_table set score = #{0} where id = #{1};")
	int changOrderScore(Integer score, Integer orderId); 
	@Update("update order_table set creditend_date = #{1}, end_date = now() where id = #{0};")
	int updateOrderCreditEndDate(Integer orderId, String creditEndDate); 

	@Delete("DELETE FROM order_table WHERE id = #{orderId};")
	int deleteOrderById(Integer orderId);

	@Insert("insert into order_table(good_name, seller, seller_id, customer, customer_id, good_id, money, status_id, submit_date, order_type ) "
			+ "values(#{goodName}, #{seller}, #{sellerId}, #{customer}, #{customerId}, #{goodId}, #{money}, 2, now(), 0);")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	int insertOrder(Order order);
	
	@Insert("insert into order_table(good_name, seller, seller_id, customer, customer_id, good_id, money, status_id, submit_date, order_type, creditend_date, score ) "
			+ "values(#{goodName}, #{seller}, #{sellerId}, #{customer}, #{customerId}, #{goodId}, #{money}, 2, now(), #{orderType}, #{creditEndDate}, 0);")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	int insertCreditOrder(Order order);
}
