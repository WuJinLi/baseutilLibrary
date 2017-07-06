package com.base.wujinli.baseutil;

import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.base.wujinli.baseutil.baseview.BaseWebViewActivity;
import com.base.wujinli.baseutil.utils.NetWorkUtils;

/**
 * author: WuJinLi
 * time  : 17/7/4
 * desc  : BaseWebViewActivity测试demo
 */

public class WebViewActivity extends BaseWebViewActivity {

    private LinearLayout ll_no_net;//网络异常(父布局)
    private ImageView img_no_net;//网络异常(图片)
    private TextView tv_no_net;//网络异常(展示)
    private Button btn_no_net;//网络异常(按钮)
    private Boolean isNetFalse = false;
    private ImageView img_back;

    @Override
    protected int getLayoutId() {
        return R.layout.ac_basewebview;
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initView() {
        webView = (WebView) findViewById(R.id.webview);
        webView.clearCache(true);


        img_back = (ImageView) findViewById(R.id.img_back);//网络异常(父布局)
        ll_no_net = (LinearLayout) findViewById(R.id.ll_no_net);//网络异常(父布局)
        img_no_net = (ImageView) findViewById(R.id.img_no_net);//网络异常(图片)
        tv_no_net = (TextView) findViewById(R.id.tv_no_net);//网络异常(展示)
        btn_no_net = (Button) findViewById(R.id.btn_no_net);//网络异常(按钮)
        btn_no_net.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //设置打开的页面地址
                    if (!TextUtils.isEmpty(url)) {
                        webView.loadUrl(url);
                        isNetFalse = false;
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void setWebViewClient() {
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                showLoadingDialog();
            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request,
                                        WebResourceError error) {
                super.onReceivedError(view, request, error);
                cancelLoadingDialog();
                netfail(view, error.getErrorCode(), error.getDescription().toString(), request
                        .getUrl().toString());
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                cancelLoadingDialog();

                if (!isNetFalse) {
                    if (ll_no_net.getVisibility() == View.VISIBLE) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ll_no_net.setVisibility(View.GONE);//网络异常(父布局)隐藏
                                    }
                                });
                            }
                        }).start();
                    }

                }
            }
        });
    }

    @Override
    public void netfail(WebView view, int errorCode, String description, String failingUrl) {
        ll_no_net.setVisibility(View.VISIBLE);
        isNetFalse = true;
        if (NetWorkUtils.isNetworkAvailable(this)) {//网络不给力
            img_no_net.setImageResource(R.drawable.web_network_error);
            tv_no_net.setText("网络不给力");
            btn_no_net.setText("刷新");
        } else if (errorCode >= 400) {//系统出错了
            img_no_net.setImageResource(R.drawable.web_network_error);
            tv_no_net.setText("系统出错了");
            btn_no_net.setText("再试一次");
        }
    }
}
