package net.sevenoclock.mobile.question;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.viewpagerindicator.TabPageIndicator;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.customobj.TryCatchJO;
import net.sevenoclock.mobile.main.MainActivity;
import net.sevenoclock.mobile.settings.Functions;
import net.sevenoclock.mobile.settings.Values;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class QuestionFragmentView extends LinearLayout {

    private Context con;

    private ViewPager pager;
    private TabPageIndicator indicator;

    String src, explain, video;

    private static final String[] CONTENT = new String[] { "문제", "해설", "동영상" };

    Values values;

    public QuestionFragmentView(Context context, String qid, String src, String explain, String video) {
        super(context);
        this.con = context;
        this.src = src;
        this.explain = explain;
        this.video = video;

        values = (Values) context.getApplicationContext();

        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        addView(inflater.inflate(R.layout.view_question_fragment, null), lp);

        pager = (ViewPager) findViewById(R.id.vp_question_detail_viewpaper);
        indicator = (TabPageIndicator) findViewById(R.id.tpi_question_detail_indicator);

        pager.setOffscreenPageLimit(10);

        FragmentPagerAdapter adapter = new QuestionFragmentAdapter(((FragmentActivity) MainActivity.activity).getSupportFragmentManager());
        pager.setAdapter(adapter);
        indicator.setViewPager(pager);

    }

    class QuestionFragmentAdapter extends FragmentPagerAdapter {
        public QuestionFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new QuestionDetailView(con, src);
                case 1:
                    return new QuestionExplainView(con, explain);
                case 2:
                    return new QuestionVideoView().newInstance(video);
            }
            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return CONTENT[position % CONTENT.length].toUpperCase();
        }

        @Override
        public int getCount() {
            return CONTENT.length;
        }
    }

}
