package net.sevenoclock.mobile.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.MapBuilder;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.customobj.FontTextView;
import net.sevenoclock.mobile.dashboard.DashboardFragment;
import net.sevenoclock.mobile.home.LoadingActivity;
import net.sevenoclock.mobile.inventory.InventoryListFragment;
import net.sevenoclock.mobile.mypage.MypageMainFragment;
import net.sevenoclock.mobile.settings.Functions;
import net.sevenoclock.mobile.settings.Values;
import net.sevenoclock.mobile.testpaper.TestpaperListFragment;
import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.Position;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    public static ActionBar actionBar;
    public static ActionBar.LayoutParams actionbar_lp;
    public static MenuDrawer menuDrawer;

    public LinearLayout ll_main_main_mainview;
    public static LinearLayout ll_main_main_loading;

    public LinearLayout ll_main_main_title;
    public static FontTextView tv_main_main_title;
    public static FontTextView tv_main_main_subtitle;

    public static MainUnionFragment fragment_main_union;

    Values values;
    public static InputMethodManager imm;
    public static int app_width = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_main);
        values = (Values) getApplicationContext();
        imm= (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        values.aq = new AQuery(this);
        values.tracker = GoogleAnalytics.getInstance(this).getTracker("UA-68827491-1");
        values.tracker.send(MapBuilder.createEvent("UserAction", "Enter", String.format("%s %s학년 %s반"
                , values.user_info.get("school_name", "-")
                , values.user_info.get("school_year", "")
                , values.user_info.get("school_room", "")), null).build());

        setActionBar();
        setMenuDrawer();

        ll_main_main_mainview = (LinearLayout)findViewById(R.id.ll_main_main_mainview);
        ll_main_main_loading = (LinearLayout)findViewById(R.id.ll_main_main_loading);

        ll_main_main_title = (LinearLayout)findViewById(R.id.ll_main_main_title);
        tv_main_main_title = (FontTextView)findViewById(R.id.tv_main_main_title);
        tv_main_main_subtitle = (FontTextView)findViewById(R.id.tv_main_main_subtitle);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        displayMetrics = getResources().getDisplayMetrics();
        values.book_height = (int) (90 * displayMetrics.density);

        fragment_main_union = new MainUnionFragment().newInstance();

        Functions.history_set_home(this, new DashboardFragment().newInstance());
    }

    private void setActionBar(){
        actionBar = getSupportActionBar();
        actionbar_lp = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(new MainActionbarView(this), actionbar_lp);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowCustomEnabled(true);
    }

    private void setMenuDrawer(){
        menuDrawer = MenuDrawer.attach(this, MenuDrawer.Type.OVERLAY, Position.LEFT,
                MenuDrawer.MENU_DRAG_WINDOW);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        app_width = dm.widthPixels;

        if(app_width>=640)
            menuDrawer.setMenuSize(app_width-100);
        else if(app_width<640)
            menuDrawer.setMenuSize(400);
        menuDrawer.setContentView(R.layout.activity_main_main);
        menuDrawer.setMenuView(new MainMenudrawerView(this));
        menuDrawer.setDropShadowEnabled(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case 100:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();

                    HashMap<String, Object> params = new HashMap<String, Object>();

                    InputStream iStream = null;
                    try {
                        iStream = getContentResolver().openInputStream(selectedImage);
                        BitmapFactory.Options opts = new BitmapFactory.Options();
                        opts.inPurgeable = true;
                        Bitmap profilepic = BitmapFactory.decodeStream(iStream, null, opts);
                        profilepic = Functions.getResizedBitmap(profilepic, 256, 256);
                        params.put("picture", bitmapToByteArray(profilepic));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    values.aq.ajax(Functions.DOMAIN+"/mobile/?mode=set_user_profilepic&uid=" + values.user_id, params, JSONObject.class, new AjaxCallback<JSONObject>() {
                        @Override
                        public void callback(String url, JSONObject object,AjaxStatus status) {
                            values.user_info = null;
                            values.user_id = 0;
                            startActivity(new Intent(MainActivity.this, LoadingActivity.class));
                            MainActivity.this.finish();
                        }
                    });
                }
        }
    }

    public byte[] bitmapToByteArray( Bitmap $bitmap ) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream() ;
        $bitmap.compress( Bitmap.CompressFormat.JPEG, 100, stream) ;
        byte[] byteArray = stream.toByteArray() ;
        return byteArray ;
    }

    public static void setTitle(String str){
        MainActivity.tv_main_main_title.setText(str);
    }
    public static void setSubtitle(String str){
        MainActivity.tv_main_main_subtitle.setText(str);
    }

    @Override
    public void onClick(View v) {
        if(v.getTag() != null){
            String tag = v.getTag().toString();
            if(tag.equals("ll_main_menudrawer_"+R.string.ic_main_menudrawer_list_dashboard)){
                Functions.history_go_home(this);
            }else if(tag.equals("ll_main_menudrawer_"+R.string.ic_main_menudrawer_list_testpaper)){
                Functions.history_go(this, new TestpaperListFragment().newInstance());
            }else if(tag.equals("ll_main_menudrawer_"+R.string.ic_main_menudrawer_list_inventory)){
                Functions.history_go(this, new InventoryListFragment().newInstance());
            }
            menuDrawer.closeMenu();
        }else{
            Vibrator Vibe = (Vibrator)getSystemService(VIBRATOR_SERVICE);
            Vibe.vibrate(30);

            switch (v.getId()){
                case R.id.fl_mypage_main_upload:
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, 100);
                    break;
                case R.id.iv_main_actionbar_unionbtn:
                    Functions.history_go(this, fragment_main_union);
                    break;
                case R.id.ll_main_menudrawer_profile:
                    menuDrawer.closeMenu();
                    Functions.history_go(this, new MypageMainFragment().newInstance());
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

    @Override
    public void onDestroy(){
        super.onDestroy();
    }
}