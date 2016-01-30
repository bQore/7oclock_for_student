package net.sevenoclock.mobile.qna;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.androidquery.AQuery;

import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.customobj.TryCatchJO;
import net.sevenoclock.mobile.main.MainActivity;
import net.sevenoclock.mobile.settings.Functions;
import net.sevenoclock.mobile.settings.Values;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class QnAQuestionListFragment extends Fragment{
    private Context con;

    private ListView lv_qna_question_list_list;

    private QnAQuestionListAdapter qla;

    private Values values;
    private JSONArray ja_question;

    int mode = 0;

    // 0 = 필터 적용 안함, 1 = 필터 적용
    public int is_filter = 0;
    // 0 = 모든 문제, 1 = 관심 학교
    int school_mode = 0;
    // 0 = 전체 문제, 1 = 답변이 없는 문제, 2 = 답변이 있는 문제
    int answer_mode = 0;
    // 0 = 전체 학년, 1 = 학년 필터링
    int grade_mode = 0;

    // 질문을 가져올 때 쓸 기준
    int question_limit = 0;

    boolean get_q = false;
    boolean loading = false;
    boolean scroll = false;

    String[] grades;
    int[] schools;

    Bundle filter;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        con = container.getContext();
        values = (Values) con.getApplicationContext();
        values.aq = new AQuery(con);
        View view = inflater.inflate(R.layout.fragment_qna_question_list, container, false);
        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        lv_qna_question_list_list = (ListView)view.findViewById(R.id.lv_qna_question_list_list);

        Bundle tmp = getArguments();
        mode = tmp.getInt("mode");

        qla = new QnAQuestionListAdapter(mode);

        lv_qna_question_list_list.setAdapter(qla);
        lv_qna_question_list_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Vibrator Vibe = (Vibrator) con.getSystemService(con.VIBRATOR_SERVICE);
                Vibe.vibrate(30);
                try {
                    Functions.history_go(con, new QnADetailFragment().newInstance(ja_question.getJSONObject(position)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        lv_qna_question_list_list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(scrollState != AbsListView.OnScrollListener.SCROLL_STATE_FLING && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE){
                    if(loading){
                        new GetQuestionTask().execute(null,null,null);

                        Log.i("@@info", "loading");
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if ((totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount)) {
                    if (!loading) {
                        loading = true;
                        question_limit += 8;

                        Log.i("@@info", "next");
                    }
                }
            }
        });

        new GetQuestionTask().execute(null, null, null);

        return view;
    }

    public void refresh(Bundle options){
        is_filter=1;
        filter = options;

        new GetQuestionTask().execute(null,null,null);

    }

    class GetQuestionTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            get_q = true;
            MainActivity.ll_main_main_loading.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        protected Boolean doInBackground(Void... Void) {
            ja_question = Functions.GET(String.format("get_qna_questions&mode_qna=%d&uid=%d&limit=%d:%d",mode,values.user_id,question_limit,question_limit+8));
            if(ja_question == null) return false;

            if(is_filter==1){
                Log.i("@@info","filter:"+filter);
                school_mode = filter.getInt("school_mode");
                answer_mode = filter.getInt("is_answer");
                grade_mode = filter.getInt("grade_mode");
                if(school_mode==1){
                    schools = filter.getIntArray("schools");
                }
                if(grade_mode!=0){
                    grades = filter.getStringArray("grades");
                }
            }
            for (int i = 0; i < ja_question.length(); i++) {
                try {
                    Log.i("@@filter","is filter:"+is_filter);
                    if(is_filter==0) qla.add(new TryCatchJO(ja_question.getJSONObject(i)));
                    else {
                        Log.i("@@filter", "====  filter is true  ====");
                        JSONObject tmp = ja_question.getJSONObject(i);
                        boolean sm = false, gm = false, am = false;
                        if (school_mode == 1) {
                            Log.i("@@filter", "qna's school_id:" + tmp.getInt("school_id"));
                            for (int j = 0; j < schools.length; j++) {
                                if (tmp.getInt("school_id") == schools[j]) {
                                    Log.i("@@filter", "correct_id:" + schools[j]);
                                    sm = true;
                                    break;
                                }
                            }
                        } else sm = true;
                        if (grade_mode == 1) {
                            Log.i("@@filter", "qna's grade_code:" + tmp.getString("grade_code"));
                            for (int j = 0; j < grades.length; j++) {
                                if (tmp.getString("grade_code").equals(grades[j])) {
                                    Log.i("@@filter", "correct_code:" + tmp.getString("grade_code"));
                                    gm = true;
                                    break;
                                }
                            }
                        } else gm = true;
                        if (answer_mode == 1) {
                            Log.i("@@filter", "is_answer false:" + tmp.getBoolean("is_answer"));
                            if (!tmp.getBoolean("is_answer")) am = true;
                        } else if (answer_mode == 2) {
                            Log.i("@@filter", "is_answer true:" + tmp.getBoolean("is_answer"));
                            if (tmp.getBoolean("is_answer")) am = true;
                        } else am = true;
                        Log.i("@@filter", "==== filter finish ====");
                        if (sm && gm && am) qla.add(new TryCatchJO(tmp));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return true;
        }

        protected void onPostExecute(Boolean result) {
            if(result) {
                try {
                    qla.notifyDataSetChanged();
                    MainActivity.ll_main_main_loading.setVisibility(View.GONE);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }else{
                Toast.makeText(con, "데이터 로드에 실패하였습니다.", Toast.LENGTH_LONG).show();
                MainActivity.ll_main_main_loading.setVisibility(View.GONE);
            }
            get_q = false;
            loading = false;
            return;
        }
    }

    public static QnAQuestionListFragment newInstance(int mode) {
        QnAQuestionListFragment view = new QnAQuestionListFragment();
        Bundle args = new Bundle();
        args.putInt("mode", mode);
        args.putInt("filter",0);
        view.setArguments(args);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}
