package net.sevenoclock.mobile.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ListView;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.customobj.TryCatchJO;
import net.sevenoclock.mobile.home.LoadingActivity;
import net.sevenoclock.mobile.settings.Values;

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

        values = (Values) getApplicationContext();

        lv_main_union_list = (ListView) findViewById(R.id.lv_main_union_list);

        mua = new MainUnionAdapter();
        lv_main_union_list.setAdapter(mua);
        lv_main_union_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                try {
                    Vibrator Vibe = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                    Vibe.vibrate(30);
                    values.union_info = new TryCatchJO(values.unions.getJSONObject(position));
                    startActivity(new Intent(MainUnionActivity.this, LoadingActivity.class));
                    MainUnionActivity.this.finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        new MainUnionTask().execute(null, null, null);
    }

    class MainUnionTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Boolean doInBackground(Void... Void) {
            Log.i("@@@@@@@",""+values.unions.length());
            return true;
        }

        protected void onPostExecute(Boolean result) {
            if(result) {
                try{
                    runOnUiThread(new Runnable() {
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
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
                return;
            }
            return;
        }
    }

}
