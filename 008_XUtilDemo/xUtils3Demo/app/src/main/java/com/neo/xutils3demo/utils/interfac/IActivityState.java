package com.neo.xutils3demo.utils.interfac;

import android.app.Activity;

/**
 * Created by Neo on 2018/3/13.
 */

public interface IActivityState {
    /**
     * 得到当前Activity
     * @return
     */
    Activity current();

    /**
     * 任务栈中Activity的总数
     * @return
     */
    int count();
    /**
     * 判断应用是否处于前台，即是否可见
     * @return
     */
    boolean isFront();
}
