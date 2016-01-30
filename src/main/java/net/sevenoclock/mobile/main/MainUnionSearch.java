package net.sevenoclock.mobile.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.customobj.FontTextView;
import net.sevenoclock.mobile.customobj.IconTextView;
import net.sevenoclock.mobile.mypage.MypageGroupFragment;
import net.sevenoclock.mobile.settings.Functions;
import net.sevenoclock.mobile.settings.UserData;
import net.sevenoclock.mobile.settings.Values;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bear on 2016-01-19.
 */
public class MainUnionSearch extends Activity implements View.OnClickListener{
    private EditText et_union_search_bar;
    private IconTextView ic_search_icon;
    private ListView lv_result_list;

    private Button btn_cancel;

    private LinearLayout progress;

    Values values;

    private String search;
    private UnionAdapt adapt;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND, WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        setContentView(R.layout.activity_main_union_search);

        values = (Values) getApplicationContext();

        et_union_search_bar = (EditText) findViewById(R.id.et_union_search);
        ic_search_icon = (IconTextView) findViewById(R.id.itv_search_done);
        ic_search_icon.setOnClickListener(this);

        lv_result_list = (ListView) findViewById(R.id.lv_union_list);

        btn_cancel = (Button) findViewById(R.id.btn_union_cancel);
        btn_cancel.setOnClickListener(this);

        progress = (LinearLayout) findViewById(R.id.ll_union_list_loading);

        search = getIntent().getStringExtra("search_name");
        Log.i("@@info", "keyword:" + search);

        adapt = new UnionAdapt();
        lv_result_list.setAdapter(adapt);
        lv_result_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            private ImageView c_icon;
            private FontTextView c_title;
            private FontTextView c_address;
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(MainUnionSearch.this);
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View v = inflater.inflate(R.layout.dialog_signup_confirm,parent,false);

                c_icon = (ImageView) v.findViewById(R.id.iv_confirm_icon);
                c_title = (FontTextView) v.findViewById(R.id.ftv_confirm_title);
                c_address = (FontTextView) v.findViewById(R.id.ftv_confirm_address);

                UnionInfo tmp = (UnionInfo) adapt.getItem(position);

                values.aq.id(c_icon).image(Functions.DOMAIN+((UnionInfo)adapt.getItem(position)).icon);
                c_title.setText(tmp.title);
                c_address.setText(tmp.address);

