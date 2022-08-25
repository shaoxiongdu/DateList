/*
 * author:dushaoxiong@lixiang.com
 */

/*
 * author:dushaoxiong@lixiang.com
 */

package com.lixiang.programmingpracticedatelist.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import androidx.annotation.Nullable;

import com.lixiang.programmingpracticedatelist.constants.DateListConstant;
import com.lixiang.programmingpracticedatelist.model.DateModel;
import com.lixiang.programmingpracticedatelist.provider.DateProvider;
import com.lixiang.programmingpracticedatelist.utils.DateListFileCacheUtil;
import com.lixiang.programmingpracticedatelist.utils.DateListNetworkUtil;

import java.util.List;

public class DateListService extends Service {

    private static final String TAG = DateListService.class.getName();
    private Handler mUiHandler;
    private DateListFileCacheUtil mDateListFileCacheUtil;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind() called with: intent = [" + intent + "]");
        return new InnerBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind() called with: intent = [" + intent + "]");
        return super.onUnbind(intent);
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate() called");
        super.onCreate();

        mDateListFileCacheUtil = DateListFileCacheUtil.getInstance(this);
        handlerData();
    }

    private void handlerData() {

        //1、获取缓存 如果缓存中有 则取缓存 然后通知Ui
        new Thread(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            List<DateModel> cache = mDateListFileCacheUtil.read();
            if (cache.isEmpty()) {
                return;
            }
            DateProvider.getInstance().setDateModelList(cache);
            sendDateChange(DateListConstant.TYPE_DATA_FOR_CACHE);
            Log.i(TAG, "handlerData: loaded for cache data: size = " + cache.size());
        }, "readCacheThread").start();

        //2、从服务器中拿最新数据 然后通知Ui并缓存数据
        new Thread(() -> {
            try {
                Thread.sleep(6000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            List<DateModel> dateModels = DateListNetworkUtil.getInstance().getAll();
            if (dateModels.isEmpty()) {
                return;
            }
            DateProvider.getInstance().setDateModelList(dateModels);
            sendDateChange(DateListConstant.TYPE_DATA_FOR_NETWORK);
            Log.i(TAG, "handlerData: loaded for server data: size = ");
            // 将数据缓存
            DateListFileCacheUtil.getInstance(this).write(DateProvider.getInstance().getAll());
        }, "readServerThread").start();
    }

    private void sendDateChange(int type) {
        if (null != mUiHandler) {
            Message msg = Message.obtain();
            msg.what = DateListConstant.MSG_WHAT_DATE_CHANGE;
            msg.arg1 = type;
            mUiHandler.sendMessage(msg);
        }
    }

    public class InnerBinder extends Binder {
        public void setUiHandler(Handler uiHandler) {
            mUiHandler = uiHandler;
        }
    }

}
