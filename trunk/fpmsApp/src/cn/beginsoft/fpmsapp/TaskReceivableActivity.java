package cn.beginsoft.fpmsapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.beginsoft.common.ActiveUser;
import org.beginsoft.common.EProceState;
import org.beginsoft.common.RequestURL;
import org.beginsoft.fpmsapp.base.BaseActivity;
import org.beginsoft.vo.UserSpn;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ta.util.http.AsyncHttpResponseHandler;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class TaskReceivableActivity extends BaseActivity {
	private ListView listView = null; // ����ListView���
	private List<Map<String, String>> list = new ArrayList<Map<String, String>>(); // ������ʾ�����ݰ�װ
	private Context context=null;
	private JSONObject jsonObject=null;
	private JSONArray jsonArray=null;
	//��ǰ��û��������б�
	private Spinner spnActiveUser = null; 
	//��Ӧ��������
	private ArrayAdapter<UserSpn> adapterActiveUser = null; 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_task);
	    this.context=this;
	    LoginCheck();
	    menuInit();
		dataFill();
    }
    /**
     * �ж��û��Ƿ��¼
     */
    private void LoginCheck(){
    	if(ActiveUser.currentUser==null){
    		
			Intent intent = new Intent(context,LoginActivity.class);  
			//��������, ����Ǵ˴���ת�ģ���ʾ�����ȵ�¼����ʾ
			intent.putExtra("loginShow", "true");
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
            //��תActivity  
            startActivityForResult(intent, 0); 
    	}
    }
    /**
     * ��ʼ��UI�ؼ�����
     */
    private void menuInit(){
		taskTextViewListener();
		menuListener();
		activeUserInit();
    	//������ɫ
		TextView bnt_task=(TextView)findViewById(R.id.bnt_task);
		bnt_task.setTextColor(this.getResources().getColor(R.color.green)); 
		//����ɫ
		LinearLayout ll_task=(LinearLayout)findViewById(R.id.ll_task);
		ll_task.setBackgroundColor(this.getResources().getColor(R.color.jblue));
		LinearLayout ll_task_receivable=(LinearLayout)findViewById(R.id.ll_task_receivable);
		ll_task_receivable.setBackgroundColor(this.getResources().getColor(R.color.jblue));
	    //�»���
		TextView tv_task_receivable=(TextView)findViewById(R.id.tv_task_receivable);
		tv_task_receivable.setTextColor(this.getResources().getColor(R.color.green)); 
    }
    /**
	 * �ѵ�¼�û������б��ʼ��
	 */
    private void activeUserInit(){
    	this.spnActiveUser = (Spinner) super.findViewById(R.id.spn_active_user); // ȡ��������
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
     * �ײ��˵�����
     */
    @Override
    public void menuListener(){
    	super.menuListener();
    }
    /**
     * ����˵�����¼�
     */
    private void taskTextViewListener(){
    	TextView tv_task_receivable=(TextView)findViewById(R.id.tv_task_receivable);
    	TextView tv_task_starting=(TextView)findViewById(R.id.tv_task_starting);
    	tv_task_receivable.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//ˢ�½���
				Intent intent = new Intent(context,TaskReceivableActivity.class);  
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
                //��תActivity  
                startActivityForResult(intent, 0); 
			}
    	});
    	tv_task_starting.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//ˢ�½���
				Intent intent = new Intent(context,TaskStartingActivity.class);  
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
                //��תActivity  
                startActivityForResult(intent, 0); 
			}
    	});
    	
	}
    /**
     * ���ݳ�ʼ�����listview
     * @param userNo �û����
     */
    private void dataFill() {
    	params.put("username", ActiveUser.currentUser.getUserNo().trim());
    	params.put("proceState", EProceState.UNSTART.getNum());
    	//�첽����
    	aSyncHttpClient.post(RequestURL.BASEURL+RequestURL.TASK, params,new AsyncHttpResponseHandler()
        {
            @Override
            public void onSuccess(String content)
            {
            	try {
	            	jsonObject=new JSONObject(content);
					jsonArray=jsonObject.getJSONArray("list");
            	} catch (JSONException e) {
    				e.printStackTrace();
    			}
            	setListView();
            }

            @Override
            public void onFailure(Throwable error)
            {
            	Toast.makeText(context,"��������쳣������Ƿ�������", Toast.LENGTH_SHORT).show(); 
            }
        });
    }
    /**
     * �������listview
     */
    private void setListView(){
		 if(jsonArray!=null){
		//�������
	    JSONObject temp;
	    Map<String, String> map;
	    for(int position=0;position<jsonArray.length();position++){
			try {
				temp = (JSONObject)jsonArray.get(position);
			    map = new HashMap<String, String>(); 
			    map.put("oneProceName", temp.getString("oneProceName"));
			    map.put("procePersonName", temp.getString("procePersonName"));
			    map.put("sofaModel", temp.getString("sofaModel"));
			    map.put("sofaName", temp.getString("sofaName"));
			    map.put("allNumber", temp.getString("allNumber"));
			    map.put("goodsName", temp.getString("goodsName"));
			    map.put("proceQuantity", temp.getString("proceQuantity"));
			    map.put("proceTheoryTime", temp.getString("proceTheoryTime"));
			    map.put("specialModel", temp.getString("specialModel"));
			    map.put("customerMark", temp.getString("customerMark"));
			    map.put("alternate", temp.getString("alternate"));
			    this.list.add(map);
			} catch (JSONException e) {
				e.printStackTrace();
			}
	    	}
		 }else{
	    		Toast.makeText(TaskReceivableActivity.this,"��������", Toast.LENGTH_SHORT).show(); 
	    	}
		this.listView =(ListView)this.findViewById(R.id.taskListView);
	    MySimpleAdapter simpleAdapter=new MySimpleAdapter();
	    this.listView.setAdapter(simpleAdapter);
    }
    /**
     * �Զ���listview��������
     */
    private class MySimpleAdapter extends BaseAdapter{

    	@Override
    	public int getCount() {
    		// TODO Auto-generated method stub
    		return list == null ? 0 : list.size();
    	}

    	@Override
    	public Object getItem(int position) {
    		// TODO Auto-generated method stub
    		return list == null ? null : list.get(position);
    	}

    	@Override
    	public long getItemId(int position) {
    		return list == null ? 0 : position;
    	}
        
    	@Override
    	public View getView(int position, View convertView, ViewGroup parent) {
		    convertView=View.inflate(context, R.layout.list_task, null);
		    TextView tv_oneProceName = (TextView) convertView.findViewById(R.id.tv_oneProceName);
		    TextView tv_sofaModel = (TextView) convertView.findViewById(R.id.tv_sofaModel);
		    TextView tv_allNumber = (TextView) convertView.findViewById(R.id.tv_allNumber);
		    TextView tv_sofaName = (TextView) convertView.findViewById(R.id.tv_sofaName);
		    TextView tv_goodsName = (TextView) convertView.findViewById(R.id.tv_goodsName);
		    TextView tv_proceQuantity = (TextView) convertView.findViewById(R.id.tv_proceQuantity);
		    TextView tv_proceTheoryTime = (TextView) convertView.findViewById(R.id.tv_proceTheoryTime);
		    TextView specialModel = (TextView) convertView.findViewById(R.id.specialModel);
		    TextView tv_customerMark = (TextView) convertView.findViewById(R.id.tv_customerMark);
		    Button start = (Button)convertView.findViewById(R.id.start_btn);
	    	    JSONObject temp ;
			    try {
			    	temp = (JSONObject)jsonArray.get(position);
			    	tv_oneProceName.setText(temp.getString("oneProceName"));
			    	tv_sofaModel.setText(temp.getString("sofaModel"));

			    	tv_allNumber.setText(temp.getString("allNumber"));
			    	tv_sofaName.setText(temp.getString("sofaName"));
			    	tv_goodsName.setText(temp.getString("goodsName"));
			    	tv_proceQuantity.setText(temp.getString("proceQuantity"));
			    	tv_proceTheoryTime.setText(temp.getString("proceTheoryTime"));
			    	specialModel.setText(temp.getString("specialModel"));
			    	tv_customerMark.setText(temp.getString("customerMark"));
			    	if(temp.getString("alternate").equals("1")){
			    		start.setFocusable(false);
			    		start.setFocusableInTouchMode(false);
			    	}
			    	else if(temp.getString("alternate").equals("0")){
			    		start.setFocusable(true);
			    		start.setFocusableInTouchMode(true);
			    		formatProceState(convertView,temp.getString("proceState"),temp.getString("id"));
			    	}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    
    	    return convertView;
    	}
    	//����״̬��button
    	private String formatProceState(View convertView,final String proceState,final String id){
    			//����ֵ
    			String str=null;
    			Button start = (Button)convertView.findViewById(R.id.start_btn);
    			str=EProceState.UNSTART.getValue();
    			//��������ʽ
    			convertView.setBackgroundResource(R.drawable.listview_selector);
    			//����button
    			start.setText("��ʼ");
    			start.setOnClickListener(new OnClickListener() {
	    		    @Override
	    		    public void onClick(View arg0) {
	    		    	params.put("id", id);
	    		    	params.put("proceState","2");
	    		    	params.put("timsapp", "proce_start_time");
	    		    	/*String resContent=taSyncHttpClient.post(RequestURL.BASEURL+RequestURL.TASKUPDATE, params);*/
	    		    	aSyncHttpClient.post(RequestURL.BASEURL+RequestURL.TASKUPDATE, params,new AsyncHttpResponseHandler()
	    		        {
	    		            @Override
	    		            public void onSuccess(String content)
	    		            {
	    		            	 if(Integer.parseInt(content.trim())>0){
	    			    		    	Toast.makeText(context,"������ճɹ�", Toast.LENGTH_SHORT).show();
	    			    		    	//ˢ�½���
	    			    				Intent intent = new Intent(TaskReceivableActivity.this,TaskReceivableActivity.class);  
	    			    				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
	    			    				//��תActivity  
	    			                    startActivityForResult(intent, 0); 
	    			    		    }else{
	    			    		    	Toast.makeText(context,"�����쳣������ϵ����Ա", Toast.LENGTH_SHORT).show();
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
    		return str;
    	}
    }
    }
