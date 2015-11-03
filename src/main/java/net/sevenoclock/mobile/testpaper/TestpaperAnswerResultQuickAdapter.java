package net.sevenoclock.mobile.testpaper;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
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

public class TestpaperAnswerResultQuickAdapter extends BaseAdapter {

    private ArrayList<TryCatchJO> m_List;

    public int qid = 0;
    public String answer = "";

    FontTextView tv_testpaper_answer_result_quick_index;
    FontTextView tv_testpaper_answer_result_quick_answer;

    Values values;

    public TestpaperAnswerResultQuickAdapter() {
        m_List = new ArrayList<TryCatchJO>();
    }

    @Override
    public int getCount() {
        Log.i("@@@", "" + m_List.size());
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
            convertView = inflater.inflate(R.layout.view_testpaper_answer_result_form, parent, false);

            tv_testpaper_answer_result_quick_index = (FontTextView) convertView.findViewById(R.id.tv_testpaper_answer_result_quick_index);
            tv_testpaper_answer_result_quick_answer = (FontTextView) convertView.findViewById(R.id.tv_testpaper_answer_result_quick_answer);

            holder = new CustomHolder();
            holder._tv_testpaper_answer_result_quick_index = tv_testpaper_answer_result_quick_index;
            holder._tv_testpaper_answer_result_quick_answer = tv_testpaper_answer_result_quick_answer;
            convertView.setTag(holder);
        }else {
            holder  = (CustomHolder) convertView.getTag();
            tv_testpaper_answer_result_quick_index = holder._tv_testpaper_answer_result_quick_index;
            tv_testpaper_answer_result_quick_answer = holder._tv_testpaper_answer_result_quick_answer;
        }

        qid = m_List.get(pos).get("id", 0);
        answer = m_List.get(pos).get("answer_mobile","");

        if(qid != 0){
            tv_testpaper_answer_result_quick_index.setText("" + (pos + 1));
            tv_testpaper_answer_result_quick_answer.setText(answer);
        }

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
        FontTextView _tv_testpaper_answer_result_quick_index;
        FontTextView _tv_testpaper_answer_result_quick_answer;
    }
}