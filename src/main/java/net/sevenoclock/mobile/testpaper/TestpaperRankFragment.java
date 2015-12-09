package net.sevenoclock.mobile.testpaper;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.customobj.TryCatchJO;
import net.sevenoclock.mobile.main.MainActivity;
import net.sevenoclock.mobile.settings.Functions;
import net.sevenoclock.mobile.settings.Values;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TestpaperRankFragment extends Fragment {

    private Context con;
    public TryCatchJO tcjo_info;

    private ListView lv_testpaper_rank_myrank;
    private ListView lv_testpaper_rank_ranks;

    private TestpaperRankAdapter tra_myrank;
    private TestpaperRankAdapter tra_ranks;

    public int qid = 0;

    Values values;

    public static TestpaperRankFragment newInstance(TryCatchJO tcjo) {
        TestpaperRankFragment view = new TestpaperRankFragment();
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
            tcjo_info = new TryCatchJO(new JSONObject(getArguments().getString("object")));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        View v = inflater.inflate(R.layout.fragment_testpaper_rank, container, false);

        MainActivity.setTitle("");
        MainActivity.setSubtitle("");

        lv_testpaper_rank_myrank = (ListView) v.findViewById(R.id.lv_testpaper_rank_myrank);
        lv_testpaper_rank_ranks = (ListView) v.findViewById(R.id.lv_testpaper_rank_ranks);

        tra_myrank = new TestpaperRankAdapter(true);
        lv_testpaper_rank_myrank.setAdapter(tra_myrank);
        tra_ranks = new TestpaperRankAdapter(false);
        lv_testpaper_rank_ranks.setAdapter(tra_ranks);

        new TestpaperRankTask().execute(null, null, null);

        return v;
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
                try {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                tra_myrank.add(new TryCatchJO(jo_myrank));
                            } catch (Exception e) {

                            }
                            for (int i = 0; i < jo_ranks.length(); i++) {
                                try {
                                    tra_ranks.add(new TryCatchJO(jo_ranks.getJSONObject(i)));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            MainActivity.setTitle("랭킹보기");
                            MainActivity.ll_main_main_loading.setVisibility(View.GONE);
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
                return;
            }
            MainActivity.ll_main_main_loading.setVisibility(View.GONE);
            return;
        }
    }
}