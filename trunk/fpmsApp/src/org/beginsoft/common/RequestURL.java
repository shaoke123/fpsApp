package org.beginsoft.common;


public class RequestURL {
	public static int ConnectTimeout = 30;
	public static int ReadTimeout = 60;
	//public static final String BASEURL="http://mngc402.oicp.net:36256/fpms/";
	public static final String BASEURL="http://182.254.240.108:8084/fpms/";
	//登录Controller
	public static final String LOGIN="loginController.do?loginForApp&interfaceName=datagridForApp";
	//公告Controller
	public static final String NOTICE="sfWfNoticeControllerForApp.do?datagridForApp&interfaceName=datagridForApp";
	//任务Controller
	public static final String TASK="sfPpWorktableControllerForApp.do?datagridForApp&interfaceName=datagridForApp";
	public static final String TASKUPDATE="sfPpWorktableControllerForApp.do?updateTaskForApp&interfaceName=datagridForApp";

	//驳回原因数据
	public static final String QUALITY="SfPpzjianControllerForApp.do?datagridForReasonApp&interfaceName=datagridForApp";

	public static final String LINKQUALITY="SfPpzjianControllerForApp.do?datagridForApp&interfaceName=datagridForApp";
	public static final String REJECTREASON="SfPpzjianControllerForApp.do?datagridForReasonApp&interfaceName=datagridForApp";
	public static final String REPROCESS="SfPpzjianControllerForApp.do?datagridForProcessApp&interfaceName=datagridForApp";
	public static final String REJECTCONFIRM="SfPpQualityControllerForApp.do?updateTaskForApp&interfaceName=datagridForApp";
	public static final String QUALITYCONFIRM="";

}
