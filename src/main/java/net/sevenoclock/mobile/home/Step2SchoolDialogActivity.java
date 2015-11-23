package net.sevenoclock.mobile.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.*;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.customobj.TryCatchJO;
import net.sevenoclock.mobile.main.MainActivity;
import net.sevenoclock.mobile.question.QuestionPagerFragment;
import net.sevenoclock.mobile.settings.Functions;
import net.sevenoclock.mobile.settings.Values;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class Step2SchoolDialogActivity extends Activity {

    Context context;

    private EditText et_home_step2_schooldialog_text;
    private Button btn_home_step2_schooldialog_search;
    private ProgressBar pb_home_step2_schooldialog_loading;
    private ListView lv_home_step2_schooldialog_list;
    private Step2SchoolDialogAdapter s2sda;

    private String query ="";
    JSONArray ja_school = null;

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
            new GetSchoolTask().execute(null, null, null);
        }

        btn_home_step2_schooldialog_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GetSchoolTask().execute(null, null, null);
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

    class GetSchoolTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            try {
                query = URLEncoder.encode(et_home_step2_schooldialog_text.getText().toString(), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            s2sda.reflesh();
            pb_home_step2_schooldialog_loading.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        protected Boolean doInBackground(Void... Void) {
            try {
                ja_school = Functions.GET("get_school_name&q="+query);
                return true;
            } catch (Exception e) {
                // TODO Auto-generated catch block
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {
            if(result){
                if(ja_school != null){
                    if(ja_school.length() > 0){
                        for(int i=0; i<ja_school.length(); i++){
                            try {
                                s2sda.add(new String[]{ja_school.getJSONObject(i).get("name").toString(),ja_school.getJSONObject(i).get("address").toString()});
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        pb_home_step2_schooldialog_loading.setVisibility(View.GONE);
                        return;
                    }
                }
            }
            s2sda.add(new String[]{"해당하는 검색결과가 없습니다.",""});
            pb_home_step2_schooldialog_loading.setVisibility(View.GONE);
            return;
        }
    }
}