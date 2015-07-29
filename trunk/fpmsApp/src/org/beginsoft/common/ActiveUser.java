package org.beginsoft.common;

import java.util.ArrayList;
import java.util.List;

import org.beginsoft.vo.UserSpn;

/**
 * 保存登录用户，用户登录成功，就将其用户名和密码保存起来。
 * @author Yl
 *
 */
public class ActiveUser {
	public static final  List<UserSpn> activeUserList=new ArrayList<UserSpn>();
	public static UserSpn currentUser;
	/**
	 * 等到当前登录用户在用户列表中的下标
	 */
	public static int getCurrentUserPostion(){
		int postion=0;
		for(int i=0;i<activeUserList.size();i++){
			if(currentUser.getUserNo().equals(activeUserList.get(i).getUserNo())){
				postion=i;
			}
		}
		return postion;
	}
	/**
	 * 根据用户编号判断是否已经登录
	 * @param userNo 用户编号
	 * @return
	 */
	public static boolean isLogined(String userNo){
		boolean flag=false;
		for(int i=0;i<activeUserList.size();i++){
			if(userNo.equals(activeUserList.get(i).getUserNo())){
				flag=true;
			}
		}
		return flag;
	}
	
}
