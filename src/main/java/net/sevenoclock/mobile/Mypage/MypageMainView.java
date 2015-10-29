package net.sevenoclock.mobile.Mypage;

import android.content.Context;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.customobj.FontTextView;
import net.sevenoclock.mobile.settings.Functions;
import net.sevenoclock.mobile.settings.Values;

public class MypageMainView extends LinearLayout {

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

    public MypageMainView(Context context) {
        super(context);
        this.con = context;

        values = (Values) context.getApplicationContext();

        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        addView(inflater.inflate(R.layout.view_mypage_main, null), lp);

        fl_mypage_main_upload = (FrameLayout) findViewById(R.id.fl_mypage_main_upload);
        iv_mypage_main_profile = (ImageView) findViewById(R.id.iv_mypage_main_profile);

        tv_mypage_main_name = (FontTextView) findViewById(R.id.tv_mypage_main_name);
        tv_mypage_main_group = (FontTextView) findViewById(R.id.tv_mypage_main_group);
        tv_mypage_main_email = (FontTextView) findViewById(R.id.tv_mypage_main_email);
        tv_mypage_main_phone = (FontTextView) findViewById(R.id.tv_mypage_main_phone);
        tv_mypage_main_gender = (FontTextView) findViewById(R.id.tv_mypage_main_gender);

        ll_mypage_main_group = (LinearLayout) findViewById(R.id.ll_mypage_main_group);
        ll_mypage_main_pw = (LinearLayout) findViewById(R.id.ll_mypage_main_pw);
        ll_mypage_main_info = (LinearLayout) findViewById(R.id.ll_mypage_main_info);

        fl_mypage_main_upload.setOnClickListener((OnClickListener) con);

        values.aq.id(iv_mypage_main_profile).image(Functions.borderRadius(Functions.DOMAIN + values.user_info.get("src", ""), 1000));
        values.aq.id(tv_mypage_main_name).text("이   름 : "+values.user_info.get("first_name", "-"));
        values.aq.id(tv_mypage_main_email).text("이메일 : "+values.user_info.get("email", "-"));
        values.aq.id(tv_mypage_main_phone).text("연락처 : "+values.user_info.get("phone", "-"));
        values.aq.id(tv_mypage_main_gender).text("성   별 : "+values.user_info.get("gender", "-"));
        values.aq.id(tv_mypage_main_group).text("소   속 : "+String.format("%s %s학년 %s반"
                , values.user_info.get("school_name", "-")
                , values.user_info.get("school_year", "")
                , values.user_info.get("school_room", "")));

        setTag(R.string.tag_main_title, "마이페이지");
        setTag(R.string.tag_main_subtitle, values.user_info.get("first_name","")+"님의 마이페이지 입니다.");

        ll_mypage_main_group.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ll_mypage_main_pw.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Functions.history_go(con, new MypageChangePWView(con));
            }
        });

        ll_mypage_main_info.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Functions.history_go(con, new MypageChangeInfoView(con));
            }
        });
    }

}