package net.sevenoclock.mobile.testpaper;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.customobj.TryCatchJO;
import net.sevenoclock.mobile.main.MainActivity;
import net.sevenoclock.mobile.settings.Functions;
import net.sevenoclock.mobile.settings.Values;
import org.json.JSONArray;
import org.json.JSONException;

public class TestpaperListView extends LinearLayout {

    private Context con;

    private LinearLayout ll_testpaper_list_left;
    private LinearLayout ll_testpaper_list_right;

    Values values;

    public TestpaperListView(Context context) {
        super(context);
        con = context;

        values = (Values) context.getApplicationContext();

        inflate(getContext(), R.layout.view_testpaper_list, this);
        setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        ll_testpaper_list_left = (LinearLayout)findViewById(R.id.ll_testpaper_list_left);
        ll_testpaper_list_right = (LinearLayout)findViewById(R.id.ll_testpaper_list_right);

        setTag(R.string.tag_main_title,"출제문제지");
        setTag(R.string.tag_main_subtitle, "총 0개의 문제지가 있습니다.");

        new AddBookTask().execute(null, null, null);
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
            ja_book = Functions.GET("get_testpaper_list&school=" + values.user_info.get("school_id","")
                    + "&year=" + values.user_info.get("school_year","")
                    + "&room=" + values.user_info.get("school_room","")
                    + "&uid=" + values.user_id);
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

                                tbv.setOnClickListener(new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Functions.history_go(con, new TestpaperQuestionListView(con,((TestpaperBookView)v).tcjo));
                                    }
                                });
                            }
                        }

                        MainActivity.ll_main_main_loading.setVisibility(View.GONE);
                        MainActivity.setSubtitle("총 " + ja_book.length() + "개의 문제지가 있습니다.");
                    }
                });
            }else{
                Toast.makeText(con, "데이터 로드에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                MainActivity.ll_main_main_loading.setVisibility(View.GONE);
            }
            return;
        }
    }
}