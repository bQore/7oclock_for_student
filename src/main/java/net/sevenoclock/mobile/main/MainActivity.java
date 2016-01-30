package net.sevenoclock.mobile.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.analytics.tracking.android.GoogleAnalytics;

import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.customobj.FontTextView;
import net.sevenoclock.mobile.customobj.IconTextView;
import net.sevenoclock.mobile.dashboard.DashboardFragment;
import net.sevenoclock.mobile.dashboard.NewsFeedListFragment;
import net.sevenoclock.mobile.home.LoadingActivity;
import net.sevenoclock.mobile.inventory.InventoryPagerFragment;
import net.sevenoclock.mobile.mypage.MypageMainFragment;
import net.sevenoclock.mobile.qna.QnAPagerFragment;
import net.sevenoclock.mobile.settings.Functions;
import net.sevenoclock.mobile.settings.Values;
import net.sevenoclock.mobile.testpaper.TestpaperListFragment;
import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.Position;

import org.json.JSONObject;

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

    public static LinearLayout ll_main_main_title;
    public static FontTextView tv_main_main_title;
    public static FontTextView tv_main_main_subtitle;

    public static RelativeLayout rl_main_search_bar;
    public static EditText et_search_bar;
    public static IconTextView ic_union_search;

    Values values;
    public static InputMethodManager imm;
    public static int app_width = 0;

    public static int news_count = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_main);
        values = (Values) getApplicationContext();
        imm= (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        values.aq = new AQuery(this);
        values.tracker = GoogleAnalytics.getInstance(this).getTracker("UA-68827491-1");

        setActionBar();
        setMenuDrawer();

        ll_main_main_mainview = (LinearLayout)findViewById(R.id.ll_main_main_mainview);
        ll_main_main_loading = (LinearLayout)findViewById(R.id.ll_main_main_loading);

        rl_main_search_bar = (RelativeLayout)findViewById(R.id.rl_union_search);
        et_search_bar = (EditText)findViewById(R.id.et_main_union_search);
        ic_union_search = (IconTextView)findViewById(R.id.itv_main_search_done);

        ll_main_main_title = (LinearLayout)findViewById(R.id.ll_main_main_title);
        tv_main_main_title = (FontTextView)findViewById(R.id.tv_main_main_title);
        tv_main_main_subtitle = (FontTextView)findViewById(R.id.tv_main_main_subtitle);

        ic_union_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(MainActivity.this, MainUnionSearch.class);
                it.putExtra("search_name",et_search_bar.getText().toString());
                Log.i("@@info","search init...keyword:"+et_search_bar.getText().toString());
                startActivity(it);
            }
        });
        DisplayMetrics displayMetrics = new DisplayMetrics();
        displayMetrics = getResources().getDisplayMetrics();
        values.book_height = (int) (90 * displayMetrics.density);

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

                    Log.i("@@info",selectedImage.toString());

                    HashMap<String, Object> params = new HashMap<String, Object>();

                    InputStream iStream = null;
                    try {
                        iStream = getContentResolver().openInputStream(selectedImage);
                        BitmapFactory.Options opts = new BitmapFactory.Options();
                        opts.inPurgeable = true;
                        Bitmap profilepic = BitmapFactory.decodeStream(iStream, null, opts);
                        profilepic = Functions.getResizedBitmap(profilepic, 256, 256);
                        params.put("picture", Functions.bitmapToByteArray(profilepic));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }catch (NullPointerException e){
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),selectedImage);
                            params.put("picture",Functions.bitmapToByteArray(bitmap));
                        } catch (IOException ioe) {
                            e.printStackTrace();
                        }
                    }


                    values.aq.ajax(Functions.DOMAIN+"/mobile/?mode=set_user_profilepic&uid=" + values.user_id, params, JSONObject.class, new AjaxCallback<JSONObject>() {
                        @Override
                        public void callback(String url, JSONObject object,AjaxStatus status) {
                            if(status.getCode()==200) {
                                values.user_info = null;
                                values.user_id = 0;
                                startActivity(new Intent(MainActivity.this, LoadingActivity.class));
                                MainActivity.this.finish();
                            }else
                                Toast.makeText(MainActivity.this,"사진을 가져오는데 실패하였습니다!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
        }
    }

    public static void setTitle(String str){
        MainActivity.tv_main_main_title.setText(str);
    }
    public static void setSubtitle(String str){
        MainActivity.tv_main_main_subtitle.setText(str);
    }
    public static void setTitleVisible(int value){ MainActivity.ll_main_main_title.setVisibility(value);}
    public static void setSearchBarVisible(int value){ MainActivity.rl_main_search_bar.setVisibility(value);}

    @Override
    public void onClick(View v) {
        if(v.getTag() != null){
            Functions.history_clear();
            String tag = v.getTag().toString();
            if(tag.equals("ll_main_menudrawer_"+R.string.ic_main_menudrawer_list_dashboard)){
                Functions.history_go_home(this);
            }else if(tag.equals("ll_main_menudrawer_"+R.string.ic_main_menudrawer_list_testpaper)){
                Functions.history_go(this, new TestpaperListFragment().newInstance());
            }else if(tag.equals("ll_main_menudrawer_"+R.string.ic_main_menudrawer_list_qna)){
                Functions.history_go(this, new QnAPagerFragment().newInstance());
            }else if(tag.equals("ll_main_menudrawer_"+R.string.ic_main_menudrawer_list_inventory)){
                Functions.history_go(this, new InventoryPagerFragment().newInstance());
            }else if(tag.equals("ll_main_menudrawer_333")){
                Functions.history_go(this, new NewsFeedListFragment().newInstance());
            }
            menuDrawer.closeMenu();
        }else{
            Vibrator Vibe = (Vibrator)getSystemService(VIBRATOR_SERVICE);
            Vibe.vibrate(30);

            switch (v.getId()){
                case R.id.fl_mypage_main_upload:
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    //여기서부터 프로필 이미지 잘라오기
                    //Crop 활성화(잘라내기)
                    photoPickerIntent.putExtra("crop","true");
                    //X:Y 비율 1:1로 지정
                    photoPickerIntent.putExtra("aspectX",1);
                    photoPickerIntent.putExtra("aspectY",1);
                    //X:Y 픽셀 지정. 1:1이므로 서로 같은 픽셀
                    photoPickerIntent.putExtra("outputX",256);
                    photoPickerIntent.putExtra("outputY",256);
                    //이미지 잘라오기 끝
                    startActivityForResult(photoPickerIntent, 100);
                    break;
                case R.id.iv_main_actionbar_unionbtn:
                    Intent intent = new Intent(this, MainUnionActivity.class);
                    startActivity(intent);
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
                                    values.union_info = null;
                                    values.unions = null;
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