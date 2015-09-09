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
    private TabPageIndicator indicator;

    Values values;

    public SearchFragmentView(Context context, JSONArray ja_unit, String unit_title) {
        super(context);

        values = (Values) context.getApplicationContext();

        inflate(getContext(), R.layout.view_search_fragment, this);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        MainActivity.ll_main_main_title.setVisibility(View.GONE);
        setTag(R.string.tag_main_title, "");
        setTag(R.string.tag_main_subtitle, "");

        pager = (ViewPager) findViewById(R.id.vp_search_fragment_viewpaper);
        indicator = (TabPageIndicator) findViewById(R.id.tpi_search_fragment_indicator);

        pager.setOffscreenPageLimit(10);

        if(ja_unit != null){
            SearchFragmentAdapter adapter = new SearchFragmentAdapter(((FragmentActivity) MainActivity.activity).getSupportFragmentManager(), ja_unit);
            adapter.notifyDataSetChanged();
            pager.setAdapter(adapter);
            indicator.setViewPager(pager);
        }
    }

    class SearchFragmentAdapter extends FragmentStatePagerAdapter {

        private JSONArray ja;

        public SearchFragmentAdapter(FragmentManager fm, JSONArray ja) {
            super(fm);
            this.ja = ja;
        }

        @Override
        public Fragment getItem(int position) {
            try{
                return new SearchListView().newInstance(ja.getJSONObject(position).getString("id"));
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
