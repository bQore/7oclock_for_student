package net.sevenoclock.mobile.main;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import com.androidquery.AQuery;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.settings.Functions;
import net.sevenoclock.mobile.settings.Values;
import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.Position;

public class MainActivity extends ActionBarActivity {

    private AQuery aq = new AQuery(this);

    private ActionBar actionBar;
    public static MenuDrawer menuDrawer;

    Values values;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_main);
        values = (Values) getApplicationContext();

        setActionBar();
        setMenuDrawer();
    }

    private void setActionBar(){
        actionBar = getSupportActionBar();
        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(new ActionbarView(this).getView(), lp);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowCustomEnabled(true);
    }

    private void setMenuDrawer(){
        menuDrawer = MenuDrawer.attach(this, MenuDrawer.Type.OVERLAY, Position.LEFT,
                MenuDrawer.MENU_DRAG_WINDOW);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;

        if(width>=640)
            menuDrawer.setMenuSize(600);
        else if(width<640)
            menuDrawer.setMenuSize(400);
        menuDrawer.setContentView(R.layout.activity_main_main);
        menuDrawer.setMenuView(R.layout.view_main_menudrawer);
        menuDrawer.setDropShadowEnabled(false);

        aq.id(R.id.iv_main_menudrawer_profilepic).image(Functions.borderRadius(Functions.DOMAIN + values.user_info.get("src", ""),100));
        aq.id(R.id.tv_main_menudrawer_username).text(values.user_info.get("first_name", "다시 로그인해주세요."));
        aq.id(R.id.tv_main_menudrawer_schoolname).text(String.format("%s %s학년 %s반"
                ,values.user_info.get("school_name", "-")
                ,values.user_info.get("school_year", "")
                ,values.user_info.get("school_room", "")));

    }
}