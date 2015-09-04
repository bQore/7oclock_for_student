package net.sevenoclock.mobile.main;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import net.sevenoclock.mobile.R;

public class MainActivity extends Activity {

    ActionBar actionBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionbar();
    }

    private void setActionbar(){
        actionBar =getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_main_main);

        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.view_main_actionbar);
    }
}