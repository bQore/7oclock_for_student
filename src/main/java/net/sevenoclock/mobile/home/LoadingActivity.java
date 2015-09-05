package net.sevenoclock.mobile.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import net.sevenoclock.mobile.customobj.TryCatchJO;
import net.sevenoclock.mobile.main.MainActivity;
import net.sevenoclock.mobile.settings.Functions;
import net.sevenoclock.mobile.settings.Values;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;
import net.sevenoclock.mobile.R;

public class LoadingActivity extends Activity {
    Values values;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home_loading);

        values = (Values) getApplicationContext();

        if(Functions.chkNetwork(this))
            new LoginTask().execute(null, null, null);
        else
            new AlertDialog.Builder(this).setTitle("인터넷 연결 실패")
                    .setMessage("인터넷 연결에 실패하였습니다.\n" +
                            "연결상태를 확인해주세요.")
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                        }
                    }).show();
    }

    class LoginTask extends AsyncTask<Void, Void, Integer> {
        protected Integer doInBackground(Void... Void) {
            int uid = Functions.get_pref(getApplicationContext(), "uid", 0);
            if(uid != 0){
                values.user_id = uid;
                try {
                    values.user_info = new TryCatchJO(Functions.GET("get_user_info&uid="+values.user_id).getJSONObject(0));
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    return 2;
                }
                try {
                    Thread.sleep(500);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return 1;
            }else{
                try {
                    Thread.sleep(500);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return 2;
            }
        }

        protected void onPostExecute(Integer result) {
            if(result.intValue() == 1){
                startActivity(new Intent(LoadingActivity.this, MainActivity.class));
                LoadingActivity.this.finish();
            }else if(result.intValue() == 2){
                startActivity(new Intent(LoadingActivity.this, LandingActivity.class));
                LoadingActivity.this.finish();
            }
            return;
        }
    }
}