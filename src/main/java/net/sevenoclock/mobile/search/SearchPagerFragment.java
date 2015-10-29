package net.sevenoclock.mobile.search;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.customobj.FullnameTabIndicator;
import net.sevenoclock.mobile.main.MainActivity;
import net.sevenoclock.mobile.settings.Values;
import org.json.JSONArray;
import org.json.JSONException;

public class SearchPagerFragment extends Fragment {

    Context con;

    private ViewPager pager;
    private FullnameTabIndicator indicator;

    JSONArray ja_unit;
    int position;
    int unit_level;
    String unit_title;

    Values values;

    public static SearchPagerFragment newInstance(JSONArray ja, int position, int uni_level, String unit_title) {
        SearchPagerFragment view = new SearchPagerFragment();
        Bundle args = new Bundle();
        args.putString("array", ja.toString());
        args.putInt("position", position);
        args.putInt("level", uni_level);
        args.putString("title", unit_title);
        view.setArguments(args);
        return view;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        con = container.getContext();
        values = (Values) con.getApplicationContext();
        position = getArguments().getInt("position");
        unit_level = getArguments().getInt("level");
        unit_title = getArguments().getString("title");

        try {
            ja_unit = new JSONArray(getArguments().getString("array"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        View v = inflater.inflate(R.layout.fragment_search_pager, container, false);

        MainActivity.setTitle(unit_title);
        MainActivity.setSubtitle("");

        pager = (ViewPager) v.findViewById(R.id.vp_search_fragment_viewpaper);
        indicator = (FullnameTabIndicator) v.findViewById(R.id.tpi_search_fragment_indicator);

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

        return v;
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
                return new SearchListFragment().newInstance(unit_level, ja.getJSONObject(position).getInt("id"), ja.getJSONObject(position).getString("title"));
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
