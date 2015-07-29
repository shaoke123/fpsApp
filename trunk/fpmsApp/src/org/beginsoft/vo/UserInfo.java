package org.beginsoft.vo;

public class UserInfo {
	/**主键ID*/
	private String id;
	/**编号 */
	private String userNo;
	/**姓名（真实名） */
	private String userName;
	/**密码*/
	private String passWord;
	
	public UserInfo(){}
	public UserInfo(String userNo,String userName){
		this.userNo=userNo;
		this.userName=userName;
	}
	public UserInfo(String id,String userNo,String userName,String passWord){
		this.id=id;
		this.userNo=userNo;
		this.userName=userName;
		this.passWord=passWord;
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassWord() {
		return passWord;
	}
	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
	public String getUserNo() {
		return userNo;
	}
	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
}
