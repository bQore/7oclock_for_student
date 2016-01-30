package net.sevenoclock.mobile.dashboard;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.*;

import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.customobj.FontTextView;
import net.sevenoclock.mobile.customobj.TryCatchJO;
import net.sevenoclock.mobile.inventory.InventoryPagerFragment;
import net.sevenoclock.mobile.main.MainActivity;
import net.sevenoclock.mobile.mypage.MypageGroupFragment;
import net.sevenoclock.mobile.mypage.MypageMainFragment;
import net.sevenoclock.mobile.qna.QnAPagerFragment;
import net.sevenoclock.mobile.settings.Functions;
import net.sevenoclock.mobile.settings.Values;
import net.sevenoclock.mobile.testpaper.TestpaperListFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DashboardFragment extends Fragment implements View.OnClickListener {

    private Context con;

    private ImageView iv_main_profile_image;
    private FontTextView ftv_main_profile_name;
    private FontTextView ftv_main_profile_group;
    private ListView lv_main_newsfeed;

    private RelativeLayout rl_main_profile;

    private LinearLayout ll_main_testpaper;
    private LinearLayout ll_main_qna;
    private LinearLayout ll_main_inven;
    private LinearLayout ll_main_union;
    private LinearLayout ll_main_notice;
    private LinearLayout ll_main_null;

    Values values;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        con = container.getContext();
        values = (Values) con.getApplicationContext();

        View v = inflater.inflate(R.layout.fragment_dashboard, container, false);

        iv_main_profile_image = (ImageView)v.findViewById(R.id.iv_main_home_profile_image);
        ftv_main_profile_name = (FontTextView)v.findViewById(R.id.ftv_main_home_profile_name);
        ftv_main_profile_group = (FontTextView)v.findViewById(R.id.ftv_main_home_profile_group);
        lv_main_newsfeed = (ListView)v.findViewById(R.id.lv_main_home_newsfeed);

        rl_main_profile = (RelativeLayout)v.findViewById(R.id.rl_main_home_profile);

        ll_main_testpaper = (LinearLayout)v.findViewById(R.id.ll_main_home_quiz);
        ll_main_qna = (LinearLayout)v.findViewById(R.id.ll_main_home_qna);
        ll_main_inven = (LinearLayout)v.findViewById(R.id.ll_main_home_inven);
        ll_main_union = (LinearLayout)v.findViewById(R.id.ll_main_home_union_setting);
        ll_main_notice = (LinearLayout) v.findViewById(R.id.ll_main_home_notice_list);
        ll_main_null = (LinearLayout)v.findViewById(R.id.ll_main_home_null);

        ll_main_testpaper.setOnClickListener(this);
        ll_main_qna.setOnClickListener(this);
        ll_main_inven.setOnClickListener(this);
        ll_main_union.setOnClickListener(this);
        ll_main_notice.setOnClickListener(this);
        ll_main_null.setOnClickListener(this);


        values.aq.id(iv_main_profile_image).image(Functions.borderRadius(Functions.DOMAIN + values.user_info.get("src", ""), 1000));
        values.aq.id(ftv_main_profile_name).text(values.user_info.get("first_name", "-"));
        values.aq.id(ftv_main_profile_group).text(String.format("%s %s", values.union_info.get("title", "-"), values.union_info.get("level_title", "-")));


        MainActivity.setTitleVisible(LinearLayout.GONE);

        iv_main_profile_image.setOnClickListener(this);


        NewsfeedAdapter adapter = new NewsfeedAdapter(values.user_id);
        lv_main_newsfeed.setAdapter(adapter);
        adapter.add(0);

        return v;
    }

    @Override
    public void onClick(View view) {
        Vibrator Vibe = (Vibrator) getActivity().getSystemService(getActivity().VIBRATOR_SERVICE);
        switch (view.getId()){
            case R.id.iv_main_home_profile_image:
                Vibe.vibrate(30);
                Functions.history_clear();
                Functions.history_go(con,new MypageMainFragment().newInstance());
                break;
            case R.id.ll_main_home_quiz:
                Vibe.vibrate(30);
                Functions.history_clear();
                Functions.history_go(con,new TestpaperListFragment().newInstance());
                break;
            case R.id.ll_main_home_qna:
                Vibe.vibrate(30);
                Functions.history_clear();
                Functions.history_go(con,new QnAPagerFragment().newInstance());
                break;
            case R.id.ll_main_home_inven:
                Vibe.vibrate(30);
                Functions.history_clear();
                Functions.history_go(con,new InventoryPagerFragment().newInstance());
                break;
            case R.id.ll_main_home_union_setting:
                Vibe.vibrate(30);
                Functions.history_clear();
                Functions.history_go(con,new MypageGroupFragment().newInstance());
                break;
            case R.id.ll_main_home_notice_list:
                Vibe.vibrate(30);
                Functions.history_clear();
                Functions.history_go(con, new NewsFeedListFragment().newInstance());
                break;
            case R.id.ll_main_home_null:
                Vibe.vibrate(30);
                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{"eaeao@naver.com", "storm0812@hanmail.net", "tellme0218@naver.com","sumael017@gmail.com"});
                email.putExtra(Intent.EXTRA_SUBJECT, "[모두를위한수학] 문의합니다!");
                email.putExtra(Intent.EXTRA_TEXT, "제목 : 문의합니다.\n\n내용 : ");
                email.setType("text/plain");
                final PackageManager pm = getActivity().getPackageManager();
                final List<ResolveInfo> matches = pm.queryIntentActivities(email, 0);
                ResolveInfo best = null;
                for (final ResolveInfo info : matches)
                    if (info.activityInfo.packageName.endsWith(".gm") || info.activityInfo.name.toLowerCase().contains("gmail"))
                        best = info;
                if (best != null)
                    email.setClassName(best.activityInfo.packageName, best.activityInfo.name);
                getActivity().startActivity(email);
                break;
        }
    }

    public static DashboardFragment newInstance() {
        DashboardFragment view = new DashboardFragment();
        return view;
    }

    @Override
    public void onDestroyView(){
        MainActivity.setTitleVisible(LinearLayout.VISIBLE);
        super.onDestroyView();
    }

    @Override
    public void onResume(){
        MainActivity.setTitleVisible(View.GONE);
        super.onResume();
    }

    protected class NewsfeedAdapter extends BaseAdapter{
        private int host_id = 0;

        private ArrayList<Integer> n_list;

        private ImageView iv_newsfeed_icon;
        private FontTextView ftv_newsfeed_text;
        private FontTextView ftv_newsfeed_date;

        Values adapt_values;

        public NewsfeedAdapter(int host_id){
            n_list = new ArrayList<Integer>();
            this.host_id = host_id;
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