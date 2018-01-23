package com.example.neo.utils.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.example.neo.utils.BaseApplication;

/**
 * @author wing
 * 缓存类, 所有的数据都是采用SharedPreferences方式存储和获取.
 */
public class SPUtils {
	
	private static final String CACHE_FILE_NAME = "config";
	private static SharedPreferences mSharedPreferences;

	/**
	 * @param context
	 * @param key 要取的数据的键
	 * @param defValue 缺省值
	 * @return
	 */
	public static boolean getBoolean(Context context, String key, boolean defValue) {
		if(mSharedPreferences == null) {
			mSharedPreferences = context.getSharedPreferences(CACHE_FILE_NAME, Context.MODE_PRIVATE);
		}
		return mSharedPreferences.getBoolean(key, defValue);
	}
	
	/**
	 * 存储一个boolean类型数据
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void putBoolean(Context context, String key, boolean value) {
		if(mSharedPreferences == null) {
			mSharedPreferences = context.getSharedPreferences(CACHE_FILE_NAME, Context.MODE_PRIVATE);
		}
		mSharedPreferences.edit().putBoolean(key, value).apply();
	}
	
	/**
	 * 存储一个String类型的数据
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void putString(Context context, String key, String value) {
		if(mSharedPreferences == null) {
			mSharedPreferences = context.getSharedPreferences(CACHE_FILE_NAME, Context.MODE_PRIVATE);
		}
		mSharedPreferences.edit().putString(key, value==null?"":value).apply();
	}
	
	/**
	 * 根据key取出一个String类型的值
	 * @param context
	 * @param key
	 * @param defValue
	 * @return
	 */
	public static String getString(Context context, String key, String defValue) {
		if(mSharedPreferences == null) {
			mSharedPreferences = context.getSharedPreferences(CACHE_FILE_NAME, Context.MODE_PRIVATE);
		}
		return mSharedPreferences.getString(key, defValue);
	}
	/**
	 * 存储一个int类型的数据
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void putInt(Context context, String key, int value) {
		if(mSharedPreferences == null) {
			mSharedPreferences = context.getSharedPreferences(CACHE_FILE_NAME, Context.MODE_PRIVATE);
		}
		mSharedPreferences.edit().putInt(key, value).apply();
	}
	
	/**
	 * 根据key取出一个int类型的值
	 * @param context
	 * @param key
	 * @param defValue
	 * @return
	 */
	public static int getInt(Context context, String key, int defValue) {
		if(mSharedPreferences == null) {
			mSharedPreferences = context.getSharedPreferences(CACHE_FILE_NAME, Context.MODE_PRIVATE);
		}
		return mSharedPreferences.getInt(key, defValue);
	}
	/**
	 * 存储一个long类型的数据
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void putLong(Context context, String key, long value) {
		if(mSharedPreferences == null) {
			mSharedPreferences = context.getSharedPreferences(CACHE_FILE_NAME, Context.MODE_PRIVATE);
		}
		mSharedPreferences.edit().putLong(key, value).apply();
	}
	
	/**
	 * 根据key取出一个long类型的值
	 * @param context
	 * @param key
	 * @param defValue
	 * @return
	 */
	public static long getLong(Context context, String key, long defValue) {
		if(mSharedPreferences == null) {
			mSharedPreferences = context.getSharedPreferences(CACHE_FILE_NAME, Context.MODE_PRIVATE);
		}
		return mSharedPreferences.getLong(key, defValue);
	}
	/**
	 * 存储一个Float类型的数据
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void putFloat(Context context, String key, float value) {
		if(mSharedPreferences == null) {
			mSharedPreferences = context.getSharedPreferences(CACHE_FILE_NAME, Context.MODE_PRIVATE);
		}
		mSharedPreferences.edit().putFloat(key, value).apply();
	}
	
	/**
	 * 根据key取出一个Float类型的值
	 * @param context
	 * @param key
	 * @param defValue
	 * @return
	 */
	public static float getFloat(Context context, String key, float defValue) {
		if(mSharedPreferences == null) {
			mSharedPreferences = context.getSharedPreferences(CACHE_FILE_NAME, Context.MODE_PRIVATE);
		}
		return mSharedPreferences.getFloat(key, defValue);
	}

	/**
	 * 获取PreferenceManagerEditor,在设置完数据后要apply或者commit
	 */
	public static SharedPreferences.Editor getPreferenceManagerEditor(Context context){
		return PreferenceManager.getDefaultSharedPreferences(context).edit();
	}

	public static boolean getBooleanFromPreferencesManager(String key,boolean defValue){
		return PreferenceManager.getDefaultSharedPreferences(BaseApplication.getContext()).getBoolean(key, defValue);
	}
	public static int getIntFromPreferencesManager(String key,int defValue){
		String value = PreferenceManager.getDefaultSharedPreferences(BaseApplication.getContext()).getString(key, "");
		if (TextUtils.isEmpty(value)){
			return defValue;
		}else{
			return Integer.valueOf(value);
		}
	}
	public static long getLongFromPreferencesManager(String key,long defValue){
        String value =  PreferenceManager.getDefaultSharedPreferences(BaseApplication.getContext()).getString(key, "");
        if (TextUtils.isEmpty(value)){
            return defValue;
        }else{
            return Long.valueOf(value);
        }
	}
	public static float getFloatFromPreferencesManager(String key,float defValue){
		String value = PreferenceManager.getDefaultSharedPreferences(BaseApplication.getContext()).getString(key, "");
		if (TextUtils.isEmpty(value)){
			return defValue;
		}else{
			return Float.valueOf(value);
		}
	}

}
