package cn.beginsoft.fpmsapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ta.util.http.AsyncHttpResponseHandler;
import org.beginsoft.common.RequestURL;
import org.beginsoft.fpmsapp.base.BaseActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.view.ViewGroup;

import android.widget.Spinner;


import org.beginsoft.vo.MassQus;
import org.beginsoft.vo.QualityProduct;
import org.beginsoft.vo.Reprocess;


public class RejectActivity extends BaseActivity {
    private Context context = null;
    private ListView mListView = null;
    private List<Map<String, String>> list = new ArrayList<Map<String, String>>();
    private TextView textTotalNum;
    private TextView textCustomerName;
    private TextView textProductName;
    private TextView textProductType;
    private TextView textCustomerNum;
    private TextView textReprocessEmployee;
    private TextView textProcessPrice;
    private TextView textSelfNum;
    private TextView textRejectReason1;
    private TextView textRejectReason2;
    private Spinner spinnerReprocess;


    private Button buttonSelect;
    private Button buttonClear;
    private Button buttonConfirm;
    private Button buttonCancel;


    private QualityProduct qualityProduct;
    List<Reprocess> reprocessList;
    private Handler handler;

    //驳回原因
    private ArrayList<MassQus> mList;
    private String rejectReason1 = " ";
    private String rejectReason2 = " ";

    //    提交网络数据
    private JSONObject allJson;
    private JSONObject rejectJson;
    private JSONObject qualityProductJson;
    private JSONObject reprocessJson;
    private JSONObject rejectReasonJson;


    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reject);
        initDate();
        initView();
        initEvent();

    }

    private void initEvent() {
        //新建驳回dialog
        buttonSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RejectActivity.this, RejectReasonActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, 1);
            }
        });
        //清空驳回原因
        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mList.clear();
                textRejectReason1.setText(" ");
                textRejectReason2.setText(" ");
            }
        });

        spinnerReprocess.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                reprocessJson = new JSONObject();
                reprocessJson.put("beforeProcessId", reprocessList.get(position).getBeforeProcessId());
                reprocessJson.put("threeProcessCode", reprocessList.get(position).getThreeProcessCode());
                reprocessJson.put("threeProcessName", reprocessList.get(position).getThreeProcessName());
                Log.e("reprocessJson", reprocessJson.toString());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        ReprocessAdapter reprocessAdapter = new ReprocessAdapter(RejectActivity.this);
                        spinnerReprocess.setAdapter(reprocessAdapter);
                        break;
                    case 1:
                        textRejectReason1.setText(rejectReason1);
                        textRejectReason2.setText(rejectReason2);
                        break;
                    default:
                        break;
                }

            }
        };

        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qualityProductJson = new JSONObject();
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
                Log.e("qualityProductJson", qualityProductJson.toString());
