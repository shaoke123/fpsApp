package cn.beginsoft.fpmsapp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

import org.beginsoft.common.ActiveUser;
import org.beginsoft.common.EProceState;
import org.beginsoft.common.RequestURL;
import org.beginsoft.fpmsapp.base.BaseActivity;
import org.beginsoft.vo.MassQus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.beginsoft.fpmsapp.LinkQualityActivity.DataAdapter.ViewHolder;

import com.ta.util.http.AsyncHttpResponseHandler;

import android.app.AlertDialog;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class RejectReasonActivity extends BaseActivity implements OnClickListener{
	private Context context=null;
	private ListView mListView=null;
	private TextView mTotalNum;
	private TextView mCurrentNum;
	private Button mPreview;
	private Button mNext;
	private Button btn_ok;
	private Button btn_cancel;
	private List<Map<String, String>> list = new ArrayList<Map<String, String>>();
	
	private Map<String, String> map=new HashMap<String, String>();
	private ArrayList<MassQus> massList = new ArrayList<MassQus>();
	//定义每一页显示的行数
	private int mRow=7;
	//定义的页数
	private int index=0;
	MyRejectAdapter mAdapter;
    /*此处json用于获取质量金额列表*/
	private JSONObject jsonObject=null;
	private JSONArray jsonArray=null;
	private static final String TAG="SOFT";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reject_reason);
        this.context=this;
        initView();
        initData();
        initEvent();
    }

    private void initEvent() {
    	mPreview.setOnClickListener(this);
    	mNext.setOnClickListener(this);
    	btn_ok.setOnClickListener(this);
    	btn_cancel.setOnClickListener(this);
    }

    private void initData() {
    	dataFill();
    	
    }

    private void initView() {
        //驳回管理
    	mListView=(ListView)findViewById(R.id.list_reject_type);
    	View view=LayoutInflater.from(this).inflate(R.layout.list_reject_type_item, null);
    	view.findViewById(R.id.check_box).setVisibility(View.INVISIBLE);
    	mListView.addHeaderView(view);
    	mTotalNum=(TextView) findViewById(R.id.total_number);
    	mCurrentNum=(TextView) findViewById(R.id.current_number);
    	mPreview=(Button) findViewById(R.id.btn_preview);
    	mNext=(Button) findViewById(R.id.btn_next);
    	btn_ok=(Button) findViewById(R.id.reject_ok);
    	btn_cancel=(Button) findViewById(R.id.reject_cancel);
    	
    }

    @Override
	public void onClick(View v) {
    	switch (v.getId()) {
		case R.id.btn_preview:
			preView();
			break;
		case R.id.btn_next:
			nextView();
			break;
		case R.id.reject_ok:
			chooseMassQus();
			break;
		case R.id.reject_cancel:
			finish();
			break;
		}
	}
    
    private void chooseMassQus() {
    	Intent intent = new Intent();
    	//intent 传递参数
    	for(int i=0;i<massList.size();i++){
    		MassQus mass = massList.get(i);  
			String question=mass.getMassQus();
			String money=mass.getMonly();
			Log.i("RESULT", "put==="+question+","+money);
		}
    	intent.setClass(RejectReasonActivity.this, RejectActivity.class);
    	intent.putExtra("massList", (Serializable)massList);
    	this.setResult(RESULT_OK, intent);
    	this.finish();
	}

	private void nextView() {
    	index++;
    	mCurrentNum.setText("current:" + String.valueOf(index));
    	
		//重新更新数据
		mAdapter.notifyDataSetChanged();
		checkView();
	}

	private void preView() {
		index--;
		mCurrentNum.setText("current:" + String.valueOf(index));
		//重新更新数据
		mAdapter.notifyDataSetChanged();
		checkView();
	}

	public void checkView() {
		if (index <= 0) {
			//如果页数小于0就让preButton不能点击
			mPreview.setEnabled(false);
			if (list.size() <= mRow) {
				//如果总长度小于行数就让nextButton不能点击
				mNext.setEnabled(false);
			}else{
				mNext.setEnabled(true);
			}
		} else if (list.size() - index * mRow <= mRow) {
			//当当前数据前进到最后一页的时候就让nextButton不能点击
			mNext.setEnabled(false);
			mPreview.setEnabled(true);
		} else {
			mPreview.setEnabled(true);
			mNext.setEnabled(true);
		}
	}

	
	
	/**
     * 数据初始化填充驳回原因listview
     */
    private void dataFill() {
    	//异步访问
    	
    	aSyncHttpClient.post(RequestURL.BASEURL+RequestURL.QUALITY, params,new AsyncHttpResponseHandler()
        {
            @Override
            public void onSuccess(String content)
            {
            	try {
	            	jsonObject=new JSONObject(content);
					jsonArray=jsonObject.getJSONArray("list");
					Log.i(TAG, jsonArray.toString());
            	} catch (JSONException e) {
    				e.printStackTrace();
    			}
            	setListView();
            }

			@Override
            public void onFailure(Throwable error)
            {
            	Toast.makeText(context,"网络访问异常，检测是否开启网络", Toast.LENGTH_SHORT).show(); 
            }
        });
    }

    private void setListView() {
		 if(jsonArray!=null){
			    JSONObject temp;
			    Map<String, String> map1;
			    for(int position=0;position<jsonArray.length();position++){
					try {
						temp = (JSONObject)jsonArray.get(position);
					    map1 = new HashMap<String, String>(); 
					    map1.put("gdamage", temp.getString("gdamage"));
		                map1.put("massQus", temp.getString("massQus"));
					    map1.put("monly", temp.getString("monly"));
					    this.list.add(map1);
					} catch (JSONException e) {
						e.printStackTrace();
					}
			    	}
				 }else{
			    		Toast.makeText(context,"暂无数据", Toast.LENGTH_SHORT).show(); 
			    }
		    Log.i(TAG, list.toString());
		 	mAdapter=new MyRejectAdapter(this);
		 	this.mListView.setAdapter(mAdapter);
		 	mTotalNum.setText("total:" + String.valueOf(list.size()));
	    	mCurrentNum.setText("current:" + String.valueOf(index));
	    	checkView();	    	
	}
    
    private class MyRejectAdapter extends BaseAdapter{
    	LayoutInflater mInflater;
		Context context;
		public MyRejectAdapter(Context context){
			super();
			this.context=context;
		}
		public void notifyDataSetChanged() {
			// TODO Auto-generated method stub
			//每次清空数据，重新填充
//			list.clear();
			super.notifyDataSetChanged();
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if (list.size() - mRow * index < mRow) {
				return list.size() - mRow * index;
			} else {
				return mRow;
			}
//			return list == null ? 0 : list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list == null ? null : list.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return list == null ? 0 : position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder=null;
			final int pos=position + index * mRow;
			if(list!=null){
				map=list.get(pos);
			}
			if(convertView==null){
				viewHolder=new ViewHolder();
				mInflater=LayoutInflater.from(context);
				convertView=mInflater.inflate(R.layout.list_reject_type_item, null);
				
				viewHolder.textNum = (TextView) convertView.findViewById(R.id.text_typeNum);
				viewHolder.checkBox=(CheckBox) convertView.findViewById(R.id.check_box);
				viewHolder.textTypeName = (TextView) convertView.findViewById(R.id.text_typeName);
				viewHolder.textMassQus = (TextView) convertView.findViewById(R.id.quality_question);
				viewHolder.textMoney = (TextView) convertView.findViewById(R.id.text_money);

				convertView.setTag(viewHolder);
			}else{
				viewHolder=(ViewHolder) convertView.getTag();
			}
		    
//		    JSONObject temp ;
			    try {
//			    	temp = (JSONObject)jsonArray.get(position);
			    	viewHolder.textNum.setText(pos+1+"");
			    	viewHolder.textTypeName.setText(map.get("gdamage"));			    	
			    	viewHolder.textMassQus.setText(map.get("massQus"));
			    	viewHolder.textMoney.setText(map.get("monly"));
			    	viewHolder.checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			    		MassQus massQus;
						public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
							if(isChecked){
								massQus=new MassQus();
								massQus.setMassQus(list.get(pos).get("massQus"));
								massQus.setMonly(list.get(pos).get("monly"));
								massList.add(massQus);
							}
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
		    
    	    return convertView;
		}
    	
		 class ViewHolder{
				TextView textNum;
				CheckBox checkBox;
				TextView textTypeName;
				TextView textMassQus;
				TextView textMoney;
			}
    }

	

}
