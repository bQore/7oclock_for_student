package net.sevenoclock.mobile.qna;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.ImageOptions;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.customobj.FontTextView;
import net.sevenoclock.mobile.customobj.TryCatchJO;
import net.sevenoclock.mobile.settings.Functions;
import net.sevenoclock.mobile.settings.Values;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class QnADetailAdapter extends BaseAdapter {

    private int qna_host_id = 0;

    private ArrayList<TryCatchJO> m_List;

    private ImageView iv_qna_detail_icon;
    private FontTextView tv_qna_detail_username;
    private ImageView iv_qna_detail_question;
    private ProgressBar pb_qna_detail_loading;
    private FontTextView tv_qna_detail_contents;
    public FontTextView tv_qna_detail_select;

    Values values;

    public QnADetailAdapter(int host_id) {
        m_List = new ArrayList<TryCatchJO>();
        qna_host_id = host_id;
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
            convertView = inflater.inflate(R.layout.view_qna_detail, parent, false);

            iv_qna_detail_icon = (ImageView) convertView.findViewById(R.id.iv_qna_detail_icon);
            tv_qna_detail_username = (FontTextView) convertView.findViewById(R.id.tv_qna_detail_username);
            iv_qna_detail_question = (ImageView) convertView.findViewById(R.id.iv_qna_detail_question);
            iv_qna_detail_question.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent it = new Intent(context, QnADetailImage.class);
                    ImageView tmp = (ImageView)view;
                    BitmapDrawable bd = (BitmapDrawable)tmp.getDrawable();
                    Bitmap img = bd.getBitmap();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    img.compress(Bitmap.CompressFormat.PNG,100,stream);
                    byte[] imgbytes = stream.toByteArray();
                    it.putExtra("img",imgbytes);
                    context.startActivity(it);
                }
            });
            iv_qna_detail_question.setScaleType(ImageView.ScaleType.CENTER_CROP);
            pb_qna_detail_loading = (ProgressBar) convertView.findViewById(R.id.pb_qna_detail_loading);
            tv_qna_detail_contents = (FontTextView) convertView.findViewById(R.id.tv_qna_detail_contents);
            tv_qna_detail_select = (FontTextView) convertView.findViewById(R.id.tv_qna_detail_select);

            holder = new CustomHolder();
            holder._iv_qna_detail_icon = iv_qna_detail_icon;
            holder._tv_qna_detail_username = tv_qna_detail_username;
            holder._iv_qna_detail_question = iv_qna_detail_question;
            holder._pb_qna_detail_loading = pb_qna_detail_loading;
            holder._tv_qna_detail_contents = tv_qna_detail_contents;
            holder._tv_qna_detail_select = tv_qna_detail_select;
            convertView.setTag(holder);
        }else {
            holder  = (CustomHolder) convertView.getTag();
            iv_qna_detail_icon = holder._iv_qna_detail_icon;
            tv_qna_detail_username = holder._tv_qna_detail_username;
            iv_qna_detail_question = holder._iv_qna_detail_question;
            pb_qna_detail_loading = holder._pb_qna_detail_loading;
            tv_qna_detail_contents = holder._tv_qna_detail_contents;
            tv_qna_detail_select = holder._tv_qna_detail_select;
        }

        ImageOptions options = new ImageOptions();
        options.round = 1000;
        Bitmap bitmap = values.aq.id(iv_qna_detail_icon).getCachedImage(Functions.DOMAIN + m_List.get(pos).get("user_src",""));
        if(bitmap != null) values.aq.id(iv_qna_detail_icon).image(Functions.borderRadius(bitmap, 1000));
        else values.aq.id(iv_qna_detail_icon).image(Functions.DOMAIN + m_List.get(pos).get("user_src",""), options);

        tv_qna_detail_username.setText(m_List.get(pos).get("username","-"));
        tv_qna_detail_contents.setText(m_List.get(pos).get("contents","-"));
        tv_qna_detail_select.setTag(m_List.get(pos).get("id","-"));

        try {
            values.aq.id(iv_qna_detail_question)
                    .progress(pb_qna_detail_loading)
                    .image(Functions.DOMAIN +m_List.get(pos).getJSONArray("src").get(0), true, true);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(qna_host_id == values.user_id){
            tv_qna_detail_select.setVisibility(View.VISIBLE);
            tv_qna_detail_select.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    values.aq.ajax(Functions.DOMAIN +"/mobile/?mode=select_qna_answer&answer_id="+v.getTag(), String.class, new AjaxCallback<String>() {
                        @Override
                        public void callback(String url, String json, AjaxStatus status) {
                            Functions.QnADetail_reflesh(context);
                        }
                    });
                }
            });
            if(m_List.get(pos).get("is_selected",0) == 1){
                tv_qna_detail_select.setText("★");
                tv_qna_detail_select.setTextColor(Color.parseColor("#f4a62a"));
            }else{
                tv_qna_detail_select.setText("☆");
//                tv_qna_detail_select.setTextColor(Color.parseColor("#CCCCCC"));
            }
        }else{
            if(m_List.get(pos).get("is_selected",0) == 1){
                tv_qna_detail_select.setVisibility(View.VISIBLE);
                tv_qna_detail_select.setText("★");
                tv_qna_detail_select.setTextColor(Color.parseColor("#f4a62a"));
            }else{
                tv_qna_detail_select.setVisibility(View.GONE);
            }
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
        ImageView _iv_qna_detail_icon;
        FontTextView _tv_qna_detail_username;
        ImageView _iv_qna_detail_question;
        ProgressBar _pb_qna_detail_loading;
        FontTextView _tv_qna_detail_contents;
        FontTextView _tv_qna_detail_select;
    }
}