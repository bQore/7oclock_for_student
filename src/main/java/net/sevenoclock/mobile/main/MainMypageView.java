package net.sevenoclock.mobile.main;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.customobj.FontTextView;
import net.sevenoclock.mobile.customobj.TryCatchJO;
import net.sevenoclock.mobile.inventory.InventoryQuestionView;
import net.sevenoclock.mobile.question.QuestionFragmentView;
import net.sevenoclock.mobile.settings.Functions;
import net.sevenoclock.mobile.settings.Values;
import org.json.JSONArray;
import org.json.JSONException;

public class MainMypageView extends LinearLayout {

    private Context con;

    private FrameLayout fl_main_mypage_upload;
    private ImageView iv_main_mypage_profile;
    private FontTextView tv_main_mypage_username;

    Values values;

    public MainMypageView(Context context) {
        super(context);
        this.con = context;

        values = (Values) context.getApplicationContext();

        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        addView(inflater.inflate(R.layout.view_main_mypage, null), lp);

        fl_main_mypage_upload = (FrameLayout) findViewById(R.id.fl_main_mypage_upload);
        iv_main_mypage_profile = (ImageView) findViewById(R.id.iv_main_mypage_profile);
        tv_main_mypage_username = (FontTextView) findViewById(R.id.tv_main_mypage_username);

        fl_main_mypage_upload.setOnClickListener((OnClickListener)con);

        values.aq.id(iv_main_mypage_profile).image(Functions.borderRadius(Functions.DOMAIN + values.user_info.get("src", ""), 1000));
        values.aq.id(tv_main_mypage_username).text(values.user_info.get("first_name", "다시 로그인해주세요."));

        setTag(R.string.tag_main_title, "마이페이지");
        setTag(R.string.tag_main_subtitle, values.user_info.get("first_name","")+"님의 마이페이지 입니다.");
    }

}