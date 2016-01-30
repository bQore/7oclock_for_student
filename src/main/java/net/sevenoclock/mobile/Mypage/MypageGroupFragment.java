package net.sevenoclock.mobile.mypage;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.customobj.FontTextView;
import net.sevenoclock.mobile.customobj.IconTextView;
import net.sevenoclock.mobile.customobj.TryCatchJO;
import net.sevenoclock.mobile.main.MainActivity;
import net.sevenoclock.mobile.settings.Functions;
import net.sevenoclock.mobile.settings.UserData;
import net.sevenoclock.mobile.settings.Values;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by user on 2016-01-14.
 */
public class MypageGroupFragment extends Fragment {
    private Context con;

    private ImageView iv_group_icon;
    private FontTextView ftv_group_name;
    private FontTextView ftv_group_level;
    private IconTextView ic_group_info;
    private GridView gv_group_list;

    private LinearLayout ll_main_set_btn;

    Values values;

    GroupAdapter adapt;

    ArrayList<GroupInfo> union_list;

    int select = 0;

    public static MypageGroupFragment newInstance(){
        MypageGroupFragment view = new MypageGroupFragment();
        return view;
    }

    @Override
    public void onDestroyView(){
        MainActivity.setSearchBarVisible(View.GONE);
        super.onDestroyView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        con = container.getContext();
        values = (Values)con.getApplicationContext();

        union_list = new ArrayList<GroupInfo>();

        MainActivity.setTitle("소속 관리");
        MainActivity.setSubtitle("");
        MainActivity.setSearchBarVisible(View.VISIBLE);

        View v = inflater.inflate(R.layout.fragment_mypage_group,container,false);

        iv_group_icon = (ImageView) v.findViewById(R.id.iv_mypage_main_group_icon);
        ftv_group_name = (FontTextView) v.findViewById(R.id.ftv_group_name);
        ftv_group_level = (FontTextView) v.findViewById(R.id.ftv_group_level);
        ic_group_info = (IconTextView) v.findViewById(R.id.itv_mypage_group_info);
        gv_group_list = (GridView) v.findViewById(R.id.gv_group_list);

        ll_main_set_btn = (LinearLayout) v.findViewById(R.id.ll_mypage_group_set_btn);
        if(UserData.getMainUnion() == -1)ll_main_set_btn.setVisibility(View.VISIBLE);
        ll_main_set_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.ll_main_main_loading.setVisibility(View.VISIBLE);
                GroupInfo info = (GroupInfo) adapt.getItem(select);
                int id = UserData.getMainUnion();
                UserData.setMainUnion(info.union_id);
                for(int i=0;i<union_list.size();i++){
                    if(union_list.get(i).union_id == id) union_list.get(i).main = false;
                    if(union_list.get(i).union_id == info.union_id) union_list.get(i).main = true;
                }
                try {
                    for (int i = 0; i < values.unions.length(); i++) {
                        if (values.unions.getJSONObject(i).getInt("id") == UserData.getMainUnion())
                            values.union_info = new TryCatchJO(values.unions.getJSONObject(i));
                    }
                    Log.i("@@info",values.union_info.toString());
                }catch(JSONException e){
                    e.printStackTrace();
                }
                adapt.removeAll();
                adapt.notifyDataSetChanged();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                setAdapt();



                MainActivity.ll_main_main_loading.setVisibility(View.GONE);
            }
        });

        ic_group_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                GroupInfo info = (GroupInfo)adapt.getItem(select);
