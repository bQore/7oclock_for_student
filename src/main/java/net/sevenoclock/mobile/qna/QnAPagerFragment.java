package net.sevenoclock.mobile.qna;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.viewpagerindicator.TabPageIndicator;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.main.MainActivity;
import net.sevenoclock.mobile.settings.Functions;
import net.sevenoclock.mobile.settings.Values;

public class QnAPagerFragment extends Fragment {

    private Context con;

    private ViewPager pager;
    private TabPageIndicator indicator;
    private LinearLayout ll_qna_pager_ask;

    private QnAFragmentAdapter adapter;

    private static final String[] CONTENT = new String[] { "그룹질문", "전체질문" };

    Values values;

    public static QnAPagerFragment newInstance() {
        QnAPagerFragment view = new QnAPagerFragment();
        return view;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        con = container.getContext();
        values = (Values) con.getApplicationContext();

        View v = inflater.inflate(R.layout.fragment_qna_pager, container, false);

        MainActivity.setTitle("실시간질문");
        MainActivity.setSubtitle("실시간 질문하기 입니다.");

        pager = (ViewPager) v.findViewById(R.id.vp_qna_fragment_viewpaper);
        indicator = (TabPageIndicator) v.findViewById(R.id.tpi_qna_fragment_indicator);

        ll_qna_pager_ask = (LinearLayout) v.findViewById(R.id.ll_qna_pager_ask);
        ll_qna_pager_ask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Functions.history_go(con, new QnAQuestionWriteFragment());
            }
        });

        adapter = new QnAFragmentAdapter(this.getChildFragmentManager());
        adapter.notifyDataSetChanged();
        pager.setOffscreenPageLimit(2);
        pager.setAdapter(adapter);
        indicator.setViewPager(pager);
        return v;
    }

    class QnAFragmentAdapter extends FragmentPagerAdapter {

        private QnAQuestionListFragment[] qlf;

        public QnAFragmentAdapter(FragmentManager fm) {
            super(fm);
            qlf = new QnAQuestionListFragment[2];
            qlf[0] = new QnAQuestionListFragment().newInstance(1);
            qlf[1] = new QnAQuestionListFragment().newInstance(2);
        }

        @Override
        public Fragment getItem(int position) {
            return qlf[position];
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
