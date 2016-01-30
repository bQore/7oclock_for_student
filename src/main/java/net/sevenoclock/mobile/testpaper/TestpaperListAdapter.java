package net.sevenoclock.mobile.testpaper;

import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.customobj.FontTextView;
import net.sevenoclock.mobile.customobj.TryCatchJO;
import net.sevenoclock.mobile.settings.Functions;
import net.sevenoclock.mobile.settings.Values;

import java.util.ArrayList;

public class TestpaperListAdapter extends BaseAdapter {

    private ArrayList<TryCatchJO> m_List;

    private ImageView iv_testpater_list_book_src;
    private FontTextView tv_testpaper_list_book_title;
    private FontTextView tv_testpaper_list_book_date;
//    private FontTextView tv_testpaper_list_book_purpose;
    private FontTextView tv_testpaper_list_book_teacher;

    Values values;

    public TestpaperListAdapter() {
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

            DisplayMetrics displayMetrics = new DisplayMetrics();
            displayMetrics = context.getResources().getDisplayMetrics();

            int book_height = (int) (90 * displayMetrics.density);

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.view_testpaper_list_book, parent, false);
            convertView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, book_height * 3 - 100));

            iv_testpater_list_book_src = (ImageView) convertView.findViewById(R.id.iv_testpaper_list_book_src);
            tv_testpaper_list_book_title = (FontTextView) convertView.findViewById(R.id.tv_testpaper_list_book_title);
            tv_testpaper_list_book_date = (FontTextView) convertView.findViewById(R.id.tv_testpaper_list_book_date);
//            tv_testpaper_list_book_purpose = (FontTextView) convertView.findViewById(R.id.tv_testpaper_list_book_purpose);
            tv_testpaper_list_book_teacher = (FontTextView) convertView.findViewById(R.id.tv_testpaper_list_book_teacher);

            holder = new CustomHolder();
            holder._iv_testpaper_list_book_src = iv_testpater_list_book_src;
            holder._tv_testpaper_list_book_title = tv_testpaper_list_book_title;
            holder._tv_testpaper_list_book_date = tv_testpaper_list_book_date;
//            holder._tv_testpaper_list_book_purpose = tv_testpaper_list_book_purpose;
            holder._tv_testpaper_list_book_teacher = tv_testpaper_list_book_teacher;
            convertView.setTag(holder);
        }else {
            holder  = (CustomHolder) convertView.getTag();
            iv_testpater_list_book_src= holder._iv_testpaper_list_book_src;
            tv_testpaper_list_book_title = holder._tv_testpaper_list_book_title;
            tv_testpaper_list_book_date = holder._tv_testpaper_list_book_date;
//            tv_testpaper_list_book_purpose = holder._tv_testpaper_list_book_purpose;
            tv_testpaper_list_book_teacher = holder._tv_testpaper_list_book_teacher;
        }

        values.aq.id(iv_testpater_list_book_src).image(Functions.borderRadius(Functions.DOMAIN+m_List.get(pos).get("icon",""),1000));
        tv_testpaper_list_book_title.setText(m_List.get(pos).get("title", ""));
        tv_testpaper_list_book_date.setText(m_List.get(pos).get("date", "").replace(".","-"));
//        tv_testpaper_list_book_purpose.setText(m_List.get(pos).get("purpose", ""));
        tv_testpaper_list_book_teacher.setText(values.union_info.get("title","-"));

        return convertView;
    }

    public void add(TryCatchJO obj) {
        m_List.add(obj);
    }

    public void remove(int _position) {
        m_List.remove(_position);
    }

    public void reflesh() {
        m_List.clear();
    }

    private class CustomHolder {
        ImageView _iv_testpaper_list_book_src;
        FontTextView _tv_testpaper_list_book_title;
        FontTextView _tv_testpaper_list_book_date;
//        FontTextView _tv_testpaper_list_book_purpose;
        FontTextView _tv_testpaper_list_book_teacher;
    }
}