//                UserData.setMainUnion(info.union_id);
//                adapt.removeAll();
//
//                setAdapt();
            }
        });

        adapt = new GroupAdapter(values.user_id);
        gv_group_list.setAdapter(adapt);

        new UnionTask().execute(null,null,null);

        return v;
    }

    private Comparator<GroupInfo> compare = new Comparator<GroupInfo>() {
        @Override
        public int compare(GroupInfo groupInfo, GroupInfo t1) {
            if(groupInfo.main)return -1;
            else if(t1.main)return 1;
            else return 0;
        }
    };
    private void setAdapt(){
        Collections.sort(union_list, compare);

        gv_group_list.setAdapter(adapt);

        for(int i=0;i<union_list.size();i++) adapt.add(union_list.get(i));
        adapt.notifyDataSetChanged();

        GroupInfo info = (GroupInfo)adapt.getItem(0);
        iv_group_icon.setImageBitmap(info.icon);
        ftv_group_name.setText(info.name);
        ftv_group_level.setText(info.level);
        ll_main_set_btn.setVisibility(View.GONE);
    }

    private class GroupAdapter extends BaseAdapter{

        private int host_id = 0;
        private ArrayList<GroupInfo> g_list;

        private ImageView iv_icon;
        private IconTextView itv_delete;
        private TextView tv_main;

        Values g_values;

        public GroupAdapter(int id){
            g_list = new ArrayList<GroupInfo>();
            this.host_id = id;
        }

        public void add(GroupInfo e){
            g_list.add(e);
        }

        public void removeAll(){
            g_list.clear();
        }

        @Override
        public int getCount() {
            return g_list.size();
        }

        @Override
        public Object getItem(int i) {
            return g_list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            final int pos = i;
            final Context con = viewGroup.getContext();
            g_values = (Values) con.getApplicationContext();

            if(view == null){
                LayoutInflater inflater = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.view_mypage_group_list, viewGroup, false);

                iv_icon = (ImageView) view.findViewById(R.id.iv_group_icon);
                itv_delete = (IconTextView) view.findViewById(R.id.itv_mypage_group_delete);
                tv_main = (TextView) view.findViewById(R.id.tv_main_group);

                if(!g_list.get(pos).main){
                    tv_main.setVisibility(View.GONE);

                    // 추후 소속 탈퇴 구현 시 아래 매개변수를 View.VISIBLE 로 변경하여 사용할 것.
                    itv_delete.setVisibility(View.GONE);
                }
                else{
                    tv_main.setVisibility(View.VISIBLE);
                    itv_delete.setVisibility(View.GONE);
                }

                if(!g_list.get(pos).active){
                    tv_main.setVisibility(View.VISIBLE);
                    tv_main.setText("승인 대기");

                    // 추후 소속 탈퇴 구현 시 아래 매개변수를 View.VISIBLE 로 변경하여 사용할 것.
                    itv_delete.setVisibility(View.GONE);
                }

                iv_icon.setImageBitmap(g_list.get(pos).icon);

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        iv_group_icon.setImageBitmap(g_list.get(pos).icon);
                        ftv_group_name.setText(g_list.get(pos).name);
                        ftv_group_level.setText(g_list.get(pos).level);
                        if(!g_list.get(pos).main){
                            ll_main_set_btn.setVisibility(View.VISIBLE);
                        }else{
                            ll_main_set_btn.setVisibility(View.GONE);
                        }
                        select = pos;
                    }
                });
            }

            return view;
        }
    }

    private class GroupInfo{
        public int union_id;
        public Bitmap icon;
        public String name;
        public String level;
        public boolean main;
        public boolean active;

        public GroupInfo(int id,Bitmap icon, String name, String level, boolean active){
            this.union_id = id;
            this.icon = icon;
            this.name = name;
            this.level = level;
            this.active = active;
            this.main = false;
        }
    }

    private class UnionTask extends AsyncTask<Void, Void, Boolean>{
        @Override
        protected void onPreExecute() {
            MainActivity.ll_main_main_loading.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            values.unions = Functions.GET("get_union_info&id="+values.user_id);
            try {
                for (int i = 0; i < values.unions.length(); i++) {
                    TryCatchJO tmp = new TryCatchJO(values.unions.getJSONObject(i));
                    int id = tmp.get("id",-1);
                    String union_title = tmp.get("title","-");
                    String union_level = tmp.get("level_title","-");
                    String union_icon = tmp.get("icon","-");
                    boolean active = tmp.get("unionUser_active", false);
                    if(union_title.contains("임시소속"))continue;
                    Bitmap icon = Functions.getBitmapFromURL(Functions.DOMAIN+union_icon);
                    GroupInfo g_tmp = new GroupInfo(id,icon, union_title, union_level, active);
                    if(g_tmp.union_id == UserData.getMainUnion())g_tmp.main = true;
                    union_list.add(g_tmp);
                }
            }catch(JSONException e){
                Toast.makeText(con,"오류가 발생하였습니다!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
//            List<GroupInfo> tmp_list = new ArrayList<GroupInfo>();
//            try {
//                for (int i = 0; i < values.unions.length(); i++) {
//                    TryCatchJO unions = new TryCatchJO(values.unions.getJSONObject(i));
//                    int id = unions.get("id",-1);
//                    String union_title = unions.get("title","-");
//                    String union_level = unions.get("level_title", "-");
//                    String union_icon = unions.get("icon", "-");
//                    boolean active = unions.get("unionUser_active",false);
//                    Log.i("@@info","is_active:"+active);
//                    if(union_title.contains("임시소속")){
//                        Log.i("@@info", "length:"+unions.length());
//                        Log.i("@@info","break! title:"+union_title);
//                        continue;
//                    }
//                    Bitmap icon = Functions.getBitmapFromURL(Functions.DOMAIN+union_icon);
//                    if(id == UserData.getMainUnion())tmp_list.add(new GroupInfo(id,icon,union_title,union_level,true, active));
//                    else tmp_list.add(new GroupInfo(id,icon, union_title, union_level, false, active));
//                }
//            }catch(JSONException e){
//                Toast.makeText(con,"오류가 발생하였습니다!",Toast.LENGTH_SHORT).show();
//                e.printStackTrace();
//            }
//
//            Collections.sort(tmp_list, new Comparator<GroupInfo>() {
//                @Override
//                public int compare(GroupInfo groupInfo, GroupInfo t1) {
//                    if (groupInfo.main) return -1;
//                    else if (t1.main) return 1;
//                    else return 0;
//                }
//            });
//
//            for(int i=0;i<tmp_list.size();i++)adapt.add(tmp_list.get(i));

            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result){
//                GroupInfo info = (GroupInfo)adapt.getItem(0);
//                iv_group_icon.setImageBitmap(info.icon);
//                ftv_group_name.setText(info.name);
//                ftv_group_level.setText(info.level);
//                MainActivity.ll_main_main_loading.setVisibility(View.GONE);
                setAdapt();


                MainActivity.ll_main_main_loading.setVisibility(View.GONE);
            }
        }
    }
}
