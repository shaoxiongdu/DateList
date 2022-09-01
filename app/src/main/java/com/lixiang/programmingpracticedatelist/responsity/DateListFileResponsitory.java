package com.lixiang.programmingpracticedatelist.responsity;

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

public class DateListFileResponsitory {
    private static DateListFileResponsitory mInstance;
    SharedPreferences mSharedPreferences;


    private DateListFileResponsitory(Context context) {
        mSharedPreferences = context.getSharedPreferences(DateListConstant.CACHE_FILE_NAME, Context.MODE_PRIVATE);
    }

    public static DateListFileResponsitory getInstance(Context context) {
        if (null == mInstance) {
            mInstance = new DateListFileResponsitory(context);
        }
        return mInstance;
    }

    public void write(List<DateModel> dateModels) {
        if (dateModels.isEmpty()) {
            return;
        }
        SharedPreferences.Editor edit = mSharedPreferences.edit();
        String json = new Gson().toJson(dateModels);
        edit.putString(DateListConstant.CACHE_FILE_NAME, json);
        edit.apply();
    }

    public List<DateModel> read() {
        String json = mSharedPreferences.getString(DateListConstant.CACHE_FILE_NAME, null);
        if (TextUtils.isEmpty(json)) {
            return new ArrayList<>();
        }
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<DateModel>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

}
