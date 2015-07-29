package org.beginsoft.fpmsapp.base;

import cn.beginsoft.fpmsapp.LinkQualityActivity;
import cn.beginsoft.fpmsapp.LoginActivity;
import cn.beginsoft.fpmsapp.MainActivity;
import cn.beginsoft.fpmsapp.R;
import cn.beginsoft.fpmsapp.TaskReceivableActivity;

import com.ta.TASyncHttpClient;
import com.ta.util.http.AsyncHttpClient;
import com.ta.util.http.RequestParams;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class BaseActivity extends Activity {
	
	//同步
	protected TASyncHttpClient taSyncHttpClient = new TASyncHttpClient();
	//异步
	protected AsyncHttpClient aSyncHttpClient = new AsyncHttpClient();
	//参数
	protected RequestParams params = new RequestParams();
	private BaseActivity baseActivity=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseActivity=this;
    }
    /**
     * 底部菜单事件监听
     */
    public  void menuListener(){
    	TextView bnt_link_quality=(TextView) findViewById(R.id.btn_link_quality);
    	TextView bnt_anno=(TextView)findViewById(R.id.bnt_anno);
    	TextView bnt_task=(TextView)findViewById(R.id.bnt_task);
    	TextView bnt_userinfo=(TextView)findViewById(R.id.bnt_userinfo);
    	bnt_anno.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(baseActivity,MainActivity.class);  
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
		        startActivityForResult(intent, 0);  
			}
    	});
    	bnt_task.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(baseActivity,TaskReceivableActivity.class);  
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
		        startActivityForResult(intent, 0);  
			}
    	});
    	
    	 bnt_link_quality.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(baseActivity,LinkQualityActivity.class);  
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
		        startActivityForResult(intent, 0);  
			}
    	});
    	
    	bnt_userinfo.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(baseActivity,LoginActivity.class);  
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
		        startActivityForResult(intent, 0);  
			}
    	});
    	
    }
}