//                提交网络数据的根节点
                allJson = new JSONObject();
                rejectJson = new JSONObject();
                rejectJson.put("qualityProductJson", qualityProductJson);
                rejectJson.put("reprocessJson", reprocessJson);
                rejectJson.put("rejectReasonJson", rejectReasonJson);
                allJson.put("allJson", rejectJson);
                Log.e("allJson", allJson.toString());


                params.put("rejectJson", rejectJson.toString());
                aSyncHttpClient.post(RequestURL.BASEURL + RequestURL.REJECTCONFIRM, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(String content) {
                        if (!"false".equals(content.trim())) {
                            finish();
                        }

                    }

                    @Override
                    public void onFailure(Throwable error) {
                        Toast.makeText(context, "网络访问异常，检测是否开启网络", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    reprocessList.clear();
                    mList.clear();
                    rejectJson.clear();
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });


    }


    @SuppressWarnings("unchecked")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mList = (ArrayList<MassQus>) data.getSerializableExtra("massList");
        JSONArray massAraay = new JSONArray();
        for (int i = 0; i < mList.size(); i++) {
            MassQus mass = mList.get(i);
//                提交到网络
            JSONObject massObject = new JSONObject();
            massObject.put("massQus", mass.getMassQus());
            massObject.put("monly", mass.getMonly());
            massAraay.add(massObject);

//                传到UI线程
            rejectReason1 = rejectReason1 + "," + mass.getMassQus();
            rejectReason2 = rejectReason2 + "," + mass.getMonly();
            handler.sendEmptyMessage(1);
            Log.e("RESULT", "GET=====" + rejectReason1 + "," + rejectReason2);
        }
        rejectReasonJson = new JSONObject();
        rejectReasonJson.put("rejectReasonJson", massAraay);
        Log.e("rejectReasonJson", massAraay.toString());
    }

    private void initDate() {

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        qualityProduct = (QualityProduct) bundle.get("qualityProduct");
        aSyncHttpClient.post(RequestURL.BASEURL + RequestURL.REPROCESS, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                if (!"false".equals(content.trim())) {
                    JSONObject jsonObject = JSON.parseObject(content);
                    JSONArray jsonArray = jsonObject.getJSONArray("list");
                    reprocessList = new ArrayList<Reprocess>();
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        Reprocess reprocess = new Reprocess();
                        reprocess.setBeforeProcessId(object.getString("beforeProcessId"));
                        reprocess.setBeforeProcessName(object.getString("beforeProcessName"));
                        reprocess.setThreeProcessCode(object.getString("threeProcessCode"));
                        reprocess.setThreeProcessName(object.getString("threeProcessName"));
                        reprocessList.add(reprocess);
                        handler.sendEmptyMessage(0);

                    }
                }

            }

            @Override
            public void onFailure(Throwable error) {
                Toast.makeText(context, "网络访问异常，检测是否开启网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView() {
        //驳回管理
        buttonCancel = (Button) findViewById(R.id.button_cancel);
        buttonClear = (Button) findViewById(R.id.button_clear);
        buttonConfirm = (Button) findViewById(R.id.button_confirm);
        buttonSelect = (Button) findViewById(R.id.button_select);
        textTotalNum = (TextView) findViewById(R.id.text_total_counter);
        textCustomerName = (TextView) findViewById(R.id.text_customer_name);
        textProductName = (TextView) findViewById(R.id.text_product_name);
        textProductType = (TextView) findViewById(R.id.text_product_type);
        textCustomerNum = (TextView) findViewById(R.id.text_customer_num);
        textReprocessEmployee = (TextView) findViewById(R.id.text_re_process_employee);
        textProcessPrice = (TextView) findViewById(R.id.text_process_price);
        textSelfNum = (TextView) findViewById(R.id.text_self_num);
        textRejectReason1 = (TextView) findViewById(R.id.text_reject_reason1);
        textRejectReason2 = (TextView) findViewById(R.id.text_reject_reason2);
        //返工工序
        spinnerReprocess = (Spinner) findViewById(R.id.spinner_re_process);


        textTotalNum.setText(qualityProduct.getAllNumber());
        textCustomerName.setText(qualityProduct.getCustomerName());
        textProductName.setText(qualityProduct.getGoodsName());
        textProductType.setText(qualityProduct.getSofaModel());
        textCustomerNum.setText(qualityProduct.getEmployeeNumber());
        textReprocessEmployee.setText(qualityProduct.getProcePersonName());
        textProcessPrice.setText(qualityProduct.getProceQuantity());
        textSelfNum.setText(qualityProduct.getThreeProceNum());

    }

    class ReprocessAdapter extends BaseAdapter {
        LayoutInflater mInflater;
        Context context;

        public ReprocessAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return reprocessList.size();
        }

        @Override
        public Object getItem(int position) {
            return reprocessList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder viewHolder = null;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                mInflater = LayoutInflater.from(context);
                convertView = mInflater.inflate(R.layout.spinner_reprocess, null);
                viewHolder.textView = (TextView) convertView.findViewById(R.id.text_reprocess_name);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            //设置数据
            final Reprocess reprocess = reprocessList.get(position);
            viewHolder.textView.setText(reprocess.getThreeProcessName());

            return convertView;
        }

        class ViewHolder {
            public TextView textView;
        }
    }

}
