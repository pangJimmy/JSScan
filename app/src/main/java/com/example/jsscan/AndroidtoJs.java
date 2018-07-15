package com.example.jsscan;

import android.util.Log;
import android.webkit.JavascriptInterface;

/**
 * Created by pangshanwei on 2018/7/16.
 */

public class AndroidtoJs extends Object {

    // 定义JS需要调用的方法
    // 被JS调用的方法必须加入@JavascriptInterface注解
    @JavascriptInterface
    public void hello(String msg) {
//        System.out.println("JS调用了Android的hello方法");
        Log.e("AndroidtoJs", msg) ;

    }
}
