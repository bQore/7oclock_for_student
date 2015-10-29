package net.sevenoclock.mobile.testpaper;

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
import net.sevenoclock.mobile.settings.Functions;
import net.sevenoclock.mobile.settings.Values;
import org.json.JSONArray;
import org.json.JSONException;

public class TestpaperListFragment extends Fragment {

    private Context con;

    private LinearLayout ll_testpaper_list_left;
    private LinearLayout ll_testpaper_list_right;

    Values values;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        con = container.getContext();
        values = (Values) con.getApplicationContext();

        View v = inflater.inflate(R.layout.fragment_testpaper_list, container, false);

        ll_testpaper_list_left = (LinearLayout)v.findViewById(R.id.ll_testpaper_list_left);
        ll_testpaper_list_right = (LinearLayout)v.findViewById(R.id.ll_testpaper_list_right);

        MainActivity.setTitle("출제문제지");
        MainActivity.setSubtitle( "총 0개의 문제지가 있습니다.");

        new AddBookTask().execute(null, null, null);

        return v;
    }

    public static TestpaperListFragment newInstance() {
        TestpaperListFragment view = new TestpaperListFragment();
        return view;
    }

    public void reflesh(){
        ll_testpaper_list_left.removeAllViews();
        ll_testpaper_list_right.removeAllViews();
        new AddBookTask().execute(null, null, null);
    }

    class AddBookTask extends AsyncTask<Void, Void, Boolean> {
        JSONArray ja_book;

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
                MainActivity.activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < ja_book.length(); i++) {
                            TestpaperBookView tbv = null;
                            try {
                                TryCatchJO tcjo = new TryCatchJO(ja_book.getJSONObject(i));
                                tbv = new TestpaperBookView(con, tcjo);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            if (tbv != null) {
                                int count_left = ll_testpaper_list_left.getChildCount();
                                int count_right = ll_testpaper_list_right.getChildCount();

                                if (count_left > count_right) ll_testpaper_list_right.addView(tbv);
                                else ll_testpaper_list_left.addView(tbv);

                                tbv.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Functions.history_go(con, new TestpaperQuestionListFragment().newInstance(((TestpaperBookView)v).tcjo));
                                    }
                                });

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