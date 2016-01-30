package net.sevenoclock.mobile.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.Window;
import android.widget.*;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.settings.Functions;
import net.sevenoclock.mobile.settings.Values;

import java.util.HashMap;
import java.util.Map;

public class Step3Activity extends Activity {

    Context context;

    private Button btn_home_step3_submit;

    Values values;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home_step3);

        context = getApplicationContext();
        values = (Values) getApplicationContext();

        btn_home_step3_submit = (Button) findViewById(R.id.btn_home_step3_submit);
        btn_home_step3_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Vibrator Vibe = (Vibrator)getSystemService(VIBRATOR_SERVICE);
                Vibe.vibrate(30);

                startActivity(new Intent(Step3Activity.this, LoadingActivity.class));
                finish();
            }
        });

    }

}