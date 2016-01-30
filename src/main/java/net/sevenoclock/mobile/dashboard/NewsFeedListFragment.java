package net.sevenoclock.mobile.dashboard;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.customobj.FontTextView;
import net.sevenoclock.mobile.main.MainActivity;
import net.sevenoclock.mobile.settings.Values;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by sumae on 2016-01-23.
 */
public class NewsFeedListFragment extends Fragment {
    private Context con;

    private ListView lv_newsfeed_list;

    private NewsfeedAdapter adapter;

    Values values;

    public static NewsFeedListFragment newInstance(){
        NewsFeedListFragment view = new NewsFeedListFragment();
        return  view;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        con = container.getContext();
        values = (Values) con.getApplicationContext();

        View v = inflater.inflate(R.layout.fragment_newsfeed, container, false);

        MainActivity.setTitle("알림");
        MainActivity.setSubtitle(MainActivity.news_count+"개의 새 알림이 있습니다.");

        lv_newsfeed_list = (ListView) v.findViewById(R.id.lv_newsfeed_list);

        adapter = new NewsfeedAdapter();
        lv_newsfeed_list.setAdapter(adapter);
        adapter.add(0);

        return v;
    }

    protected class NewsfeedAdapter extends BaseAdapter {
        private ArrayList<Integer> n_list;

        private ImageView iv_newsfeed_icon;
        private FontTextView ftv_newsfeed_text;
        private FontTextView ftv_newsfeed_date;

        Values adapt_values;

        public NewsfeedAdapter(){
            n_list = new ArrayList<Integer>();
        }

        public void add(Integer i){
            n_list.add(i);
        }

        @Override
        public int getCount() {
            return n_list.size();
        }

        @Override
        public Object getItem(int i) {
            return n_list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            final Context con = viewGroup.getContext();
            final int pos = i;
            adapt_values = (Values) con.getApplicationContext();
            if(view == null){
                LayoutInflater inflater = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.view_newsfeed_list,viewGroup,false);

                iv_newsfeed_icon = (ImageView) view.findViewById(R.id.iv_newsfeed_icon);
                ftv_newsfeed_text = (FontTextView) view.findViewById(R.id.ftv_newsfeed_text);
                ftv_newsfeed_date = (FontTextView) view.findViewById(R.id.ftv_newsfeed_date);

                if(n_list.get(pos)==0) {
                    iv_newsfeed_icon.setImageResource(R.drawable.ic_launcher);
                    ftv_newsfeed_text.setText("모두를 위한 수학에 오신 것을 환영합니다!");
                    Calendar now_date = Calendar.getInstance();
                    ftv_newsfeed_date.setText(now_date.get(Calendar.YEAR) + "." + now_date.get(Calendar.MONTH) + "." + now_date.get(Calendar.DAY_OF_MONTH) + "  " + now_date.get(Calendar.HOUR) + ":" + now_date.get(Calendar.MINUTE));
                }

            }

            return view;
        }
    }
}
