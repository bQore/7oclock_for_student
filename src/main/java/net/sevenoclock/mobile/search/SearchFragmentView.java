package net.sevenoclock.mobile.search;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import com.viewpagerindicator.TabPageIndicator;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.customobj.FontTextView;
import net.sevenoclock.mobile.customobj.FullnameTabIndicator;
import net.sevenoclock.mobile.main.MainActivity;
import net.sevenoclock.mobile.question.QuestionDetailView;
import net.sevenoclock.mobile.question.QuestionExplainView;
import net.sevenoclock.mobile.question.QuestionVideoView;
import net.sevenoclock.mobile.settings.Functions;
import net.sevenoclock.mobile.settings.Values;
import org.json.JSONArray;
import org.json.JSONException;

public class SearchFragmentView extends LinearLayout {

    private ViewPager pager;
    private FullnameTabIndicator indicator;

    Values values;

    public SearchFragmentView(Context context, final int position, JSONArray ja_unit, int unit_level, String unit_title) {
        super(context);

        values = (Values) context.getApplicationContext();

        inflate(getContext(), R.layout.view_search_fragment, this);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        setTag(R.string.tag_main_title, unit_title);
        setTag(R.string.tag_main_subtitle, "");

        pager = (ViewPager) findViewById(R.id.vp_search_fragment_viewpaper);
        indicator = (FullnameTabIndicator) findViewById(R.id.tpi_search_fragment_indicator);

        pager.setOffscreenPageLimit(ja_unit.length());

        if(ja_unit != null){
            SearchFragmentAdapter adapter = new SearchFragmentAdapter(((FragmentActivity) MainActivity.activity).getSupportFragmentManager(), ja_unit, unit_level);
            adapter.notifyDataSetChanged();
            pager.setAdapter(adapter);
            pager.postDelayed(new Runnable() {
                @Override
                public void run() {
                    pager.setCurrentItem(position);
                }
            }, 100);
            indicator.setViewPager(pager);
        }
    }

    class SearchFragmentAdapter extends FragmentStatePagerAdapter {

        private JSONArray ja;
        private int unit_level;
        private String unit_title;

        public SearchFragmentAdapter(FragmentManager fm, JSONArray ja, int unit_level) {
            super(fm);
            this.ja = ja;
            this.unit_level = unit_level;
        }

        @Override
        public Fragment getItem(int position) {
            try{
                return new SearchListView().newInstance(unit_level, ja.getJSONObject(position).getInt("id"), ja.getJSONObject(position).getString("title"));
            }catch (Exception e){
                return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            try{
                return ja.getJSONObject(position).getString("title");
            }catch (Exception e){
                return "";
            }
        }

        @Override
        public int getCount() {
            return ja.length();
        }
    }

}
