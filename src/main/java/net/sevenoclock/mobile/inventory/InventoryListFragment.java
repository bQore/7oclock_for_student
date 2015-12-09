package net.sevenoclock.mobile.inventory;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class InventoryListFragment extends Fragment {

    private Context con;

    private LinearLayout ll_inventory_list_view;
    private LinearLayout ll_inventory_list_left;
    private LinearLayout ll_inventory_list_right;
    private ImageView iv_inventory_list_nodata;

    Values values;

    public static InventoryListFragment newInstance() {
        InventoryListFragment view = new InventoryListFragment();
        return view;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        con = container.getContext();
        values = (Values) con.getApplicationContext();

        View v = inflater.inflate(R.layout.fragment_inventory_list, container, false);

        MainActivity.setTitle("내 보관함");
        MainActivity.setSubtitle(values.user_info.get("first_name","-")+"님의 보관함입니다.");

        ll_inventory_list_view = (LinearLayout) v.findViewById(R.id.ll_inventory_list_view);
        ll_inventory_list_left = (LinearLayout) v.findViewById(R.id.ll_inventory_list_left);
        ll_inventory_list_right = (LinearLayout) v.findViewById(R.id.ll_inventory_list_right);
        iv_inventory_list_nodata = (ImageView) v.findViewById(R.id.iv_inventory_list_nodata);

        iv_inventory_list_nodata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Vibrator Vibe = (Vibrator) con.getSystemService(con.VIBRATOR_SERVICE);
                Vibe.vibrate(30);
                Functions.history_go_home(con);
            }
        });

        new AddInventoryTask().execute(null, null, null);

        return v;
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
                try {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i < ja_book.length(); i++) {
                                InventoryBookView ibv = null;
                                try {
                                    TryCatchJO tcjo = new TryCatchJO(ja_book.getJSONObject(i));
                                    ibv = new InventoryBookView(con, tcjo);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                if (ibv != null) {
                                    int count_left = ll_inventory_list_left.getChildCount();
                                    int count_right = ll_inventory_list_right.getChildCount();

                                    if (count_left > count_right) ll_inventory_list_right.addView(ibv);
                                    else ll_inventory_list_left.addView(ibv);

                                    ibv.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Functions.history_go(con, new InventoryQuestionListFragment().newInstance(((InventoryBookView) v).tcjo));
                                        }
                                    });
                                }
                            }
                            if (ja_book.length() <= 0) {
                                ll_inventory_list_view.setVisibility(View.GONE);
                                iv_inventory_list_nodata.setVisibility(View.VISIBLE);
                            } else {
                                iv_inventory_list_nodata.setVisibility(View.GONE);
                                ll_inventory_list_view.setVisibility(View.VISIBLE);
                            }
                            MainActivity.ll_main_main_loading.setVisibility(View.GONE);
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
            }else{
                Toast.makeText(con, "데이터 로드에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                MainActivity.ll_main_main_loading.setVisibility(View.GONE);
            }
            return;
        }
    }
}