                builder.setView(v);
                builder.setPositiveButton("신청하기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(!UserData.isSignup()){
                            Map<String, Object> key = new HashMap<String, Object>();
                            key.put("id",values.user_id);
                            key.put("union_id", ((UnionInfo) adapt.getItem(position)).union_id);
                            Log.i("@@info","key:"+key);
                            values.aq.ajax(Functions.DOMAIN + "/mobile/?mode=set_union_info", key, JSONObject.class, new AjaxCallback<JSONObject>() {
                                @Override
                                public void callback(String url, JSONObject object, AjaxStatus status) {
                                    Log.i("@@info", "url:" + url + " code:" + status.getCode() + " obj:" + object);
                                    if (status.getCode() == 200) {
                                        Toast.makeText(MainUnionSearch.this, "신청되었습니다!", Toast.LENGTH_SHORT).show();
                                        Functions.history_back_delete(MainUnionSearch.this);
                                        Functions.history_go(MainUnionSearch.this,new MypageGroupFragment().newInstance());
                                    }
                                }
                            });

                            finish();
                        }else{
                            Toast.makeText(MainUnionSearch.this,"이미 가입을 신청한 소속이 있습니다!",Toast.LENGTH_SHORT).show();
                        }

                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.show();
            }
        });

        if(search != null) {
            if (!search.equals("")) {
                et_union_search_bar.setText(search);
                new SearchTask().execute(null,null,null);
            }
            else Toast.makeText(this, "키워드를 입력해주세요!", Toast.LENGTH_SHORT).show();
        }
        else Toast.makeText(this, "키워드를 입력해주세요!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.itv_search_done:
                search = et_union_search_bar.getText().toString();
                if(search != null) {
                    if (!search.equals("")) {
                        Log.i("@@info","keyword:"+search);
                        new SearchTask().execute(null,null,null);
                    }
                    else Toast.makeText(this, "키워드를 입력해주세요!", Toast.LENGTH_SHORT).show();
                }
                else Toast.makeText(this, "키워드를 입력해주세요!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_union_cancel:
                this.finish();
                break;
        }
    }

    private class UnionAdapt extends BaseAdapter{
        private ImageView iv_union_icon;
        private FontTextView ftv_union_title;
        private FontTextView ftv_union_address;

        private ArrayList<UnionInfo> list;

        public UnionAdapt(){
            list = new ArrayList<UnionInfo>();
        }

        public void add(UnionInfo i){
            list.add(i);
        }

        public void refresh(){
            list.clear();
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final int pos = position;
            final Context con = parent.getContext();

            if(convertView==null){
                LayoutInflater inflater = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.view_union_list, parent, false);

                iv_union_icon = (ImageView) convertView.findViewById(R.id.iv_union_icon);
                ftv_union_title = (FontTextView) convertView.findViewById(R.id.ftv_union_title);
                ftv_union_address = (FontTextView) convertView.findViewById(R.id.ftv_union_address);

//                iv_union_icon.setImageBitmap(list.get(pos).icon);
                values.aq.id(iv_union_icon).image(Functions.DOMAIN+list.get(pos).icon);
                ftv_union_title.setText(list.get(pos).title);
                ftv_union_address.setText(list.get(pos).address);
            }
            return convertView;
        }
    }

    private class UnionInfo{
        public int union_id;
        public String icon;
        public String title;
        public String address;

        public UnionInfo(int id, String icon, String name, String address){
            this.union_id = id;
            this.icon = icon;
            this.title = name;
            this.address = address;
        }
    }

    // AsyncTask 전달 인자 : 시작할 때 스일 데이터, 처리 중에 보일 데이터, 처리 후의 데이터
    private class SearchTask extends AsyncTask<Void, Void, Boolean>{

        @Override
        protected void onPreExecute(){
            progress.setVisibility(View.VISIBLE);
            adapt.refresh();
            adapt.notifyDataSetChanged();
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void...params) {
//            //아래는 GET 형식으로 파라미터 전송
//            JSONArray obj = Functions.GET(String.format("get_union_info_list&id=%d&q=%s",values.user_id,search));
//            Log.i("@@info", "obj:"+obj);

            // 아래는 POST 방식으로 파라미터 전송.
            Map<String, Object> p = new HashMap<String, Object>();
            p.put("q", search);
            p.put("id", values.user_id);
            values.aq.ajax(Functions.DOMAIN + "/mobile/?mode=get_union_info_list", p, JSONArray.class, new AjaxCallback<JSONArray>() {

                @Override
                public void callback(String url, JSONArray jo, AjaxStatus status) {
                    if (status.getCode() == 200) {
                        try {
                            Log.i("@@info", "length:" + jo.length());
                            if (jo != null) {
                                for (int i = 0; i < jo.length(); i++) {
                                    JSONObject data = jo.getJSONObject(i);
                                    int id = data.getInt("id");
                                    String title = data.getString("title");
                                    String src = data.getString("icon");
                                    adapt.add(new UnionInfo(id, src, title, "소속 주소를 입력해주세요."));
                                }
                            } else
                                Toast.makeText(MainUnionSearch.this, "검색된 소속이 없습니다!", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (status.getCode() == 400)
                        Toast.makeText(MainUnionSearch.this, "목록을 불러오는데 실패하였습니다!", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(MainUnionSearch.this, "ErrorCode:" + status.getCode(), Toast.LENGTH_SHORT).show();


                    adapt.notifyDataSetChanged();
                }
            });
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result){
            if(result){
                //처리 결과가 TRUE 일 경우 실행
                progress.setVisibility(View.GONE);
            }
        }
    }
}
