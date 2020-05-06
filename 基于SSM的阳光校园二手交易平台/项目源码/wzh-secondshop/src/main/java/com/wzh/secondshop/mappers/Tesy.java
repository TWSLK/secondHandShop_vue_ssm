package com.wzh.secondshop.mappers;

import java.util.Calendar;
import java.util.Date;

public class Tesy {

	public static void main(String[] args) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		System.out.println("1---- " + calendar.getTime());
		calendar.add(Calendar.MONTH, 1);
		System.out.println("2---- " + calendar.getTime());
	}
}
