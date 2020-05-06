package com.wzh.secondshop.models;

import java.text.SimpleDateFormat;

/**
 * 订单表
 * 
 * @author WEN
 *
 */
public class Order {
	private int id;
	private String goodName;
	private String seller;
	private int sellerId;
	private String customer;
	private int customerId;
	private int goodId;
	private int money;
	private String submitDate;
	private String endDate;
	private int statusId;
	private int orderType;
	private String creditEndDate;
	private int score;
	
	

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getCreditEndDate() {
		return creditEndDate;
	}

	public void setCreditEndDate(String creditEndDate) {
		this.creditEndDate = creditEndDate;
	}

	public int getOrderType() {
		return orderType;
	}

	public void setOrderType(int orderType) {
		this.orderType = orderType;
	}

	public void setSubmitDate(String submitDate) {
		this.submitDate = submitDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getGoodName() {
		return goodName;
	}

	public void setGoodName(String goodName) {
		this.goodName = goodName;
	}

	public String getSeller() {
		return seller;
	}

	public void setSeller(String seller) {
		this.seller = seller;
	}

	public int getSellerId() {
		return sellerId;
	}

	public void setSellerId(int sellerId) {
		this.sellerId = sellerId;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public int getGoodId() {
		return goodId;
	}

	public void setGoodId(int goodId) {
		this.goodId = goodId;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public String getSubmitDate() {
		return submitDate;
	}

	public void setSubmitDate(java.sql.Timestamp submitDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		this.submitDate = sdf.format(submitDate);
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(java.sql.Timestamp endDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		this.endDate = sdf.format(endDate);
	}

	public int getStatusId() {
		return statusId;
	}

	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}

}
