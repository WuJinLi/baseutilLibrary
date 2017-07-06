package com.base.wujinli.baseutil.baseview;

import android.content.Context;

/**
 * author: WuJinLi
 * time  : 17/7/3
 * desc  :
 */

public interface IBaseview {
    /**
     * 没有提示语
     */
    public void showLoadingDialog();

    public void showLoadingDialog(boolean canCancel, String msg);

    /**
     * 提示语为空则提示指点君，在路途中
     *
     * @param msg
     */
    public void showLoadingDialog(String msg);

    public void showLoadingDialog(boolean canCancel, String msg, boolean alive);

    public void cancelLoadingDialog();

    public void showSystemToast(String msg);

    public void showSystemShortToast(String msg);

    public void showToast(String msg);

    public void showToastRight(String msg);

    public void showToast(int res);

    public void showToastShort(int res);

    public void showToastShort(String res);


}
