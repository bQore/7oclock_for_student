package net.sevenoclock.mobile.testpaper;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.customobj.TryCatchJO;
import net.sevenoclock.mobile.main.MainActivity;
import net.sevenoclock.mobile.settings.Functions;
import net.sevenoclock.mobile.settings.Values;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TestpaperRankView extends LinearLayout {

    private Context con;
    public TryCatchJO tcjo_info;

    private ListView lv_testpaper_rank_myrank;
    private ListView lv_testpaper_rank_ranks;

    private TestpaperRankAdapter tra_myrank;
    private TestpaperRankAdapter tra_ranks;

    public int qid = 0;

    Values values;

    public TestpaperRankView(Context context, TryCatchJO jo) {
        super(context);
        this.con = context;
        this.tcjo_info = jo;

        values = (Values) context.getApplicationContext();
        inflate(getContext(), R.layout.view_testpaper_rank, this);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        lv_testpaper_rank_myrank = (ListView)findViewById(R.id.lv_testpaper_rank_myrank);
        lv_testpaper_rank_ranks = (ListView)findViewById(R.id.lv_testpaper_rank_ranks);

        setTag(R.string.tag_main_title, "");
        setTag(R.string.tag_main_subtitle, "");

        tra_myrank = new TestpaperRankAdapter(true);
        lv_testpaper_rank_myrank.setAdapter(tra_myrank);
        tra_ranks = new TestpaperRankAdapter(false);
        lv_testpaper_rank_ranks.setAdapter(tra_ranks);

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
                        try{
                            tra_myrank.add(new TryCatchJO(jo_myrank));
                        }catch (Exception e){

                        }
                        for (int i = 0; i < jo_ranks.length(); i++) {
                            try {
                                tra_ranks.add(new TryCatchJO(jo_ranks.getJSONObject(i)));
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
            MainActivity.ll_main_main_loading.setVisibility(View.GONE);
            return;
        }
    }
}