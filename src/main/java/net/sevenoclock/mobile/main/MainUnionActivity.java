package net.sevenoclock.mobile.main;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.customobj.TryCatchJO;
import net.sevenoclock.mobile.home.LoadingActivity;
import net.sevenoclock.mobile.settings.Functions;
import net.sevenoclock.mobile.settings.UserData;
import net.sevenoclock.mobile.settings.Values;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class MainUnionActivity extends Activity {

    private ListView lv_main_union_list;
    private MainUnionAdapter mua;

    Values values;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
                WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        setContentView(R.layout.activity_main_union);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        MainActivity.setTitleVisible(MainActivity.ll_main_main_title.getVisibility());

        values = (Values) getApplicationContext();

        lv_main_union_list = (ListView) findViewById(R.id.lv_main_union_list);

        mua = new MainUnionAdapter();
        lv_main_union_list.setAdapter(mua);
        lv_main_union_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                try {
                    Vibrator Vibe = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                    Vibe.vibrate(30);
                    UserData.setMainGroup(mua.getItem(position).getInt("group_id"));
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

//        Map<String,Object> key = new HashMap<String, Object>();
//        key.put("id", values.user_id);
//        values.aq.ajax(Functions.DOMAIN + "/mobile/?mode=get_group_user_info_list", key, JSONArray.class, new AjaxCallback<JSONArray>() {
//            @Override
//            public void callback(String url, JSONArray object, AjaxStatus status) {
//                super.callback(url, object, status);
//                Log.i("@@group", "url:" + url + " code:" + status.getCode() + "  obj:" + object);
//                if (status.getCode() == 200) {
//                    try {
//                        for (int i = 0; i < object.length(); i++) {
//                            mua.add(new TryCatchJO(object.getJSONObject(i)));
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });

        new MainUnionTask().execute(null, null, null);
    }

    class MainUnionTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Boolean doInBackground(Void... Void) {
            Map<String,Object> key = new HashMap<String, Object>();
            key.put("id", values.user_id);
            values.aq.ajax(Functions.DOMAIN + "/mobile/?mode=get_group_user_info_list", key, JSONArray.class, new AjaxCallback<JSONArray>() {
                @Override
                public void callback(String url, JSONArray object, AjaxStatus status) {
                    super.callback(url, object, status);
                    Log.i("@@group", "url:" + url + " code:" + status.getCode() + "  obj:" + object);
                    if (status.getCode() == 200) {
                        try {
                            for (int i = 0; i < object.length(); i++) {
                                mua.add(new TryCatchJO(object.getJSONObject(i)));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        mua.notifyDataSetChanged();
                    }
                }
            });
            return true;
        }

        protected void onPostExecute(Boolean result) {
            if(result) {
//                try{
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            mua.notifyDataSetChanged();
//                            for (int i = 0; i < values.unions.length(); i++) {
//                                try {
//                                    mua.add(new TryCatchJO(values.unions.getJSONObject(i)));
//                                }catch(Exception e){
//                                    e.printStackTrace();
//                                }
//                            }
//                        }
//                    });
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//                return;
            }
            mua.notifyDataSetChanged();
        }
    }

}
