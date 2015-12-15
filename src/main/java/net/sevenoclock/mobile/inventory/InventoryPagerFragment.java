package net.sevenoclock.mobile.inventory;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.viewpagerindicator.TabPageIndicator;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.main.MainActivity;
import net.sevenoclock.mobile.qna.QnAAnswerListFragment;
import net.sevenoclock.mobile.qna.QnAQuestionListFragment;
import net.sevenoclock.mobile.settings.Values;

public class InventoryPagerFragment extends Fragment {

    private Context con;

    private ViewPager pager;
    private TabPageIndicator indicator;

    private static final String[] CONTENT = new String[] { "내 문제", "내 질문", "내 답변" };

    Values values;

    public static InventoryPagerFragment newInstance() {
        InventoryPagerFragment view = new InventoryPagerFragment();
        return view;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        con = container.getContext();
        values = (Values) con.getApplicationContext();

        View v = inflater.inflate(R.layout.fragment_inventory_pager, container, false);

        MainActivity.setTitle("내 보관함");
        MainActivity.setSubtitle(values.user_info.get("first_name","-")+"님의 보관함입니다.");

        pager = (ViewPager) v.findViewById(R.id.vp_qna_fragment_viewpaper);
        indicator = (TabPageIndicator) v.findViewById(R.id.tpi_qna_fragment_indicator);

        InventoryFragmentAdapter adapter = new InventoryFragmentAdapter(((FragmentActivity) getActivity()).getSupportFragmentManager());
        adapter.notifyDataSetChanged();

        pager.setOffscreenPageLimit(3);

        pager.setAdapter(adapter);
        indicator.setViewPager(pager);
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        return v;
    }

    class InventoryFragmentAdapter extends FragmentPagerAdapter {

        public InventoryFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new InventoryListFragment().newInstance();
                case 1:
                    return new QnAQuestionListFragment().newInstance(0);
                case 2:
                    return new QnAAnswerListFragment().newInstance();
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
