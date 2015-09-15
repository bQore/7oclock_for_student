package net.sevenoclock.mobile.inventory;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.customobj.TryCatchJO;
import net.sevenoclock.mobile.main.MainActivity;
import net.sevenoclock.mobile.settings.Functions;
import net.sevenoclock.mobile.settings.Values;
import org.json.JSONArray;
import org.json.JSONException;

public class InventoryListView extends LinearLayout {

    private Context con;

    private LinearLayout ll_inventory_list_view;
    private LinearLayout ll_inventory_list_left;
    private LinearLayout ll_inventory_list_right;
    private ImageView iv_inventory_list_nodata;

    Values values;

    public InventoryListView(Context context) {
        super(context);
        con = context;

        values = (Values) context.getApplicationContext();

        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        addView(inflater.inflate(R.layout.view_inventory_list, null), lp);

        setTag(R.string.tag_main_title, "내 보관함");
        setTag(R.string.tag_main_subtitle, values.user_info.get("first_name","-")+"님의 보관함입니다.");

        ll_inventory_list_view = (LinearLayout)findViewById(R.id.ll_inventory_list_view);
        ll_inventory_list_left = (LinearLayout)findViewById(R.id.ll_inventory_list_left);
        ll_inventory_list_right = (LinearLayout)findViewById(R.id.ll_inventory_list_right);
        iv_inventory_list_nodata = (ImageView)findViewById(R.id.iv_inventory_list_nodata);

        iv_inventory_list_nodata.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Vibrator Vibe = (Vibrator)con.getSystemService(con.VIBRATOR_SERVICE);
                Vibe.vibrate(30);
                Functions.history_go(con, MainActivity.view_search_fragment);
            }
        });

        new AddInventoryTask().execute(null, null, null);
    }

    public void reflesh(){
        ll_inventory_list_left.removeAllViews();
        ll_inventory_list_right.removeAllViews();
        new AddInventoryTask().execute(null, null, null);
    }

    class AddInventoryTask extends AsyncTask<Void, Void, Boolean> {
        JSONArray ja_book;

        @Override
        protected void onPreExecute() {
            MainActivity.ll_main_main_loading.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        protected Boolean doInBackground(Void... Void) {
            ja_book = Functions.GET("get_inventory_list&uid=" + values.user_id);
            if(ja_book == null) return false;
            return true;
        }

        protected void onPostExecute(Boolean result) {
            if(result) {
                Log.i("@@@@@@@@@@@@", "get_inventory_list&uid=" + ja_book.length());
                MainActivity.activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < ja_book.length(); i++) {
                            InventoryBookView ibv = null;
                            try {
                                TryCatchJO tcjo = new TryCatchJO(ja_book.getJSONObject(i));
                                ibv = new InventoryBookView(con, tcjo);
                                ibv.setTag(R.string.tag_inventory_list_id, tcjo.get("id", "0"));
                                ibv.setTag(R.string.tag_inventory_list_title, tcjo.get("title", "0"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            if (ibv != null) {
                                int count_left = ll_inventory_list_left.getChildCount();
                                int count_right = ll_inventory_list_right.getChildCount();

                                if (count_left > count_right) ll_inventory_list_right.addView(ibv);
                                else ll_inventory_list_left.addView(ibv);

                                ibv.setOnClickListener(new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Functions.history_go(con, new InventoryQuestionListView(con
                                                , Integer.parseInt(v.getTag(R.string.tag_inventory_list_id).toString())
                                                ,v.getTag(R.string.tag_inventory_list_title).toString()));
                                    }
                                });
                            }
                        }
                        if(ja_book.length() <= 0){
                            ll_inventory_list_view.setVisibility(View.GONE);
                            iv_inventory_list_nodata.setVisibility(View.VISIBLE);
                        }else{
                            iv_inventory_list_nodata.setVisibility(View.GONE);
                            ll_inventory_list_view.setVisibility(View.VISIBLE);
                        }
                        MainActivity.ll_main_main_loading.setVisibility(View.GONE);
                    }
                });
            }else{
                Toast.makeText(con, "데이터 로드에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                MainActivity.ll_main_main_loading.setVisibility(View.GONE);
            }
            return;
        }
    }
}