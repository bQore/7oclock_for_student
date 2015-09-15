package net.sevenoclock.mobile.inventory;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.customobj.TryCatchJO;
import net.sevenoclock.mobile.main.MainActivity;
import net.sevenoclock.mobile.question.QuestionFragmentView;
import net.sevenoclock.mobile.settings.Functions;
import net.sevenoclock.mobile.settings.Values;
import org.json.JSONArray;
import org.json.JSONException;

public class InventoryQuestionListView extends LinearLayout {

    private Context con;
    private int unit_id;

    private LinearLayout ll_inventory_question_list_left;
    private LinearLayout ll_inventory_question_list_right;

    Values values;

    public InventoryQuestionListView(Context context, int unit_id, String title) {
        super(context);
        this.con = context;
        this.unit_id = unit_id;

        values = (Values) context.getApplicationContext();

        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        addView(inflater.inflate(R.layout.view_inventory_question_list, null), lp);

        ll_inventory_question_list_left = (LinearLayout)findViewById(R.id.ll_inventory_question_list_left);
        ll_inventory_question_list_right = (LinearLayout)findViewById(R.id.ll_inventory_question_list_right);

        setTag(R.string.tag_main_title, title);
        setTag(R.string.tag_main_subtitle, "총 0개의 문제가 있습니다.");

        new AddQuestionTask().execute(null, null, null);
    }

    public void reflesh(){
        ll_inventory_question_list_left.removeAllViews();
        ll_inventory_question_list_right.removeAllViews();
        new AddQuestionTask().execute(null, null, null);
    }

    public void readMore(){
        new AddQuestionTask().execute(null, null, null);
    }

    class AddQuestionTask extends AsyncTask<Void, Void, Boolean> {
        JSONArray ja_question;

        @Override
        protected void onPreExecute() {
            MainActivity.ll_main_main_loading.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        protected Boolean doInBackground(Void... Void) {
            ja_question = Functions.GET("get_inventory_question_list&unit_id=" + unit_id + "&uid=" + values.user_id);
            if(ja_question == null) return false;
            return true;
        }

        protected void onPostExecute(Boolean result) {
            if(result) {
                if(ja_question.length() == 0){
                    Functions.history_back(con,false);
                    MainActivity.view_inventory_list.reflesh();
                    MainActivity.ll_main_main_loading.setVisibility(View.GONE);
                    return;
                }
                MainActivity.activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < ja_question.length(); i++) {
                            InventoryQuestionView iqv = null;
                            try {
                                TryCatchJO tcjo = new TryCatchJO(ja_question.getJSONObject(i));
                                iqv = new InventoryQuestionView(con, tcjo);
                                iqv.setTag(R.string.tag_QuestionFragmentView_id, tcjo.get("id", "0"));
                                iqv.setTag(R.string.tag_QuestionFragmentView_title, tcjo.get("unit", "-"));
                                iqv.setTag(R.string.tag_QuestionFragmentView_src, tcjo.get("src", ""));
                                iqv.setTag(R.string.tag_QuestionFragmentView_explain, tcjo.get("explain", ""));
                                iqv.setTag(R.string.tag_QuestionFragmentView_video, tcjo.get("video", ""));

                                iqv.setOnClickListener(new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Functions.history_go(con, new QuestionFragmentView(con
                                                , v.getTag(R.string.tag_QuestionFragmentView_id).toString()
                                                , v.getTag(R.string.tag_QuestionFragmentView_title).toString()
                                                , v.getTag(R.string.tag_QuestionFragmentView_src).toString()
                                                , v.getTag(R.string.tag_QuestionFragmentView_explain).toString()
                                                , v.getTag(R.string.tag_QuestionFragmentView_video).toString()));
                                    }
                                });

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            int count_left = ll_inventory_question_list_left.getChildCount();
                            int count_right = ll_inventory_question_list_right.getChildCount();

                            if (count_left > count_right) ll_inventory_question_list_right.addView(iqv);
                            else ll_inventory_question_list_left.addView(iqv);
                        }
                        MainActivity.setSubtitle("총 "+ja_question.length()+"개의 문제가 있습니다.");
                    }
                });
            }else{
                Toast.makeText(con, "데이터 로드에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
            MainActivity.ll_main_main_loading.setVisibility(View.GONE);
            return;
        }
    }
}