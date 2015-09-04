package net.sevenoclock.mobile.home;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.custom.TryCatchJO;
import net.sevenoclock.mobile.main.MainActivity;
import net.sevenoclock.mobile.settings.Functions;
import net.sevenoclock.mobile.settings.Values;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class SignupActivity extends Activity implements View.OnClickListener {

    private EditText et_home_signup_userid;
    private EditText et_home_signup_userpw;
    private EditText et_home_signup_userpw_ck;
    private EditText et_home_signup_username;
    private EditText et_home_signup_useremail;
    private EditText et_home_signup_userphone;
    private EditText et_home_signup_userschool;
    private EditText et_home_signup_usergrade;
    private EditText et_home_signup_userclass;
    private Button btn_home_signup_search;
    private Button btn_home_signup_done;

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
        et_home_signup_userschool = (EditText) findViewById(R.id.et_home_signup_userschool);
        et_home_signup_usergrade = (EditText) findViewById(R.id.et_home_signup_usergrade);
        et_home_signup_userclass = (EditText) findViewById(R.id.et_home_signup_userclass);
        btn_home_signup_search = (Button) findViewById(R.id.btn_home_signup_search);
        btn_home_signup_done = (Button) findViewById(R.id.btn_home_signup_done);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_home_signup_search:
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(et_home_signup_userschool.getWindowToken(), 0);
                String temp_name ="";
                //한글 검색 땜에 -_-
                try {
                    temp_name = URLEncoder.encode(et_home_signup_userschool.getText().toString(), "utf-8");
                } catch (UnsupportedEncodingException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                if ( et_home_signup_userschool.getText().toString().length() == 0 ) {
                    Toast.makeText(getApplicationContext(), "최소한 한글자 이상의 학교명을 쓰시오.", Toast.LENGTH_LONG).show();
                } else {
                    JSONArray schoolname = Functions.GET("get_school_name&q=" + temp_name);
                    final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                            SignupActivity.this,android.R.layout.select_dialog_singlechoice);
                    for(int i=0;i<schoolname.length();i++){
                        try {
                            //List Adapter 생성
                            adapter.add(schoolname.getJSONObject(i).getString("title"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(SignupActivity.this);
                    alertBuilder.setIcon(R.drawable.ic_launcher);
                    alertBuilder.setTitle("항목중에 하나를 선택하세요.");

                    // 버튼 생성
                    alertBuilder.setNegativeButton("취소",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.dismiss();
                                }
                            });
                    // Adapter 셋팅
                    alertBuilder.setAdapter(adapter,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    // AlertDialog 안에 있는 AlertDialog
                                    final String strName  = adapter.getItem(id);
                                    AlertDialog.Builder innBuilder = new AlertDialog.Builder(
                                            SignupActivity.this);
                                    innBuilder.setMessage(strName);
                                    innBuilder.setTitle("선택한 학교가 맞습니까? ");
                                    innBuilder
                                            .setPositiveButton("확인",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(
                                                                DialogInterface dialog,
                                                                int which) {
                                                            school_coercion = strName;
                                                            et_home_signup_userschool.setText(strName);
                                                            dialog.dismiss();
                                                        }
                                                    });
                                    innBuilder.show();
                                }
                            });
                    alertBuilder.show();
                }
                break;
            case R.id.btn_home_signup_done:
                if(school_coercion.equals(et_home_signup_userschool.getText().toString())){
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
                    else if(et_home_signup_usergrade.getText().toString().equals(""))
                        Toast.makeText(getApplicationContext(), "학년을 입력해주세요.", Toast.LENGTH_LONG).show();
                    else if(et_home_signup_userclass.getText().toString().equals(""))
                        Toast.makeText(getApplicationContext(), "반을 입력해주세요.", Toast.LENGTH_LONG).show();
                    else{
                        new LoginTask().execute(null, null, null);
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "검색을 통해 학교를 기입해주세요.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    class LoginTask extends AsyncTask<Void, Void, Boolean> {
        protected Boolean doInBackground(Void... Void) {
            try {

                String uid=et_home_signup_userid.getText().toString();
                String pw=et_home_signup_userpw.getText().toString();
                String pwck=et_home_signup_userpw_ck.getText().toString();
                String name=et_home_signup_username.getText().toString();
                String school_name =et_home_signup_userschool.getText().toString();
                String grade=et_home_signup_usergrade.getText().toString();
                String classroom=et_home_signup_userclass.getText().toString();

                JSONArray ja = Functions.GET("set_user&user_id=" + uid +
                        "&password=" + pw + "&user_name=" + name + "&user_email=" + et_home_signup_useremail.getText().toString()
                        + "&user_phone=" + et_home_signup_userphone.getText().toString() + "&school_name=" + school_name + "&school_year=" + grade
                        + "&school_room=" + classroom);

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
                startActivity(new Intent(SignupActivity.this, MainActivity.class));
                ((Activity)LandingActivity.context).finish();
                SignupActivity.this.finish();
            }
            return;
        }
    }
}