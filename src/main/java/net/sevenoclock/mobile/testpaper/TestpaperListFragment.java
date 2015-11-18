package net.sevenoclock.mobile.testpaper;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.customobj.RefreshScrollView;
import net.sevenoclock.mobile.customobj.TryCatchJO;
import net.sevenoclock.mobile.main.MainActivity;
import net.sevenoclock.mobile.settings.Functions;
import net.sevenoclock.mobile.settings.Values;
import org.json.JSONArray;
import org.json.JSONException;

public class TestpaperListFragment extends Fragment {

    private Context con;

    private TestpaperListAdapter tla;
    private GridView gv_testpaper_list_grid;

    private JSONArray ja_book;

    Values values;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        con = container.getContext();
        values = (Values) con.getApplicationContext();

        View v = inflater.inflate(R.layout.fragment_testpaper_list, container, false);

        gv_testpaper_list_grid = (GridView) v.findViewById(R.id.gv_testpaper_list_grid);

        MainActivity.setTitle("출제문제지");
        MainActivity.setSubtitle( "총 0개의 문제지가 있습니다.");

        tla = new TestpaperListAdapter();
        gv_testpaper_list_grid.setAdapter(tla);
        gv_testpaper_list_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                try {
                    Functions.history_go(con, new TestpaperQuestionListFragment().newInstance(new TryCatchJO(ja_book.getJSONObject(position))));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        new AddBookTask().execute(null, null, null);

        return v;
    }

    public static TestpaperListFragment newInstance() {
        TestpaperListFragment view = new TestpaperListFragment();
        return view;
    }

    public void reflesh(){
        new AddBookTask().execute(null, null, null);
    }

    class AddBookTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            MainActivity.ll_main_main_loading.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        protected Boolean doInBackground(Void... Void) {
            ja_book = Functions.GET("get_testpaper_list&&uid=" + values.user_id);
            if(ja_book == null) return false;
            return true;
        }

        protected void onPostExecute(Boolean result) {
            if(result) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tla.reflesh();
                        tla.notifyDataSetChanged();
                        for (int i = 0; i < ja_book.length(); i++) {
                            try {
                                tla.add(new TryCatchJO(ja_book.getJSONObject(i)));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        MainActivity.ll_main_main_loading.setVisibility(View.GONE);
                        MainActivity.setSubtitle("총 " + ja_book.length() + "개의 문제지가 있습니다.");
                    }
                });
            }else{
                Toast.makeText(con, "데이터 로드에 실패하였습니다.", Toast.LENGTH_LONG).show();
                MainActivity.ll_main_main_loading.setVisibility(View.GONE);
            }
            return;
        }
    }
}