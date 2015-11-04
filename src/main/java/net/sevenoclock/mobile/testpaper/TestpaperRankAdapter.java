package net.sevenoclock.mobile.testpaper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.androidquery.callback.ImageOptions;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.customobj.FontTextView;
import net.sevenoclock.mobile.customobj.TryCatchJO;
import net.sevenoclock.mobile.settings.Functions;
import net.sevenoclock.mobile.settings.Values;

import java.util.ArrayList;

public class TestpaperRankAdapter extends BaseAdapter {

    private ArrayList<TryCatchJO> m_List;

    private FontTextView tv_testpaper_rank_list_index;
    private ImageView iv_testpaper_rank_list_pic;
    private ImageView iv_testpaper_rank_list_crown;
    private FontTextView tv_testpaper_rank_list_name;
    private FontTextView tv_testpaper_rank_list_score;

    private boolean isShownScore = true;

    Values values;

    public TestpaperRankAdapter(boolean isShownScore) {
        this.isShownScore = isShownScore;
        m_List = new ArrayList<TryCatchJO>();
    }

    @Override
    public int getCount() {
        return m_List.size();
    }

    @Override
    public Object getItem(int position) {
        return m_List.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();
        values = (Values) context.getApplicationContext();
        CustomHolder holder  = null;

        if ( convertView == null ) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.view_testpaper_rank_list, parent, false);

            tv_testpaper_rank_list_index = (FontTextView) convertView.findViewById(R.id.tv_testpaper_rank_list_index);
            iv_testpaper_rank_list_pic = (ImageView) convertView.findViewById(R.id.iv_testpaper_rank_list_pic);
            iv_testpaper_rank_list_crown = (ImageView) convertView.findViewById(R.id.iv_testpaper_rank_list_crown);
            tv_testpaper_rank_list_name = (FontTextView) convertView.findViewById(R.id.tv_testpaper_rank_list_name);
            tv_testpaper_rank_list_score = (FontTextView) convertView.findViewById(R.id.tv_testpaper_rank_list_score);

            holder = new CustomHolder();
            holder._tv_testpaper_rank_list_index = tv_testpaper_rank_list_index;
            holder._iv_testpaper_rank_list_pic = iv_testpaper_rank_list_pic;
            holder._iv_testpaper_rank_list_crown = iv_testpaper_rank_list_crown;
            holder._tv_testpaper_rank_list_name = tv_testpaper_rank_list_name;
            holder._tv_testpaper_rank_list_score = tv_testpaper_rank_list_score;
            convertView.setTag(holder);
        }else {
            holder  = (CustomHolder) convertView.getTag();
            tv_testpaper_rank_list_index = holder._tv_testpaper_rank_list_index;
            iv_testpaper_rank_list_pic = holder._iv_testpaper_rank_list_pic;
            iv_testpaper_rank_list_crown = holder._iv_testpaper_rank_list_crown;
            tv_testpaper_rank_list_name = holder._tv_testpaper_rank_list_name;
            tv_testpaper_rank_list_score = holder._tv_testpaper_rank_list_score;
        }

        if (m_List.get(pos) == null) {
            setNull();
            return convertView;
        }

        tv_testpaper_rank_list_index.setText(m_List.get(pos).get("rank", ""));
        ImageOptions options = new ImageOptions();
        options.round = 1000;
        Bitmap bitmap = values.aq.id(iv_testpaper_rank_list_pic).getCachedImage(Functions.DOMAIN + m_List.get(pos).get("src", ""));
        if(bitmap != null) values.aq.id(iv_testpaper_rank_list_pic).image(Functions.borderRadius(bitmap, 1000));
        else values.aq.id(iv_testpaper_rank_list_pic).image(Functions.DOMAIN + m_List.get(pos).get("src", ""), options);
        tv_testpaper_rank_list_name.setText(m_List.get(pos).get("username", ""));

        if (m_List.get(pos).get("rank", 0) == 0) {
            setNull();
            return convertView;
        }else if (m_List.get(pos).get("rank", 0) == 1) {
            tv_testpaper_rank_list_index.setBackgroundColor(Color.parseColor("#f1c40f"));
            iv_testpaper_rank_list_crown.setVisibility(View.VISIBLE);
        }else{
            tv_testpaper_rank_list_index.setBackgroundColor(Color.parseColor("#34495e"));
            iv_testpaper_rank_list_crown.setVisibility(View.GONE);
        }
        if(!isShownScore) tv_testpaper_rank_list_score.setText("");
        else tv_testpaper_rank_list_score.setText(String.format("%.1f점", m_List.get(pos).get("score", 0.0)));

        return convertView;
    }

    void setNull(){
        tv_testpaper_rank_list_index.setText("-");
        values.aq.id(iv_testpaper_rank_list_pic).image(Functions.borderRadius(Functions.DOMAIN + values.user_info.get("src", ""), 1000));
        tv_testpaper_rank_list_name.setText(values.user_info.get("first_name", ""));
        tv_testpaper_rank_list_score.setTextSize(14);
        tv_testpaper_rank_list_score.setText("미제출");
    }

    public void add(TryCatchJO obj) {
        m_List.add(obj);
    }

    public void remove(int _position) {
        m_List.remove(_position);
    }

    private class CustomHolder {
        FontTextView _tv_testpaper_rank_list_index;
        ImageView _iv_testpaper_rank_list_pic;
        ImageView _iv_testpaper_rank_list_crown;
        FontTextView _tv_testpaper_rank_list_name;
        FontTextView _tv_testpaper_rank_list_score;
    }
}