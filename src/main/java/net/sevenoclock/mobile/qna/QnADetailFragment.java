package net.sevenoclock.mobile.qna;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.ImageOptions;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.customobj.FontTextView;
import net.sevenoclock.mobile.customobj.TryCatchJO;
import net.sevenoclock.mobile.main.MainActivity;
import net.sevenoclock.mobile.settings.Functions;
import net.sevenoclock.mobile.settings.Values;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class QnADetailFragment extends Fragment {

    private Context con;

    private TryCatchJO tcjo;
    private JSONArray ja_answer;

    private ImageView iv_qna_detail_head_icon;
    private FontTextView tv_qna_detail_head_username;
    private FontTextView tv_qna_detail_head_unit;
    private ImageView iv_qna_detail_head_question;
    private ProgressBar pb_qna_detail_head_loading;
    private FontTextView tv_qna_detail_head_contents;
    private FontTextView tv_qna_detail_head_answer_count;

    public static QnADetailAdapter qda;
    private ListView lv_qna_detail_list;
    private LinearLayout ll_qna_detail_btns;
    private LinearLayout ll_qna_detail_answer;

    Values values;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        con = container.getContext();
        values = (Values) con.getApplicationContext();
        try {
            tcjo = new TryCatchJO(new JSONObject(getArguments().getString("jo")));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        View v = inflater.inflate(R.layout.fragment_qna_detail, container, false);

        lv_qna_detail_list = (ListView) v.findViewById(R.id.lv_qna_detail_list);
        ll_qna_detail_btns = (LinearLayout) v.findViewById(R.id.ll_qna_detail_btns);
        ll_qna_detail_answer = (LinearLayout) v.findViewById(R.id.ll_qna_detail_answer);

        if(tcjo.get("user_id",0) == values.user_id) ll_qna_detail_btns.setVisibility(View.GONE);
        else ll_qna_detail_btns.setVisibility(View.VISIBLE);

        MainActivity.setTitle(tcjo.get("username","")+"님의 질문");
        MainActivity.setSubtitle(tcjo.get("unit1","")+"|"+tcjo.get("unit2",""));

        qda = new QnADetailAdapter(tcjo.get("user_id",0));
        lv_qna_detail_list.setAdapter(qda);

        View header = LayoutInflater.from(con).inflate(R.layout.view_qna_detail_head, lv_qna_detail_list, false);

        iv_qna_detail_head_icon = (ImageView) header.findViewById(R.id.iv_qna_detail_head_icon);
        tv_qna_detail_head_username = (FontTextView) header.findViewById(R.id.tv_qna_detail_head_username);
        tv_qna_detail_head_unit = (FontTextView) header.findViewById(R.id.tv_qna_detail_head_unit);
        iv_qna_detail_head_question = (ImageView) header.findViewById(R.id.iv_qna_detail_head_question);
        pb_qna_detail_head_loading = (ProgressBar) header.findViewById(R.id.pb_qna_detail_head_loading);
        tv_qna_detail_head_contents = (FontTextView) header.findViewById(R.id.tv_qna_detail_head_contents);
        tv_qna_detail_head_answer_count = (FontTextView) header.findViewById(R.id.tv_qna_detail_head_answer_count);

        ImageOptions options = new ImageOptions();
        options.round = 1000;
        Bitmap bitmap = values.aq.id(iv_qna_detail_head_icon).getCachedImage(Functions.DOMAIN + tcjo.get("user_src",""));
        if(bitmap != null) values.aq.id(iv_qna_detail_head_icon).image(Functions.borderRadius(bitmap, 1000));
        else values.aq.id(iv_qna_detail_head_icon).image(Functions.DOMAIN + tcjo.get("user_src",""), options);

        tv_qna_detail_head_username.setText(tcjo.get("username","-"));
        tv_qna_detail_head_unit.setText(tcjo.get("unit1","-")+" | "+tcjo.get("unit2","-"));
        tv_qna_detail_head_contents.setText(tcjo.get("contents","-"));

        try {
            values.aq.id(iv_qna_detail_head_question)
                    .progress(pb_qna_detail_head_loading)
                    .image(Functions.DOMAIN +tcjo.getJSONArray("src").get(0), true, true);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        lv_qna_detail_list.addHeaderView(header, null, false);

        new GetAnswerTask().execute(null, null, null);

        return v;
    }

    public static QnADetailFragment newInstance(JSONObject jo) {
        QnADetailFragment view = new QnADetailFragment();
        Bundle args = new Bundle();
        args.putString("jo",jo.toString());
        view.setArguments(args);
        return view;
    }

    public void reflesh(){
        new GetAnswerTask().execute(null, null, null);
    }

    class GetAnswerTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            MainActivity.ll_main_main_loading.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        protected Boolean doInBackground(Void... Void) {
            ja_answer = Functions.GET(String.format("get_qna_answers&qid=%d&limit=0:5",tcjo.get("id",0)));
            if(ja_answer == null) return false;
            qda.reflesh();
            for (int i = 0; i < ja_answer.length(); i++) {
                try {
                    qda.add(new TryCatchJO(ja_answer.getJSONObject(i)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return true;
        }

        protected void onPostExecute(Boolean result) {
            if(result) {
                try {
                    tv_qna_detail_head_answer_count.setText("답변 총 "+ja_answer.length()+"개");
                    qda.notifyDataSetChanged();
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

}