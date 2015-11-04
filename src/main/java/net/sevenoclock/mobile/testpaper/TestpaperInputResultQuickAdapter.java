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

public class TestpaperInputResultQuickAdapter extends BaseAdapter {

    private ArrayList<TryCatchJO> m_List;

    public int qid = 0;
    public String answer_user = "";
    public String answer_correct = "";

    IconTextView tv_testpaper_input_result_quick_icon;
    FontTextView tv_testpaper_input_result_quick_index;
    FontTextView tv_testpaper_input_result_quick_o;
    FontTextView tv_testpaper_input_result_quick_x;

    Values values;

    public TestpaperInputResultQuickAdapter() {
        m_List = new ArrayList<TryCatchJO>();
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
            convertView = inflater.inflate(R.layout.view_testpaper_input_result_form, parent, false);

            tv_testpaper_input_result_quick_icon = (IconTextView) convertView.findViewById(R.id.tv_testpaper_input_result_quick_icon);
            tv_testpaper_input_result_quick_index = (FontTextView) convertView.findViewById(R.id.tv_testpaper_input_result_quick_index);
            tv_testpaper_input_result_quick_o = (FontTextView) convertView.findViewById(R.id.tv_testpaper_input_result_quick_o);
            tv_testpaper_input_result_quick_x = (FontTextView) convertView.findViewById(R.id.tv_testpaper_input_result_quick_x);

            holder = new CustomHolder();
            holder._tv_testpaper_input_result_quick_icon = tv_testpaper_input_result_quick_icon;
            holder._tv_testpaper_input_result_quick_index = tv_testpaper_input_result_quick_index;
            holder._tv_testpaper_input_result_quick_o = tv_testpaper_input_result_quick_o;
            holder._tv_testpaper_input_result_quick_x = tv_testpaper_input_result_quick_x;
            convertView.setTag(holder);
        }else {
            holder  = (CustomHolder) convertView.getTag();
            tv_testpaper_input_result_quick_icon = holder._tv_testpaper_input_result_quick_icon;
            tv_testpaper_input_result_quick_index = holder._tv_testpaper_input_result_quick_index;
            tv_testpaper_input_result_quick_o = holder._tv_testpaper_input_result_quick_o;
            tv_testpaper_input_result_quick_x = holder._tv_testpaper_input_result_quick_x;
        }

        qid = m_List.get(pos).get("tps_id", 0);
        answer_user = m_List.get(pos).get("answer_user", "");
        answer_correct = m_List.get(pos).get("answer_correct", "");

        tv_testpaper_input_result_quick_index.setText("" + (pos + 1));
        String[] answers = answer_correct.split(";");
        if(answers.length > 1){
            answer_correct = String.format("%s 또는 %s",answers[0],answers[1]);
            if(answers[0].equals(answer_user) || answers[1].equals(answer_user)){
                setAnswer(true);
                return convertView;
            }
        }else{
            if(answer_correct.equals(answer_user)){
                setAnswer(true);
                return convertView;
            }
        }
        setAnswer(false);

        return convertView;
    }

    void setAnswer(boolean b){
        if(b){
            tv_testpaper_input_result_quick_icon.setText(R.string.ic_testpaper_input_result_quick_o);
            tv_testpaper_input_result_quick_icon.setTextColor(Color.parseColor("#2ecc71"));
            tv_testpaper_input_result_quick_x.setVisibility(View.GONE);
            tv_testpaper_input_result_quick_o.setText(answer_user);
        }else{
            tv_testpaper_input_result_quick_icon.setText(R.string.ic_testpaper_input_result_quick_x);
            tv_testpaper_input_result_quick_icon.setTextColor(Color.parseColor("#e74c3c"));
            tv_testpaper_input_result_quick_x.setPaintFlags(tv_testpaper_input_result_quick_x.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            tv_testpaper_input_result_quick_x.setVisibility(View.VISIBLE);
            tv_testpaper_input_result_quick_x.setText(answer_user);
            tv_testpaper_input_result_quick_o.setText(answer_correct);
        }
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
        IconTextView _tv_testpaper_input_result_quick_icon;
        FontTextView _tv_testpaper_input_result_quick_index;
        FontTextView _tv_testpaper_input_result_quick_o;
        FontTextView _tv_testpaper_input_result_quick_x;
    }
}