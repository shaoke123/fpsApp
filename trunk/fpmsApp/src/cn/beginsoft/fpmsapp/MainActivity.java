package cn.beginsoft.fpmsapp;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.beginsoft.common.RequestURL;
import org.beginsoft.fpmsapp.base.BaseActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ta.util.http.AsyncHttpResponseHandler;
import com.widget.time.JudgeDate;
import com.widget.time.ScreenInfo;
import com.widget.time.WheelMain;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends BaseActivity {
		private WheelMain wheelMain;
		private EditText dateStart;
		private EditText dateEnd;
		private Button notice_search;
		
		@SuppressLint("SimpleDateFormat")
		private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		private ListView noticeListView = null; // ����ListView���
		private List<Map<String, String>> list = new ArrayList<Map<String, String>>(); // ������ʾ�����ݰ�װ
		private Context context=null;
		private JSONObject jsonObject=null;
		private JSONArray jsonArray=null;
		protected void onCreate(Bundle savedInstanceState) {
			    super.onCreate(savedInstanceState);
			    setContentView(R.layout.activity_main);
			    this.context=this;
			    menuInit();	
			    dataFill();
			    }
			/**
			 * ��ʼ��UI�ؼ�����
			 */
			private void menuInit(){
					menuListener();
					dateInit();
				    //���ò˵�ѡ����ɫ
				    TextView bnt_anno=(TextView)findViewById(R.id.bnt_anno);
				    bnt_anno.setTextColor(this.getResources().getColor(R.color.green)); 
				    LinearLayout ll_anno=(LinearLayout)findViewById(R.id.ll_anno);
				    ll_anno.setBackgroundColor(this.getResources().getColor(R.color.jblue));
			}
		 	/**
		 	 * �ײ��˵�����¼�����
		 	 */
		 	@Override
		    public void menuListener(){
		    	super.menuListener();
		    }
		 	/**
		 	 * ������������ʼ��
		 	 */
		 	private void dateInit(){
		 		 dateStart = (EditText)findViewById(R.id.notice_date_start);
		 		 //���������뷨
		 		 dateStart.setInputType(InputType.TYPE_NULL);  
		 		 dateEnd = (EditText)findViewById(R.id.notice_date_end);
		 		 //���������뷨
		 		 dateEnd.setInputType(InputType.TYPE_NULL); 
		 		 
		 		notice_search=(Button)findViewById(R.id.notice_search);
		 		Calendar calendar = Calendar.getInstance();
		 		//���ó�ʼʱ�䣬Ĭ����ʾ���һ����
		 		dateStart.setText(calendar.get(Calendar.YEAR) + "-" +
		 	  				    (calendar.get(Calendar.MONTH) )+ "-" +
		 	  				    calendar.get(Calendar.DAY_OF_MONTH) + "");
		 		dateEnd.setText(calendar.get(Calendar.YEAR) + "-" +
 	  				    (calendar.get(Calendar.MONTH) + 1 )+ "-" +
 	  				    calendar.get(Calendar.DAY_OF_MONTH) + "");
		 		 //�������
		 		 dateAddListener(dateStart); 
		 		 dateAddListener(dateEnd); 
		 		 
		 		 notice_search.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						try {
							list.clear();
							dataFill();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
		 		});
		 	}
		 	/**
		 	 * ��ʼ�������ڵ������
		 	 */
		    private void dateAddListener(final EditText et){
		    	
		    	et.setOnClickListener(new OnClickListener() {
		 			
		 			@Override
		 			public void onClick(View arg0) {
		 				LayoutInflater inflater=LayoutInflater.from(MainActivity.this);
		 				final View timepickerview=inflater.inflate(R.layout.timepicker, null);
		 				ScreenInfo screenInfo = new ScreenInfo(MainActivity.this);
		 				wheelMain = new WheelMain(timepickerview);
		 				wheelMain.screenheight = screenInfo.getHeight();
		 				String time = et.getText().toString();
		 				Calendar calendar = Calendar.getInstance();
		 				if(JudgeDate.isDate(time, "yyyy-MM-dd")){
		 					try {
		 						calendar.setTime(dateFormat.parse(time));
		 					} catch (ParseException e) {
		 						// TODO Auto-generated catch block
		 						e.printStackTrace();
		 					}
		 				}
		 				int year = calendar.get(Calendar.YEAR);
		 				int month = calendar.get(Calendar.MONTH);
		 				int day = calendar.get(Calendar.DAY_OF_MONTH);
		 				wheelMain.initDateTimePicker(year,month,day);
		 				new AlertDialog.Builder(MainActivity.this)
		 				.setTitle("ѡ������")
		 				.setView(timepickerview)
		 				.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
		 					@Override
		 					public void onClick(DialogInterface dialog, int which) {
		 						et.setText(wheelMain.getTime());
		 					}
		 				})
		 				.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
		 					@Override
		 					public void onClick(DialogInterface dialog, int which) {
		 					}
		 				})
		 				.show();
		 			}
		 		});
		    }
		    /**
		 	 * ������䵽ListView
		 	 */
		    private void dataFill(){
		    	//��������õ�json����
		    	params.put("noticeDateBegin", dateStart.getText().toString());
		    	params.put("noticeDateEnd", dateEnd.getText().toString());
		    	//�첽����
		    	aSyncHttpClient.post(RequestURL.BASEURL+RequestURL.NOTICE, params,new AsyncHttpResponseHandler()
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
		    
		    private void setListView(){
		    		 if(jsonArray!=null){
					//�������
				    JSONObject temp;
				    Map<String, String> map;
				    for(int position=0;position<jsonArray.length();position++){
				    	try {
							temp = (JSONObject)jsonArray.get(position);
						    map = new HashMap<String, String>(); 
						    map.put("noticeTitle", temp.getString("noticeTitle"));
						    map.put("noticeTime", temp.getString("noticeTime"));
						    map.put("id", temp.getString("id"));
						    this.list.add(map);
				    	} catch (JSONException e) {
		    				e.printStackTrace();
		    			}
				    	}
		    		 }else{
		 	    		Toast.makeText(context,"��������", Toast.LENGTH_SHORT).show(); 
		 	    	}
				    MySimpleAdapter simpleAdapter=new MySimpleAdapter();
				    this.noticeListView =(ListView)this.findViewById(R.id.noticeListView);
				    this.noticeListView.setAdapter(simpleAdapter);
				    this.noticeListView.setOnItemClickListener(new OnItemClickListenerImpl());
		    }
		    /**
		 	 *�Զ���������
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
				    
				    convertView=View.inflate(context, R.layout.list_notice, null);
				    //��������ʽ
	    			convertView.setBackgroundResource(R.drawable.listview_selector);
				    TextView tv1 = (TextView) convertView.findViewById(R.id.noticeTitle);
				    TextView tv2 = (TextView) convertView.findViewById(R.id.noticeTime);
				    TextView tv3 = (TextView) convertView.findViewById(R.id.noticeId);
				    
			    	    JSONObject temp ;
					    try {
					    	temp = (JSONObject)jsonArray.get(position);
							tv1.setText(temp.getString("noticeTitle"));
							tv2.setText(temp.getString("noticeTime"));
							tv3.setText(temp.getString("id"));
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				    
		    	    return convertView;
		    	}

		    }
		    /**
		 	 * listview��������
		 	 */
			private class OnItemClickListenerImpl implements OnItemClickListener {

					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {
						
						String noticeId= ((TextView)view.findViewById(R.id.noticeId)).getText().toString();
						
	    				Intent intent = new Intent(MainActivity.this,NoticeDetailActivity.class);  
	    				//��������
	    				intent.putExtra("noticeId", noticeId);
	    				//ע�Ȿ�е�FLAG���� 
	    				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
	                    //��תActivity  
	                    startActivityForResult(intent, 0);  
					}

				}
}
