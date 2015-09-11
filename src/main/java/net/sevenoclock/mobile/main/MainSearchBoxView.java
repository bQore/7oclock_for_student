package net.sevenoclock.mobile.main;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.LinearLayout;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.customobj.FontTextView;
import net.sevenoclock.mobile.settings.Functions;
import net.sevenoclock.mobile.settings.Values;
import org.json.JSONArray;
import org.json.JSONException;

public class MainSearchBoxView extends LinearLayout {

    private Context con;

    private FontTextView tv_main_search_box_title;

    Values values;

    public MainSearchBoxView(Context context, JSONArray jo_box) throws JSONException {
        super(context);
        this.con = context;
        values = (Values) context.getApplicationContext();

        inflate(getContext(), R.layout.view_main_search_box, this);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        tv_main_search_box_title = (FontTextView)findViewById(R.id.tv_main_search_box_title);

        tv_main_search_box_title.setText(jo_box.getJSONObject(0).getString("title"));

    }

}