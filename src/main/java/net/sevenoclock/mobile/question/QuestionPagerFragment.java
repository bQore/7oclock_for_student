package net.sevenoclock.mobile.question;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
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
import net.sevenoclock.mobile.quiz.QuizPagerFragment;
import net.sevenoclock.mobile.settings.Functions;
import net.sevenoclock.mobile.settings.UserData;
import net.sevenoclock.mobile.settings.Values;
import net.sevenoclock.mobile.testpaper.TestpaperListAdapter;
import net.sevenoclock.mobile.testpaper.TestpaperListFragment;
import net.sevenoclock.mobile.testpaper.TestpaperQuestionListFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class QuestionPagerFragment extends Fragment {

    private Context con;
    private String feedback_state = "";
    TryCatchJO tcjo;
    TryCatchJO tcjo_feedback = null;
    TryCatchJO tcjo_save = null;

    private ViewPager pager;
    private TabPageIndicator indicator;
    private LinearLayout ll_question_fragment_up, ll_question_fragment_down, ll_question_fragment_save, ll_question_fragment_same, ll_question_fragment_error;
    private IconTextView itv_question_fragment_up, itv_question_fragment_down,itv_question_fragment_save, itv_question_fragment_same, itv_question_fragment_error;
    private FontTextView ftv_question_fragment_up, ftv_question_fragment_down,ftv_question_fragment_save, ftv_question_fragment_same, ftv_question_fragment_error;

    private static final String[] CONTENT = new String[] { "문제", "해설", "동영상" };

    Values values;

    public static QuestionPagerFragment newInstance(TryCatchJO tcjo) {
        QuestionPagerFragment view = new QuestionPagerFragment();
        Bundle args = new Bundle();
        args.putString("object", tcjo.toString());
        view.setArguments(args);
        return view;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        con = container.getContext();
        values = (Values) con.getApplicationContext();

        View v = inflater.inflate(R.layout.fragment_question_pager, container, false);

        try {
            tcjo = new TryCatchJO(new JSONObject(getArguments().getString("object")));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        MainActivity.setTitle("");
        MainActivity.setSubtitle(tcjo.get("unit_title", ""));

        pager = (ViewPager) v.findViewById(R.id.vp_question_fragment_viewpaper);
        indicator = (TabPageIndicator) v.findViewById(R.id.tpi_question_fragment_indicator);

        ll_question_fragment_up = (LinearLayout) v.findViewById(R.id.ll_question_fragment_up);
        ll_question_fragment_down = (LinearLayout) v.findViewById(R.id.ll_question_fragment_down);
        ll_question_fragment_save = (LinearLayout) v.findViewById(R.id.ll_question_fragment_save);

        itv_question_fragment_up = (IconTextView) v.findViewById(R.id.itv_question_fragment_up);
        itv_question_fragment_down = (IconTextView) v.findViewById(R.id.itv_question_fragment_down);
        itv_question_fragment_save = (IconTextView) v.findViewById(R.id.itv_question_fragment_save);

        ftv_question_fragment_up = (FontTextView) v.findViewById(R.id.ftv_question_fragment_up);
        ftv_question_fragment_down = (FontTextView) v.findViewById(R.id.ftv_question_fragment_down);
        ftv_question_fragment_save = (FontTextView) v.findViewById(R.id.ftv_question_fragment_save);

        ll_question_fragment_same = (LinearLayout) v.findViewById(R.id.ll_question_fragment_same);
        ll_question_fragment_error = (LinearLayout) v.findViewById(R.id.ll_question_fragment_error);

        itv_question_fragment_same = (IconTextView) v.findViewById(R.id.itv_question_fragment_same);
        itv_question_fragment_error = (IconTextView) v.findViewById(R.id.itv_question_fragment_error);

        ftv_question_fragment_same = (FontTextView) v.findViewById(R.id.ftv_question_fragment_same);
        ftv_question_fragment_error = (FontTextView) v.findViewById(R.id.ftv_question_fragment_error);

        ll_question_fragment_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Vibrator Vibe = (Vibrator)con.getSystemService(con.VIBRATOR_SERVICE);
                Vibe.vibrate(30);
                if(feedback_state.equals("-1")) feedback_state = "1";
                else if(feedback_state.equals("0")) feedback_state = "1";
                else if(feedback_state.equals("1")) feedback_state = "-1";
                setFeedbackBtn();
            }
        });

        ll_question_fragment_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Vibrator Vibe = (Vibrator)con.getSystemService(con.VIBRATOR_SERVICE);
                Vibe.vibrate(30);
                if(feedback_state.equals("-1")) feedback_state = "0";
                else if(feedback_state.equals("0")) feedback_state = "-1";
                else if(feedback_state.equals("1")) feedback_state = "0";
                setFeedbackBtn();
            }
        });

        ll_question_fragment_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Vibrator Vibe = (Vibrator) con.getSystemService(con.VIBRATOR_SERVICE);
                Vibe.vibrate(30);
                try {
                    tcjo_save = new TryCatchJO(Functions.GET("set_invenroty&uid=" + values.user_id + "&qid=" + tcjo.get("id",0)).getJSONObject(0));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                setSaveBtn(true);
            }
        });

        ll_question_fragment_same.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    JSONArray array = Functions.GET("get_testpaper_similar&qid=" + tcjo.get("id",0));
                    TryCatchJO question = new TryCatchJO(array.getJSONObject(0));
                    Functions.history_back_delete(con);
                    Functions.history_go(con, new QuizPagerFragment().newInstance(question,array));
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });

        ll_question_fragment_error.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Vibrator Vibe = (Vibrator) getActivity().getSystemService(getActivity().VIBRATOR_SERVICE);
                Vibe.vibrate(30);
                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{"eaeao@naver.com", "storm0812@hanmail.net", "tellme0218@naver.com"});
                email.putExtra(Intent.EXTRA_SUBJECT, "[모두를위한수학]오류신고합니다!");
                email.putExtra(Intent.EXTRA_TEXT, "제목 : " + getArguments().getInt("qid") + "번 문제 오류신고합니다!\n\n내용 : ");
                email.setType("text/plain");
                final PackageManager pm = getActivity().getPackageManager();
                final List<ResolveInfo> matches = pm.queryIntentActivities(email, 0);
                ResolveInfo best = null;
                for (final ResolveInfo info : matches)
                    if (info.activityInfo.packageName.endsWith(".gm") || info.activityInfo.name.toLowerCase().contains("gmail"))
                        best = info;
                if (best != null)
                    email.setClassName(best.activityInfo.packageName, best.activityInfo.name);
                getActivity().startActivity(email);
            }
        });

        pager.setOffscreenPageLimit(10);

        QuestionFragmentAdapter adapter = new QuestionFragmentAdapter(((FragmentActivity) getActivity()).getSupportFragmentManager(), tcjo);
        adapter.notifyDataSetChanged();
        pager.setAdapter(adapter);
        indicator.setViewPager(pager);

        setFeedbackBtn();
        try {
            tcjo_save = new TryCatchJO(Functions.GET("get_invenroty&uid=" + values.user_id + "&qid=" + tcjo.get("id",0)).getJSONObject(0));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setSaveBtn(false);

        return v;
    }

    private void setSaveBtn(boolean is_clicked){
        if (tcjo_save.get("question",0) == 0){
            if(is_clicked) Toast.makeText(con, "저장 취소되었습니다.", Toast.LENGTH_LONG).show();
            setFeedbackFont(itv_question_fragment_save, ftv_question_fragment_save, false);
            return;
        }
        if(is_clicked) Toast.makeText(con, "저장되었습니다.", Toast.LENGTH_LONG).show();
        setFeedbackFont(itv_question_fragment_save, ftv_question_fragment_save, true);
        return;
    }

    private void setFeedbackBtn(){
        try {
            if(tcjo_feedback == null) tcjo_feedback = new TryCatchJO(Functions.GET("get_question_feedback&uid=" + values.user_id + "&qid=" + tcjo.get("id",0)).getJSONObject(0));
            else tcjo_feedback = new TryCatchJO(Functions.GET("set_question_feedback&uid=" + values.user_id + "&qid=" + tcjo.get("id",0) + "&is_good=" + feedback_state).getJSONObject(0));

            feedback_state = tcjo_feedback.get("is_good","");

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

        private QuestionTextFragment qdv;
        private QuestionExplainFragment qev;
        private QuestionVideoFragment qvv;

        TryCatchJO tcjo;

        public QuestionFragmentAdapter(FragmentManager fm, TryCatchJO jo) {
            super(fm);
            tcjo = jo;

            qdv = new QuestionTextFragment().newInstance(tcjo.get("id",0), tcjo.get("src",""));
            qev = new QuestionExplainFragment().newInstance(tcjo.get("id",0), tcjo.get("explain",""));
            qvv = new QuestionVideoFragment().newInstance(tcjo.get("id",0), tcjo.get("video",""));
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
