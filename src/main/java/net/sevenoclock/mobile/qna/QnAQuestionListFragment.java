package net.sevenoclock.mobile.qna;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class QnAQuestionListFragment extends Fragment {
    private Context con;

    private ListView lv_qna_question_list_list;

    private QnAQuestionListAdapter qla;

    private Values values;
    private JSONArray ja_question;

    int mode = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        con = container.getContext();
        values = (Values) con.getApplicationContext();
        values.aq = new AQuery(con);
        View view = inflater.inflate(R.layout.fragment_qna_question_list, container, false);
        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        lv_qna_question_list_list = (ListView)view.findViewById(R.id.lv_qna_question_list_list);

        mode = getArguments().getInt("mode");

        qla = new QnAQuestionListAdapter();
        lv_qna_question_list_list.setAdapter(qla);
        lv_qna_question_list_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Vibrator Vibe = (Vibrator) con.getSystemService(con.VIBRATOR_SERVICE);
                Vibe.vibrate(30);
                try {
                    Functions.history_go(con,new QnADetailFragment().newInstance(ja_question.getJSONObject(position)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        new GetQuestionTask().execute(null, null, null);

        return view;
    }

    class GetQuestionTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            MainActivity.ll_main_main_loading.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        protected Boolean doInBackground(Void... Void) {
            ja_question = Functions.GET(String.format("get_qna_questions&mode_qna=%d&uid=%d&limit=0:5",mode,values.user_id));
            if(ja_question == null) return false;
            qla.reflesh();
            for (int i = 0; i < ja_question.length(); i++) {
                try {
                    qla.add(new TryCatchJO(ja_question.getJSONObject(i)));
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
            return;
        }
    }

    public static QnAQuestionListFragment newInstance(int mode) {
        QnAQuestionListFragment view = new QnAQuestionListFragment();
        Bundle args = new Bundle();
        args.putInt("mode", mode);
        view.setArguments(args);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}
