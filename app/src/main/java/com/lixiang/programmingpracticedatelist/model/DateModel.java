/*
 * author:dushaoxiong@lixiang.com
 */

/*
 * author:dushaoxiong@lixiang.com
 */

package com.lixiang.programmingpracticedatelist.model;

import androidx.annotation.NonNull;

import java.util.Objects;

/**
 * 时间实体模型
 *
 * @see <a href="https://li.feishu.cn/docx/doxcnKXD5UvSTW655yqAROs8yAd">Android培训-Android基础部分编程实践-产品需求文档</a>
 */
public class DateModel implements Comparable<DateModel> {
    private final String name;
    private final Integer hour;
    private final Integer min;

    public DateModel(String name, Integer hour, Integer min) {
        this.name = name;
        this.hour = hour;
        this.min = min;
    }

    public String getName() {
        return name;
    }

    public Integer getHour() {
        return hour;
    }

    public Integer getMin() {
        return min;
    }

    @NonNull
    @Override
    public String toString() {
        return "DateModel{" +
                "name='" + name + '\'' +
                ", hour=" + hour +
                ", min=" + min +
                '}';
    }

    /**
     * 按时排序 如果时相同 则按分排序
     *
     * @param dateModel 其他对象
     * @return 结果
     */
    @Override
    public int compareTo(DateModel dateModel) {
        if (null == dateModel || dateModel == this) {
            return 0;
        }

        return !Objects.equals(this.hour, dateModel.hour) ?
                Integer.compare(this.hour, dateModel.getHour()) : Integer.compare(this.min, dateModel.min);
    }
}
