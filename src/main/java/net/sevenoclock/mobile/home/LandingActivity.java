package net.sevenoclock.mobile.home;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.facebook.*;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.customobj.TryCatchJO;
import net.sevenoclock.mobile.main.MainActivity;
import net.sevenoclock.mobile.settings.Functions;
import net.sevenoclock.mobile.settings.Values;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;

public class LandingActivity extends Activity implements View.OnClickListener {

    static Context context;

    private EditText et_home_landing_userid;
    private EditText et_home_landing_userpw;
    private LinearLayout ll_home_landing_login_mosu;
    private LinearLayout ll_home_landing_login_facebook;
    private LinearLayout ll_home_landing_login_kakao;
    private LinearLayout ll_home_landing_signup;
    private LinearLayout ll_home_landing_email;
    private LinearLayout ll_home_landing_loading;

    Values values;
    CallbackManager callbackManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home_landing);

        values = (Values) getApplicationContext();
        context = LandingActivity.this;

        et_home_landing_userid = (EditText) findViewById(R.id.et_home_landing_userid);
        et_home_landing_userpw = (EditText) findViewById(R.id.et_home_landing_userpw);
        ll_home_landing_login_mosu = (LinearLayout) findViewById(R.id.ll_home_landing_login_mosu);
        ll_home_landing_login_facebook = (LinearLayout) findViewById(R.id.ll_home_landing_login_facebook);
        ll_home_landing_login_kakao = (LinearLayout) findViewById(R.id.ll_home_landing_login_kakao);
        ll_home_landing_signup = (LinearLayout) findViewById(R.id.ll_home_landing_signup);
        ll_home_landing_email= (LinearLayout) findViewById(R.id.ll_home_landing_email);
        ll_home_landing_loading = (LinearLayout) findViewById(R.id.ll_home_landing_loading);

        ll_home_landing_login_mosu.setOnClickListener((View.OnClickListener) this);
        ll_home_landing_login_facebook.setOnClickListener((View.OnClickListener) this);
        ll_home_landing_login_kakao.setOnClickListener((View.OnClickListener) this);
        ll_home_landing_signup.setOnClickListener((View.OnClickListener) this);
        ll_home_landing_email.setOnClickListener((View.OnClickListener) this);

        ll_home_landing_loading.setVisibility(View.GONE);

        FacebookSdk.sdkInitialize(this.getApplicationContext());

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.i("onSuccess", "@@@");
                        ll_home_landing_loading.setVisibility(View.VISIBLE);
                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(
                                            JSONObject object,
                                            GraphResponse response) {
                                        try {
                                            String user_id = (String) response.getJSONObject().get("id");
                                            String password = user_id;
                                            String user_name = URLEncoder.encode(response.getJSONObject().get("name").toString(), "utf-8");
                                            String user_email = (String) response.getJSONObject().get("email");
                                            String gender = (String) response.getJSONObject().get("gender");
                                            String img_url = "http://graph.facebook.com/"+user_id+"/picture?type=large";
                                            if(gender.equals("남성")) gender = "0";
                                            else gender = "1";

                                            JSONArray ja = Functions.GET("get_user&user_id=" + user_id + "&password=" + user_id+"&login_from=1");
                                            if (ja != null) {
                                                values.user_id = ja.getJSONObject(0).getInt("id");
                                                Functions.put_pref(getApplicationContext(), "uid", values.user_id);
                                                new UserInfoTask().execute(null, null, null);
                                                return;
                                            }

                                            String query = "set_user&login_from=1&user_id="+user_id+"&password="+password+"&user_name="+user_name+"&user_email="+user_email
                                                    +"&gender="+gender+"&img_url="+img_url;

                                            ja = Functions.GET(query);
                                            if (ja != null) {
                                                values.user_id = ja.getJSONObject(0).getInt("id");
                                                Functions.put_pref(getApplicationContext(), "uid", values.user_id);
                                                new UserInfoTask().execute(null, null, null);
                                            }

                                        } catch (Exception e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        }
                                        // new joinTask().execute(); //자신의 서버에서 로그인 처리를 해줍니다
                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email,gender");
                        parameters.putString("locale", "ko_KR");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        Log.i("onCancel", "@@@");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Log.i("onError", "@@@");
                    }
                });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        Vibrator Vibe = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        Vibe.vibrate(30);

        switch (v.getId()) {
            case R.id.ll_home_landing_login_mosu:
                new LoginIDPWTask().execute(null, null, null);
                break;
            case R.id.ll_home_landing_login_facebook:
                LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email"));
                break;
            case R.id.ll_home_landing_signup:
                Intent intent = new Intent(this, SignupActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_home_landing_email:
                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{"eaeao@naver.com","storm0812@hanmail.net","tellme0218@naver.com"});
                email.putExtra(Intent.EXTRA_SUBJECT, "[모두를위한수학]회원정보를 찾습니다!");
                email.putExtra(Intent.EXTRA_TEXT, "제목 : 회원정보를 찾습니다!\n\n내용 : ");
                email.setType("text/plain");
                final PackageManager pm = getPackageManager();
                final List<ResolveInfo> matches = pm.queryIntentActivities(email, 0);
                ResolveInfo best = null;
                for(final ResolveInfo info : matches)
                    if (info.activityInfo.packageName.endsWith(".gm") || info.activityInfo.name.toLowerCase().contains("gmail"))
                        best = info;
                if (best != null)
                    email.setClassName(best.activityInfo.packageName, best.activityInfo.name);
                startActivity(email);
                break;
        }
    }

    class LoginIDPWTask extends AsyncTask<Void, Void, Boolean> {

        String login_id;
        String login_pw;

        @Override
        protected void onPreExecute() {
            login_id = et_home_landing_userid.getText().toString();
            login_pw = et_home_landing_userpw.getText().toString();
            ll_home_landing_loading.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        protected Boolean doInBackground(Void... Void) {
            try {
                JSONArray ja = Functions.GET("get_user&user_id=" + login_id + "&password=" + login_pw+"&login_from=0");
                if (ja != null) {
                    values.user_id = ja.getJSONObject(0).getInt("id");
                    Functions.put_pref(getApplicationContext(), "uid", values.user_id);
                } else {
                    return false;
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return false;
            }
            return true;
        }

        protected void onPostExecute(Boolean result) {
            if (result) {
                new UserInfoTask().execute(null, null, null);
                return;
            }
            ll_home_landing_loading.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "로그인에 실패하였습니다.", Toast.LENGTH_LONG).show();
            return;
        }
    }

    class UserInfoTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            ll_home_landing_loading.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        protected Boolean doInBackground(Void... Void) {
            try {
                JSONArray ja = Functions.GET("get_user_info&uid=" + values.user_id);
                if (ja != null) {
                    values.user_info = new TryCatchJO(ja.getJSONObject(0));
                    values.unions = values.user_info.getJSONArray("union");
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return false;
            }
            return true;
        }

        protected void onPostExecute(Boolean result) {
            if (result) {
                try {
                    if(values.user_info.get("grade_code").equals("")){
                        startActivity(new Intent(LandingActivity.this, Step1Activity.class));
                        LandingActivity.this.finish();
                    }else{
                        values.union_info = new TryCatchJO(values.unions.getJSONObject(0));
                        startActivity(new Intent(LandingActivity.this, MainActivity.class));
                        LandingActivity.this.finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return;
            }
            ll_home_landing_loading.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "로그인에 실패하였습니다.", Toast.LENGTH_LONG).show();
            return;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                new AlertDialog.Builder(this).setTitle("종료")
                        .setMessage("정말 종료하시겠습니까?")
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        })
                        .setPositiveButton("종료", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                                return;
                            }
                        }).show();
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}
