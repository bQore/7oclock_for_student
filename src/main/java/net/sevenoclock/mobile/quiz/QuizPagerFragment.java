package net.sevenoclock.mobile.quiz;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import com.viewpagerindicator.TabPageIndicator;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.customobj.TryCatchJO;
import net.sevenoclock.mobile.main.MainActivity;
import net.sevenoclock.mobile.settings.Values;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class QuizPagerFragment extends Fragment {

    private Context con;
    TryCatchJO tcjo;
    JSONArray ja_questions;

    private ViewPager pager;
    private TabPageIndicator indicator;
    public static LinearLayout ll_quiz_text_input_quick_btns;
    public LinearLayout ll_quiz_text_input_quick_btns_inner;

    public QuizTextFragment qtf[];
    private QuizFinalFragment qff;

    public static EditText et_focused = null;


    Values values;

    public static QuizPagerFragment newInstance(TryCatchJO tcjo, JSONArray ja_questions) {
        QuizPagerFragment view = new QuizPagerFragment();
        Bundle args = new Bundle();
        args.putString("object", tcjo.toString());
        args.putString("array", ja_questions.toString());
        view.setArguments(args);
        return view;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        con = container.getContext();
        values = (Values) con.getApplicationContext();

        View v = inflater.inflate(R.layout.fragment_quiz_pager, container, false);

        try {
            ja_questions = new JSONArray(getArguments().getString("array"));
            tcjo = new TryCatchJO(new JSONObject(getArguments().getString("object")));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        MainActivity.setTitle("답안 입력");
        MainActivity.setSubtitle(tcjo.get("title",""));

        pager = (ViewPager) v.findViewById(R.id.vp_quiz_fragment_viewpaper);
        indicator = (TabPageIndicator) v.findViewById(R.id.tpi_quiz_fragment_indicator);
        ll_quiz_text_input_quick_btns = (LinearLayout) v.findViewById(R.id.ll_quiz_text_input_quick_btns);
        ll_quiz_text_input_quick_btns_inner = (LinearLayout) v.findViewById(R.id.ll_quiz_text_input_quick_btns_inner);

        pager.setOffscreenPageLimit(ja_questions.length()+1);

        QuizFragmentAdapter adapter = new QuizFragmentAdapter(((FragmentActivity) getActivity()).getSupportFragmentManager(), ja_questions);
        adapter.notifyDataSetChanged();
        pager.setAdapter(adapter);
        indicator.setViewPager(pager);

        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                indicator.requestFocus();
                pager.requestFocus();
            }

            @Override
            public void onPageSelected(int position) {
                if (position == ja_questions.length()) {
                    MainActivity.ll_main_main_loading.setVisibility(View.VISIBLE);
                }
                // 소프트키에 대한 권한을 얻기 위해 매니저 클래스 선언
                InputMethodManager input_manager = (InputMethodManager)con.getSystemService(Context.INPUT_METHOD_SERVICE);
                // 현재 포커스인 EditText에 대한 정보를 얻어오기 위해 사용
                IBinder now_focus;
                if(((Activity)con).getCurrentFocus() != null){
                    now_focus = ((Activity)con).getCurrentFocus().getApplicationWindowToken();
                    if(now_focus!=null)input_manager.hideSoftInputFromWindow(now_focus, 0);
                }
                // 키보드 감춤
                ll_quiz_text_input_quick_btns.setVisibility(View.GONE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    int position = pager.getCurrentItem();
                    if (position == ja_questions.length()) {
                        qff.qfla.reflesh();
                        for (int i = 0; i < ja_questions.length(); i++) {
                            if (qtf[i].answer.equals(""))
                                qff.qfla.add(new String[]{"" + (i + 1), "-", "" + qtf[i].qid});
                            else qff.qfla.add(new String[]{"" + (i + 1), qtf[i].answer, "" + qtf[i].qid});
                        }
                        qff.qfla.notifyDataSetChanged();
                        MainActivity.ll_main_main_loading.setVisibility(View.GONE);
                    }
                }
                indicator.requestFocus();
                pager.requestFocus();
            }
        });

        ll_quiz_text_input_quick_btns_inner.addView(NewBtn("√"));
        ll_quiz_text_input_quick_btns_inner.addView(NewBtn("π"));
        ll_quiz_text_input_quick_btns_inner.addView(NewBtn("/"));
        ll_quiz_text_input_quick_btns_inner.addView(NewBtn("."));
        ll_quiz_text_input_quick_btns_inner.addView(NewBtn("+"));
        ll_quiz_text_input_quick_btns_inner.addView(NewBtn("-"));

        return v;
    }

    private Button NewBtn(String str){
        Button btn = new Button(con);
        btn.setText(str);
        btn.setBackgroundResource(R.drawable.btn_quiz_text_input_btns);
        LinearLayout.LayoutParams layout_113 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layout_113.rightMargin = 5;
        btn.setLayoutParams(layout_113);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_focused != null) et_focused.append(((Button) v).getText());
            }
        });
        return btn;
    }

    class QuizFragmentAdapter extends FragmentStatePagerAdapter {

        JSONArray ja;
        int ja_length = 0;

        public QuizFragmentAdapter(FragmentManager fm, JSONArray ja) {
            super(fm);
            this.ja = ja;
            this.ja_length = ja.length();

            qtf = new QuizTextFragment[ja_length];
            for(int i=0; i<ja_length; i++){
                try {
                    TryCatchJO tcjo_tmp = new TryCatchJO(ja.getJSONObject(i));
                    Log.i("@@info","tmp:"+tcjo_tmp);
                    qtf[i] = new QuizTextFragment().newInstance(i+1, tcjo_tmp);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            qff = new QuizFinalFragment().newInstance(tcjo.get("id",0),tcjo.toString());

        }

        @Override
        public Fragment getItem(int position) {
            if(position == ja_length) return qff;
            else return qtf[position];
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == ja_length) return "답안지";
            return String.format("%d",position+1);
        }

        @Override
        public int getCount() {
            return ja_length+1;
        }
    }

}
