/*
 * author:dushaoxiong@lixiang.com
 */

/*
 * author:dushaoxiong@lixiang.com
 */

package com.lixiang.programmingpracticedatelist.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.BaseInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.lixiang.programmingpracticedatelist.R;
import com.lixiang.programmingpracticedatelist.constants.DateListConstant;
import com.lixiang.programmingpracticedatelist.model.DateModel;
import com.lixiang.programmingpracticedatelist.provider.DateProvider;

public class DateDetailActivity extends AppCompatActivity {

    private static final int PIN_TYPE_HOUR = 1;
    private static final int PIN_TYPE_MINUTE = 2;

    private static final String TAG = DateDetailActivity.class.getName();

    // 自定义动画曲线   see: https://easings.net/#easeOutQuart
    private static final BaseInterpolator easeOutQuartInterpolator = new BaseInterpolator() {
        @Override
        public float getInterpolation(float v) {
            return 1 - (float) Math.pow(1 - v, 4);
        }
    };

    private DateModel mDateModel;
    private ImageView mHourImageView;
    private ImageView mMinuteImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_detail);
        initDateModel();
        initView();

        // 分针目标度数
        float minuteTargetAngle = mDateModel.getMin() * (360.f / 60);
        // 启动分针动画
        startAnim(PIN_TYPE_MINUTE, minuteTargetAngle);

        // 时针目标度数
        float hourTargetAngle = mDateModel.getHour() % 12 * 360.f / 12;
        // 加上分针百分比的偏移量
        hourTargetAngle += 360.f / 12 * mDateModel.getMin() * 1.0f / 60;
        // 启动时针动画
        startAnim(PIN_TYPE_HOUR, hourTargetAngle);

    }

    private void startAnim(int type, float angle) {
        // 旋转动画
        RotateAnimation anim = new RotateAnimation(0f,
                angle,
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);

        // 自定义动画曲线 easeOutQuart
        anim.setInterpolator(DateDetailActivity.easeOutQuartInterpolator);

        // 设置动画持续周期 500ms
        anim.setDuration(DateListConstant.ROTATE_TIME);
        // 动画执行完后是否停留在执行完的状态
        anim.setFillAfter(true);

        switch (type) {
            case PIN_TYPE_HOUR:
                mHourImageView.setAnimation(anim);
                break;
            case PIN_TYPE_MINUTE:
                mMinuteImageView.setAnimation(anim);
                break;
            default:
        }
        anim.start();
    }

    private void initDateModel() {
        Intent intent = getIntent();
        int position = intent.getIntExtra("position", 0);
        mDateModel = DateProvider.getInstance().getByPosition(position);
        Log.d(TAG, "initData() called :" + mDateModel);
    }

    private void initView() {
        mHourImageView = findViewById(R.id.hour_image_view);
        mMinuteImageView = findViewById(R.id.minute_image_view);
    }

}