package com.base.wujinli.baseutil.utils;

import java.util.List;

/**
 * author: WuJinLi
 * time  : 17/7/3
 */

public interface PermissionListener {
    void onGranted();

    void onDenied(List<String> deniedPermissions);
}
