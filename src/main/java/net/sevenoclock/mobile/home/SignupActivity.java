package net.sevenoclock.mobile.home;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.customobj.TryCatchJO;
import net.sevenoclock.mobile.main.MainActivity;
import net.sevenoclock.mobile.settings.Functions;
import net.sevenoclock.mobile.settings.Values;
import org.json.JSONArray;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class SignupActivity extends Activity implements View.OnClickListener {

    private EditText et_home_signup_userid;
    private EditText et_home_signup_userpw;
    private EditText et_home_signup_userpw_ck;
    private EditText et_home_signup_username;
    private EditText et_home_signup_useremail;
    private EditText et_home_signup_userphone;
    private Button btn_home_signup_done;
    private RadioButton rb_home_signup_gender_0;
    private RadioButton rb_home_signup_gender_1;

    private int gender = 0;

    String school_coercion="!@#$%%%^&*()";

    Values values;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
                WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        setContentView(R.layout.activity_home_signup);

        values = (Values) getApplicationContext();

        et_home_signup_userid = (EditText) findViewById(R.id.et_home_signup_userid);
        et_home_signup_userpw = (EditText) findViewById(R.id.et_home_signup_userpw);
        et_home_signup_userpw_ck = (EditText) findViewById(R.id.et_home_signup_userpw_ck);
        et_home_signup_username = (EditText) findViewById(R.id.et_home_signup_username);
        et_home_signup_useremail = (EditText) findViewById(R.id.et_home_signup_useremail);
        et_home_signup_userphone = (EditText) findViewById(R.id.et_home_signup_userphone);
        btn_home_signup_done = (Button) findViewById(R.id.btn_home_signup_done);
        rb_home_signup_gender_0 = (RadioButton) findViewById(R.id.rb_home_signup_gender_0);
        rb_home_signup_gender_1 = (RadioButton) findViewById(R.id.rb_home_signup_gender_1);

        btn_home_signup_done.setOnClickListener((View.OnClickListener)this);
        rb_home_signup_gender_0.setOnClickListener((View.OnClickListener)this);
        rb_home_signup_gender_1.setOnClickListener((View.OnClickListener)this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rb_home_signup_gender_0:
                gender = 0;
                break;
            case R.id.rb_home_signup_gender_1:
                gender = 1;
                break;
            case R.id.btn_home_signup_done:
                if(et_home_signup_userid.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "아이디를 입력해주세요.", Toast.LENGTH_LONG).show();
                }else if(et_home_signup_userpw.getText().toString().equals("") || 6 > et_home_signup_userpw.getText().toString().length()){
                    Toast.makeText(getApplicationContext(), "비밀번호를 6자리 이상 입력해주세요.", Toast.LENGTH_LONG).show();
                }else if(!et_home_signup_userpw.getText().toString().equals(et_home_signup_userpw_ck.getText().toString()) ){
                    Toast.makeText(getApplicationContext(), "비밀번호를 확인해주세요.", Toast.LENGTH_LONG).show();
                }else if(et_home_signup_username.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "이름을 입력해주세요.", Toast.LENGTH_LONG).show();
                }else if(et_home_signup_useremail.getText().toString().equals("") ){
                    Toast.makeText(getApplicationContext(), "이메일을 입력해주세요.", Toast.LENGTH_LONG).show();
                }else if(et_home_signup_userphone.getText().toString().equals(""))
                    Toast.makeText(getApplicationContext(), "연락처를 입력해주세요.", Toast.LENGTH_LONG).show();
                else{
                    new LoginTask().execute(null, null, null);
                }
                break;
        }
    }

    class LoginTask extends AsyncTask<Void, Void, Boolean> {

        String uid = "";
        String pw = "";
        String pwck = "";
        String name = "";
        String email = "";
        String phone = "";

        @Override
        protected void onPreExecute() {
            uid = et_home_signup_userid.getText().toString();
            pw = et_home_signup_userpw.getText().toString();
            pwck = et_home_signup_userpw_ck.getText().toString();
            email = et_home_signup_useremail.getText().toString();
            phone = et_home_signup_userphone.getText().toString();
            try {
                name=URLEncoder.encode(et_home_signup_username.getText().toString(), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            super.onPreExecute();
        }

        protected Boolean doInBackground(Void... Void) {
            try {
                JSONArray ja = Functions.GET("set_user&user_id=" + uid +
                        "&password=" + pw + "&user_name=" + name + "&user_email=" + email
                        + "&user_phone=" + phone + "&user_gender=" + gender);

                if (ja != null) {
                    values.user_info = new TryCatchJO(ja.getJSONObject(0));
                    values.user_id = values.user_info.getInt("id");
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
            if(result){
                startActivity(new Intent(SignupActivity.this, Step2Activity.class));
                ((Activity)LandingActivity.context).finish();
                SignupActivity.this.finish();
            }
            return;
        }
    }
}