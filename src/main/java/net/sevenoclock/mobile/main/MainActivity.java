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
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.customobj.FontTextView;
import net.sevenoclock.mobile.home.LoadingActivity;
import net.sevenoclock.mobile.inventory.InventoryListView;
import net.sevenoclock.mobile.search.SearchFragmentView;
import net.sevenoclock.mobile.settings.Functions;
import net.sevenoclock.mobile.settings.Values;
import net.sevenoclock.mobile.testpaper.TestpaperListView;
import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.Position;

public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    public static ActionBar actionBar;
    public static ActionBar.LayoutParams actionbar_lp;
    public static MenuDrawer menuDrawer;
    public static Activity activity;

    public static LinearLayout ll_main_main_mainview;
    public static LinearLayout ll_main_main_loading;

    public static LinearLayout ll_main_main_title;
    public static FontTextView tv_main_main_title;
    public static FontTextView tv_main_main_subtitle;

    public static ActionbarDefaultView adv;
    public static ActionbarSearchView asv;
    public static TestpaperListView tlv;
    public static InventoryListView ilv;
    public static SearchFragmentView sfv;
    public static MainSearchView msv;

    Values values;
    public static InputMethodManager imm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_main);
        activity = (Activity) this;
        values = (Values) getApplicationContext();
        imm= (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);

        setActionBar();
        setMenuDrawer();

        ll_main_main_mainview = (LinearLayout)findViewById(R.id.ll_main_main_mainview);
        ll_main_main_loading = (LinearLayout)findViewById(R.id.ll_main_main_loading);

        ll_main_main_title = (LinearLayout)findViewById(R.id.ll_main_main_title);
        tv_main_main_title = (FontTextView)findViewById(R.id.tv_main_main_title);
        tv_main_main_subtitle = (FontTextView)findViewById(R.id.tv_main_main_subtitle);


        adv = new ActionbarDefaultView(this);
        asv = new ActionbarSearchView(this);
        tlv = new TestpaperListView(this);
        ilv = new InventoryListView(this);
        sfv = new SearchFragmentView(this,0,Functions.GET("get_question_unit_new"), 0, values.user_info.get("school_name",""));
        msv = new MainSearchView(this);

        Functions.history_set_home(this, tlv);

    }

    private void setActionBar(){
        actionBar = getSupportActionBar();
        actionbar_lp = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(new ActionbarDefaultView(this), actionbar_lp);
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
                ilv.reflesh();
                Functions.history_go(this, ilv);
            }else if(tag.equals("ll_main_menudrawer_"+R.string.ic_main_menudrawer_list_search)){
                Functions.history_go(this, sfv);
            }
            menuDrawer.closeMenu();
        }else{
            Vibrator Vibe = (Vibrator)getSystemService(VIBRATOR_SERVICE);
            Vibe.vibrate(30);

            switch (v.getId()){
                case R.id.tv_main_actionbar_searchbtn:
                    actionBar.setCustomView(asv, actionbar_lp);
                    Functions.history_go(this,msv);
                    asv.et_main_actionbar_search_form.setText("");
                    asv.et_main_actionbar_search_form.requestFocus();
                    imm.showSoftInput(asv.et_main_actionbar_search_form, InputMethodManager.SHOW_FORCED);
                    break;
                case R.id.tv_main_actionbar_search_backbtn:
                    Functions.history_back(this);
                    imm.hideSoftInputFromWindow(asv.et_main_actionbar_search_form.getWindowToken(), 0);
                    actionBar.setCustomView(adv, actionbar_lp);
                    break;
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
                if(menuDrawer.isMenuVisible()){
                    menuDrawer.closeMenu();
                    return false;
                }
                if(actionBar.getCustomView() == asv ){
                    Functions.history_back(this);
                    actionBar.setCustomView(adv, actionbar_lp);
                    return false;
                }
                if (Functions.history_length() > 1) {
                    Functions.history_back(this);
                    return false;
                }
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
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}