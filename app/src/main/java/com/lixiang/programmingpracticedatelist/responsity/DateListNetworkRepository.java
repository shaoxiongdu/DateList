/*
 * author:dushaoxiong@lixiang.com
 */

/*
 * author:dushaoxiong@lixiang.com
 */

/*
 * author:dushaoxiong@lixiang.com
 */

package com.lixiang.programmingpracticedatelist.responsity;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.lixiang.programmingpracticedatelist.constants.DateListConstant;
import com.lixiang.programmingpracticedatelist.model.DateModel;
import com.lixiang.programmingpracticedatelist.utils.OkHttpUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.Response;

public class DateListNetworkRepository {
    private static final String TAG = DateListNetworkRepository.class.getName();

    private static String ip;
    private static int port;
    private static String url;

    public DateListNetworkRepository() {
        ip = DateListConstant.SERVER_IP;
        port = DateListConstant.SERVER_PORT;
        url = DateListConstant.SERVER_URL_RANDOM_DATE;
    }

    private static final class Holder {
        private static final DateListNetworkRepository INSTANCE = new DateListNetworkRepository();
    }

    public static DateListNetworkRepository getInstance() {
        return Holder.INSTANCE;
    }

    public List<DateModel> getAll() {
        Log.d(TAG, "getAll() called");
        List<DateModel> dateModelList = new ArrayList<>();

        String url = buildUrl();
        Response response = OkHttpUtil.getInstance().getData(url);
        if (null == response || null == response.body()) {
            return dateModelList;
        }
        String responseBodyStr = null;
        try {
            responseBodyStr = Objects.requireNonNull(response.body()).string();
        } catch (IOException e) {
            Log.e(TAG, "getAll: get res body failure:", e);
        }
        if (TextUtils.isEmpty(responseBodyStr)) {
            return dateModelList;
        }

        DateListRes dateListRes = null;
        try {
            dateListRes = new Gson().fromJson(responseBodyStr, DateListRes.class);
            Log.d(TAG, "getAll(): response for network: " + dateListRes);
        } catch (Exception e) {
            Log.e(TAG, "getAll: res body to DateListRes failure", e);
        }

        assert dateListRes != null;
        // 没有数据
        if (200 != dateListRes.code) {
            Log.i(TAG, "getAll: res 400 ---> " + dateListRes.message);
            return dateModelList;
        }
        // 有数据
        dateModelList.addAll(dateListRes.data);
        return dateModelList;
    }

    private String buildUrl() {
        return "http://" + ip + ":" + port + "/" + url;
    }

    private static class DateListRes {
        public int code;
        public String message;
        public List<DateModel> data;
    }
}

