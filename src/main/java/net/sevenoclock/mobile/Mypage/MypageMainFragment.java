package net.sevenoclock.mobile.mypage;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.customobj.FontTextView;
import net.sevenoclock.mobile.customobj.TryCatchJO;
import net.sevenoclock.mobile.main.MainActivity;
import net.sevenoclock.mobile.settings.Functions;
import net.sevenoclock.mobile.settings.UserData;
import net.sevenoclock.mobile.settings.Values;
import net.sevenoclock.mobile.testpaper.TestpaperRankAdapter;
import org.json.JSONException;
import org.json.JSONObject;

public class MypageMainFragment extends Fragment {

    private Context con;

    private FrameLayout fl_mypage_main_upload;
    private ImageView iv_mypage_main_profile;

    private FontTextView tv_mypage_main_name;
    private FontTextView tv_mypage_main_group;
    private FontTextView tv_mypage_main_email;
    private FontTextView tv_mypage_main_phone;
    private FontTextView tv_mypage_main_gender;

    private LinearLayout ll_mypage_main_group;
    private LinearLayout ll_mypage_main_pw;
    private LinearLayout ll_mypage_main_info;

    Values values;

    public static MypageMainFragment newInstance() {
        MypageMainFragment view = new MypageMainFragment();
        return view;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        con = container.getContext();
        values = (Values) con.getApplicationContext();

        View v = inflater.inflate(R.layout.view_mypage_main, container, false);

        MainActivity.setTitle("마이페이지");
        MainActivity.setSubtitle(values.user_info.get("first_name","")+"님의 마이페이지 입니다.");

        fl_mypage_main_upload = (FrameLayout) v.findViewById(R.id.fl_mypage_main_upload);
        iv_mypage_main_profile = (ImageView) v.findViewById(R.id.iv_mypage_main_profile);

        tv_mypage_main_name = (FontTextView) v.findViewById(R.id.tv_mypage_main_name);
        tv_mypage_main_group = (FontTextView) v.findViewById(R.id.tv_mypage_main_group);
        tv_mypage_main_email = (FontTextView) v.findViewById(R.id.tv_mypage_main_email);
        tv_mypage_main_phone = (FontTextView) v.findViewById(R.id.tv_mypage_main_phone);
        tv_mypage_main_gender = (FontTextView) v.findViewById(R.id.tv_mypage_main_gender);

        ll_mypage_main_group = (LinearLayout) v.findViewById(R.id.ll_mypage_main_group_setting);
        ll_mypage_main_pw = (LinearLayout) v.findViewById(R.id.ll_mypage_main_pw);
        ll_mypage_main_info = (LinearLayout) v.findViewById(R.id.ll_mypage_main_info);

        fl_mypage_main_upload.setOnClickListener((View.OnClickListener) con);

        values.aq.id(iv_mypage_main_profile).image(Functions.borderRadius(Functions.DOMAIN + values.user_info.get("src", ""), 1000));
        values.aq.id(tv_mypage_main_name).text("이   름 : "+values.user_info.get("first_name", "-"));
        values.aq.id(tv_mypage_main_email).text("이메일 : "+values.user_info.get("email", "-"));
        values.aq.id(tv_mypage_main_phone).text("연락처 : "+values.user_info.get("phone", "-"));
        values.aq.id(tv_mypage_main_gender).text("성   별 : " + values.user_info.get("gender", "-"));
        values.aq.id(tv_mypage_main_group).text("소   속 : "+values.union_info.get("title","-"));

        ll_mypage_main_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(con, "구현 중입니다!", Toast.LENGTH_SHORT).show();
                Functions.history_go(con, new MypageGroupFragment().newInstance());
            }
        });

        ll_mypage_main_pw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Functions.history_go(con, new MypageChangePWFragment().newInstance());
            }
        });

        ll_mypage_main_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Functions.history_go(con, new MypageChangeInfoFragment().newInstance());
            }
        });

        return v;
    }

}