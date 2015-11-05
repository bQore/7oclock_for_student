package net.sevenoclock.mobile.mypage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.home.LoadingActivity;
import net.sevenoclock.mobile.main.MainActivity;
import net.sevenoclock.mobile.settings.Functions;
import net.sevenoclock.mobile.settings.Values;

import java.util.HashMap;
import java.util.Map;

public class MypageChangeInfoFragment extends Fragment {

    private Context con;

    private Button btn_mypage_info_submit;
    private EditText et_mypage_info_name;
    private EditText et_mypage_info_email;
    private EditText et_mypage_info_phone;
    private RadioGroup rg_mypage_info_gender;
    private RadioButton rb_mypage_info_gender_0, rb_mypage_info_gender_1;

    int gender_no = 0;

    Values values;

    public static MypageChangeInfoFragment newInstance() {
        MypageChangeInfoFragment view = new MypageChangeInfoFragment();
        return view;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        con = container.getContext();
        values = (Values) con.getApplicationContext();

        View v = inflater.inflate(R.layout.fragment_mypage_info, container, false);

        MainActivity.setTitle("개인정보 수정");
        MainActivity.setSubtitle("개인정보를 수정해주세요.");

        et_mypage_info_name = (EditText) v.findViewById(R.id.et_mypage_info_name);
        et_mypage_info_email = (EditText) v.findViewById(R.id.et_mypage_info_email);
        et_mypage_info_phone = (EditText) v.findViewById(R.id.et_mypage_info_phone);
        btn_mypage_info_submit = (Button) v.findViewById(R.id.btn_mypage_info_submit);
        rg_mypage_info_gender = (RadioGroup) v.findViewById(R.id.rg_mypage_info_gender);
        rb_mypage_info_gender_0 = (RadioButton) v.findViewById(R.id.rb_mypage_info_gender_0);
        rb_mypage_info_gender_1 = (RadioButton) v.findViewById(R.id.rb_mypage_info_gender_1);

        et_mypage_info_name.setText(values.user_info.get("first_name",""));
        et_mypage_info_email.setText(values.user_info.get("email",""));
        et_mypage_info_phone.setText(values.user_info.get("phone", ""));

        if(values.user_info.get("gender","남자").equals("남자")){
            gender_no = 0;
            rg_mypage_info_gender.check(rb_mypage_info_gender_0.getId());
        }else{
            gender_no = 1;
            rg_mypage_info_gender.check(rb_mypage_info_gender_1.getId());
        }

        rb_mypage_info_gender_0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gender_no = 0;
            }
        });

        rb_mypage_info_gender_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gender_no = 1;
            }
        });

        btn_mypage_info_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Vibrator Vibe = (Vibrator) con.getSystemService(con.VIBRATOR_SERVICE);
                Vibe.vibrate(30);

                String str_name = et_mypage_info_name.getText().toString();
                String str_email = et_mypage_info_email.getText().toString();
                String str_phone = et_mypage_info_phone.getText().toString();

                if (str_name.length() < 1) {
                    Toast.makeText(con, "이름을 입력해주세요.", Toast.LENGTH_LONG).show();
                    return;
                }
                if (str_email.length() < 1) {
                    Toast.makeText(con, "이메일을 입력해주세요.", Toast.LENGTH_LONG).show();
                    return;
                }
                if (str_phone.length() < 1) {
                    Toast.makeText(con, "연락처을 입력해주세요.", Toast.LENGTH_LONG).show();
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
                            Toast.makeText(con, "개인정보 수정 완료", Toast.LENGTH_LONG).show();
                            values.user_info = null;
                            values.user_id = 0;
                            con.startActivity(new Intent(getActivity(), LoadingActivity.class));
                            getActivity().finish();
                        } else {
                            Toast.makeText(con, "개인정보 수정 실패\n" +
                                    "입력을 다시 확인해주세요.", Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });

        return v;
    }

}