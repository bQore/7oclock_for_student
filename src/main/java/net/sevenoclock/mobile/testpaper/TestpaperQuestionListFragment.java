package net.sevenoclock.mobile.testpaper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.customobj.FontTextView;
import net.sevenoclock.mobile.customobj.RefreshScrollView;
import net.sevenoclock.mobile.customobj.TryCatchJO;
import net.sevenoclock.mobile.main.MainActivity;
import net.sevenoclock.mobile.question.QuestionPagerFragment;
import net.sevenoclock.mobile.settings.Functions;
import net.sevenoclock.mobile.settings.Values;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TestpaperQuestionListFragment extends Fragment {

    private Context con;
    private TryCatchJO tcjo;
    private JSONArray ja_question;

    private FontTextView tv_testpaper_question_list_title;
    private FontTextView tv_testpaper_question_list_school;
    private FontTextView tv_testpaper_question_list_teacher;

    private LinearLayout ll_testpaper_question_list_left;
    private LinearLayout ll_testpaper_question_list_right;
    private RefreshScrollView sv_testpaper_question_list_scrollview;

    private LinearLayout ll_testpaper_question_list_quickinput;
    private LinearLayout ll_testpaper_question_list_quickresult;
    private LinearLayout ll_testpaper_question_list_quickrank;
    private LinearLayout ll_testpaper_question_list_quickanswer;

    private int count_left;
    private int count_right;
    private int element_count = 0;
    private boolean semaphore = true;
    private boolean is_solved = false;

    Values values;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        con = container.getContext();
        values = (Values) con.getApplicationContext();
        try {
            tcjo = new TryCatchJO(new JSONObject(getArguments().getString("object")));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        View v = inflater.inflate(R.layout.fragment_testpaper_question_list, container, false);

        tv_testpaper_question_list_title = (FontTextView) v.findViewById(R.id.tv_testpaper_question_list_title);
        tv_testpaper_question_list_school = (FontTextView) v.findViewById(R.id.tv_testpaper_question_list_school);
        tv_testpaper_question_list_teacher = (FontTextView) v.findViewById(R.id.tv_testpaper_question_list_teacher);

        ll_testpaper_question_list_left = (LinearLayout) v.findViewById(R.id.ll_testpaper_question_list_left);
        ll_testpaper_question_list_right = (LinearLayout) v.findViewById(R.id.ll_testpaper_question_list_right);
        sv_testpaper_question_list_scrollview = (RefreshScrollView) v.findViewById(R.id.sv_testpaper_question_list_scrollview);

        ll_testpaper_question_list_quickinput = (LinearLayout) v.findViewById(R.id.ll_testpaper_question_list_quickinput);
        ll_testpaper_question_list_quickresult = (LinearLayout) v.findViewById(R.id.ll_testpaper_question_list_quickresult);
        ll_testpaper_question_list_quickrank = (LinearLayout) v.findViewById(R.id.ll_testpaper_question_list_quickrank);
        ll_testpaper_question_list_quickanswer = (LinearLayout) v.findViewById(R.id.ll_testpaper_question_list_quickanswer);

        tv_testpaper_question_list_title.setText(tcjo.get("title", ""));
        tv_testpaper_question_list_school.setText(tcjo.get("school_name", ""));
        tv_testpaper_question_list_teacher.setText(tcjo.get("user", "") + " 선생님");

        MainActivity.setTitle(tcjo.get("title", ""));
        MainActivity.setSubtitle("총 "+tcjo.get("question_len", 0)+"개 문제가 있습니다.");

        sv_testpaper_question_list_scrollview.setOnScrollViewListener(new RefreshScrollView.OnScrollViewListener() {
            public void onScrollChanged(RefreshScrollView v, int l, int t, int oldl, int oldt) {
                View view = (View) v.getChildAt(v.getChildCount() - 1);
                int diff = view.getBottom() - (v.getHeight() + v.getScrollY());
                if (diff == 0) {
                    int view_count = ll_testpaper_question_list_left.getChildCount() + ll_testpaper_question_list_right.getChildCount();
                    if (view_count >= element_count && semaphore) readMore();
                }
            }
        });

        ll_testpaper_question_list_quickinput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Functions.history_go(con, new TestpaperInputQuickFragment().newInstance(tcjo));
            }
        });

        ll_testpaper_question_list_quickresult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Functions.history_go(con, new TestpaperInputQuickFragment().newInstance(tcjo));
            }
        });

        ll_testpaper_question_list_quickrank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Functions.history_go(con, new TestpaperRankFragment().newInstance(tcjo));
            }
        });

        ll_testpaper_question_list_quickanswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Functions.history_go(con, new TestpaperAnswerQuickFragment().newInstance(ja_question));
            }
        });

        element_count = 0;
        new AddQuestionTask().execute(null, null, null);

        return v;
    }

    public static TestpaperQuestionListFragment newInstance(TryCatchJO tcjo) {
        TestpaperQuestionListFragment view = new TestpaperQuestionListFragment();
        Bundle args = new Bundle();
        args.putString("object", tcjo.toString());
        view.setArguments(args);
        return view;
    }

    public void readMore(){
        semaphore = false;
        MainActivity.ll_main_main_loading.setVisibility(View.VISIBLE);
        MainActivity.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int max = element_count+10;
                for (; element_count < max; element_count++) {
                    if(element_count>=ja_question.length()) break;
                    TestpaperQuestionView tqv = null;
                    try {
                        TryCatchJO tcjo_question = new TryCatchJO(ja_question.getJSONObject(element_count));
                        tqv = new TestpaperQuestionView(con, element_count, tcjo_question);

                        tqv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(final View v) {
                                if (is_solved){
                                    Functions.history_go(con, new QuestionPagerFragment().newInstance(((TestpaperQuestionView) v).tcjo));
                                }else{
                                    new AlertDialog.Builder(con).setTitle("답안 미입력")
                                            .setMessage("답안제출 후 열람가능합니다.\n답안을 입력하시겠습니까?")
                                            .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    return;
                                                }
                                            })
                                            .setPositiveButton("예", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Functions.history_go(con, new TestpaperInputQuickFragment().newInstance(tcjo));
                                                    return;
                                                }
                                            }).show();
                                }
                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    count_left = ll_testpaper_question_list_left.getChildCount();
                    count_right = ll_testpaper_question_list_right.getChildCount();

                    if (count_left > count_right) ll_testpaper_question_list_right.addView(tqv);
                    else ll_testpaper_question_list_left.addView(tqv);
                }

                try {
                    if (ja_question.getJSONObject(0).getInt("purpose") == 0){
                        try {
                            if (ja_question.getJSONObject(0).getInt("is_solved") == 1){
                                is_solved = true;
                                ll_testpaper_question_list_quickresult.setVisibility(View.VISIBLE);
                                ll_testpaper_question_list_quickinput.setVisibility(View.GONE);
                                ll_testpaper_question_list_quickrank.setVisibility(View.VISIBLE);
                                ll_testpaper_question_list_quickanswer.setVisibility(View.GONE);
                            }else{
                                is_solved = false;
                                ll_testpaper_question_list_quickresult.setVisibility(View.GONE);
                                ll_testpaper_question_list_quickinput.setVisibility(View.VISIBLE);
                                ll_testpaper_question_list_quickrank.setVisibility(View.VISIBLE);
                                ll_testpaper_question_list_quickanswer.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else{
                        is_solved = true;
                        ll_testpaper_question_list_quickresult.setVisibility(View.GONE);
                        ll_testpaper_question_list_quickinput.setVisibility(View.GONE);
                        ll_testpaper_question_list_quickrank.setVisibility(View.GONE);
                        ll_testpaper_question_list_quickanswer.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                count_left = ll_testpaper_question_list_left.getChildCount();
                count_right = ll_testpaper_question_list_right.getChildCount();

                semaphore = true;
                MainActivity.ll_main_main_loading.setVisibility(View.GONE);
            }
        });
    }

    class AddQuestionTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            semaphore = false;
            MainActivity.ll_main_main_loading.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        protected Boolean doInBackground(Void... Void) {
            ja_question = Functions.GET("get_testpaper_question_list&uid="+values.user_id+"&tpid=" + tcjo.get("id", ""));
            if(ja_question == null) return false;
            return true;
        }

        protected void onPostExecute(Boolean result) {
            if(result) {
                readMore();
            }else{
                Toast.makeText(con, "데이터 로드에 실패하였습니다.", Toast.LENGTH_LONG).show();
            }
            return;
        }
    }
}