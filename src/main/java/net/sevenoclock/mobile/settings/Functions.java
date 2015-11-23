package net.sevenoclock.mobile.settings;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.*;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.main.MainActivity;
import net.sevenoclock.mobile.testpaper.TestpaperListFragment;
import net.sevenoclock.mobile.testpaper.TestpaperQuestionListFragment;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by Administrator on 2015-09-03.
 */
public class Functions {

    public static final String YOUTUBE_KEY = "AIzaSyA5ajaURV7840WCtQsHMFUKZFsR1kLMA2A";
    public static final String DOMAIN = "http://yan3140.cafe24.com";
    private static final String PREF_NAME = "net.sevenoclock.mobile";
    private static Typeface mTypeface = null;
    private static Values values;

    public static void history_go(Context con, Fragment fragment){
        try{
            Vibrator Vibe = (Vibrator)con.getSystemService(con.VIBRATOR_SERVICE);
            Vibe.vibrate(30);
            values.fragment_history.add(fragment);
            fragmentReplace(con, fragment);
            System.gc();
        }catch (Exception e){
            Log.i("history_go_Error",e.getMessage());
        }
    }

    public static void history_go_home(Context con){
        try{
            Vibrator Vibe = (Vibrator)con.getSystemService(con.VIBRATOR_SERVICE);
            Vibe.vibrate(30);
            TestpaperListFragment home = (TestpaperListFragment)values.fragment_history.get(0);
            home.reflesh();
            values.fragment_history.clear();
            values.fragment_history.add(home);
            fragmentReplace(con, home);
            System.gc();
        }catch (Exception e){
            Log.i("history_go_home_Error",e.getMessage());
        }
    }

    public static void history_set_home(Context con, Fragment fragment){
        try{
            values = (Values)con.getApplicationContext();
            Vibrator Vibe = (Vibrator)con.getSystemService(con.VIBRATOR_SERVICE);
            Vibe.vibrate(30);
            values.fragment_history.add(0, fragment);
            fragmentReplace(con, fragment);
            System.gc();
        }catch (Exception e){
            Log.i("history_set_home_Error",e.getMessage());
        }
    }

    public static void history_back(Context con){
        history_back(con, true);
    }

    public static void history_back(Context con, Boolean vibrate){
        try{
            if(vibrate){
                Vibrator Vibe = (Vibrator)con.getSystemService(con.VIBRATOR_SERVICE);
                Vibe.vibrate(30);
            }
            values.fragment_history.remove(values.fragment_history.size() - 1);
            Fragment fragment = values.fragment_history.get(values.fragment_history.size() - 1);
            fragmentReplace(con, fragment);
            System.gc();
        }catch (Exception e){
            Log.i("history_back_Error",e.getMessage());
        }
    }

    public static void history_back_delete(Context con){
        try{
            values.fragment_history.remove(values.fragment_history.size() - 1);
        }catch (Exception e){
            Log.i("history_back_Error",e.getMessage());
        }
    }

    public static int history_length(){
        return values.fragment_history.size();
    }

    public static void fragmentReplace(Context con, Fragment newFragment) {
        FragmentActivity activity = (FragmentActivity)con;
        final FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.ll_main_main_mainview, newFragment);
        transaction.commit();
        //values.tracker.send(MapBuilder.createEvent("UserAction", "PageMove", newFragment.getClass().toString(), null).build());
        values.tracker.send(MapBuilder.createAppView().set(Fields.SCREEN_NAME, newFragment.getClass().toString().replaceAll("net.sevenoclock.mobile.","")).build());
    }

    public static Bitmap borderRadius(String src, int pixels) { return borderRadius(getBitmapFromURL(src), pixels); }

    public static Bitmap borderRadius(Bitmap bitmap, int pixels) {

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    public static Bitmap getBitmapFromURL(String src) {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        HttpURLConnection connection = null;
        try {
            URL url = new URL(src);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }finally{
            if(connection!=null)connection.disconnect();
        }
    }

    public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        Matrix matrix = new Matrix();

        matrix.postScale(scaleWidth, scaleHeight);

        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }

    public static JSONArray GET(String var){
        String jsonStr = null;
        try{
            jsonStr = downloadHtml(var);
            jsonStr = jsonStr.trim();
            JSONArray ja = new JSONArray(jsonStr);
            return ja;
        }catch(Exception e){
            Log.i("JSON ERROR", "" + e.getMessage());
        }
        return null;
    }

    public static String downloadHtml(String addr){
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        StringBuilder html = new StringBuilder();
        addr = DOMAIN+"/mobile/?mode="+addr;
        try{
            URL url = new URL(addr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if(conn!=null){
                conn.setConnectTimeout(10000);
                conn.setUseCaches(false);
                if(conn.getResponseCode()==HttpURLConnection.HTTP_OK){
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));
                    while(true){
                        String line = br.readLine();
                        if(line==null) break;
                        html.append(line+"\n");
                    }
                    br.close();
                }
                conn.disconnect();
            }
        }catch(Exception ex){}
        return html.toString();
    }

//	public static void setBadge(Context context, int count) {
//	    String launcherClassName = getLauncherClassName(context);
//	    if (launcherClassName == null) {
//	        return;
//	    }
//	    Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
//	    intent.putExtra("badge_count", count);
//	    intent.putExtra("badge_count_package_name", context.getPackageName());
//	    intent.putExtra("badge_count_class_name", launcherClassName);
//	    context.sendBroadcast(intent);
//	}

    public static Boolean chkNetwork(Context con){
        //인터넷에 연결돼 있나 확인
        ConnectivityManager connect = (ConnectivityManager)con.getSystemService(con.CONNECTIVITY_SERVICE);
        if (connect.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED){
            return true;
        }else if (connect.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED) {
            return true;
        } else {
            return false;
        }
    }

    public static void put_pref(Context mContext, String key, String value) {
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void put_pref(Context mContext, String key, boolean value) {
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static void put_pref(Context mContext, String key, int value) {
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static void remove_pref(Context mContext) {
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }

    public static String get_pref(Context mContext, String key, String dftValue) {
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);

        try {
            return pref.getString(key, dftValue);
        } catch (Exception e) {
            return dftValue;
        }

    }

    public static int get_pref(Context mContext, String key, int dftValue) {
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);

        try {
            return pref.getInt(key, dftValue);
        } catch (Exception e) {
            return dftValue;
        }

    }

    public static boolean get_pref(Context mContext, String key, boolean dftValue) {
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);

        try {
            return pref.getBoolean(key, dftValue);
        } catch (Exception e) {
            return dftValue;
        }
    }

}
