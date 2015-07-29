package cn.beginsoft.fpmsapp;

import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.*;
import android.widget.AdapterView.OnItemSelectedListener;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ta.util.http.AsyncHttpResponseHandler;
import org.beginsoft.common.ActiveUser;
import org.beginsoft.common.RequestURL;
import org.beginsoft.fpmsapp.base.BaseActivity;
import org.beginsoft.vo.UserSpn;
import android.os.Bundle;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import org.beginsoft.vo.QualityProduct;

public class LinkQualityActivity extends BaseActivity {

	private ListView listView;
	private Context context=null;
	List<QualityProduct> qualityProductList;
	//当前活动用户的下拉列表
	private Spinner spnActiveUser = null; 
	//对应的适配器
	private ArrayAdapter<UserSpn> adapterActiveUser = null; 
	public Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_link_quality);
		this.context=this;
		initView();
		LoginCheck();
		menuInit();
		initData();
		initEvent();		
	}
	
    /**
     * 判断用户是否登录
     */
    private void LoginCheck(){
    	if(ActiveUser.currentUser==null){
    		
			Intent intent = new Intent(context,LoginActivity.class);  
			//参数传递, 如果是此处跳转的，显示：请先登录的提示
			intent.putExtra("loginShow", "true");
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
            //跳转Activity  
            startActivityForResult(intent, 0); 
    	}
    }
    
    private void menuInit(){
         menuListener();
         activeUserInit();
         //设置菜单选中颜色
         TextView bnt_userinfo=(TextView)findViewById(R.id.btn_link_quality);
         bnt_userinfo.setTextColor(this.getResources().getColor(R.color.green)); 
         LinearLayout ll_link_quality=(LinearLayout)findViewById(R.id.ll_link_quality);
         ll_link_quality.setBackgroundColor(this.getResources().getColor(R.color.jblue));
    }
    
    /**
	 * 已登录用户下拉列表初始化
	 */
    private void activeUserInit(){
    	this.spnActiveUser = (Spinner) super.findViewById(R.id.spn_active_user_check); // 取得下拉框
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
						Intent intent = new Intent(context,LinkQualityActivity.class);  
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
    
	@Override
	public void menuListener() {
		super.menuListener();
	}

	private void initView() {
		listView=(ListView) findViewById(R.id.list_link_quality);
		View view=LayoutInflater.from(this).inflate(R.layout.list_link_quality, null);
    	view.findViewById(R.id.button_reject).setVisibility(View.INVISIBLE);
    	view.findViewById(R.id.button_ok).setVisibility(View.INVISIBLE);
    	listView.addHeaderView(view);
		
	}

	private void initData() {
		aSyncHttpClient.post(RequestURL.BASEURL + RequestURL.LINKQUALITY, null, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String content) {
				if (!"false".equals(content.trim())) {
					Log.e("content", content);
					JSONObject jsonObject = JSON.parseObject(content);
					JSONArray jsonArray=jsonObject.getJSONArray("list");
					qualityProductList=new ArrayList<QualityProduct>();

					for(int i=0;i<jsonArray.size();i++){

						JSONObject object=jsonArray.getJSONObject(i);
						QualityProduct qualityProduct=new QualityProduct();
						qualityProduct.setId(object.getString("id"));
						qualityProduct.setAllNumber(object.getString("allNumber"));
						qualityProduct.setCustomerMark(object.getString("customerMark"));
						qualityProduct.setEmployeeNumber(object.getString("employeeNumber"));
						qualityProduct.setFlowLine(object.getString("flowLine"));
						qualityProduct.setGoodsName(object.getString("goodsName"));
						qualityProduct.setProcePersonName(object.getString("procePersonName"));
						qualityProduct.setProceQuantity(object.getString("proceQuantity"));
						qualityProduct.setProceState(object.getString("proceState"));
						qualityProduct.setSofaModel(object.getString("sofaModel"));
						qualityProduct.setSofaName(object.getString("sofaName"));
						qualityProduct.setZstatu(object.getString("zstatu"));
						qualityProduct.setWorkShop(object.getString("workShop"));
						qualityProduct.setThreeProceNum(object.getString("threeProceNum"));
						qualityProduct.setTwoProceName(object.getString("twoProceName"));
						qualityProduct.setCustomerName(object.getString("customerName"));
						qualityProductList.add(qualityProduct);
						Log.e("qualityProductList",qualityProduct.getAllNumber());
						handler.sendEmptyMessage(0);
					}

				}

			}

			@Override
			public void onFailure(Throwable error) {
				Toast.makeText(LinkQualityActivity.this, "网络访问异常，检测是否开启网络", Toast.LENGTH_SHORT).show();
			}
		});

	}

	@SuppressLint("HandlerLeak")
	private void initEvent() {

		handler=new Handler(){
			@Override
			public void handleMessage(Message msg) {
				if(msg.what==0){
					DataAdapter dataAdapter=new DataAdapter(LinkQualityActivity.this);
					listView.setAdapter(dataAdapter);
					dataAdapter.notifyDataSetChanged();
				}
			}
		};
	}

	class DataAdapter extends BaseAdapter{
		LayoutInflater mInflater;
		Context context;
		

		public DataAdapter(Context context) {
			super();
			this.context = context;
		}

		@Override
		public int getCount() {
			return qualityProductList.size();
		}

		@Override
		public Object getItem(int position) {
			return qualityProductList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
		    ViewHolder viewHolder=null;
			if(convertView==null){
				viewHolder=new ViewHolder();
				mInflater=LayoutInflater.from(context);
				convertView=mInflater.inflate(R.layout.list_link_quality, null);
				viewHolder.allNumber=(TextView) convertView.findViewById(R.id.tv_check_allNumber);
				viewHolder.workShop=(TextView) convertView.findViewById(R.id.tv_check_workShop);
				viewHolder.flowLine=(TextView) convertView.findViewById(R.id.tv_check_flowLine);
				viewHolder.zstatu=(TextView) convertView.findViewById(R.id.tv_check_zstatu);
				viewHolder.proceState=(TextView) convertView.findViewById(R.id.tv_check_proceState);
				viewHolder.sofaName=(TextView) convertView.findViewById(R.id.tv_check_sofaName);
				viewHolder.sofaModel=(TextView) convertView.findViewById(R.id.tv_check_sofaModel);
				viewHolder.goodsName=(TextView) convertView.findViewById(R.id.tv_check_goodsName);
				viewHolder.employeeNumber=(TextView) convertView.findViewById(R.id.tv_check_employeeNumber);
				viewHolder.procePersonName=(TextView) convertView.findViewById(R.id.tv_check_procePersonName);
				viewHolder.threeProceNum=(TextView) convertView.findViewById(R.id.tv_check_twoProceNum);
				viewHolder.twoProceName=(TextView) convertView.findViewById(R.id.tv_check_twoProceName);
				viewHolder.proceQuantity=(TextView) convertView.findViewById(R.id.tv_check_proce);
				viewHolder.customerMark=(TextView) convertView.findViewById(R.id.tv_check_customerMark);
				viewHolder.buttonOK=(Button) convertView.findViewById(R.id.button_ok);
				viewHolder.buttonReject=(Button) convertView.findViewById(R.id.button_reject);
				
				convertView.setTag(viewHolder);
			}else{
				viewHolder=(ViewHolder) convertView.getTag();
			}
			
			//设置数据
			final QualityProduct qualityProduct=qualityProductList.get(position);

			viewHolder.allNumber.setText(qualityProduct.getAllNumber());
			viewHolder.workShop.setText(qualityProduct.getWorkShop());
			viewHolder.flowLine.setText(qualityProduct.getFlowLine());
			viewHolder.zstatu.setText(qualityProduct.getZstatu());
			viewHolder.proceState.setText(qualityProduct.getProceState());
			viewHolder.sofaName.setText(qualityProduct.getSofaName());
			viewHolder.sofaModel.setText(qualityProduct.getSofaModel());
			viewHolder.goodsName.setText(qualityProduct.getGoodsName());
			viewHolder.employeeNumber.setText(qualityProduct.getEmployeeNumber());
			viewHolder.procePersonName.setText(qualityProduct.getProcePersonName());
			viewHolder.threeProceNum.setText(qualityProduct.getThreeProceNum());
			viewHolder.twoProceName.setText(qualityProduct.getTwoProceName());
			viewHolder.proceQuantity.setText(qualityProduct.getProceQuantity());
			viewHolder.customerMark.setText(qualityProduct.getCustomerMark());

			viewHolder.buttonOK.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					JSONObject qualityProductJson=new JSONObject();
					JSONObject qualityJson=new JSONObject();
					qualityProductJson.put("qualityProductId", qualityProduct.getId());
					qualityProductJson.put("allNumber", qualityProduct.getAllNumber());
					qualityProductJson.put("workShop", qualityProduct.getWorkShop());
					qualityProductJson.put("flowLine", qualityProduct.getFlowLine());
					qualityProductJson.put("zstatu", qualityProduct.getZstatu());
					qualityProductJson.put("proceState", qualityProduct.getProceState());
					qualityProductJson.put("sofaName", qualityProduct.getSofaName());
					qualityProductJson.put("sofaModel", qualityProduct.getSofaModel());
					qualityProductJson.put("employeeNumber", qualityProduct.getEmployeeNumber());
					qualityProductJson.put("procePersonName", qualityProduct.getProcePersonName());
					qualityProductJson.put("threeProceNum", qualityProduct.getThreeProceNum());
					qualityProductJson.put("twoProceName", qualityProduct.getTwoProceName());
					qualityProductJson.put("proceQuantity", qualityProduct.getProceQuantity());
					qualityProductJson.put("customerMark", qualityProduct.getCustomerMark());
					qualityJson.put("qualityJson",qualityProductJson);
					Log.e("qualityJson",qualityJson.toString());
					params.put("qualityProductJson",qualityProductJson.toString());
					aSyncHttpClient.post(RequestURL.BASEURL + RequestURL.QUALITYCONFIRM, params, new AsyncHttpResponseHandler() {
						@Override
						public void onSuccess(String content) {
							if (!"false".equals(content.trim())) {
								finish();
							}

						}

						@Override
						public void onFailure(Throwable error) {
							Toast.makeText(LinkQualityActivity.this, "网络访问异常，检测是否开启网络", Toast.LENGTH_SHORT).show();
						}
					});

				}
			});


			viewHolder.buttonReject.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent=new Intent();
					Bundle bundle=new Bundle();
					intent.setClass(LinkQualityActivity.this,RejectActivity.class);
					bundle.putSerializable("qualityProduct",qualityProductList.get(position));
					intent.putExtras(bundle);
					startActivity(intent);
				}
			});
			
			return convertView;
		}
		
		
	 class ViewHolder{
			TextView allNumber;
			TextView workShop;
			TextView flowLine;
			TextView zstatu;
			TextView proceState;
			TextView sofaName;
			TextView sofaModel;
			TextView goodsName;
			TextView employeeNumber;
			TextView procePersonName;
			TextView threeProceNum;
			TextView twoProceName;
			TextView proceQuantity;
			TextView customerMark;
			Button buttonOK;
			Button buttonReject;
		}
		
	}


}
