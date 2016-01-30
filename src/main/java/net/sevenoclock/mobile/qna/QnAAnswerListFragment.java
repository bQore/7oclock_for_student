package net.sevenoclock.mobile.qna;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
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

public class QnAAnswerListFragment extends Fragment {
    private Context con;

    private ListView lv_qna_question_list_list;

    private QnAQuestionListAdapter qla;

    private Values values;
    private JSONArray ja_question;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        con = container.getContext();
        values = (Values) con.getApplicationContext();
        values.aq = new AQuery(con);
        View view = inflater.inflate(R.layout.fragment_qna_question_list, container, false);
        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        lv_qna_question_list_list = (ListView)view.findViewById(R.id.lv_qna_question_list_list);

        qla = new QnAQuestionListAdapter(0);
        lv_qna_question_list_list.setAdapter(qla);
        lv_qna_question_list_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

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
            ja_question = Functions.GET(String.format("get_qna_answers&uid=%d&limit=0:5",values.user_id));
            if(ja_question == null) return false;
            qla.removeAll();
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

    public static QnAAnswerListFragment newInstance() {
        QnAAnswerListFragment view = new QnAAnswerListFragment();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}
