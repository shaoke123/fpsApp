package org.beginsoft.vo;

public class Product {
	private String serialNum;
	private String customerName;
	private String productName;
	private String productVersion;
	private String employeeNum;
	private String employeeName;
	private String processPrice;
	private String selfNum;
	
	

	public Product(String serialNum, String customerName, String productName,
			String productVersion, String employeeNum, String employeeName,
			String processPrice, String selfNum) {
		super();
		this.serialNum = serialNum;
		this.customerName = customerName;
		this.productName = productName;
		this.productVersion = productVersion;
		this.employeeNum = employeeNum;
		this.employeeName = employeeName;
		this.processPrice = processPrice;
		this.selfNum = selfNum;
	}
	public String getSerialNum() {
		return serialNum;
	}
	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductVersion() {
		return productVersion;
	}
	public void setProductVersion(String productVersion) {
		this.productVersion = productVersion;
	}
	public String getEmployeeNum() {
		return employeeNum;
	}
	public void setEmployeeNum(String employeeNum) {
		this.employeeNum = employeeNum;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public String getProcessPrice() {
		return processPrice;
	}
	public void setProcessPrice(String processPrice) {
		this.processPrice = processPrice;
	}
	public String getSelfNum() {
		return selfNum;
	}
	public void setSelfNum(String selfNum) {
		this.selfNum = selfNum;
	}
	
	
}
