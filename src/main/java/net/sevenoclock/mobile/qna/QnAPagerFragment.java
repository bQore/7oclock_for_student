package net.sevenoclock.mobile.qna;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.viewpagerindicator.TabPageIndicator;

import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.customobj.IconTextView;
import net.sevenoclock.mobile.main.MainActivity;
import net.sevenoclock.mobile.settings.Functions;
import net.sevenoclock.mobile.settings.UserData;
import net.sevenoclock.mobile.settings.Values;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class QnAPagerFragment extends Fragment {

    private Context con;

    private ViewPager pager;
    private TabPageIndicator indicator;
    private LinearLayout ll_qna_pager_ask;
    private IconTextView itv_filter_set;

    private QnAFragmentAdapter adapter;

    private static final String[] CONTENT = new String[] { "소속질문", "전체질문" };

    Values values;

    // 0 = 모든 문제, 1 = 관심 학교
    private int school_mode = 0;
    // 0 = 전체 문제, 1 = 답변이 없는 문제, 2 = 답변이 있는 문제
    private int comment_mode = 0;
    // 0 = 모든 학년, 1 = 선택된 학년
    private int grade_mode = 0;

    private FragmentManager manager;

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
                Functions.history_go(con, new QnAQuestionWriteFragment().newInstance());
            }
        });

        itv_filter_set = (IconTextView) v.findViewById(R.id.ic_filter_setting);
        itv_filter_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        manager = this.getChildFragmentManager();
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

        public void refresh(Bundle options){
            qlf[1].refresh(options);
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
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            return CONTENT.length;
        }
    }

    protected IconTextView info;
    protected TextView select_all, select_school;
    protected ListView school_list;
    protected ToggleButton[] tb_grade;
    protected ToggleButton tb_all_grade;
    protected RadioButton[] rb_comment_set;
    protected Button btn_apply;

    protected AlertDialog dialog;

    protected boolean is_paid=false;

    private ArrayAdapter<String> dialog_adapter;
    private int[] schools;

    protected void showDialog(){

        try {
            is_paid = values.union_info.getBoolean("is_paid");
            Log.i("@@info",values.union_info.get("title")+" is paid:"+is_paid);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(con);

        LayoutInflater inflater = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.dialog_school_filter, null, false);

        info = (IconTextView) v.findViewById(R.id.itv_filter_info);
        info.setOnClickListener(clickListener);
        select_all = (TextView) v.findViewById(R.id.tv_filter_select_all);
        select_all.setOnClickListener(clickListener);
        select_school = (TextView) v.findViewById(R.id.tv_filter_select_school);
        select_school.setOnClickListener(clickListener);
        school_list = (ListView) v.findViewById(R.id.lv_school_list);

        dialog_adapter = new ArrayAdapter<String>(con, android.R.layout.simple_list_item_1);
        school_list.setAdapter(dialog_adapter);

        Map<String,Object> key = new HashMap<String, Object>();
        key.put("union_id", UserData.getMainUnion());
        values.aq.ajax(Functions.DOMAIN+"/mobile/?mode=get_interest_schools_list", key, JSONArray.class, new AjaxCallback<JSONArray>(){
            @Override
            public void callback(String url, JSONArray object, AjaxStatus status) {
                if(status.getCode()==200){
                    schools = new int[object.length()];
                    for(int i=0;i<object.length();i++){
                        try {
                            schools[i] = object.getJSONObject(i).getInt("school_id");
                            dialog_adapter.add(object.getJSONObject(i).getString("school_name"));
                        } catch (JSONException e) {
//                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        tb_grade = new ToggleButton[6];
        tb_grade[0] = (ToggleButton) v.findViewById(R.id.tb_m1_select);
        tb_grade[1] = (ToggleButton) v.findViewById(R.id.tb_m2_select);
        tb_grade[2] = (ToggleButton) v.findViewById(R.id.tb_m3_select);
        tb_grade[3] = (ToggleButton) v.findViewById(R.id.tb_h1_select);
        tb_grade[4] = (ToggleButton) v.findViewById(R.id.tb_h2_select);
        tb_grade[5] = (ToggleButton) v.findViewById(R.id.tb_h3_select);
        for(int i=0;i<6;i++){
            tb_grade[i].setOnCheckedChangeListener(checkListener);
        }
        tb_all_grade = (ToggleButton) v.findViewById(R.id.tb_all_select);
        tb_all_grade.setOnCheckedChangeListener(checkListener);


        rb_comment_set = new RadioButton[3];
        rb_comment_set[0] = (RadioButton) v.findViewById(R.id.rb_all_select);
        rb_comment_set[1] = (RadioButton) v.findViewById(R.id.rb_no_comment_select);
        rb_comment_set[2] = (RadioButton) v.findViewById(R.id.rb_comment_select);
        for(int i=0;i<3;i++){
            rb_comment_set[i].setOnCheckedChangeListener(checkListener);
        }

        btn_apply = (Button) v.findViewById(R.id.btn_filter_apply);
        btn_apply.setOnClickListener(clickListener);

        builder.setView(v);

        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    // 아래는 Toggle과 Radio 를 체크할 경우 사용되는 리스너.
    private CompoundButton.OnCheckedChangeListener checkListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            int id = buttonView.getId();
            switch (id){
                case R.id.rb_all_select:
                    comment_mode=0;
                    break;
                case R.id.rb_no_comment_select:
                    comment_mode=1;
                    break;
                case R.id.rb_comment_select:
                    comment_mode=2;
                    break;
                case R.id.tb_all_select:
                    for(int i=0;i<6;i++){
                        tb_grade[i].setChecked(false);
                        tb_grade[i].setBackgroundResource(R.drawable.view_toggle_off);
                    }
                    //tb_all_grade.setChecked(true);
                    tb_all_grade.setBackgroundResource(R.drawable.view_toggle_on);
                    grade_mode = 0;
                    break;
                default:
                    tb_all_grade.setChecked(false);
                    tb_all_grade.setBackgroundResource(R.drawable.view_toggle_off);
                    buttonView.setChecked(isChecked);
                    if(isChecked){
                        buttonView.setBackgroundResource(R.drawable.view_toggle_on);
                    }else {
                        buttonView.setBackgroundResource(R.drawable.view_toggle_off);
                    }
                    Log.i("@@info",""+buttonView.getText()+" is "+buttonView.isChecked());
                    grade_mode = 1;
                    break;
            }
        }
    };

    // 아래는 버튼과 뷰(이미지나 버튼)를 클릭했을 경우 사용되는 리스너.
    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //if 문 안의 true는 유료결제 유무 확인하는 부분
            switch (v.getId()){
                case R.id.btn_filter_apply:
                    Bundle filter = new Bundle();
                    filter.putIntArray("schools", schools);
                    String[] grades;
                    if(tb_all_grade.isChecked()){
                        grades = new String[1];
                        grades[0] = "all";
                    }else{
                        ArrayList<String> list = new ArrayList<String>();
                        for(int i=0;i<tb_grade.length;i++){
                            if(tb_grade[i].isChecked()){
                                list.add(tb_grade[i].getTag().toString());
                            }
                        }
                        grades = list.toArray(new String[list.size()]);
                    }
                    filter.putStringArray("grades",grades);
                    filter.putInt("is_answer", comment_mode);
                    filter.putInt("school_mode", school_mode);
                    filter.putInt("grade_mode", grade_mode);

                    adapter.refresh(filter);
                    indicator.setCurrentItem(1);
                    dialog.dismiss();
                    break;
                case R.id.tv_filter_select_all:
                    if(is_paid) {
                        select_all.setBackgroundResource(R.drawable.view_select_box);
                        select_school.setBackgroundResource(R.drawable.view_unselect_box);
                        school_mode = 0;
                        select_all.setTextColor(Color.parseColor("#eeeeee"));
                        select_school.setTextColor(Color.parseColor("#333333"));
                        school_list.setVisibility(View.GONE);
                    }
                    break;
                case R.id.tv_filter_select_school:
                    if(is_paid) {
                        select_all.setBackgroundResource(R.drawable.view_unselect_box);
                        select_school.setBackgroundResource(R.drawable.view_select_box);
                        school_mode = 1;
                        select_all.setTextColor(Color.parseColor("#333333"));
                        select_school.setTextColor(Color.parseColor("#eeeeee"));
                        school_list.setVisibility(View.VISIBLE);
                    }else showInfo();
                    break;
                case R.id.itv_filter_info:
                    showInfo();
                    break;
            }
        }
    };

    protected void showInfo(){
        AlertDialog.Builder info_builder = new AlertDialog.Builder(con);
        LayoutInflater inflater = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        info_builder.setView(inflater.inflate(R.layout.view_information, null, false));
        info_builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        info_builder.show();
    }


}
