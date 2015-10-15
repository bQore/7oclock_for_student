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
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.customobj.TryCatchJO;
import net.sevenoclock.mobile.main.MainActivity;
import net.sevenoclock.mobile.settings.Functions;
import net.sevenoclock.mobile.settings.Values;
import org.json.JSONArray;

import java.util.List;

public class LandingActivity extends Activity implements View.OnClickListener {

    static Context context;

    private EditText et_home_landing_userid;
    private EditText et_home_landing_userpw;
    private Button btn_home_landing_login;
    private LinearLayout ll_home_landing_signup;
    private LinearLayout ll_home_landing_email;
    private LinearLayout ll_home_landing_loading;

    Values values;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home_landing);

        values = (Values) getApplicationContext();
        context = LandingActivity.this;

        et_home_landing_userid = (EditText) findViewById(R.id.et_home_landing_userid);
        et_home_landing_userpw = (EditText) findViewById(R.id.et_home_landing_userpw);
        btn_home_landing_login = (Button) findViewById(R.id.btn_home_landing_login);
        ll_home_landing_signup = (LinearLayout) findViewById(R.id.ll_home_landing_signup);
        ll_home_landing_email= (LinearLayout) findViewById(R.id.ll_home_landing_email);
        ll_home_landing_loading = (LinearLayout) findViewById(R.id.ll_home_landing_loading);

        btn_home_landing_login.setOnClickListener((View.OnClickListener) this);
        ll_home_landing_signup.setOnClickListener((View.OnClickListener) this);
        ll_home_landing_email.setOnClickListener((View.OnClickListener) this);

        ll_home_landing_loading.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        Vibrator Vibe = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        Vibe.vibrate(30);

        switch (v.getId()) {
            case R.id.btn_home_landing_login:
                new LoginTask().execute(null, null, null);
                break;
            case R.id.ll_home_landing_signup:
                Intent intent = new Intent(this, SignupActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_home_landing_email:
                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{"eaeao@naver.com","storm0812@hanmail.net","tellme0218@naver.com"});
                email.putExtra(Intent.EXTRA_SUBJECT, "[모두를위한수학]회원정보를 찾습니다!");
                email.putExtra(Intent.EXTRA_TEXT, "제목 : 회원정보를 찾습니다!\n\n내용 : 내용을 입력하십시오.");
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

    class LoginTask extends AsyncTask<Void, Void, Boolean> {

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
                JSONArray ja = Functions.GET("get_user&uid=" + login_id + "&password=" + login_pw);
                if (ja != null) {
                    values.user_id = ja.getJSONObject(0).getInt("id");
                    Functions.put_pref(getApplicationContext(), "uid", values.user_id);
                } else {
                    return false;
                }

                ja = Functions.GET("get_user_info&uid=" + values.user_id);
                if (ja != null) {
                    values.user_info = new TryCatchJO(ja.getJSONObject(0));
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
                startActivity(new Intent(LandingActivity.this, MainActivity.class));
                LandingActivity.this.finish();
                return;
            }
            ll_home_landing_loading.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show();
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
