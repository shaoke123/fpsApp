package cn.beginsoft.fpmsapp;

import org.beginsoft.common.ActiveUser;
import org.beginsoft.common.RequestURL;
import org.beginsoft.fpmsapp.base.BaseActivity;
import org.beginsoft.vo.UserSpn;

import com.ta.util.http.AsyncHttpResponseHandler;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class LoginActivity extends BaseActivity {
	private Button btn_login,btn_exit;
	private EditText  tv_username,tv_password; 
	private Context context;
	//当前活动用户的下拉列表
	private Spinner spnActiveUser = null; 
	//对应的适配器
	private ArrayAdapter<UserSpn> adapterActiveUser = null; 
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context=this;
        loginShowCkeck();
        setContentView(R.layout.activity_userinfo);
        menuInit();
    }
	 /**
		 * 已登录用户下拉列表初始化
		 */
	    private void activeUserInit(){
	    	this.spnActiveUser = (Spinner) super.findViewById(R.id.spn_active_user_lg); // 取得下拉框
	 		this.spnActiveUser.setPrompt("请选择要切换的用户：");
	 		this.adapterActiveUser = new ArrayAdapter<UserSpn>(this,
	 				android.R.layout.simple_spinner_item, ActiveUser.activeUserList); // 准备好下拉列表框的内容
	 		this.adapterActiveUser 
	 				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // 换个风格
	 		this.spnActiveUser.setAdapter(this.adapterActiveUser);
	 		//默认值设为当前用户
	 		this.spnActiveUser.setSelection(ActiveUser.getCurrentUserPostion());
	 		//添加事件Spinner事件监听
	 		this.spnActiveUser.setOnItemSelectedListener(new OnItemSelectedListener(){
	 			boolean isFirst = true;
				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					//首次加载不执行
					if(isFirst){
						isFirst=false;
					}else{
							ActiveUser.currentUser=ActiveUser.activeUserList.get(arg2);
							//刷新界面
							Intent intent = new Intent(context,TaskReceivableActivity.class);  
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
			                //跳转Activity  
			                startActivityForResult(intent, 0); 
					}
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub
					
				}
	 			
	 		});
	    }
	/**
	 * 检测是否显示：请先登录的字样
	 */
	private void loginShowCkeck(){
		String loginShow=getIntent().getStringExtra("loginShow");
		if(loginShow!=null&&"true".endsWith(loginShow)){
			Toast.makeText(context,"请先登录", Toast.LENGTH_SHORT).show(); 
		}
		
	}
	 /**
     * 按始化UI控件设置
     */
    private void menuInit(){
    	activeUserInit();
    	exitListener();
    	 loginListener();
         menuListener();
         //设置菜单选中颜色
         TextView bnt_userinfo=(TextView)findViewById(R.id.bnt_userinfo);
         bnt_userinfo.setTextColor(this.getResources().getColor(R.color.green)); 
         LinearLayout ll_userinfo=(LinearLayout)findViewById(R.id.ll_userinfo);
         ll_userinfo.setBackgroundColor(this.getResources().getColor(R.color.jblue));
    }
	@Override
    public void menuListener(){
    	super.menuListener();
    }
	private void loginListener(){
		
		btn_login=(Button)findViewById(R.id.btn_login);
		//登录
		btn_login.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				tv_username=(EditText)findViewById(R.id.et_username);
				tv_password=(EditText)findViewById(R.id.et_password);
				final String username=tv_username.getText().toString();
				final String password=tv_password.getText().toString();
				 if(username == null||username.trim().length() == 0||password == null||password.trim().length() == 0) {
					 Toast.makeText(context,"用户名和密码不能为空", Toast.LENGTH_LONG).show();
					 return;
				 }
				
					params.put("username", username);
					params.put("password", password);
					aSyncHttpClient.post(RequestURL.BASEURL+RequestURL.LOGIN, params,new AsyncHttpResponseHandler()
			        {
			            @Override
			            public void onSuccess(String content)
			            {		//如果登录失败，接口返回false， 否则返回当前登录用户的真实姓名
				                if(!"false".equals(content.trim())){
				                	Toast.makeText(context,"登录成功", Toast.LENGTH_SHORT).show(); 
				                	//保存用户信息到全局变量,username为编号，content为真实名
				                	UserSpn userSpn=new UserSpn(username,content.trim());
				                	//判断是否重复登录
				                	if(!ActiveUser.isLogined(username)){
				                		
					            	    ActiveUser.activeUserList.add(userSpn);
				                	}
					            	    ActiveUser.currentUser=userSpn;
				            	   //登录成功跳转到任务界面
				    				Intent intent = new Intent(context,TaskReceivableActivity.class);  
				    				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
				                    //跳转Activity  
				                    startActivityForResult(intent, 0);  
				            	}else{
				                	Toast.makeText(context,"用户名或密码错误，请重新登录", Toast.LENGTH_LONG).show(); 
				            	}
			            }
	
			            @Override
			            public void onFailure(Throwable error)
			            {
			            	Toast.makeText(context,"网络访问异常，检测是否开启网络", Toast.LENGTH_SHORT).show(); 
			            }
			        });
				
			}
    	});
		
	}
	private void exitListener(){
		
		btn_exit=(Button)findViewById(R.id.btn_exit);
		//登录
		btn_exit.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//finish();  
				//退出后台线程,以及销毁静态变量  
				System.exit(0);  
			}
		});
	}
}
