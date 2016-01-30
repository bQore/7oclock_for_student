package net.sevenoclock.mobile.mypage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.home.LoadingActivity;
import net.sevenoclock.mobile.main.MainActivity;
import net.sevenoclock.mobile.settings.Functions;
import net.sevenoclock.mobile.settings.Values;

import java.util.HashMap;
import java.util.Map;

public class MypageChangePWFragment extends Fragment {

    private Context con;

    private EditText et_mypage_pw_now;
    private EditText et_mypage_pw_new;
    private EditText et_mypage_pw_new_;
    private Button btn_mypage_pw_submit;

    Values values;

    public static MypageChangePWFragment newInstance() {
        MypageChangePWFragment view = new MypageChangePWFragment();
        return view;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        con = container.getContext();
        values = (Values) con.getApplicationContext();

        View v = inflater.inflate(R.layout.fragment_mypage_pw, container, false);

        MainActivity.setTitle("비밀번호 재설정");
        MainActivity.setSubtitle("비밀번호를 재설정해주세요.");

        et_mypage_pw_now = (EditText) v.findViewById(R.id.et_mypage_pw_now);
        et_mypage_pw_new = (EditText) v.findViewById(R.id.et_mypage_pw_new);
        et_mypage_pw_new_ = (EditText) v.findViewById(R.id.et_mypage_pw_new_);
        btn_mypage_pw_submit = (Button) v.findViewById(R.id.btn_mypage_pw_submit);

        btn_mypage_pw_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Vibrator Vibe = (Vibrator) con.getSystemService(con.VIBRATOR_SERVICE);
                Vibe.vibrate(30);

                String pw_now = et_mypage_pw_now.getText().toString();
                String pw_new = et_mypage_pw_new.getText().toString();
                String pw_new_ = et_mypage_pw_new_.getText().toString();

                if (pw_new.length() < 6) {
                    Toast.makeText(con, "새 비밀번호는 6자리 이상 가능합니다.", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!(pw_new.equals(pw_new_))) {
                    Toast.makeText(con, "새 비밀번호를 다시 확인해주세요.", Toast.LENGTH_LONG).show();
                    return;
                }

                Map<String, Object> params = new HashMap<String, Object>();
                params.put("pw_now", pw_now);
                params.put("pw_new", pw_new);

                values.aq.ajax(Functions.DOMAIN + "/mobile/?mode=set_user_passwd&uid=" + values.user_id, params, String.class, new AjaxCallback<String>() {
                    @Override
                    public void callback(String url, String html, AjaxStatus status) {
                        if (status.getCode() == 200) {
                            Toast.makeText(con, "비밀번호 재설정 완료", Toast.LENGTH_LONG).show();
                            values.union_info = null;
                            values.unions = null;
                            values.user_info = null;
                            values.user_id = 0;
                            Functions.remove_pref(con);
                            con.startActivity(new Intent(getActivity(), LoadingActivity.class));
                            getActivity().finish();
                        } else {
                            Toast.makeText(con, "비밀번호 재설정 실패\n" +
                                    "현재 비밀번호 및 새 비밀번호를 다시 확인해주세요.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        return v;
    }

}