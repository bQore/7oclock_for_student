package net.sevenoclock.mobile.search;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.androidquery.AQuery;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.settings.Functions;
import org.json.JSONArray;

public class SearchListView extends Fragment {

    private Context con;

    private ListView lv_search_list_list;
    private ArrayAdapter<String> lv_search_list_adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        con = container.getContext();

        View view = inflater.inflate(R.layout.view_search_list, container, false);
        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        lv_search_list_list = (ListView)view.findViewById(R.id.lv_search_list_list);
        lv_search_list_adapter = new ArrayAdapter<String>(con, android.R.layout.simple_list_item_1);

        JSONArray ja_unit = Functions.GET(String.format("get_question_unit_new&u%d=%d", unit_level + 1, unit_id));

        lv_search_list_list.setAdapter(lv_search_list_adapter);
        lv_search_list_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Vibrator Vibe = (Vibrator) con.getSystemService(con.VIBRATOR_SERVICE);
                Vibe.vibrate(30);
            }
        });

        return view;
    }

    public static SearchListView newInstance(int unit_level,int unit_id) {
        SearchListView view = new SearchListView();
        Bundle args = new Bundle();
        args.putInt("unit_level", unit_level);
        args.putInt("unit_id", unit_id);
        view.setArguments(args);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
