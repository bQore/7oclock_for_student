package net.sevenoclock.mobile.home;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.customobj.TryCatchJO;
import net.sevenoclock.mobile.main.MainActivity;
import net.sevenoclock.mobile.settings.Functions;
import net.sevenoclock.mobile.settings.Values;
import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

public class LoadingActivity extends Activity {
    Values values;
    AQuery aq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home_loading);

        values = (Values) getApplicationContext();
        aq = new AQuery(this);

        if(Functions.chkNetwork(this))
            login();
        else
            new AlertDialog.Builder(this).setTitle("인터넷 연결 실패")
                    .setMessage("인터넷 연결에 실패하였습니다.\n" +
                            "연결상태를 확인해주세요.")
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                        }
                    }).show();
    }

    private void login(){
        int uid = Functions.get_pref(getApplicationContext(), "uid", 0);
        if(uid != 0) {
            values.user_id = uid;

            Map<String, Object> params = new HashMap<String, Object>();
            params.put("id", values.user_id);

            aq.ajax(Functions.DOMAIN + "/mobile/?mode=get_user_info", params, JSONArray.class, new AjaxCallback<JSONArray>() {
                @Override
                public void callback(String url, JSONArray ja, AjaxStatus status) {
                    if (status.getCode() == 200) {

                        try {
                            Thread.sleep(500);
                            values.user_info = new TryCatchJO(ja.getJSONObject(0));
                            values.unions = values.user_info.getJSONArray("union");

                            if (values.user_info.get("grade_code").equals("")) {
                                if(values.user_info.get("login_from", 1)==0) startActivity(new Intent(LoadingActivity.this, Step2Activity.class));
                                else startActivity(new Intent(LoadingActivity.this, Step1Activity.class));
                                LoadingActivity.this.finish();
                                return;
                            } else {
                                if (values.union_info == null && values.unions.length() > 0) {
                                    values.union_info = new TryCatchJO(values.unions.getJSONObject(0));
                                }
                                startActivity(new Intent(LoadingActivity.this, MainActivity.class));
                                LoadingActivity.this.finish();
                                return;
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    startActivity(new Intent(LoadingActivity.this, LandingActivity.class));
                    LoadingActivity.this.finish();

                }
            });
        }else{
            startActivity(new Intent(LoadingActivity.this, LandingActivity.class));
            LoadingActivity.this.finish();
        }

    }

}