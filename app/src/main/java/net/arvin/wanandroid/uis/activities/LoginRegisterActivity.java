package net.arvin.wanandroid.uis.activities;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.rengwuxian.materialedittext.MaterialEditText;

import net.arvin.baselib.base.BaseActivity;
import net.arvin.baselib.utils.DialogUtil;
import net.arvin.baselib.utils.ToastUtil;
import net.arvin.baselib.widgets.TitleBar;
import net.arvin.wanandroid.R;
import net.arvin.wanandroid.entities.LoginResultEntity;
import net.arvin.wanandroid.entities.Response;
import net.arvin.wanandroid.entities.events.LoginSuccessEvent;
import net.arvin.wanandroid.nets.ApiObserver;
import net.arvin.wanandroid.nets.repositories.UserRepo;
import net.arvin.wanandroid.utils.SharePreferenceUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by arvinljw on 2018/11/28 14:16
 * Function：
 * Desc：
 */
public class LoginRegisterActivity extends BaseActivity implements View.OnClickListener {

    private TitleBar titleBar;
    private ShimmerFrameLayout layoutShimmer;
    private ShimmerFrameLayout layoutRegisterShimmer;

    private View layoutLogin;
    private MaterialEditText edName;
    private MaterialEditText edPassword;

    private View layoutRegister;
    private MaterialEditText edRegisterName;
    private MaterialEditText edRegisterPassword;
    private MaterialEditText edRegisterRepassword;
    private AnimatorSet mRightOutSet;
    private AnimatorSet mLeftInSet;
    private boolean mIsShowBack;
    private DialogUtil dialogUtil;


    public static void open(Context context) {
        Intent intent = new Intent(context, LoginRegisterActivity.class);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        context.startActivity(intent);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_login;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        layoutShimmer = findViewById(R.id.layout_shimmer);
        layoutRegisterShimmer = findViewById(R.id.layout_shimmer_register);
        layoutShimmer.startShimmer();
        layoutRegisterShimmer.startShimmer();

        titleBar = findViewById(R.id.title_bar);
        titleBar.getCenterTextView().setText(R.string.login);
        titleBar.getLeftImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        layoutLogin = findViewById(R.id.layout_login);
        edName = findViewById(R.id.ed_name);
        edPassword = findViewById(R.id.ed_password);
        findViewById(R.id.btn_login).setOnClickListener(this);
        findViewById(R.id.tv_to_register).setOnClickListener(this);

        layoutRegister = findViewById(R.id.layout_register);
        edRegisterName = findViewById(R.id.ed_register_name);
        edRegisterPassword = findViewById(R.id.ed_register_password);
        edRegisterRepassword = findViewById(R.id.ed_register_repassword);
        findViewById(R.id.btn_register).setOnClickListener(this);
        findViewById(R.id.tv_to_login).setOnClickListener(this);

        setAnimators(); // 设置动画
        setCameraDistance(); // 设置镜头距离
        dialogUtil = new DialogUtil(this);
    }

