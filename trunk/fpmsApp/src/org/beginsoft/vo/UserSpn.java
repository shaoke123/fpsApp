package org.beginsoft.vo;

public class UserSpn {
	/**��� */
	private String userNo;
	/**��������ʵ���� */
	private String userName;
	
	public UserSpn(String userNo,String userName){
		this.userNo=userNo;
		this.userName=userName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserNo() {
		return userNo;
	}
	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}
	@Override
	public String toString() { 
		return this.userName;
	}
}
