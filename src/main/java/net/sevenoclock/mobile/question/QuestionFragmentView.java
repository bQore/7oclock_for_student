package net.sevenoclock.mobile.question;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
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
import org.json.JSONException;

public class QuestionFragmentView extends LinearLayout {

    private Context con;
    TryCatchJO tcjo;
    private String qid;
    private String feedback_state = "";
    private String save_state = "";

    private ViewPager pager;
    private TabPageIndicator indicator;
    private LinearLayout ll_question_fragment_up, ll_question_fragment_down, ll_question_fragment_save;
    private IconTextView itv_question_fragment_up, itv_question_fragment_down,itv_question_fragment_save;
    private FontTextView ftv_question_fragment_up, ftv_question_fragment_down,ftv_question_fragment_save;

    private static final String[] CONTENT = new String[] { "문제", "해설", "동영상" };

    Values values;

    public QuestionFragmentView(Context context, String qid, String title, String src, String explain, String video) {
        super(context);
        this.con = context;
        this.qid = qid;
        this.tcjo = null;

        values = (Values) context.getApplicationContext();

        inflate(getContext(), R.layout.view_question_fragment, this);
        setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        pager = (ViewPager) findViewById(R.id.vp_question_fragment_viewpaper);
        indicator = (TabPageIndicator) findViewById(R.id.tpi_question_fragment_indicator);

        setTag(R.string.tag_main_title,"");
        setTag(R.string.tag_main_subtitle, title);

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
                if(save_state == "add") Toast.makeText(con, "저장되었습니다.", Toast.LENGTH_SHORT).show();
                else Toast.makeText(con, "저장 취소되었습니다.", Toast.LENGTH_SHORT).show();
                MainActivity.view_inventory_list.reflesh();
                setSaveBtn();
            }
        });

        pager.setOffscreenPageLimit(10);

        QuestionFragmentAdapter adapter = new QuestionFragmentAdapter(((FragmentActivity) MainActivity.activity).getSupportFragmentManager(), src, explain, video);
        adapter.notifyDataSetChanged();
        pager.setAdapter(adapter);
        indicator.setViewPager(pager);

        setFeedbackBtn();
        setSaveBtn();

    }

    private void setSaveBtn(){
        TryCatchJO tcjo_save = null;
        try {
            tcjo_save = new TryCatchJO(Functions.GET("set_invenroty&uid=" + values.user_id + "&qid=" + qid + "&method=" + save_state).getJSONObject(0));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (tcjo_save.get("question",0) == 0){
            setFeedbackFont(itv_question_fragment_save, ftv_question_fragment_save, false);
            save_state = "add";
            return;
        }
        setFeedbackFont(itv_question_fragment_save, ftv_question_fragment_save, true);
        save_state = "delete";
        return;
    }

    private void setFeedbackBtn(){
        try {
            if(tcjo == null) tcjo = new TryCatchJO(Functions.GET("get_question_feedback&uid=" + values.user_id + "&qid=" + qid).getJSONObject(0));
            else tcjo = new TryCatchJO(Functions.GET("set_question_feedback&uid=" + values.user_id + "&qid=" + qid + "&is_good=" + feedback_state).getJSONObject(0));

            feedback_state = tcjo.get("is_good","");

            if(feedback_state.equals("")){
                setFeedbackFont(itv_question_fragment_up, ftv_question_fragment_up, false);
                setFeedbackFont(itv_question_fragment_down, ftv_question_fragment_down, false);
                feedback_state = "-1";
            }else if(feedback_state.equals("false")){
                setFeedbackFont(itv_question_fragment_up, ftv_question_fragment_up, false);
                setFeedbackFont(itv_question_fragment_down, ftv_question_fragment_down, true);
                feedback_state = "0";
            }else if(feedback_state.equals("true")){
                setFeedbackFont(itv_question_fragment_up, ftv_question_fragment_up, true);
                setFeedbackFont(itv_question_fragment_down, ftv_question_fragment_down, false);
                feedback_state = "1";
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setFeedbackFont(IconTextView itv, FontTextView ftv, Boolean selected){
        if(selected){
            itv.setTextColor(Color.parseColor("#e74c3c"));
            ftv.setTextColor(Color.parseColor("#e74c3c"));
            itv.setTypeface(itv.getTypeface(), Typeface.BOLD);
            ftv.setTypeface(ftv.getTypeface(), Typeface.BOLD);
        }else{
            itv.setTextColor(Color.parseColor("#666666"));
            ftv.setTextColor(Color.parseColor("#666666"));
            itv.setTypeface(itv.getTypeface(), Typeface.NORMAL);
            ftv.setTypeface(ftv.getTypeface(), Typeface.NORMAL);
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
