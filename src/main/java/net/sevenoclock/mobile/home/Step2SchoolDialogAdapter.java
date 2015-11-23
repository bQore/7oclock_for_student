package net.sevenoclock.mobile.home;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.customobj.FontTextView;
import net.sevenoclock.mobile.customobj.IconTextView;
import net.sevenoclock.mobile.customobj.TryCatchJO;
import net.sevenoclock.mobile.settings.Values;

import java.util.ArrayList;

public class Step2SchoolDialogAdapter extends BaseAdapter {

    private ArrayList<String[]> m_List;

    FontTextView tv_home_step2_schooldialog_list_name;
    FontTextView tv_home_step2_schooldialog_list_address;

    Values values;

    public Step2SchoolDialogAdapter() {
        m_List = new ArrayList<String[]>();
    }

    @Override
    public int getCount() { return m_List.size(); }

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
            convertView = inflater.inflate(R.layout.view_home_step2_schooldialog_list, parent, false);
            tv_home_step2_schooldialog_list_name = (FontTextView) convertView.findViewById(R.id.tv_home_step2_schooldialog_list_name);
            tv_home_step2_schooldialog_list_address = (FontTextView) convertView.findViewById(R.id.tv_home_step2_schooldialog_list_address);

            holder = new CustomHolder();
            holder._tv_home_step2_schooldialog_list_name = tv_home_step2_schooldialog_list_name;
            holder._tv_home_step2_schooldialog_list_address = tv_home_step2_schooldialog_list_address;
            convertView.setTag(holder);
        }else {
            holder  = (CustomHolder) convertView.getTag();
            tv_home_step2_schooldialog_list_name = holder._tv_home_step2_schooldialog_list_name;
            tv_home_step2_schooldialog_list_address = holder._tv_home_step2_schooldialog_list_address;
        }

        tv_home_step2_schooldialog_list_name.setText(m_List.get(pos)[0]);
        tv_home_step2_schooldialog_list_address.setText(m_List.get(pos)[1]);

        return convertView;
    }

    public void add(String[] obj) {
        m_List.add(obj);
    }

    public void remove(int _position) {
        m_List.remove(_position);
    }

    public void reflesh() {
        m_List.clear();
    }

    private class CustomHolder {
        FontTextView _tv_home_step2_schooldialog_list_name;
        FontTextView _tv_home_step2_schooldialog_list_address;
    }
}