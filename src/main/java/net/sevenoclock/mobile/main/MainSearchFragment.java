package net.sevenoclock.mobile.main;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.settings.Functions;
import net.sevenoclock.mobile.settings.Values;
import net.sevenoclock.mobile.testpaper.TestpaperRankAdapter;
import org.json.JSONArray;

import java.net.URLEncoder;

public class MainSearchFragment extends Fragment {

    private Context con;
    private String search_query = "";

    private LinearLayout ll_main_search_list;

    Values values;

    public static MainSearchFragment newInstance() {
        MainSearchFragment view = new MainSearchFragment();
        return view;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        con = container.getContext();
        values = (Values) con.getApplicationContext();

        View v = inflater.inflate(R.layout.fragment_main_search, container, false);

        MainActivity.setTitle(String.format("\"%s\"에 대한 검색결과", search_query));
        MainActivity.setSubtitle("");

        ll_main_search_list = (LinearLayout) v.findViewById(R.id.ll_main_search_list);

        return v;
    }

    public void reset(){
        search_query = "";
        MainActivity.tv_main_main_title.setText(String.format("\"%s\"에 대한 검색결과", search_query));
        if(ll_main_search_list != null)
            if(ll_main_search_list.getChildCount() > 0)
                ll_main_search_list.removeAllViews();
    }

    public void search(String str){
        try{
            MainActivity.tv_main_main_title.setText(String.format("\"%s\"에 대한 검색결과", str));
            search_query = URLEncoder.encode(str, "utf-8");
            new AddListTask().execute(null, null, null);
        }catch (Exception e){
            Log.i("SearchError",e.getMessage());
        }
    }

    class AddListTask extends AsyncTask<Void, Void, Boolean> {
        JSONArray ja_search;

        @Override
        protected void onPreExecute() {
            if(ll_main_search_list.getChildCount() > 0) ll_main_search_list.removeAllViews();
            MainActivity.ll_main_main_loading.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        protected Boolean doInBackground(Void... Void) {
            ja_search = Functions.GET("get_question_search&q="+search_query);
            if(ja_search == null) return false;
            return true;
        }

        protected void onPostExecute(Boolean result) {
            if(result) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            for(int i=0; i < ja_search.length(); i++){
                                ll_main_search_list.addView(new MainSearchBoxView(con, ja_search.getJSONObject(i)));
                            }
                        }catch (Exception e){
                            Log.i("SearchListAddError",e.getMessage());
                        }
                        MainActivity.ll_main_main_loading.setVisibility(View.GONE);
                    }
                });
            }
            return;
        }
    }

}
