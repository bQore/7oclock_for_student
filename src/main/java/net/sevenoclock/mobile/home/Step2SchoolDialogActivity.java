package net.sevenoclock.mobile.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.*;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.settings.Functions;
import net.sevenoclock.mobile.settings.Values;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class Step2SchoolDialogActivity extends Activity {

    Context context;

    private EditText et_home_step2_schooldialog_text;
    private Button btn_home_step2_schooldialog_search;
    private ProgressBar pb_home_step2_schooldialog_loading;
    private ListView lv_home_step2_schooldialog_list;
    private Step2SchoolDialogAdapter s2sda;

    private String query ="";
    JSONArray ja_school = null;

    AQuery aq;
    Values values;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
                WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        setContentView(R.layout.activity_home_step2_schooldialog);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        context = getApplicationContext();
        values = (Values) getApplicationContext();
        aq = new AQuery(this);

        et_home_step2_schooldialog_text = (EditText) findViewById(R.id.et_home_step2_schooldialog_text);
        btn_home_step2_schooldialog_search = (Button) findViewById(R.id.btn_home_step2_schooldialog_search);
        pb_home_step2_schooldialog_loading = (ProgressBar) findViewById(R.id.pb_home_step2_schooldialog_loading);
        lv_home_step2_schooldialog_list = (ListView) findViewById(R.id.lv_home_step2_schooldialog_list);

        s2sda = new Step2SchoolDialogAdapter();
        lv_home_step2_schooldialog_list.setAdapter(s2sda);

        s2sda.add(new String[]{"학교를 검색해주세요.",""});
        pb_home_step2_schooldialog_loading.setVisibility(View.GONE);

        Intent intent = getIntent();
        if(!intent.getExtras().getString("query").equals("")){
            et_home_step2_schooldialog_text.setText(intent.getExtras().getString("query"));
            get_school_name();
        }

        btn_home_step2_schooldialog_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get_school_name();
            }
        });

        lv_home_step2_schooldialog_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                try {
                    Step2Activity.setSchool(ja_school.getJSONObject(position).getInt("id"),ja_school.getJSONObject(position).get("name").toString());
                    Step2SchoolDialogActivity.this.finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void get_school_name(){
        s2sda.reflesh();
        pb_home_step2_schooldialog_loading.setVisibility(View.VISIBLE);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("q", et_home_step2_schooldialog_text.getText().toString());

        aq.ajax(Functions.DOMAIN+"/mobile/?mode=get_school_name", params, JSONArray.class, new AjaxCallback<JSONArray>() {
            @Override
            public void callback(String url, JSONArray ja, AjaxStatus status) {
                if(status.getCode()==200){
                    ja_school = ja;
                    if(ja_school.length() > 0){
                        for(int i=0; i<ja.length(); i++){
                            try {
                                s2sda.add(new String[]{ja_school.getJSONObject(i).get("name").toString(),ja_school.getJSONObject(i).get("address").toString()});
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        s2sda.notifyDataSetChanged();
                        pb_home_step2_schooldialog_loading.setVisibility(View.GONE);
                        return;
                    }
                }
                s2sda.add(new String[]{"해당하는 검색결과가 없습니다.",""});
                pb_home_step2_schooldialog_loading.setVisibility(View.GONE);
            }
        });
    }

}