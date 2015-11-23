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

public class Step2Activity extends Activity implements View.OnClickListener {

    Context context;

    private RadioGroup rg_home_step2_grade_m;
    private RadioGroup rg_home_step2_grade_h;
    private RadioGroup rg_home_step2_grade_n;
    private LinearLayout ll_home_step2_part2;
    private static TextView tv_home_step2_school;
    private Button btn_home_step2_school;
    private Button btn_home_step2_submit;

    String grade_code = "";
    static int school_id = 0;

    Values values;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home_step2);

        context = getApplicationContext();
        values = (Values) getApplicationContext();
        values.aq = new AQuery(this);

        rg_home_step2_grade_m = (RadioGroup) findViewById(R.id.rg_home_step2_grade_m);
        rg_home_step2_grade_h = (RadioGroup) findViewById(R.id.rg_home_step2_grade_h);
        rg_home_step2_grade_n = (RadioGroup) findViewById(R.id.rg_home_step2_grade_n);
        ll_home_step2_part2 = (LinearLayout) findViewById(R.id.ll_home_step2_part2);
        tv_home_step2_school = (TextView) findViewById(R.id.tv_home_step2_school);
        btn_home_step2_school = (Button) findViewById(R.id.btn_home_step2_school);
        btn_home_step2_submit = (Button) findViewById(R.id.btn_home_step2_submit);

        tv_home_step2_school.setOnClickListener((View.OnClickListener) this);
        btn_home_step2_school.setOnClickListener((View.OnClickListener) this);
        btn_home_step2_submit.setOnClickListener((View.OnClickListener) this);

    }

    @Override
    public void onClick(View v) {
        Vibrator Vibe = (Vibrator)getSystemService(VIBRATOR_SERVICE);
        Vibe.vibrate(30);

        switch (v.getId()){
            case R.id.tv_home_step2_school:
            case R.id.btn_home_step2_school:
                Intent intent = new Intent(this, Step2SchoolDialogActivity.class);
                intent.putExtra("query",tv_home_step2_school.getText().toString());
                startActivity(intent);
                break;
            case R.id.btn_home_step2_submit:
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("uid", values.user_id);
                params.put("grade", grade_code);
                if(!grade_code.startsWith("n")) params.put("school_id", school_id);

                values.aq.ajax(Functions.DOMAIN + "/mobile/?mode=set_school_info", params, String.class, new AjaxCallback<String>() {
                    @Override
                    public void callback(String url, String html, AjaxStatus status) {
                        if (status.getCode() == 200) {
                            startActivity(new Intent(Step2Activity.this, Step3Activity.class));
                            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                            finish();
                        } else {
                            Toast.makeText(context, "정보 저장 실패\n" +
                                    "입력을 다시 확인해주세요.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                break;
        }
    }

    public static void setSchool(int sid, String sname){
        school_id = sid;
        tv_home_step2_school.setText(sname);
    }

    public void oneRadioButtonClicked(View view) {
        rg_home_step2_grade_m.clearCheck();
        rg_home_step2_grade_h.clearCheck();
        rg_home_step2_grade_n.clearCheck();

        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()) {
            case R.id.rb_home_step2_grade_m1:
                ll_home_step2_part2.setVisibility(View.VISIBLE);
                grade_code = "m1";
                rg_home_step2_grade_m.check(view.getId());
                break;
            case R.id.rb_home_step2_grade_m2:
                ll_home_step2_part2.setVisibility(View.VISIBLE);
                grade_code = "m2";
                rg_home_step2_grade_m.check(view.getId());
                break;
            case R.id.rb_home_step2_grade_m3:
                ll_home_step2_part2.setVisibility(View.VISIBLE);
                grade_code = "m3";
                rg_home_step2_grade_m.check(view.getId());
                break;
            case R.id.rb_home_step2_grade_h1:
                ll_home_step2_part2.setVisibility(View.VISIBLE);
                grade_code = "h1";
                rg_home_step2_grade_h.check(view.getId());
                break;
            case R.id.rb_home_step2_grade_h2:
                ll_home_step2_part2.setVisibility(View.VISIBLE);
                grade_code = "h2";
                rg_home_step2_grade_h.check(view.getId());
                break;
            case R.id.rb_home_step2_grade_h3:
                ll_home_step2_part2.setVisibility(View.VISIBLE);
                grade_code = "h3";
                rg_home_step2_grade_h.check(view.getId());
                break;
            case R.id.rb_home_step2_grade_n1:
                ll_home_step2_part2.setVisibility(View.GONE);
                grade_code = "n1";
                rg_home_step2_grade_n.check(view.getId());
                break;
            case R.id.rb_home_step2_grade_n2:
                ll_home_step2_part2.setVisibility(View.GONE);
                grade_code = "n2";
                rg_home_step2_grade_n.check(view.getId());
                break;
        }
    }
}