package net.sevenoclock.mobile.search;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.androidquery.AQuery;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.customobj.TryCatchJO;
import net.sevenoclock.mobile.main.MainActivity;
import net.sevenoclock.mobile.settings.Functions;
import net.sevenoclock.mobile.testpaper.TestpaperBookView;
import net.sevenoclock.mobile.testpaper.TestpaperQuestionListView;
import org.json.JSONArray;
import org.json.JSONException;

public class SearchListView extends Fragment {

    private Context con;

    private ImageView iv_search_list_nodata;
    private LinearLayout ll_search_list_view;
    private ListView lv_search_list_list;
    private ArrayAdapter<String> lv_search_list_adapter;

    private int unit_level = 0;
    private int unit_id = 0;
    private String unit_title = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        con = container.getContext();

        View view = inflater.inflate(R.layout.view_search_list, container, false);
        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        iv_search_list_nodata = (ImageView)view.findViewById(R.id.iv_search_list_nodata);
        ll_search_list_view = (LinearLayout)view.findViewById(R.id.ll_search_list_view);
        lv_search_list_list = (ListView)view.findViewById(R.id.lv_search_list_list);
        lv_search_list_adapter = new ArrayAdapter<String>(con, android.R.layout.simple_list_item_1);

        unit_level = getArguments().getInt("unit_level")+1;
        unit_id = getArguments().getInt("unit_id");
        unit_title = getArguments().getString("unit_title");

        new AddListTask().execute(null, null, null);

        return view;
    }

    class AddListTask extends AsyncTask<Void, Void, Boolean> {
        JSONArray ja_unit;

        @Override
        protected void onPreExecute() {
            MainActivity.ll_main_main_loading.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        protected Boolean doInBackground(Void... Void) {
            ja_unit = Functions.GET(String.format("get_question_unit_new&u%d=%d", unit_level, unit_id));
            if(ja_unit == null) return false;
            return true;
        }

        protected void onPostExecute(Boolean result) {
            if(result) {
                MainActivity.activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < ja_unit.length(); i++) {
                            try {
                                lv_search_list_adapter.add(ja_unit.getJSONObject(i).getString("title"));
                            } catch (Exception e) {
                                Log.i("SearchListError", e.getMessage());
                            }
                        }

                        lv_search_list_list.setAdapter(lv_search_list_adapter);
                        lv_search_list_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                if(unit_level < 4) {
                                    try {
                                        Functions.history_go(con
                                                , new SearchFragmentView(con, position, Functions.GET("get_question_unit_new&u" + unit_level + "=" + unit_id), unit_level, unit_title));
                                    } catch (Exception e) {
                                        Log.i("QuestionUnitError", e.getMessage());
                                    }
                                }else{
                                    try {
                                        Functions.history_go(con
                                                , new SearchQuestionListView(con, ja_unit.getJSONObject(position).getInt("id"), ja_unit.getJSONObject(position).getString("title")));
                                    } catch (Exception e) {
                                        Log.i("QuestionUnitError", e.getMessage());
                                    }
                                }
                            }
                        });
                        if(ja_unit.length() <= 0){
                            ll_search_list_view.setVisibility(View.GONE);
                            iv_search_list_nodata.setVisibility(View.VISIBLE);
                        }
                        MainActivity.ll_main_main_loading.setVisibility(View.GONE);
                    }
                });
            }else{
                MainActivity.ll_main_main_loading.setVisibility(View.GONE);
            }
            return;
        }
    }

    public static SearchListView newInstance(int unit_level,int unit_id, String unit_title) {
        SearchListView view = new SearchListView();
        Bundle args = new Bundle();
        args.putInt("unit_level", unit_level);
        args.putInt("unit_id", unit_id);
        args.putString("unit_title", unit_title);
        view.setArguments(args);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
