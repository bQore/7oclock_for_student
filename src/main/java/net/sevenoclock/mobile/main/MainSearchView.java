package net.sevenoclock.mobile.main;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import com.viewpagerindicator.TabPageIndicator;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.customobj.FontTextView;
import net.sevenoclock.mobile.customobj.IconTextView;
import net.sevenoclock.mobile.customobj.TryCatchJO;
import net.sevenoclock.mobile.question.QuestionDetailView;
import net.sevenoclock.mobile.question.QuestionExplainView;
import net.sevenoclock.mobile.question.QuestionVideoView;
import net.sevenoclock.mobile.search.SearchFragmentView;
import net.sevenoclock.mobile.search.SearchQuestionListView;
import net.sevenoclock.mobile.settings.Functions;
import net.sevenoclock.mobile.settings.Values;
import org.json.JSONArray;
import org.json.JSONException;

import java.net.URLEncoder;

public class MainSearchView extends LinearLayout {

    private Context con;
    private String search_query = "";

    private LinearLayout ll_main_search_list;

    Values values;

    public MainSearchView(Context context) {
        super(context);
        this.con = context;
        values = (Values) context.getApplicationContext();

        inflate(getContext(), R.layout.view_main_search, this);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        setTag(R.string.tag_main_title, String.format("\"%s\"에 대한 검색결과", search_query));
        setTag(R.string.tag_main_subtitle, "");

        ll_main_search_list = (LinearLayout)findViewById(R.id.ll_main_search_list);

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
                MainActivity.activity.runOnUiThread(new Runnable() {
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
