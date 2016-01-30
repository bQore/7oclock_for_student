package net.sevenoclock.mobile.main;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

public class MainUnionAdapter extends BaseAdapter {

    private ArrayList<TryCatchJO> m_List;

    private ImageView iv_main_union_list_icon;
    private FontTextView tv_main_union_list_title;

    Values values;

    public MainUnionAdapter() {
        m_List = new ArrayList<TryCatchJO>();
    }

    @Override
    public int getCount() {
        return m_List.size();
    }

    @Override
    public TryCatchJO getItem(int position) {
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
            convertView = inflater.inflate(R.layout.view_main_union_list, parent, false);

            iv_main_union_list_icon = (ImageView) convertView.findViewById(R.id.iv_main_union_list_icon);
            tv_main_union_list_title = (FontTextView) convertView.findViewById(R.id.tv_main_union_list_title);

            holder = new CustomHolder();
            holder._iv_main_union_list_icon = iv_main_union_list_icon;
            holder._tv_main_union_list_title = tv_main_union_list_title;
            convertView.setTag(holder);
        }else {
            holder  = (CustomHolder) convertView.getTag();
            iv_main_union_list_icon = holder._iv_main_union_list_icon;
            tv_main_union_list_title = holder._tv_main_union_list_title;
        }

//        ImageOptions options = new ImageOptions();
//        options.round = 1000;
//        BitmapFactory.Options option = new BitmapFactory.Options();
//        Bitmap bitmap = values.aq.id(iv_main_union_list_icon).getCachedImage(Functions.DOMAIN + m_List.get(pos).get("icon", ""),300);
//        if(bitmap != null) values.aq.id(iv_main_union_list_icon).image(Functions.borderRadius(bitmap, 1000));
//        else values.aq.id(iv_main_union_list_icon).image(Functions.DOMAIN + m_List.get(pos).get("icon", ""), options);

        tv_main_union_list_title.setText(m_List.get(pos).get("group_title", ""));

        return convertView;
    }

    public void add(TryCatchJO obj) {
        Log.i("@@info",obj.get("group_id",0)+"'s info:"+obj.get("group_title",""));
        m_List.add(obj);
    }

    public void remove(int _position) {
        m_List.remove(_position);
    }

    private class CustomHolder {
        ImageView _iv_main_union_list_icon;
        FontTextView _tv_main_union_list_title;
    }
}