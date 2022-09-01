/*
 * author:dushaoxiong@lixiang.com
 */

/*
 * author:dushaoxiong@lixiang.com
 */

package com.lixiang.programmingpracticedatelist.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lixiang.programmingpracticedatelist.R;
import com.lixiang.programmingpracticedatelist.adapter.DateAdapter;
import com.lixiang.programmingpracticedatelist.constants.DateListConstant;
import com.lixiang.programmingpracticedatelist.listener.OnDateItemClickListener;
import com.lixiang.programmingpracticedatelist.model.DateModel;
import com.lixiang.programmingpracticedatelist.provider.DateProvider;
import com.lixiang.programmingpracticedatelist.service.DateListService;

import java.lang.ref.WeakReference;
import java.util.List;

public class DateListActivity extends AppCompatActivity {

    private static final String TAG = DateListActivity.class.getName();
    private RecyclerView mRecyclerView;
    private DateAdapter mDateAdapter;
    private UiHandler mUiHandler;

    private ServiceConnection dateServiceConnection;

    private OnDateItemClickListeners mOnDateItemClickListeners;
    private DateListService.InnerBinder mServiceInnerBinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_list);

        initUi();
        initService();
    }

    /**
     * 1、init view
     * 2、initAdapter
     * 3、initAnim
     * 4、initUiHandler
     */
    private void initUi() {
        // uiHandler
        mUiHandler = new UiHandler(this);

        // view
        mRecyclerView = findViewById(R.id.date_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        // adapter
        List<DateModel> dateModels = DateProvider.getInstance().getAll();
        mDateAdapter = new DateAdapter(dateModels);

        mOnDateItemClickListeners = new OnDateItemClickListeners();
        mDateAdapter.setOnDateItemClickListener(mOnDateItemClickListeners);
        mRecyclerView.setAdapter(mDateAdapter);

        // anim
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.alpha_anim);
        LayoutAnimationController layoutAnimationController = new LayoutAnimationController(animation);
        layoutAnimationController.setOrder(LayoutAnimationController.ORDER_NORMAL);
        layoutAnimationController.setDelay(0.2f);
        mRecyclerView.setLayoutAnimation(layoutAnimationController);
    }

    private void initService() {
        dateServiceConnection = new DateServiceConnection();
        Intent serviceIntent = new Intent(this, DateListService.class);
        bindService(serviceIntent, dateServiceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDateAdapter.removeOnDateItemClickListener();
        unbindService(dateServiceConnection);
        dateServiceConnection = null;
    }

    private static class UiHandler extends Handler {
        private final WeakReference<DateListActivity> mWeakActivity;

        public UiHandler(DateListActivity activity) {
            mWeakActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            DateListActivity dateListActivity = mWeakActivity.get();
            Log.d(TAG, "handleMessage() called with: msg = [" + msg + "]");
            switch (msg.what) {
                case DateListConstant.MSG_WHAT_DATE_CHANGE:
                    Log.d(TAG, "activity receive date change : " + DateProvider.getInstance().getAll());
                    String toastMsg = null;
                    switch (msg.arg1) {
                        case DateListConstant.TYPE_DATA_FOR_CACHE:
                            toastMsg = dateListActivity.getString(R.string.get_date_for_cache);
                            break;
                        case DateListConstant.TYPE_DATA_FOR_NETWORK:
                            toastMsg = dateListActivity.getString(R.string.get_date_for_network);
                            ;
                            break;
                        default:
                            toastMsg = dateListActivity.getString(R.string.get_date_default);
                            ;
                    }
                    Toast.makeText(dateListActivity, toastMsg, Toast.LENGTH_SHORT).show();
                    dateListActivity.mDateAdapter.notifyDataSetChanged();
                    break;
            }
        }
    }

    public class DateServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d(TAG, "onServiceConnected() called with: componentName = [" + componentName + "], iBinder = [" + iBinder + "]");
            mServiceInnerBinder = (DateListService.InnerBinder) iBinder;
            mServiceInnerBinder.setUiHandler(mUiHandler);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d(TAG, "onServiceDisconnected() called with: componentName = [" + componentName + "]");
            mServiceInnerBinder.setUiHandler(null);
        }
    }

    private class OnDateItemClickListeners implements OnDateItemClickListener {
        @Override
        public void onDateItemClick(int position) {
            Log.d(TAG, "onDateItemClick() called with: position = [" + position + "]");
            Intent intent = new Intent(DateListActivity.this, DateDetailActivity.class);
            intent.putExtra(DateListConstant.POSITION_KEY, position);
            startActivity(intent);
        }
    }

}