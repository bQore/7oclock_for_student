package net.sevenoclock.mobile.testpaper;

import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.customobj.FontTextView;
import net.sevenoclock.mobile.customobj.TryCatchJO;
import net.sevenoclock.mobile.settings.Values;

import java.util.ArrayList;

public class TestpaperListAdapter extends BaseAdapter {

    private ArrayList<TryCatchJO> m_List;

    private FontTextView tv_testpaper_list_book_title;
    private FontTextView tv_testpaper_list_book_date;
    private FontTextView tv_testpaper_list_book_purpose;

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

            tv_testpaper_list_book_title = (FontTextView) convertView.findViewById(R.id.tv_testpaper_list_book_title);
            tv_testpaper_list_book_date = (FontTextView) convertView.findViewById(R.id.tv_testpaper_list_book_date);
            tv_testpaper_list_book_purpose = (FontTextView) convertView.findViewById(R.id.tv_testpaper_list_book_purpose);

            holder = new CustomHolder();
            holder._tv_testpaper_list_book_title = tv_testpaper_list_book_title;
            holder._tv_testpaper_list_book_date = tv_testpaper_list_book_date;
            holder._tv_testpaper_list_book_purpose = tv_testpaper_list_book_purpose;
            convertView.setTag(holder);
        }else {
            holder  = (CustomHolder) convertView.getTag();
            tv_testpaper_list_book_title = holder._tv_testpaper_list_book_title;
            tv_testpaper_list_book_date = holder._tv_testpaper_list_book_date;
            tv_testpaper_list_book_purpose = holder._tv_testpaper_list_book_purpose;
        }

        tv_testpaper_list_book_title.setText(m_List.get(pos).get("title", ""));
        tv_testpaper_list_book_date.setText(m_List.get(pos).get("date", ""));
        tv_testpaper_list_book_purpose.setText(m_List.get(pos).get("purpose", ""));

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
        FontTextView _tv_testpaper_list_book_title;
        FontTextView _tv_testpaper_list_book_date;
        FontTextView _tv_testpaper_list_book_purpose;
    }
}