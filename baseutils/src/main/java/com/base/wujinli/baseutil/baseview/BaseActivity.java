package com.base.wujinli.baseutil.baseview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.base.wujinli.baseutil.R;
import com.base.wujinli.baseutil.baseview.dialog.PrompfDialog;
import com.base.wujinli.baseutil.utils.ActivityUtils;
import com.base.wujinli.baseutil.utils.MyToast;
import com.base.wujinli.baseutil.utils.OnClickUtil;
import com.base.wujinli.baseutil.utils.PermissionListener;
import com.base.wujinli.baseutil.utils.ToastUtil;
import com.base.wujinli.baseutil.utils.ToastUtilHaveRight;

import java.util.ArrayList;
import java.util.List;

/**
 * author: WuJinLi
 * time  : 17/7/3
 * desc  : activity基类
 */

public abstract class BaseActivity extends Activity implements IBaseview {

    public static ToastUtil toastUtil;
    public static ToastUtilHaveRight toastUtilRight;
    public Context context;
    public Dialog progressDialog;
    static PermissionListener mPermissionListener;
    private static final int CODE_REQUEST_PERMISSION = 1;
    private Toast mToast;

    /**
     * 自定义对话框
     */

    PrompfDialog customDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(getLayoutId());
        // 输入框默认隐藏
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ActivityUtils.addActivity(this);
        initBasic();
        initFontScale();
        initDatas();
        initView();
        setListener();
        setView();
    }

    /**
     * 获取布局文件的id
     *
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 初始化一些创建activity的数据，例如页面传递的intent等
     */
    protected abstract void initDatas();

    /**
     * 初始化控件
     */
    protected abstract void initView();

    /**
     * 设置监听器
     */
    protected abstract void setListener();

    /**
     * 控件的赋值
     */
    protected abstract void setView();


    private void initBasic() {
        this.context = BaseActivity.this;
        if (toastUtil == null) {
            toastUtil = new ToastUtil(this);
        }
    }


    private void initFontScale() {
        Configuration configuration = getResources().getConfiguration();
        configuration.fontScale = (float) 1; //0.85 small size, 1 normal size, 1,15 big etc
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        metrics.scaledDensity = configuration.fontScale * metrics.density;
        getBaseContext().getResources().updateConfiguration(configuration, metrics);
    }


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


    /**********************权限申请****************************/
    /**
     * 申请权限
     *
     * @param permissions 需要申请的权限(数组)
     * @param listener    权限回调接口
     */
    public static void requestPermissions(String[] permissions, PermissionListener listener) {
        Activity activity = ActivityUtils.getTopActivity();
        if (null == activity) {
            return;
        }

        mPermissionListener = listener;
        List<String> permissionList = new ArrayList<>();
        for (String permission : permissions) {
            //权限没有授权
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager
                    .PERMISSION_GRANTED) {
                permissionList.add(permission);
            }
        }

        if (!permissionList.isEmpty()) {
            ActivityCompat.requestPermissions(activity, permissionList.toArray(new
                    String[permissionList.size()]), CODE_REQUEST_PERMISSION);
        } else {
            mPermissionListener.onGranted();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CODE_REQUEST_PERMISSION:
                if (grantResults.length > 0) {
                    List<String> deniedPermissions = new ArrayList<>();
                    for (int i = 0; i < grantResults.length; i++) {
                        int result = grantResults[i];
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            String permission = permissions[i];
                            deniedPermissions.add(permission);
                        }
                    }

                    if (deniedPermissions.isEmpty()) {
                        mPermissionListener.onGranted();
                    } else {
                        mPermissionListener.onDenied(deniedPermissions);
                    }
                }
                break;

            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityUtils.removeActivity(this);
    }
}
