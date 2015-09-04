package net.sevenoclock.mobile.settings;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.*;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2015-09-03.
 */
public class Functions {

    public static final String DOMAIN = "http://storm1113.cafe24.com";
    private static final String PREF_NAME = "net.sevenoclock.mobile";
    private static Typeface mTypeface = null;

    public static Typeface setFont(Context con){
        if(mTypeface == null){
            if (Integer.parseInt(Build.VERSION.SDK) > Build.VERSION_CODES.FROYO) {
                mTypeface = Typeface.createFromAsset(con.getAssets(), "font.ttf");
            }else{
                mTypeface = Typeface.SANS_SERIF;
            }
        }

        return mTypeface;
    }

    public static Bitmap borderRadius(String src, int pixels) {

        Bitmap bitmap = getBitmapFromURL(src);

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

    public static String stripHTML(String htmlStr) {
        Pattern p = Pattern.compile("<(?:.|\\s)*?>");
        Matcher m = p.matcher(htmlStr);
        return m.replaceAll("");
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

    public static String getLauncherClassName(Context context) {

        PackageManager pm = context.getPackageManager();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resolveInfos) {
            String pkgName = resolveInfo.activityInfo.applicationInfo.packageName;
            if (pkgName.equalsIgnoreCase(context.getPackageName())) {
                String className = resolveInfo.activityInfo.name;
                return className;
            }
        }
        return null;
    }

    public static Boolean chkNetwork(Context con){
        //인터넷에 연결돼 있나 확인
        ConnectivityManager connect = (ConnectivityManager)con.getSystemService(con.CONNECTIVITY_SERVICE);
        if (connect.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connect.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED ) {
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
