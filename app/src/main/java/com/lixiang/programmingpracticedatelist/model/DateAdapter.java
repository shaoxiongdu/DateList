/*
 * author:dushaoxiong@lixiang.com
 */

/*
 * author:dushaoxiong@lixiang.com
 */

package com.lixiang.programmingpracticedatelist.model;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lixiang.programmingpracticedatelist.R;

import java.util.List;

public class DateAdapter extends RecyclerView.Adapter<DateAdapter.ViewHolder> {

    private static final String TAG = DateAdapter.class.getName();
    private List<DateModel> mDateList;

    private OnDateItemClickListener mOnDateItemClickListener;

    public DateAdapter(List<DateModel> dateList) {
        mDateList = dateList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.date_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (mDateList == null || mDateList.size() == 0) {
            return;
        }
        DateModel dateModel = mDateList.get(position);
        holder.dateName.setText(dateModel.getName());
        holder.dateHour.setText(dateModel.getHour().toString());
        holder.dateMin.setText(dateModel.getMin().toString());

        holder.itemView.setOnClickListener((view) -> {
            if (null != mOnDateItemClickListener) {
                mOnDateItemClickListener.onDateItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDateList == null ? 0 : mDateList.size();
    }

    /**
     * @param listener
     */
    public void setOnDateItemClickListener(OnDateItemClickListener listener) {
        mOnDateItemClickListener = listener;
    }

    public void removeOnDateItemClickListener() {
        mOnDateItemClickListener = null;
    }

    public interface OnDateItemClickListener {
        void onDateItemClick(int position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dateName;
        TextView dateHour;
        TextView dateMin;

        public ViewHolder(View view) {
            super(view);
            dateName = view.findViewById(R.id.name);
            dateHour = view.findViewById(R.id.hour);
            dateMin = view.findViewById(R.id.min);
        }

    }

}
