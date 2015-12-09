package net.sevenoclock.mobile.inventory;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.customobj.TryCatchJO;
import net.sevenoclock.mobile.main.MainActivity;
import net.sevenoclock.mobile.question.QuestionPagerFragment;
import net.sevenoclock.mobile.settings.Functions;
import net.sevenoclock.mobile.settings.Values;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class InventoryQuestionListFragment extends Fragment {

    private Context con;
    TryCatchJO tcjo;

    private LinearLayout ll_inventory_question_list_left;
    private LinearLayout ll_inventory_question_list_right;

    Values values;

    public static InventoryQuestionListFragment newInstance(TryCatchJO tcjo) {
        InventoryQuestionListFragment view = new InventoryQuestionListFragment();
        Bundle args = new Bundle();
        args.putString("object", tcjo.toString());
        view.setArguments(args);
        return view;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        con = container.getContext();
        values = (Values) con.getApplicationContext();
        try {
            tcjo = new TryCatchJO(new JSONObject(getArguments().getString("object")));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        View v = inflater.inflate(R.layout.fragment_inventory_question_list, container, false);

        MainActivity.setTitle(tcjo.get("title",""));
        MainActivity.setSubtitle("총 0개의 문제가 있습니다.");

        ll_inventory_question_list_left = (LinearLayout) v.findViewById(R.id.ll_inventory_question_list_left);
        ll_inventory_question_list_right = (LinearLayout) v.findViewById(R.id.ll_inventory_question_list_right);

        new AddQuestionTask().execute(null, null, null);

        return v;
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
            ja_question = Functions.GET("get_inventory_question_list&unit_id=" + tcjo.get("id",0) + "&uid=" + values.user_id);
            if(ja_question == null) return false;
            return true;
        }

        protected void onPostExecute(Boolean result) {
            if(result) {
                if(ja_question.length() == 0){
                    Functions.history_back(con,false);
                    MainActivity.ll_main_main_loading.setVisibility(View.GONE);
                    return;
                }
                try{
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i < ja_question.length(); i++) {
                                InventoryQuestionView iqv = null;
                                try {
                                    TryCatchJO tcjo = new TryCatchJO(ja_question.getJSONObject(i));
                                    iqv = new InventoryQuestionView(con, tcjo);

                                    iqv.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Functions.history_go(con, new QuestionPagerFragment().newInstance(((InventoryQuestionView)v).tcjo));
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
                }catch (Exception e){
                    e.printStackTrace();
                }
            }else{
                Toast.makeText(con, "데이터 로드에 실패하였습니다.", Toast.LENGTH_LONG).show();
            }
            MainActivity.ll_main_main_loading.setVisibility(View.GONE);
            return;
        }
    }
}