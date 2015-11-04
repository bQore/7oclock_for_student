package net.sevenoclock.mobile.quiz;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.customobj.FontTextView;
import net.sevenoclock.mobile.customobj.TryCatchJO;
import net.sevenoclock.mobile.settings.Values;

import java.util.ArrayList;

public class QuizFinalListAdapter extends BaseAdapter {

    private ArrayList<String[]> m_List;

    public String qid = "";
    public String answer = "";

    FontTextView tv_quiz_final_list_index;
    FontTextView tv_quiz_final_list_answer;
    FontTextView tv_quiz_final_list_more;

    Values values;

    public QuizFinalListAdapter() {
        m_List = new ArrayList<String[]>();
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
            convertView = inflater.inflate(R.layout.view_quiz_final_list_form, parent, false);

            tv_quiz_final_list_index = (FontTextView) convertView.findViewById(R.id.tv_quiz_final_list_index);
            tv_quiz_final_list_answer = (FontTextView) convertView.findViewById(R.id.tv_quiz_final_list_answer);
            tv_quiz_final_list_more = (FontTextView) convertView.findViewById(R.id.tv_quiz_final_list_more);

            holder = new CustomHolder();
            holder._tv_quiz_final_list_index = tv_quiz_final_list_index;
            holder._tv_quiz_final_list_answer = tv_quiz_final_list_answer;
            holder._tv_quiz_final_list_more = tv_quiz_final_list_more;
            convertView.setTag(holder);
        }else {
            holder  = (CustomHolder) convertView.getTag();
            tv_quiz_final_list_index = holder._tv_quiz_final_list_index;
            tv_quiz_final_list_answer = holder._tv_quiz_final_list_answer;
            tv_quiz_final_list_more = holder._tv_quiz_final_list_more;
        }

        qid = m_List.get(pos)[0];
        answer = m_List.get(pos)[1];

        if(qid != ""){
            tv_quiz_final_list_index.setText("" + (pos + 1));
            tv_quiz_final_list_answer.setText(answer);
            tv_quiz_final_list_more.setVisibility(View.GONE);
        }

        return convertView;
    }

    public void add(String[] arr) {
        m_List.add(arr);
    }

    public void remove(int _position) {
        m_List.remove(_position);
    }

    public void reflesh() {
        m_List.clear();
    }

    private class CustomHolder {
        FontTextView _tv_quiz_final_list_index;
        FontTextView _tv_quiz_final_list_answer;
        FontTextView _tv_quiz_final_list_more;
    }
}