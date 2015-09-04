package net.sevenoclock.mobile.custom;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015-09-04.
 */
public class TryCatchJO extends JSONObject {

    public TryCatchJO(JSONObject jo) throws JSONException {
        super(jo.toString());
    }

    public String get(String key, String default_value){
        try{
            return getString(key);
        }catch (Exception e){
            return default_value;
        }
    }

    public int get(String key, int default_value){
        try{
            return getInt(key);
        }catch (Exception e){
            return default_value;
        }
    }

    public double get(String key, double default_value){
        try{
            return getDouble(key);
        }catch (Exception e){
            return default_value;
        }
    }

}
