package com.lixiang.programmingpracticedatelist.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lixiang.programmingpracticedatelist.constants.DateListConstant;
import com.lixiang.programmingpracticedatelist.model.DateModel;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class DateListFileCacheUtil {

    private static DateListFileCacheUtil mInstance;
    private static final ReentrantLock reentrantLock = new ReentrantLock();
    SharedPreferences mSharedPreferences;

    /**
     * @return {@link DateListFileCacheUtil}
     */
    public static DateListFileCacheUtil getInstance(Context context) {
        if (null == mInstance) {
            reentrantLock.lock();
            try {
                mInstance = new DateListFileCacheUtil(context);
            }finally {
                reentrantLock.unlock();
            }
        }
        return mInstance;
    }

    private DateListFileCacheUtil(Context context) {
        mSharedPreferences = context.getSharedPreferences(DateListConstant.CACHE_FILE_NAME, Context.MODE_PRIVATE);
    }

    public void write(List<DateModel> dateModels) {
        if (dateModels.isEmpty()) {
            return;
        }
        SharedPreferences.Editor edit = mSharedPreferences.edit();
        String json = new Gson().toJson(dateModels);
        edit.putString(DateListConstant.CACHE_FILE_NAME,json);
        edit.apply();
    }

    public List<DateModel> read() {
        String json = mSharedPreferences.getString(DateListConstant.CACHE_FILE_NAME, null);
        if (TextUtils.isEmpty(json)) {
            return new ArrayList<>();
        }
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<DateModel>>(){}.getType();
        return gson.fromJson(json, type);
    }

}
