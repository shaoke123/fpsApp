package cn.beginsoft.fpmsapp;

import org.beginsoft.common.RequestURL;
import org.beginsoft.fpmsapp.base.BaseActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ta.util.http.AsyncHttpResponseHandler;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class NoticeDetailActivity  extends BaseActivity {
	private Intent intent=null;
	private JSONObject jsonObject=null;
	private JSONArray jsonArray=null;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.noitce_detail);
        intent=getIntent();
        try {
			dataFill();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	//数据填充
    private void dataFill() throws Exception{
    	
    	//发送请求得的json数据
    	params.put("id", intent.getStringExtra("noticeId"));
    	/*String resContent=taSyncHttpClient.post(RequestURL.BASEURL+RequestURL.NOTICE,params);*/
    	aSyncHttpClient.post(RequestURL.BASEURL+RequestURL.NOTICE, params,new AsyncHttpResponseHandler()
        {
            @Override
            public void onSuccess(String content)
            {
            	try {
	            	jsonObject=new JSONObject(content);
					jsonArray=jsonObject.getJSONArray("list");
					if(jsonArray.length()>0){
						JSONObject temp = (JSONObject)jsonArray.get(0);
						((TextView) findViewById(R.id.notice_detail_title)).setText(temp.getString("noticeTitle")==null?"":temp.getString("noticeTitle"));
						((TextView) findViewById(R.id.notice_detail_content)).setText(temp.getString("noticeContent")==null?"":temp.getString("noticeContent"));
						((TextView) findViewById(R.id.notice_detail_time)).setText(temp.getString("noticeTime")==null?"":temp.getString("noticeTime"));
					}
            	} catch (JSONException e) {
    				e.printStackTrace();
    			}
            }
        });
    }
}

