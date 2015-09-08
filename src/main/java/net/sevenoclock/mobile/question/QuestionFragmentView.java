package net.sevenoclock.mobile.question;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.support.v4.app.*;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.viewpagerindicator.TabPageIndicator;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.customobj.FontTextView;
import net.sevenoclock.mobile.customobj.IconTextView;
import net.sevenoclock.mobile.customobj.TryCatchJO;
import net.sevenoclock.mobile.main.MainActivity;
import net.sevenoclock.mobile.settings.Functions;
import net.sevenoclock.mobile.settings.Values;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class QuestionFragmentView extends LinearLayout {

    private Context con;
    TryCatchJO tcjo;
    private String qid;
    private String feedback_state = "";

    private ViewPager pager;
    private TabPageIndicator indicator;
    private LinearLayout ll_question_fragment_up, ll_question_fragment_down, ll_question_fragment_save;
    private IconTextView itv_question_fragment_up, itv_question_fragment_down,itv_question_fragment_save;
    private FontTextView ftv_question_fragment_up, ftv_question_fragment_down,ftv_question_fragment_save;

    private static final String[] CONTENT = new String[] { "문제", "해설", "동영상" };

    Values values;

    public QuestionFragmentView(Context context, String qid, String src, String explain, String video) {
        super(context);
        this.qid = qid;
        this.tcjo = null;

        values = (Values) context.getApplicationContext();

        inflate(getContext(), R.layout.view_question_fragment, this);
        setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        pager = (ViewPager) findViewById(R.id.vp_question_detail_viewpaper);
        indicator = (TabPageIndicator) findViewById(R.id.tpi_question_detail_indicator);

        ll_question_fragment_up = (LinearLayout) findViewById(R.id.ll_question_fragment_up);
        ll_question_fragment_down = (LinearLayout) findViewById(R.id.ll_question_fragment_down);
        ll_question_fragment_save = (LinearLayout) findViewById(R.id.ll_question_fragment_save);

        itv_question_fragment_up = (IconTextView) findViewById(R.id.itv_question_fragment_up);
        itv_question_fragment_down = (IconTextView) findViewById(R.id.itv_question_fragment_down);
        itv_question_fragment_save = (IconTextView) findViewById(R.id.itv_question_fragment_save);

        ftv_question_fragment_up = (FontTextView) findViewById(R.id.ftv_question_fragment_up);
        ftv_question_fragment_down = (FontTextView) findViewById(R.id.ftv_question_fragment_down);
        ftv_question_fragment_save = (FontTextView) findViewById(R.id.ftv_question_fragment_save);


        ll_question_fragment_up.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Vibrator Vibe = (Vibrator)getContext().getSystemService(getContext().VIBRATOR_SERVICE);
                Vibe.vibrate(30);
                if(feedback_state.equals("-1")) feedback_state = "1";
                else if(feedback_state.equals("0")) feedback_state = "1";
                else if(feedback_state.equals("1")) feedback_state = "-1";
                setFeedbackBtn();
            }
        });

        ll_question_fragment_down.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Vibrator Vibe = (Vibrator)getContext().getSystemService(getContext().VIBRATOR_SERVICE);
                Vibe.vibrate(30);
                if(feedback_state.equals("-1")) feedback_state = "0";
                else if(feedback_state.equals("0")) feedback_state = "-1";
                else if(feedback_state.equals("1")) feedback_state = "0";
                setFeedbackBtn();
            }
        });

        ll_question_fragment_save.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Vibrator Vibe = (Vibrator)getContext().getSystemService(getContext().VIBRATOR_SERVICE);
                Vibe.vibrate(30);
            }
        });

        pager.setOffscreenPageLimit(10);

        QuestionFragmentAdapter adapter = new QuestionFragmentAdapter(((FragmentActivity) MainActivity.activity).getSupportFragmentManager(), src, explain, video);
        adapter.notifyDataSetChanged();
        pager.setAdapter(adapter);
        indicator.setViewPager(pager);

        setFeedbackBtn();

    }

    private void setFeedbackBtn(){
        try {
            if(tcjo == null) tcjo = new TryCatchJO(Functions.GET("get_question_feedback&uid=" + values.user_id + "&qid=" + qid).getJSONObject(0));
            else tcjo = new TryCatchJO(Functions.GET("set_question_feedback&uid=" + values.user_id + "&qid=" + qid + "&is_good=" + feedback_state).getJSONObject(0));

            feedback_state = tcjo.get("is_good","");

            if(feedback_state.equals("")){
                ll_question_fragment_up.setBackgroundResource(R.drawable.ll_question_fragment_feedback_up_default);
                ll_question_fragment_down.setBackgroundResource(R.drawable.ll_question_fragment_feedback_down_default);
                itv_question_fragment_up.setTextColor(Color.parseColor("#666666"));
                ftv_question_fragment_up.setTextColor(Color.parseColor("#666666"));
                itv_question_fragment_down.setTextColor(Color.parseColor("#666666"));
                ftv_question_fragment_down.setTextColor(Color.parseColor("#666666"));
                feedback_state = "-1";
            }else if(feedback_state.equals("false")){
                ll_question_fragment_up.setBackgroundResource(R.drawable.ll_question_fragment_feedback_up_default);
                ll_question_fragment_down.setBackgroundResource(R.drawable.ll_question_fragment_feedback_down_checked);
                itv_question_fragment_up.setTextColor(Color.parseColor("#666666"));
                ftv_question_fragment_up.setTextColor(Color.parseColor("#666666"));
                itv_question_fragment_down.setTextColor(Color.parseColor("#dd9015"));
                ftv_question_fragment_down.setTextColor(Color.parseColor("#dd9015"));
                feedback_state = "0";
            }else if(feedback_state.equals("true")){
                ll_question_fragment_up.setBackgroundResource(R.drawable.ll_question_fragment_feedback_up_checked);
                ll_question_fragment_down.setBackgroundResource(R.drawable.ll_question_fragment_feedback_down_default);
                itv_question_fragment_up.setTextColor(Color.parseColor("#dd9015"));
                ftv_question_fragment_up.setTextColor(Color.parseColor("#dd9015"));
                itv_question_fragment_down.setTextColor(Color.parseColor("#666666"));
                ftv_question_fragment_down.setTextColor(Color.parseColor("#666666"));
                feedback_state = "1";
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class QuestionFragmentAdapter extends FragmentStatePagerAdapter {

        private QuestionDetailView qdv;
        private QuestionExplainView qev;
        private QuestionVideoView qvv;

        String src, explain, video;

        public QuestionFragmentAdapter(FragmentManager fm, String src, String explain, String video) {
            super(fm);
            this.src = src;
            this.explain = explain;
            this.video = video;

            qdv = new QuestionDetailView().newInstance(src);
            qev = new QuestionExplainView().newInstance(explain);
            qvv = new QuestionVideoView().newInstance(video);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return qdv;
                case 1:
                    return qev;
                case 2:
                    return qvv;
            }
            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return CONTENT[position % CONTENT.length].toUpperCase();
        }

        @Override
        public int getCount() {
            return CONTENT.length;
        }
    }

}
