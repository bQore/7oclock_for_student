package net.sevenoclock.mobile.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.customobj.FontTextView;
import net.sevenoclock.mobile.home.LoadingActivity;
import net.sevenoclock.mobile.inventory.InventoryListView;
import net.sevenoclock.mobile.settings.Functions;
import net.sevenoclock.mobile.settings.Values;
import net.sevenoclock.mobile.testpaper.TestpaperListView;
import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.Position;

public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    private ActionBar actionBar;
    public static MenuDrawer menuDrawer;
    public static Activity activity;

    public static LinearLayout ll_main_main_mainview;
    public static LinearLayout ll_main_main_loading;

    public static FontTextView tv_main_main_title;
    public static FontTextView tv_main_main_subtitle;

    private TestpaperListView tmv;
    private InventoryListView imv;

    Values values;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_main);
        activity = (Activity) this;
        values = (Values) getApplicationContext();

        setActionBar();
        setMenuDrawer();

        ll_main_main_mainview = (LinearLayout)findViewById(R.id.ll_main_main_mainview);
        ll_main_main_loading = (LinearLayout)findViewById(R.id.ll_main_main_loading);

        tv_main_main_title = (FontTextView)findViewById(R.id.tv_main_main_title);
        tv_main_main_subtitle = (FontTextView)findViewById(R.id.tv_main_main_subtitle);

        tmv = new TestpaperListView(this);
        imv = new InventoryListView(this);

        Functions.history_set_home(this, tmv);

    }

    private void setActionBar(){
        actionBar = getSupportActionBar();
        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(new ActionbarView(this), lp);
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
            menuDrawer.setMenuSize(width-100);
        else if(width<640)
            menuDrawer.setMenuSize(400);
        menuDrawer.setContentView(R.layout.activity_main_main);
        menuDrawer.setMenuView(new MenudrawerView(this));
        menuDrawer.setDropShadowEnabled(false);
    }

    public static void setSubtitle(String str){
        MainActivity.tv_main_main_subtitle.setText(str);
    }

    @Override
    public void onClick(View v) {
        if(v.getTag() != null){
            String tag = v.getTag().toString();
            if(tag.equals("ll_main_menudrawer_"+R.string.ic_main_menudrawer_list_testpaper)){
                Functions.history_go_home(this);
            }else if(tag.equals("ll_main_menudrawer_"+R.string.ic_main_menudrawer_list_inventory)){
                imv.reflesh();
                Functions.history_go(this, imv);
            }else if(tag.equals("ll_main_menudrawer_"+R.string.ic_main_menudrawer_list_search)){
            }
            menuDrawer.closeMenu();
        }else{
            Vibrator Vibe = (Vibrator)getSystemService(VIBRATOR_SERVICE);
            Vibe.vibrate(30);

            switch (v.getId()){
                case R.id.ll_main_menudrawer_tablist_setting:
                    break;
                case R.id.ll_main_menudrawer_tablist_logout:
                    new AlertDialog.Builder(this).setTitle("로그아웃")
                            .setMessage("로그아웃 하시겠습니까?")
                            .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    return;
                                }
                            })
                            .setPositiveButton("예", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    values.user_info = null;
                                    values.user_id = 0;
                                    Functions.remove_pref(getApplicationContext());
                                    startActivity(new Intent(MainActivity.this, LoadingActivity.class));
                                    MainActivity.this.finish();
                                    return;
                                }
                            }).show();
                    break;
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        switch(keyCode) {
            case KeyEvent.KEYCODE_BACK:
                Log.i("@@@@@@@@@@@@@@@@1", "" + Functions.history_length());

                if(menuDrawer.isMenuVisible()){
                    menuDrawer.closeMenu();
                    return false;
                }
                Log.i("@@@@@@@@@@@@@@@@2", "" + Functions.history_length());
                if (Functions.history_length() != 1) {
                    Functions.history_back(this);
                    return false;
                }
                Log.i("@@@@@@@@@@@@@@@@3", "" + Functions.history_length());
                new AlertDialog.Builder(this).setTitle("종료")
                        .setMessage("정말 종료하시겠습니까?")
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        })
                        .setPositiveButton("종료", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                                return;
                            }
                        }).show();
                Log.i("@@@@@@@@@@@@@@@@4", "" + Functions.history_length());
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}