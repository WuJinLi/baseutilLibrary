package com.base.wujinli.baseutil.baseview;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.base.wujinli.baseutil.R;
import com.base.wujinli.baseutil.baseview.dialog.PrompfDialog;
import com.base.wujinli.baseutil.utils.MyToast;
import com.base.wujinli.baseutil.utils.OnClickUtil;
import com.base.wujinli.baseutil.utils.ToastUtil;
import com.base.wujinli.baseutil.utils.ToastUtilHaveRight;

/**
 * author: WuJinLi
 * time  : 17/7/3
 * desc  :basefragment
 */

public class BaseFragment extends Fragment implements IBaseview {
    public Dialog progressDialog;

    public static ToastUtil toastUtil;

    public static ToastUtilHaveRight toastUtilRight;
    private Toast mToast;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initBasic();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    private void initBasic() {
        if (toastUtil == null) {
            toastUtil = new ToastUtil(this.getActivity());
        }
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
            progressDialog = new Dialog(getActivity(), R.style.progress_dialog);
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
            mToast = Toast.makeText(getActivity(), resourceId,
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
        MyToast.showToast(this.getActivity().getApplicationContext(), msg);
    }


    public void cancelToast() {
        if (mToast != null) {
            mToast.cancel();
        }
    }

    @Override
    public void showSystemToast(String msg) {
        if (!ToastUtil.isFastShow(3000)) {
            Toast.makeText(this.getActivity().getApplicationContext(), msg, Toast.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    public void showSystemShortToast(String msg) {
        if (!ToastUtil.isFastShow(2000)) {
            Toast.makeText(this.getActivity().getApplicationContext(), msg, Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    public void showToast(String msg) {
        try {
            if (toastUtil == null) {
                toastUtil = new ToastUtil(this.getActivity().getApplicationContext());
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
                toastUtilRight = new ToastUtilHaveRight(this.getActivity().getApplicationContext());
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
                toastUtil = new ToastUtil(this.getActivity().getApplicationContext());
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
                toastUtil = new ToastUtil(this.getActivity().getApplicationContext());
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
                toastUtil = new ToastUtil(this.getActivity().getApplicationContext());
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
    PrompfDialog customDialog;

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
        if (window != null) {
            window.setGravity(Gravity.CENTER);
        }
        if (!isHidden()) {
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
        if (window != null) {
            window.setGravity(Gravity.CENTER);
        }
        if (!isHidden()) {
            customDialog.show();
        }
    }

    @Override
    public Context getContent() {
        return this.getActivity();
    }
}
