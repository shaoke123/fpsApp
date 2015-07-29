package org.beginsoft.common;

public enum EProceState {
	UNREVIEW("0","待审核"),
	UNSTART("1","未开始ʼ"),
	STARTING("2","进行中"),
	COMPLETE("3","已完成"),
	REVIEWED("4","审核合格"),
	REJECT("5","驳回重做");
	
	private String num;
	private String value;
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	private EProceState(String num,String value) {
		this.num=num;
        this.value = value;
    }

}
