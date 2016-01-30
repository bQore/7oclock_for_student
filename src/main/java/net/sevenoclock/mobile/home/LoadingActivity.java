package net.sevenoclock.mobile.home;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.kakao.usermgmt.response.model.User;

import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.customobj.TryCatchJO;
import net.sevenoclock.mobile.main.MainActivity;
import net.sevenoclock.mobile.settings.Functions;
import net.sevenoclock.mobile.settings.UserData;
import net.sevenoclock.mobile.settings.Values;

import org.json.JSONArray;
import org.json.JSONObject;

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
//                            values.unions = values.user_info.getJSONArray("union");
                            values.unions = Functions.GET(String.format("get_union_info&id=%d", values.user_id));
                            UserData.initPref(getApplicationContext());
                            int main_union_id = UserData.getMainUnion();


                            if (values.user_info.get("grade_code").equals("")) {
                                if (values.user_info.get("login_from", 1) == 0)
                                    startActivity(new Intent(LoadingActivity.this, Step2Activity.class));
                                else
                                    startActivity(new Intent(LoadingActivity.this, Step1Activity.class));
                                LoadingActivity.this.finish();
                                return;
                            } else {
                                if (main_union_id == -1) {
                                    UserData.setMainUnion(115);
                                    if (values.union_info == null && values.unions.length() > 0) {
                                        for(int i=0;i<values.unions.length();i++){
                                            JSONObject tmp = values.unions.getJSONObject(i);
                                            String title = tmp.getString("title");
                                            if(title.contains("임시소속"))continue;
                                            else values.union_info = new TryCatchJO(values.unions.getJSONObject(i));
                                        }
                                    }
                                } else if (UserData.isSignup()) {
                                    int id = UserData.getSignupUnion();
                                    for (int i = 0; i < values.unions.length(); i++) {
                                        if (id == values.unions.getJSONObject(i).getInt("id") && values.unions.getJSONObject(i).getBoolean("is_active") == true) {
                                            UserData.applySign();
                                            main_union_id = UserData.getMainUnion();
                                            break;
                                        }
                                    }
                                    for (int i = 0; i < values.unions.length(); i++) {
                                        if (values.unions.getJSONObject(i).getInt("id") == main_union_id) {
                                            values.union_info = new TryCatchJO(values.unions.getJSONObject(i));
                                            break;
                                        }
                                    }
                                } else {
                                    if (values.union_info == null && values.unions.length() > 0) {
                                        for (int i = 0; i < values.unions.length(); i++) {
                                            if (values.unions.getJSONObject(i).getInt("id") == main_union_id) {
                                                values.union_info = new TryCatchJO(values.unions.getJSONObject(i));
                                                break;
                                            }
                                        }
                                        if(values.union_info == null){
                                            for(int i=0;i<values.unions.length();i++){
                                                JSONObject tmp = values.unions.getJSONObject(i);
                                                if(tmp.getString("title").contains("임시소속"))continue;
                                                else values.union_info = new TryCatchJO(values.unions.getJSONObject(0));
                                            }
                                        }
                                    } else if (values.union_info != null && values.unions.length() > 0) {
                                        for (int i = 0; i < values.unions.length(); i++) {
                                            if (values.unions.getJSONObject(i).getInt("id") == main_union_id) {
                                                values.union_info = new TryCatchJO(values.unions.getJSONObject(i));
                                                break;
                                            }
                                        }
                                    }
                                }
                            }

                            startActivity(new Intent(LoadingActivity.this, MainActivity.class));
                            LoadingActivity.this.finish();
                            return;

                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }

                startActivity(new Intent(LoadingActivity.this, LandingActivity.class)

                );
                LoadingActivity.this.

                finish();

            }
        });
    }else{
            startActivity(new Intent(LoadingActivity.this, LandingActivity.class));
            LoadingActivity.this.finish();
        }

    }

}