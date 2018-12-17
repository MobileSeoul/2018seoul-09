package com.example.ggavi.ahnsungmin.nomal_mode;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

// for saving id (id 저장하기 위한 클래스)
public class SavedSharedPreference {
    static final String PREF_ID = "id";
    static final String PREF_NORMAL_MODE_WEIGHT = "normal_mode_weight";
    static final String PREF_NORMAL_MODE_WEIGHT_TYPE = "normal_mode_weight_type";
    static final String PREF_LOGGED_IN_MODE_WEIGHT = "logged_in_mode_weight";
    static final String PREF_LOGGED_IN_MODE_WEIGHT_TYPE = "logged_in_mode_weight_type";

    static SharedPreferences getSharedPreferences(Context ctx){
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    //setting id (id 설정)
    public static void setId(Context ctx, String id){
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_ID, id);
        editor.commit();
    }

    //getting id (id 가져오기)
    public static String getId(Context ctx){
        return getSharedPreferences(ctx).getString(PREF_ID,"");
    }

    //clearing saved id (저장된 아이디 지우기)
    public static void clearId(Context ctx){
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.remove(PREF_ID);
        //  editor.clear();
        editor.commit();
    }

    public static void setNormalModeWeight(Context ctx, String weight){
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_NORMAL_MODE_WEIGHT,weight);
        editor.commit();
    }

    public static String getNormalModeWeight(Context ctx){
        return getSharedPreferences(ctx).getString(PREF_NORMAL_MODE_WEIGHT,"");
    }

    public static void setNormalWeightType(Context ctx, String type){
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_NORMAL_MODE_WEIGHT_TYPE,type);
        editor.commit();
    }
    public static String getNormalModeWeightType(Context ctx){
        return getSharedPreferences(ctx).getString(PREF_NORMAL_MODE_WEIGHT_TYPE,"");
    }

    public static void setLoggedInModeWeight(Context ctx, String weight){
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_LOGGED_IN_MODE_WEIGHT,weight);
        editor.commit();
    }

    public static String getLoggedInModeWeight(Context ctx){
        return getSharedPreferences(ctx).getString(PREF_LOGGED_IN_MODE_WEIGHT,"");
    }

    public static void setLoggedInModeWeightType(Context ctx, String type){
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_LOGGED_IN_MODE_WEIGHT_TYPE,type);
        editor.commit();
    }
    public static String getLoggedInModeWeightType(Context ctx){
        return getSharedPreferences(ctx).getString(PREF_LOGGED_IN_MODE_WEIGHT_TYPE,"");
    }
}
