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
	//��ǰ��û��������б�
	private Spinner spnActiveUser = null; 
	//��Ӧ��������
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
		 * �ѵ�¼�û������б��ʼ��
		 */
	    private void activeUserInit(){
	    	this.spnActiveUser = (Spinner) super.findViewById(R.id.spn_active_user_lg); // ȡ��������
	 		this.spnActiveUser.setPrompt("��ѡ��Ҫ�л����û���");
	 		this.adapterActiveUser = new ArrayAdapter<UserSpn>(this,
	 				android.R.layout.simple_spinner_item, ActiveUser.activeUserList); // ׼���������б�������
	 		this.adapterActiveUser 
	 				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // �������
	 		this.spnActiveUser.setAdapter(this.adapterActiveUser);
	 		//Ĭ��ֵ��Ϊ��ǰ�û�
	 		this.spnActiveUser.setSelection(ActiveUser.getCurrentUserPostion());
	 		//����¼�Spinner�¼�����
	 		this.spnActiveUser.setOnItemSelectedListener(new OnItemSelectedListener(){
	 			boolean isFirst = true;
				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					//�״μ��ز�ִ��
					if(isFirst){
						isFirst=false;
					}else{
							ActiveUser.currentUser=ActiveUser.activeUserList.get(arg2);
							//ˢ�½���
							Intent intent = new Intent(context,TaskReceivableActivity.class);  
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
			                //��תActivity  
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
	 * ����Ƿ���ʾ�����ȵ�¼������
	 */
	private void loginShowCkeck(){
		String loginShow=getIntent().getStringExtra("loginShow");
		if(loginShow!=null&&"true".endsWith(loginShow)){
			Toast.makeText(context,"���ȵ�¼", Toast.LENGTH_SHORT).show(); 
		}
		
	}
	 /**
     * ��ʼ��UI�ؼ�����
     */
    private void menuInit(){
    	activeUserInit();
    	exitListener();
    	 loginListener();
         menuListener();
         //���ò˵�ѡ����ɫ
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
		//��¼
		btn_login.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				tv_username=(EditText)findViewById(R.id.et_username);
				tv_password=(EditText)findViewById(R.id.et_password);
				final String username=tv_username.getText().toString();
				final String password=tv_password.getText().toString();
				 if(username == null||username.trim().length() == 0||password == null||password.trim().length() == 0) {
					 Toast.makeText(context,"�û��������벻��Ϊ��", Toast.LENGTH_LONG).show();
					 return;
				 }
				
					params.put("username", username);
					params.put("password", password);
					aSyncHttpClient.post(RequestURL.BASEURL+RequestURL.LOGIN, params,new AsyncHttpResponseHandler()
			        {
			            @Override
			            public void onSuccess(String content)
			            {		//�����¼ʧ�ܣ��ӿڷ���false�� ���򷵻ص�ǰ��¼�û�����ʵ����
				                if(!"false".equals(content.trim())){
				                	Toast.makeText(context,"��¼�ɹ�", Toast.LENGTH_SHORT).show(); 
				                	//�����û���Ϣ��ȫ�ֱ���,usernameΪ��ţ�contentΪ��ʵ��
				                	UserSpn userSpn=new UserSpn(username,content.trim());
				                	//�ж��Ƿ��ظ���¼
				                	if(!ActiveUser.isLogined(username)){
				                		
					            	    ActiveUser.activeUserList.add(userSpn);
				                	}
					            	    ActiveUser.currentUser=userSpn;
				            	   //��¼�ɹ���ת���������
				    				Intent intent = new Intent(context,TaskReceivableActivity.class);  
				    				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
				                    //��תActivity  
				                    startActivityForResult(intent, 0);  
				            	}else{
				                	Toast.makeText(context,"�û�����������������µ�¼", Toast.LENGTH_LONG).show(); 
				            	}
			            }
	
			            @Override
			            public void onFailure(Throwable error)
			            {
			            	Toast.makeText(context,"��������쳣������Ƿ�������", Toast.LENGTH_SHORT).show(); 
			            }
			        });
				
			}
    	});
		
	}
	private void exitListener(){
		
		btn_exit=(Button)findViewById(R.id.btn_exit);
		//��¼
		btn_exit.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//finish();  
				//�˳���̨�߳�,�Լ����پ�̬����  
				System.exit(0);  
			}
		});
	}
}
