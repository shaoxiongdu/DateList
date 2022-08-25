package com.lixiang.programmingpracticedatelist.provider;

import android.os.Message;

import com.lixiang.programmingpracticedatelist.constants.DateListConstant;
import com.lixiang.programmingpracticedatelist.model.DateModel;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.locks.ReentrantLock;

public class DateProvider {

    private static DateProvider mInstance;
    private static ReentrantLock reentrantLock = new ReentrantLock();
    private List<DateModel> mDateModelList;

    private DateProvider() {
        mDateModelList = new ArrayList<>();
        mDateModelList.add(new DateModel("当前暂无数据",0,0));
    }

    public static DateProvider getInstance() {
        if (null == mInstance) {
            reentrantLock.lock();
            try {
                mInstance = new DateProvider();
            }finally {
                reentrantLock.unlock();
            }
        }
        return mInstance;
    }

    public List<DateModel> getAll() {
        return mDateModelList;
    }

    public DateModel getByPosition(int position) {
        return mDateModelList.get(position);
    }

    public void setDateModelList(List<DateModel> list){
        mDateModelList.clear();
        TreeSet<DateModel> treeSet = new TreeSet<>(list);
        mDateModelList.addAll(treeSet);
    }

}
