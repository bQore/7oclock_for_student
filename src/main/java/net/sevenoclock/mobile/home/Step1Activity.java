package net.sevenoclock.mobile.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.*;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.settings.Functions;
import net.sevenoclock.mobile.settings.Values;

import java.util.HashMap;
import java.util.Map;

public class Step1Activity extends Activity {

    Context context;

    private EditText et_home_step1_name;
    private EditText et_home_step1_email;
    private EditText et_home_step1_phone;
    private Button btn_home_step1_submit;
    private RadioGroup rg_home_step1_gender;
    private RadioButton rb_home_step1_gender_0, rb_home_step1_gender_1;

    int gender_no = 0;

    Values values;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home_step1);

        context = getApplicationContext();
        values = (Values) getApplicationContext();
        values.aq = new AQuery(this);

        et_home_step1_name = (EditText) findViewById(R.id.et_home_step1_name);
        et_home_step1_email = (EditText) findViewById(R.id.et_home_step1_email);
        et_home_step1_phone = (EditText) findViewById(R.id.et_home_step1_phone);
        btn_home_step1_submit = (Button) findViewById(R.id.btn_home_step1_submit);
        rg_home_step1_gender = (RadioGroup) findViewById(R.id.rg_home_step1_gender);
        rb_home_step1_gender_0 = (RadioButton) findViewById(R.id.rb_home_step1_gender_0);
        rb_home_step1_gender_1 = (RadioButton) findViewById(R.id.rb_home_step1_gender_1);

        et_home_step1_email.setText(values.user_info.get("email","@"));
        et_home_step1_phone.setText(values.user_info.get("phone", "-"));

        if(values.user_info.get("gender","남성").equals("남성")){
            gender_no = 0;
            rg_home_step1_gender.check(rb_home_step1_gender_0.getId());
        }else{
            gender_no = 1;
            rg_home_step1_gender.check(rb_home_step1_gender_1.getId());
        }

        rb_home_step1_gender_0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gender_no = 0;
            }
        });

        rb_home_step1_gender_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gender_no = 1;
            }
        });

        btn_home_step1_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Vibrator Vibe = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                Vibe.vibrate(30);

                String str_name = et_home_step1_name.getText().toString();
                String str_email = et_home_step1_email.getText().toString();
                String str_phone = et_home_step1_phone.getText().toString();

                if (str_name.length() < 1) {
                    Toast.makeText(context, "이름을 입력해주세요.", Toast.LENGTH_LONG).show();
                    return;
                }
                if (str_email.length() < 1) {
                    Toast.makeText(context, "이메일을 입력해주세요.", Toast.LENGTH_LONG).show();
                    return;
                }
                if (str_phone.length() < 1) {
                    Toast.makeText(context, "연락처을 입력해주세요.", Toast.LENGTH_LONG).show();
                    return;
                }

                Map<String, Object> params = new HashMap<String, Object>();
                params.put("name", str_name);
                params.put("email", str_email);
                params.put("phone", str_phone);
                params.put("gender", "" + gender_no);

                values.aq.ajax(Functions.DOMAIN + "/mobile/?mode=set_user_info&uid=" + values.user_id, params, String.class, new AjaxCallback<String>() {
                    @Override
                    public void callback(String url, String html, AjaxStatus status) {
                        if (status.getCode() == 200) {
                            startActivity(new Intent(Step1Activity.this, Step2Activity.class));
                            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                            finish();
                        } else {
                            Toast.makeText(context, "정보 저장 실패\n" +
                                    "입력을 다시 확인해주세요.", Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });

    }
}