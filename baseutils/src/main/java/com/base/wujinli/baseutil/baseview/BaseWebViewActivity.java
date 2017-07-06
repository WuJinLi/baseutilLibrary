package com.base.wujinli.baseutil.baseview;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.ClientCertRequest;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.base.wujinli.baseutil.R;
import com.base.wujinli.baseutil.baseview.dialog.PrompfDialog;
import com.base.wujinli.baseutil.utils.LogUtils;
import com.base.wujinli.baseutil.utils.MyToast;
import com.base.wujinli.baseutil.utils.OnClickUtil;
import com.base.wujinli.baseutil.utils.ToastUtil;
import com.base.wujinli.baseutil.utils.ToastUtilHaveRight;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;

/**
 * author: WuJinLi
 * time  : 17/7/4
 * desc  : BasewebViewActivity基类
 */

public abstract class BaseWebViewActivity extends Activity implements IBaseview {


    public static ToastUtil toastUtil;
    public static ToastUtilHaveRight toastUtilRight;
    public Context context;
    public Dialog progressDialog;
    private Toast mToast;
    PrompfDialog customDialog;
    public static final String WEBVIEW_URL = "webView_url";//传值的key

    public WebView webView;//webVew对象
    public String url;//网址


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        try {
//            initPrivateKeyAndX509Certificate();
//        } catch (Exception e) {
//            LogUtils.e("wjl", e.getMessage());
//            e.printStackTrace();
//        }

        setContentView(getLayoutId());
        getIntents();
        initData();
        initView();
        setWebViewClient();
        setWebViewSettings();

        try {
            //设置打开的页面地址
            if (!TextUtils.isEmpty(url)) {
                webView.loadUrl(url);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    /**
     * 加载布局文件
     *
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 页面传值获取intent对象及webview访问的网址
     */
    public void getIntents() {
        Intent intent = getIntent();
        if (intent != null) {
            url = intent.getStringExtra("webView_url");
        }
    }

    /**
     * 初始化相关的数据
     */
    protected abstract void initData();

    /**
     * 初始化webview和其他的view
     */
    protected abstract void initView();




    public void setWebViewClient() {
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }

            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request,
                                        WebResourceError error) {
                netfail(view, error.getErrorCode(), error.getDescription().toString(), request
                        .getUrl().toString());
            }

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                netfail(view, errorCode,
                        description, failingUrl);

            }

            //支持处理https请求
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                //为了发生不可预知的错误处理,是自己公司的签名则信任通过,否则取消,即显示空白页
//                if("ComanyName自己签名的公司名".equals(error.getCertificate().getIssuedBy().getOName())){
//                    LogUtils.e("msj", "procceed"+error.getCertificate().getIssuedBy().getCName());
//                    handler.proceed();
//                }else{
//                    LogUtils.e("msj", "cancel"+error.getCertificate().getIssuedBy().getCName());
//                    handler.cancel();
//                }

            }

            //页面加载开始加载的时候调用
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                showLoadingDialog();
                super.onPageStarted(view, url, favicon);
            }

            //页面加载结束的时候调用
            @Override
            public void onPageFinished(WebView view, String url) {
                cancelLoadingDialog();
                super.onPageFinished(view, url);
            }

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onReceivedClientCertRequest(WebView view, ClientCertRequest request) {
                LogUtils.e("msj", "onReceivedClientCertRequest");
                if (((mX509Certificates != null) && (mX509Certificates.length != 0))) {
                    request.proceed(mPrivateKey, mX509Certificates);
                } else {
                    request.cancel();
                }

            }

