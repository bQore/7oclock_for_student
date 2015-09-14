package net.sevenoclock.mobile.main;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.customobj.FontTextView;
import net.sevenoclock.mobile.customobj.TryCatchJO;
import net.sevenoclock.mobile.question.QuestionFragmentView;
import net.sevenoclock.mobile.settings.Functions;
import net.sevenoclock.mobile.settings.Values;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainSearchBoxListView extends LinearLayout {

    private Context con;

    private JSONArray jo_questions;

    private FontTextView tv_main_search_box_list_title;
    private LinearLayout ll_main_search_box_list_list_left;
    private LinearLayout ll_main_search_box_list_list_right;

    Values values;

    public MainSearchBoxListView(Context context, JSONObject jo_list) throws JSONException {
        super(context);
        this.con = context;
        values = (Values) context.getApplicationContext();

        inflate(getContext(), R.layout.view_main_search_box_list, this);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        tv_main_search_box_list_title = (FontTextView)findViewById(R.id.tv_main_search_box_list_title);
        ll_main_search_box_list_list_left = (LinearLayout)findViewById(R.id.ll_main_search_box_list_list_left);
        ll_main_search_box_list_list_right = (LinearLayout)findViewById(R.id.ll_main_search_box_list_list_right);

        tv_main_search_box_list_title.setText(jo_list.getString("title"));

        jo_questions = jo_list.getJSONArray("quetions");
        int len = jo_questions.length();

        for(int i=0; i<len; i++){
            TryCatchJO tcjo = new TryCatchJO(jo_questions.getJSONObject(i));
            MainSearchBoxListQuestionView msblqv = new MainSearchBoxListQuestionView(con,tcjo);
            msblqv.setTag(R.string.tag_QuestionFragmentView_id, tcjo.get("id", "0"));
            msblqv.setTag(R.string.tag_QuestionFragmentView_title, tcjo.get("unit", "-"));
            msblqv.setTag(R.string.tag_QuestionFragmentView_src, tcjo.get("src", ""));
            msblqv.setTag(R.string.tag_QuestionFragmentView_explain, tcjo.get("explain", ""));
            msblqv.setTag(R.string.tag_QuestionFragmentView_video, tcjo.get("video", ""));
            msblqv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.imm.hideSoftInputFromWindow(MainActivity.asv.et_main_actionbar_search_form.getWindowToken(), 0);
                    MainActivity.actionBar.setCustomView(MainActivity.adv, MainActivity.actionbar_lp);
                    Functions.history_go(con, new QuestionFragmentView(con
                            , v.getTag(R.string.tag_QuestionFragmentView_id).toString()
                            , v.getTag(R.string.tag_QuestionFragmentView_title).toString()
                            , v.getTag(R.string.tag_QuestionFragmentView_src).toString()
                            , v.getTag(R.string.tag_QuestionFragmentView_explain).toString()
                            , v.getTag(R.string.tag_QuestionFragmentView_video).toString()));
                }
            });
            if(i == 0) ll_main_search_box_list_list_left.addView(msblqv);
            else if(i == 1) ll_main_search_box_list_list_right.addView(msblqv);
        }

    }

}