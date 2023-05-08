package com.dfs.model;


import java.sql.Timestamp;


public class DeliveryDatesSumatory {

	private Timestamp delivery_date;
	private Double pay;

	public Double getPay() {
		return pay;
	}

	public void setPay(Double pay) {
		this.pay = pay;
	}

	public Timestamp getDelivery_date() {
			return delivery_date;
	}



	public void setDelivery_date(Timestamp delivery_date) {
		this.delivery_date = delivery_date;
	}
}
