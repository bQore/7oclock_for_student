package net.sevenoclock.mobile.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by Bear on 2016-01-20.
 */
public class UserData {
    private static SharedPreferences pref;

    public static void initPref(Context con){
        pref = con.getSharedPreferences("mosu_user_data",Context.MODE_PRIVATE);
    }

    public static int getMainUnion(){
        return pref.getInt("main", -1);
    }

    public static void setMainUnion(int union_id){
        SharedPreferences.Editor edit = pref.edit();
        edit.putInt("main",union_id);
        edit.commit();
        Log.i("@@info", "set_union_id:" + union_id);
    }

    public static int getMainGroup(){
        return pref.getInt("group",-1);
    }

    public static void setMainGroup(int group_id){
        SharedPreferences.Editor edit = pref.edit();
        edit.putInt("group", group_id);
        edit.commit();
        Log.i("@@info", "set_group_id:" + group_id);
    }

    public static boolean signupUnion(int union_id){
        if(isSignup()){
            return false;
        }else {
            SharedPreferences.Editor edit = pref.edit();
            edit.putBoolean("signup", true);
            edit.putInt("signup_id", union_id);
            edit.commit();
            return true;
        }
    }

    public static boolean isSignup(){
        return pref.getBoolean("signup",false);
    }

    public static int getSignupUnion(){
        return pref.getInt("signup_id",-1);
    }

    public static void applySign(){
        SharedPreferences.Editor edit = pref.edit();
        int id = getSignupUnion();
        setMainUnion(id);
        edit.putBoolean("signup",false);
        edit.commit();
    }
}
