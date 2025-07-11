package com.study.sunnyweather.util;

import android.util.Log;

/**
 * 日志打印， 单例类
 */
public class LogUtil {
    private static final int VERBOSE = 1;
    private static final int DEBUGE = 2;
    private static final int INFO = 3;
    private static final int WARN = 4;
    private static final int ERROR = 5;
    // 打印： >= level 日志
    private static int level = VERBOSE;

    private LogUtil() {}
    private static final LogUtil logUtil = new LogUtil();

    public static LogUtil getInstance() {
        return logUtil;
    }

    public void v(String tag, String msg) {
        if (level <= VERBOSE) Log.v(tag, msg);
    }

    public void d(String tag, String msg) {
        if (level <= DEBUGE) Log.d(tag, msg);
    }

    public void i(String tag, String msg) {
        if (level <= INFO) Log.i(tag, msg);
    }

    public void w(String tag, String msg) {
        if (level <= WARN) Log.w(tag, msg);
    }

    public void e(String tag, String msg) {
        if (level <= ERROR) Log.e(tag, msg);
    }
}
