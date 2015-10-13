package net.sevenoclock.mobile.testpaper;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.customobj.TryCatchJO;
import net.sevenoclock.mobile.main.MainActivity;
import net.sevenoclock.mobile.question.QuestionFragmentView;
import net.sevenoclock.mobile.settings.Functions;
import net.sevenoclock.mobile.settings.Values;
import org.json.JSONArray;

public class TestpaperAnswerQuickView extends LinearLayout {

    private Context con;
    private JSONArray ja_question;

    public static LinearLayout ll_testpaper_input_quick_btns;
    private LinearLayout ll_testpaper_input_quick_forms;

    TestpaperAnswerResultQuickView[] tarqv;

    Values values;

    public TestpaperAnswerQuickView(Context context, final JSONArray ja_question) {
        super(context);
        this.con = context;
        this.ja_question = ja_question;

        values = (Values) context.getApplicationContext();

        inflate(getContext(), R.layout.view_testpaper_input_quick, this);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        ll_testpaper_input_quick_btns = (LinearLayout)findViewById(R.id.ll_testpaper_input_quick_btns);
        ll_testpaper_input_quick_forms = (LinearLayout)findViewById(R.id.ll_testpaper_input_quick_forms);

        setTag(R.string.tag_main_title, "빠른 정답 보기");
        setTag(R.string.tag_main_subtitle, "");

        MainActivity.ll_main_main_loading.setVisibility(View.VISIBLE);
        Handler handler = new Handler();
        Runnable r = new Runnable() {
            public void run() {
                tarqv = new TestpaperAnswerResultQuickView[ja_question.length()];
                for (int i = 0; i < ja_question.length(); i++) {
                    try {
                        TryCatchJO tcjo_question = new TryCatchJO(ja_question.getJSONObject(i));
                        tarqv[i] = new TestpaperAnswerResultQuickView(con, i, tcjo_question);
                        tarqv[i].setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Functions.history_go(con, new QuestionFragmentView(con, ((TestpaperAnswerResultQuickView) v).tcjo_info));
                            }
                        });
                        ll_testpaper_input_quick_forms.addView(tarqv[i]);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                MainActivity.ll_main_main_loading.setVisibility(View.GONE);
            }
        };
        handler.postDelayed(r, 1);

    }
}