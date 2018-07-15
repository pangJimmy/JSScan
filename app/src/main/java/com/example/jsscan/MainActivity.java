package com.example.jsscan;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.device.ScanDevice;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class MainActivity extends AppCompatActivity {

    WebView mWebView;
    private final static String SCAN_ACTION = "scan.rcv.message";
    private ScanDevice scanDevice;
    String barcodeStr;
    //扫描接收广播
    private BroadcastReceiver scanReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            byte[] barocode = intent.getByteArrayExtra("barocode");
            int barocodelen = intent.getIntExtra("length", 0);
            byte temp = intent.getByteExtra("barcodeType", (byte) 0);
            android.util.Log.e("debug", "----codetype--" + temp);
            barcodeStr = new String(barocode, 0, barocodelen);
            //将扫描结果回调
            mWebView.loadUrl("javascript:callJS('" + barcodeStr + "')");
            // tvResult.setText(barcodeStr);
            //写入TXT中
            scanDevice.stopScan();
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWebView = (WebView) findViewById(R.id.webview);

        scanDevice = new ScanDevice();
        WebSettings webSettings = mWebView.getSettings();

        // 设置与Js交互的权限
        webSettings.setJavaScriptEnabled(true);

        // 通过addJavascriptInterface()将Java对象映射到JS对象
        //参数1：Javascript对象名
        //参数2：Java对象名
        mWebView.addJavascriptInterface(new Mtest(), "scaner");//AndroidtoJS类对象映射到js的test对象

        // 加载JS代码
        // 格式规定为:file:///android_asset/文件名.html
        mWebView.loadUrl("file:///android_asset/javascript.html");
    }



   class Mtest extends Object{
       @JavascriptInterface
       public void scan() {
           scanDevice.startScan();
       }
   }




    @Override
    protected void onResume() {
        super.onResume();
        if(scanDevice != null){
            scanDevice.openScan() ;
            scanDevice.setOutScanMode(0);//接收广播
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(SCAN_ACTION);
        registerReceiver(scanReceiver, filter);
    };

    @Override
    protected void onPause() {

        super.onPause();


        if(scanDevice != null) {
            scanDevice.stopScan();
            scanDevice.closeScan();
        }
        unregisterReceiver(scanReceiver);
    }
}
