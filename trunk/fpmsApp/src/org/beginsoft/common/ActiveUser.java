package org.beginsoft.common;

import java.util.ArrayList;
import java.util.List;

import org.beginsoft.vo.UserSpn;

/**
 * �����¼�û����û���¼�ɹ����ͽ����û��������뱣��������
 * @author Yl
 *
 */
public class ActiveUser {
	public static final  List<UserSpn> activeUserList=new ArrayList<UserSpn>();
	public static UserSpn currentUser;
	/**
	 * �ȵ���ǰ��¼�û����û��б��е��±�
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
	 * �����û�����ж��Ƿ��Ѿ���¼
	 * @param userNo �û����
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
