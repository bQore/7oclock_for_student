package net.sevenoclock.mobile.qna;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.customobj.FontTextView;
import net.sevenoclock.mobile.customobj.TryCatchJO;
import net.sevenoclock.mobile.main.MainActivity;
import net.sevenoclock.mobile.settings.Functions;
import net.sevenoclock.mobile.settings.Values;
import org.json.JSONException;

import java.util.ArrayList;

public class QnAQuestionListAdapter extends BaseAdapter {

    private ArrayList<TryCatchJO> m_List;

    private ImageView iv_qna_question_list_list_src;
    private ProgressBar pb_qna_question_list_list_loading;
    private FontTextView tv_qna_question_list_list_unit;
    private FontTextView tv_qna_question_list_list_username;
    private FontTextView tv_qna_question_list_list_contents;
    private FontTextView tv_qna_question_list_list_time;
    private ImageView iv_qna_question_list_list_more;

    Values values;

    public QnAQuestionListAdapter() {
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
            convertView = inflater.inflate(R.layout.view_qnaquestion_list_list, parent, false);

            iv_qna_question_list_list_src = (ImageView) convertView.findViewById(R.id.iv_qna_question_list_list_src);
            pb_qna_question_list_list_loading = (ProgressBar) convertView.findViewById(R.id.pb_qna_question_list_list_loading);
            tv_qna_question_list_list_unit = (FontTextView) convertView.findViewById(R.id.tv_qna_question_list_list_unit);
            tv_qna_question_list_list_username = (FontTextView) convertView.findViewById(R.id.tv_qna_question_list_list_username);
            tv_qna_question_list_list_contents = (FontTextView) convertView.findViewById(R.id.tv_qna_question_list_list_contents);
            tv_qna_question_list_list_time = (FontTextView) convertView.findViewById(R.id.tv_qna_question_list_list_time);
            iv_qna_question_list_list_more = (ImageView) convertView.findViewById(R.id.iv_qna_question_list_list_more);

            holder = new CustomHolder();
            holder._iv_qna_question_list_list_src = iv_qna_question_list_list_src;
            holder._pb_qna_question_list_list_loading = pb_qna_question_list_list_loading;
            holder._tv_qna_question_list_list_unit = tv_qna_question_list_list_unit;
            holder._tv_qna_question_list_list_username = tv_qna_question_list_list_username;
            holder._tv_qna_question_list_list_contents = tv_qna_question_list_list_contents;
            holder._tv_qna_question_list_list_time = tv_qna_question_list_list_time;
            holder._iv_qna_question_list_list_more = iv_qna_question_list_list_more;
            convertView.setTag(holder);
        }else {
            holder  = (CustomHolder) convertView.getTag();
            iv_qna_question_list_list_src = holder._iv_qna_question_list_list_src;
            pb_qna_question_list_list_loading = holder._pb_qna_question_list_list_loading;
            tv_qna_question_list_list_unit = holder._tv_qna_question_list_list_unit;
            tv_qna_question_list_list_username = holder._tv_qna_question_list_list_username;
            tv_qna_question_list_list_contents = holder._tv_qna_question_list_list_contents;
            tv_qna_question_list_list_time = holder._tv_qna_question_list_list_time;
            iv_qna_question_list_list_more = holder._iv_qna_question_list_list_more;
        }

        try {
            values.aq.id(iv_qna_question_list_list_src)
                    .progress(pb_qna_question_list_list_loading)
                    .image(Functions.DOMAIN +m_List.get(pos).getJSONArray("src").get(0), true, true, 180, 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        tv_qna_question_list_list_unit.setText(String.format("%s | %s",m_List.get(pos).get("unit1",""),m_List.get(pos).get("unit2","")));
        tv_qna_question_list_list_username.setText(m_List.get(pos).get("username",""));
        if (m_List.get(pos).get("contents","").length() > 20) tv_qna_question_list_list_contents.setText(m_List.get(pos).get("contents","").substring(0,20)+"...");
        else tv_qna_question_list_list_contents.setText(m_List.get(pos).get("contents",""));
        tv_qna_question_list_list_time.setText(Functions.getTimeFormat(m_List.get(pos).get("date_created",0)));
        if(m_List.get(pos).get("user_id",0) == values.user_id){
            iv_qna_question_list_list_more.setVisibility(View.VISIBLE);
            iv_qna_question_list_list_more.setOnClickListener((View.OnClickListener)context);
        }else{
            iv_qna_question_list_list_more.setVisibility(View.GONE);
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
        ImageView _iv_qna_question_list_list_src;
        ProgressBar _pb_qna_question_list_list_loading;
        FontTextView _tv_qna_question_list_list_unit;
        FontTextView _tv_qna_question_list_list_username;
        FontTextView _tv_qna_question_list_list_contents;
        FontTextView _tv_qna_question_list_list_time;
        ImageView _iv_qna_question_list_list_more;
    }
}