//            public void onReceivedClientCertRequest(WebView view,
//                                                    ClientCertRequestHandler handler, String
// host_and_port) {
//                //注意该方法是调用的隐藏函数接口。这儿是整个验证的技术难点：就是如何调用隐藏类的接口。
//                if(((mX509Certificates != null) && (mX509Certificates.length !=0))){
//                    handler.proceed(mPrivateKey, mX509Certificates);
//                }else{
//                    handler.cancel();
//                }
//            }


        });
    }

    /**
     * 返回键(重写返回方法)
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack(); // goBack()表示返回WebView的上一页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     * websetting属性设置
     */
    private void setWebViewSettings() {
        WebSettings settings = webView.getSettings();
        //设置WebView属性，能够执行Javascript脚本
        settings.setJavaScriptEnabled(true);
        settings.setPluginState(WebSettings.PluginState.ON);
        settings.setBuiltInZoomControls(true);
        settings.setBlockNetworkImage(false);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setDefaultTextEncodingName("UTF-8");
        settings.setLoadsImagesAutomatically(true);

        // settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);

        /* 解决空白页问题 */
        settings.setDomStorageEnabled(true);//允许dom存储,部分不设置的网页会打不开
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setJavaScriptCanOpenWindowsAutomatically(false);
        if (Build.VERSION.SDK_INT >= 21) { //设置HTTPS中加载http
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
    }


    private X509Certificate[] mX509Certificates;
    private PrivateKey mPrivateKey;


    /**
     * 创建证书库，并导入证书
     *
     * @throws Exception
     */
    private void initPrivateKeyAndX509Certificate()
            throws Exception {
        // 创建一个证书库，并将证书导入证书库
//        try {
////            InputStream input =this.getResources().openRawResource();
//            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
//            keyStore.load(input, "pw123456".toCharArray());
//            Enumeration<?> localEnumeration;
//            localEnumeration = keyStore.aliases();
//            while (localEnumeration.hasMoreElements()) {
//                String str3 = (String) localEnumeration.nextElement();
//                mPrivateKey = (PrivateKey) keyStore.getKey(str3,
//                        "pw123456".toCharArray());
//                if (mPrivateKey == null) {
//                    continue;
//                } else {
//                    Certificate[] arrayOfCertificate = keyStore
//                            .getCertificateChain(str3);
//                    mX509Certificates = new X509Certificate[arrayOfCertificate.length];
//                    for (int j = 0; j < mX509Certificates.length; j++) {
//                        mX509Certificates[j] = ((X509Certificate) arrayOfCertificate[j]);
//                    }
//                }
//            }
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (CertificateException e) {
//            e.printStackTrace();
//        } catch (KeyStoreException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        LogUtils.e("wjl", "initPrivateKey" + mPrivateKey);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    /**
     * 抽象方法来呈现网络请求错误的时的页面显示
     *
     * @param view
     * @param errorCode
     * @param description
     * @param failingUrl
     */
    public abstract void netfail(WebView view, int errorCode,
                                 String description, String failingUrl);


    /******************显示加载框*********************/
    @Override
    public void showLoadingDialog() {
        showLoadingDialog(true, getString(R.string.loading));
    }

    @Override
    public void showLoadingDialog(boolean canCancel, String msg) {
        showLoadingDialog(canCancel, msg, false);
    }

    @Override
    public void showLoadingDialog(String msg) {
        if (msg.isEmpty()) {
            msg = getString(R.string.loading);
        }
        showLoadingDialog(false, msg);
    }

    @Override
    public void showLoadingDialog(boolean canCancel, String msg, boolean alive) {
        if (!alive && progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
        if (!(alive && progressDialog != null)) {
            progressDialog = new Dialog(this, R.style.progress_dialog);
            progressDialog.setContentView(R.layout.progress_dialog);
            progressDialog.setCancelable(canCancel);
            progressDialog.setCanceledOnTouchOutside(canCancel);
            if (progressDialog.getWindow() != null) {
                progressDialog.getWindow().setBackgroundDrawableResource(
                        R.drawable.trans50bg);
            }
            TextView message = (TextView) progressDialog
                    .findViewById(R.id.id_tv_loadingmsg);
            if (msg.isEmpty()) {
                message.setVisibility(View.GONE);
            } else {
                message.setVisibility(View.VISIBLE);
                message.setText(msg);
            }
        }
        try {
            progressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void cancelLoadingDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }


    /*********************信息Toast***********************/


    /**
     * 显示吐司
     *
     * @param resourceId 资源号
     */
    public void alertToast(int resourceId) {

        if (mToast == null) {
            mToast = Toast.makeText(getApplicationContext(), resourceId,
                    Toast.LENGTH_SHORT);
        } else {
            mToast.setText(resourceId);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    @SuppressLint("ShowToast")
    /**
     * 显示吐司
     * @param msg 消息字符
     */
    public void alertToast(String msg) {
        MyToast.showToast(context, msg);
    }


    public void cancelToast() {
        if (mToast != null) {
            mToast.cancel();
        }
    }

    @Override
    public void showSystemToast(String msg) {
        if (!ToastUtil.isFastShow(3000)) {
            Toast.makeText(this.getApplicationContext(), msg, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void showSystemShortToast(String msg) {
        if (!ToastUtil.isFastShow(2000)) {
            Toast.makeText(this.getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showToast(String msg) {
        try {
            if (toastUtil == null) {
                toastUtil = new ToastUtil(this.getApplicationContext());
            }
            if (!ToastUtil.isFastShow(3000)) {
                toastUtil.show(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showToastRight(String msg) {
        try {
            if (toastUtilRight == null) {
                toastUtilRight = new ToastUtilHaveRight(this.getApplicationContext());
            }
            toastUtilRight.show(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showToast(int res) {
        try {
            if (toastUtil == null) {
                toastUtil = new ToastUtil(this.getApplicationContext());
            }
            toastUtil.show(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showToastShort(int res) {
        try {
            if (toastUtil == null) {
                toastUtil = new ToastUtil(this.getApplicationContext());
            }
            if (!OnClickUtil.isFastDoubleClick(3000)) {
                toastUtil.showShort(res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showToastShort(String res) {
        try {
            if (toastUtil == null) {
                toastUtil = new ToastUtil(this.getApplicationContext());
            }
            if (!OnClickUtil.isFastDoubleClick(2000)) {
                toastUtil.showShort(res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**************************显示信息提示框****************************/
    /**
     * @param context
     * @param content    提示内容
     * @param submitName 确认按钮
     * @param cancelName 取消按钮
     * @param listener   监听器
     */
    public void showCustomDialog(final BaseActivity context, String content, String submitName,
                                 String cancelName, PrompfDialog.UpdateOnclickListener listener) {

        customDialog = null;
        customDialog = new PrompfDialog(context,
                R.style.transparentFrameWindowStyle2, submitName, cancelName,
                content, this.getString(R.string.app_name));
        customDialog.setCanceledOnTouchOutside(false);
        customDialog.setCancelable(false);
        customDialog
                .setUpdateOnClickListener(listener);
        Window window = customDialog.getWindow();
        window.setGravity(Gravity.CENTER);
        if (!isFinishing()) {
            customDialog.show();
        }


    }

    /**
     * @param context
     * @param content
     * @param submitName
     * @param cancelName
     * @param titleName  可以自定义标题的dialog
     */
    public void showCustomDialog(final BaseActivity context, String content, String submitName,
                                 String cancelName, String titleName, PrompfDialog
                                         .UpdateOnclickListener listener) {

        customDialog = null;
        customDialog = new PrompfDialog(context,
                R.style.transparentFrameWindowStyle2, submitName, cancelName,
                content, titleName);
        customDialog.setCanceledOnTouchOutside(false);
        customDialog.setCancelable(false);
        customDialog
                .setUpdateOnClickListener(listener);
        Window window = customDialog.getWindow();
        window.setGravity(Gravity.CENTER);
        if (!isFinishing()) {
            customDialog.show();
        }


    }
}
