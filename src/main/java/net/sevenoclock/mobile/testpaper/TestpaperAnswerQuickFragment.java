package net.sevenoclock.mobile.testpaper;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.customobj.TryCatchJO;
import net.sevenoclock.mobile.main.MainActivity;
import net.sevenoclock.mobile.question.QuestionPagerFragment;
import net.sevenoclock.mobile.settings.Functions;
import net.sevenoclock.mobile.settings.Values;
import org.json.JSONArray;
import org.json.JSONException;

public class TestpaperAnswerQuickFragment extends Fragment {

    private Context con;
    private JSONArray ja_question;

    public static LinearLayout ll_testpaper_input_quick_btns;
    private LinearLayout ll_testpaper_input_quick_forms;

    TestpaperAnswerResultQuickView[] tarqv;

    Values values;

    public static TestpaperAnswerQuickFragment newInstance(JSONArray ja) {
        TestpaperAnswerQuickFragment view = new TestpaperAnswerQuickFragment();
        Bundle args = new Bundle();
        args.putString("array", ja.toString());
        view.setArguments(args);
        return view;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        con = container.getContext();
        values = (Values) con.getApplicationContext();
        try {
            ja_question = new JSONArray(getArguments().getString("array"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        View v = inflater.inflate(R.layout.fragment_testpaper_input_quick, container, false);

        MainActivity.setTitle("빠른 정답 보기");
        MainActivity.setSubtitle("");

        ll_testpaper_input_quick_btns = (LinearLayout) v.findViewById(R.id.ll_testpaper_input_quick_btns);
        //ll_testpaper_input_quick_forms = (LinearLayout) v.findViewById(R.id.ll_testpaper_input_quick_forms);

        MainActivity.ll_main_main_loading.setVisibility(View.VISIBLE);
        Handler handler = new Handler();
        Runnable r = new Runnable() {
            public void run() {
                tarqv = new TestpaperAnswerResultQuickView[ja_question.length()];
                for (int i = 0; i < ja_question.length(); i++) {
                    try {
                        TryCatchJO tcjo_question = new TryCatchJO(ja_question.getJSONObject(i));
                        tarqv[i] = new TestpaperAnswerResultQuickView(con, i, tcjo_question);
                        tarqv[i].setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Functions.history_go(con, new QuestionPagerFragment().newInstance(((TestpaperAnswerResultQuickView) v).tcjo_info));
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

        return v;
    }
}