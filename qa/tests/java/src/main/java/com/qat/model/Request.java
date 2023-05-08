package com.qat.model;

public class Request {

	private String request_no;
	private String oid;
	private String quotation_required;
	private String client_ref_request_no;
	private String priority_id;


	public String getPriority_id() {
		return priority_id;
	}
	public void setPriority_id(String priority) {
		this.priority_id = priority;
	}
	public String getClient_ref_request_no() {
		return client_ref_request_no;
	}
	public void setClient_ref_request_no(String client_ref_request_no) {
		this.client_ref_request_no = client_ref_request_no;
	}
	public String getQuotation_required() {
		return quotation_required;
	}
	public void setQuotation_required(String quotation_required) {
		this.quotation_required = quotation_required;
	}
	public String getRequest_no() {
		return request_no;
	}
	public String oid() {
		return oid;
	}
	

	public void setRequest_no(String request_no) {
		this.request_no = request_no;
	}
	
	public void setOid(String oid) {
		this.oid = oid;
	}

	@Override
	public String toString() {
		return request_no;
	}

}