    private void setAnimators() {
        mRightOutSet = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.anim_out);
        mLeftInSet = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.anim_in);

        // 设置点击事件
        mRightOutSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                layoutLogin.setVisibility(View.VISIBLE);
                layoutRegister.setVisibility(View.VISIBLE);
            }
        });
        mLeftInSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (mIsShowBack) {
                    layoutLogin.setVisibility(View.GONE);
                    layoutRegister.setVisibility(View.VISIBLE);
                } else {
                    layoutLogin.setVisibility(View.VISIBLE);
                    layoutRegister.setVisibility(View.GONE);
                }
            }
        });
    }

    private void setCameraDistance() {
        int distance = 16000;
        float scale = getResources().getDisplayMetrics().density * distance;
        layoutLogin.setCameraDistance(scale);
        layoutRegister.setCameraDistance(scale);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mIsShowBack) {
            layoutShimmer.startShimmer();
        } else {
            layoutRegisterShimmer.startShimmer();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!mIsShowBack) {
            layoutShimmer.stopShimmer();
        } else {
            layoutRegisterShimmer.stopShimmer();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                login();
                break;
            case R.id.btn_register:
                register();
                break;
            case R.id.tv_to_register:
                flipCard();
                break;
            case R.id.tv_to_login:
                flipCard();
                break;
        }
    }

    public void flipCard() {
        // 正面朝上
        if (!mIsShowBack) {
            mRightOutSet.setTarget(layoutLogin);
            mLeftInSet.setTarget(layoutRegister);
            mRightOutSet.start();
            mLeftInSet.start();
            mIsShowBack = true;
            titleBar.getCenterTextView().setText(R.string.register);
        } else { // 背面朝上
            mRightOutSet.setTarget(layoutRegister);
            mLeftInSet.setTarget(layoutLogin);
            mRightOutSet.start();
            mLeftInSet.start();
            mIsShowBack = false;
            titleBar.getCenterTextView().setText(R.string.login);
        }
    }

    private void login() {
        String name = null;
        String password = null;
        Editable edNameText = edName.getText();
        Editable edPasswordText = edPassword.getText();
        if (edNameText != null) {
            name = edNameText.toString().trim();
        }
        if (edPasswordText != null) {
            password = edPasswordText.toString().trim();
        }
        if (TextUtils.isEmpty(name)) {
            edName.setError("请输入用户名");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            edPassword.setError("请输入密码");
            return;
        }
        final String psw = password;
        dialogUtil.showProgressDialog("登录中...");
        UserRepo.login(name, psw).observe(this, new ApiObserver<LoginResultEntity>() {
            @Override
            public void onSuccess(Response<LoginResultEntity> response) {
                dialogUtil.hideProgressDialog();
                LoginResultEntity data = response.getData();
                data.setPassword(psw);
                data.setIcon(SharePreferenceUtil.getKeyValue(data.getUsername()));
                SharePreferenceUtil.saveUser(data);
                EventBus.getDefault().post(new LoginSuccessEvent());
                ToastUtil.showToast(getApplicationContext(), "登录成功");
                onBackPressed();
            }

            @Override
            public void onFailure(int code, String msg) {
                super.onFailure(code, msg);
                dialogUtil.hideProgressDialog();
                ToastUtil.showToast(getApplicationContext(), msg);
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
                dialogUtil.hideProgressDialog();
            }
        });
    }

    private void register() {
        String name = "";
        String password = "";
        String repassword = "";
        Editable edNameText = edRegisterName.getText();
        Editable edPasswordText = edRegisterPassword.getText();
        Editable edRepasswordText = edRegisterRepassword.getText();
        if (edNameText != null) {
            name = edNameText.toString().trim();
        }
        if (edPasswordText != null) {
            password = edPasswordText.toString().trim();
        }
        if (edRepasswordText != null) {
            repassword = edRepasswordText.toString().trim();
        }
        if (TextUtils.isEmpty(name)) {
            edRegisterName.setError("请输入用户名");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            edRegisterPassword.setError("请输入密码");
            return;
        }
        if (TextUtils.isEmpty(repassword)) {
            edRegisterRepassword.setError("请输入确认密码");
            return;
        }
        if (!password.equals(repassword)) {
            edRegisterRepassword.setError("确认密码与原密码不一致");
            return;
        }
        final String psw = password;
        dialogUtil.showProgressDialog("注册中...");
        UserRepo.register(name, psw, repassword).observe(this, new ApiObserver<LoginResultEntity>() {
            @Override
            public void onSuccess(Response<LoginResultEntity> response) {
                dialogUtil.hideProgressDialog();
                LoginResultEntity data = response.getData();
                data.setPassword(psw);
                data.setIcon(SharePreferenceUtil.getKeyValue(data.getUsername()));
                SharePreferenceUtil.saveUser(data);
                EventBus.getDefault().post(new LoginSuccessEvent());
                ToastUtil.showToast(getApplicationContext(), "注册成功");
                onBackPressed();
            }

            @Override
            public void onFailure(int code, String msg) {
                super.onFailure(code, msg);
                dialogUtil.hideProgressDialog();
                ToastUtil.showToast(getApplicationContext(), msg);
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
                dialogUtil.hideProgressDialog();
            }
        });
    }
}
