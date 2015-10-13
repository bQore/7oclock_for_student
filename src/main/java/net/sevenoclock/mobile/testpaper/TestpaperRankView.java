package net.sevenoclock.mobile.testpaper;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.LinearLayout;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.customobj.TryCatchJO;
import net.sevenoclock.mobile.main.MainActivity;
import net.sevenoclock.mobile.question.QuestionFragmentView;
import net.sevenoclock.mobile.settings.Functions;
import net.sevenoclock.mobile.settings.Values;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TestpaperRankView extends LinearLayout {

    private Context con;
    public TryCatchJO tcjo_info;

    private LinearLayout ll_testpaper_rank_myrank;
    private LinearLayout ll_testpaper_rank_ranks;

    public int qid = 0;

    Values values;

    public TestpaperRankView(Context context, TryCatchJO jo) {
        super(context);
        this.con = context;
        this.tcjo_info = jo;

        values = (Values) context.getApplicationContext();
        inflate(getContext(), R.layout.view_testpaper_rank, this);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        ll_testpaper_rank_myrank = (LinearLayout)findViewById(R.id.ll_testpaper_rank_myrank);
        ll_testpaper_rank_ranks = (LinearLayout)findViewById(R.id.ll_testpaper_rank_ranks);

        setTag(R.string.tag_main_title, "");
        setTag(R.string.tag_main_subtitle, "");

        new TestpaperRankTask().execute(null, null, null);
    }

    class TestpaperRankTask extends AsyncTask<Void, Void, Boolean> {
        JSONArray ja;
        JSONObject jo_myrank;
        JSONArray jo_ranks;

        @Override
        protected void onPreExecute() {
            MainActivity.ll_main_main_loading.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        protected Boolean doInBackground(Void... Void) {
            ja = Functions.GET("get_testpaper_submit&uid=" + values.user_id + "&tpid=" + tcjo_info.get("id", ""));
            if(ja == null) return false;
            try {
                jo_myrank = ja.getJSONObject(0).getJSONArray("ranks").getJSONObject(0);
                jo_ranks = jo_myrank.getJSONArray("scores");
            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        protected void onPostExecute(Boolean result) {
            if(result) {
                MainActivity.activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ll_testpaper_rank_myrank.addView(new TestpaperRankListView(con, jo_myrank, true), 0);
                        for (int i = 0; i < jo_ranks.length(); i++) {
                            try {
                                ll_testpaper_rank_ranks.addView(new TestpaperRankListView(con, jo_ranks.getJSONObject(i), false));
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                        }
                        MainActivity.setTitle("랭킹보기");
                        MainActivity.ll_main_main_loading.setVisibility(View.GONE);
                    }
                });
                return;
            }
            ll_testpaper_rank_myrank.addView(new TestpaperRankListView(con, null, true), 0);
            MainActivity.ll_main_main_loading.setVisibility(View.GONE);
            return;
        }
    }
}