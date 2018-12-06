package net.arvin.baselib.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import net.arvin.permissionhelper.PermissionUtil;

/**
 * Created by arvinljw on 2018/10/30 10:50
 * Function：
 * Desc：
 */
public class PermissionHelper {
    private PermissionUtil permissionUtil;

    public PermissionHelper(FragmentActivity activity) {
        permissionUtil = build(new PermissionUtil.Builder().with(activity));
    }

    public PermissionHelper(Fragment fragment) {
        permissionUtil = build(new PermissionUtil.Builder().with(fragment));
    }

    private PermissionUtil build(PermissionUtil.Builder builder) {
        return builder.setTitleText("提示")
                .setSettingMsg("当前应用缺少必要权限。\n请点击\"设置\"-\"权限\"-打开所需权限。")
                .setInstallAppMsg("允许安装来自此来源的应用")
                .build();
    }

    public void request(String msg, String permission, PermissionUtil.RequestPermissionListener listener) {
        if (permissionUtil == null) {
            return;
        }
        permissionUtil.request(msg, permission, listener);
    }

    public void request(String msg, String[] permissions, PermissionUtil.RequestPermissionListener listener) {
        if (permissionUtil == null) {
            return;
        }
        permissionUtil.request(msg, permissions, listener);
    }

    public void onDestroy() {
        if (permissionUtil != null) {
            permissionUtil.removeListener();
            permissionUtil = null;
        }
    }
}
