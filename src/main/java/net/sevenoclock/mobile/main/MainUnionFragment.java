package net.sevenoclock.mobile.main;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.customobj.TryCatchJO;
import net.sevenoclock.mobile.home.LoadingActivity;
import net.sevenoclock.mobile.settings.Functions;
import net.sevenoclock.mobile.settings.Values;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainUnionFragment extends Fragment {

    private Context con;

    private ListView lv_main_union_list;
    private MainUnionAdapter mua;

    Values values;

    public static MainUnionFragment newInstance() {
        MainUnionFragment view = new MainUnionFragment();
        return view;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        con = container.getContext();
        values = (Values) con.getApplicationContext();

        View v = inflater.inflate(R.layout.fragment_main_union, container, false);

        MainActivity.setTitle("내 소속");
        MainActivity.setSubtitle(String.format("총 %d개의 소속이 있습니다.", values.unions.length()));

        lv_main_union_list = (ListView) v.findViewById(R.id.lv_main_union_list);

        mua = new MainUnionAdapter();
        lv_main_union_list.setAdapter(mua);
        lv_main_union_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                try {
                    Vibrator Vibe = (Vibrator) con.getSystemService(con.VIBRATOR_SERVICE);
                    Vibe.vibrate(30);
                    values.union_info = new TryCatchJO(values.unions.getJSONObject(position));
                    con.startActivity(new Intent(getActivity(), LoadingActivity.class));
                    getActivity().finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        new MainUnionTask().execute(null, null, null);

        return v;
    }

    class MainUnionTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            MainActivity.ll_main_main_loading.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        protected Boolean doInBackground(Void... Void) {
            Log.i("@@@@@@@",""+values.unions.length());
            return true;
        }

        protected void onPostExecute(Boolean result) {
            if(result) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mua.notifyDataSetChanged();
                        for (int i = 0; i < values.unions.length(); i++) {
                            try {
                                mua.add(new TryCatchJO(values.unions.getJSONObject(i)));
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                        }
                        MainActivity.ll_main_main_loading.setVisibility(View.GONE);
                    }
                });
                return;
            }
            MainActivity.ll_main_main_loading.setVisibility(View.GONE);
            return;
        }
    }

}
