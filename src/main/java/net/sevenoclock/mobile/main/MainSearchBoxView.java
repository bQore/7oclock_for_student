package net.sevenoclock.mobile.main;

import android.content.Context;
import android.util.Log;
import android.widget.LinearLayout;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.customobj.FontTextView;
import net.sevenoclock.mobile.settings.Values;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainSearchBoxView extends LinearLayout {

    private Context con;

    private JSONArray ja_unit;

    private FontTextView tv_main_search_box_title;
    private LinearLayout ll_main_search_box_list;

    Values values;

    public MainSearchBoxView(Context context, JSONObject jo_box) throws JSONException {
        super(context);
        this.con = context;
        values = (Values) context.getApplicationContext();

        inflate(getContext(), R.layout.view_main_search_box, this);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        tv_main_search_box_title = (FontTextView)findViewById(R.id.tv_main_search_box_title);
        ll_main_search_box_list = (LinearLayout)findViewById(R.id.ll_main_search_box_list);

        tv_main_search_box_title.setText(jo_box.getString("title"));

        ja_unit = jo_box.getJSONArray("unit");

        for(int i=0; i<ja_unit.length(); i++){
            ll_main_search_box_list.addView(new MainSearchBoxListView(con, ja_unit.getJSONObject(i)));
        }

    }

}