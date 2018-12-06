package net.arvin.wanandroid.uis.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import net.arvin.baselib.base.BaseFragment;
import net.arvin.baselib.utils.ImageLoader;
import net.arvin.baselib.utils.PermissionHelper;
import net.arvin.permissionhelper.PermissionUtil;
import net.arvin.selector.SelectorHelper;
import net.arvin.selector.data.ConstantData;
import net.arvin.wanandroid.R;
import net.arvin.wanandroid.entities.LoginResultEntity;
import net.arvin.wanandroid.entities.events.LoginSuccessEvent;
import net.arvin.wanandroid.entities.events.LogoutEvent;
import net.arvin.wanandroid.uis.activities.AboutUsActivity;
import net.arvin.wanandroid.uis.activities.CollectionArticleActivity;
import net.arvin.wanandroid.uis.activities.LoginRegisterActivity;
import net.arvin.wanandroid.uis.activities.SettingActivity;
import net.arvin.wanandroid.utils.SharePreferenceUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

/**
 * Created by arvinljw on 2018/11/16 14:43
 * Function：
 * Desc：
 */
public class SlidingFragment extends BaseFragment implements View.OnClickListener {
    private static final int REQ_CODE_CHANGE_AVATAR = 1001;
    private LoginResultEntity user;
    private ImageView imgAvatar;
    private TextView tvName;

    private PermissionHelper permissionHelper;
    private AlertDialog dialog;

    @Override
    protected int getContentView() {
        return R.layout.fragment_sliding;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        imgAvatar = root.findViewById(R.id.img_avatar);
        tvName = root.findViewById(R.id.tv_name);
        user = SharePreferenceUtil.getUser();
        initUser();

        imgAvatar.setOnClickListener(this);
        root.findViewById(R.id.layout_navigation).setOnClickListener(this);
        root.findViewById(R.id.item_collection).setOnClickListener(this);
        root.findViewById(R.id.item_setting).setOnClickListener(this);
        root.findViewById(R.id.item_about_us).setOnClickListener(this);
    }

    private void initUser() {
        if (user == null) {
            tvName.setText("去登录");
            tvName.setTextColor(getResources().getColor(R.color.secondary_text));
            imgAvatar.setImageResource(R.drawable.img_avatar);
            return;
        }
        tvName.setText(user.getUsername());
        tvName.setTextColor(getResources().getColor(R.color.primary_text));
        ImageLoader.loadImageCircle(getActivity(), "file://" + user.getIcon(), imgAvatar, R.drawable.img_avatar);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_avatar:
                if (!isLogin()) {
                    LoginRegisterActivity.open(getActivity());
                    return;
                }
                changeAvatar();
                break;
            case R.id.item_collection:
                if (!isLogin()) {
                    LoginRegisterActivity.open(getActivity());
                    return;
                }
                startActivity(new Intent(getActivity(), CollectionArticleActivity.class));
                break;
            case R.id.item_setting:
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
            case R.id.item_about_us:
                startActivity(new Intent(getActivity(), AboutUsActivity.class));
                break;
        }
    }

    private void changeAvatar() {
        if (permissionHelper == null) {
            permissionHelper = new PermissionHelper(this);
        }
        permissionHelper.request("修改头像需要访问相机和本地媒体文件", PermissionUtil.asArray(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE), new PermissionUtil.RequestPermissionListener() {
            @Override
            public void callback(boolean granted, boolean isAlwaysDenied) {
                showChangeAvatarDialog();
            }
        });
    }

    private void showChangeAvatarDialog() {
        final FragmentActivity activity = getActivity();
        if (activity == null) {
            return;
        }
        if (dialog == null) {
            dialog = new AlertDialog.Builder(activity)
                    .setMessage("换个帅气的头像吧～")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SelectorHelper.selectPicture(activity, true, true, REQ_CODE_CHANGE_AVATAR);
                        }
                    }).setNegativeButton("取消", null).create();
        }
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQ_CODE_CHANGE_AVATAR) {
            ArrayList<String> backPics = data.getStringArrayListExtra(ConstantData.KEY_BACK_PICTURES);
            if (backPics != null && backPics.size() > 0) {
                user.setIcon(backPics.get(0));
                SharePreferenceUtil.saveUser(user);
                initUser();
            }
        }
    }

    private boolean isLogin() {
        return user != null;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogin(LoginSuccessEvent loginSuccessEvent) {
        user = SharePreferenceUtil.getUser();
        initUser();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogout(LogoutEvent logoutEvent) {
        user = null;
        initUser();
    }
}
