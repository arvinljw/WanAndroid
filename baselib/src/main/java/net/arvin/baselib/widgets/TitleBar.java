package net.arvin.baselib.widgets;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.arvin.baselib.R;
import net.arvin.baselib.utils.DimenUtil;
import net.arvin.baselib.utils.StatusBarUtil;

import static net.arvin.baselib.widgets.TitleBar.DefaultValues.MATCH_PARENT;
import static net.arvin.baselib.widgets.TitleBar.DefaultValues.TYPE_CUSTOM_VIEW;
import static net.arvin.baselib.widgets.TitleBar.DefaultValues.TYPE_IMAGE_VIEW;
import static net.arvin.baselib.widgets.TitleBar.DefaultValues.TYPE_NONE;
import static net.arvin.baselib.widgets.TitleBar.DefaultValues.TYPE_TEXT_VIEW;
import static net.arvin.baselib.widgets.TitleBar.DefaultValues.WRAP_CONTENT;

/**
 * Created by arvinljw on 2018/11/1 15:57
 * Function：TitleBar
 * Desc：Changing TitleBar is based on <a href="https://github.com/wuhenzhizao/android-titlebar">.
 */
public class TitleBar extends RelativeLayout implements View.OnClickListener {
    private View viewStatusBarFill;
    private View viewBottomLine;
    private View viewBottomShadow;
    private RelativeLayout rlMain;

    private TextView tvLeft;
    private ImageView imgLeft;
    private View customLeft;

    private TextView tvRight;
    private ImageView imgRight;
    private View customRight;

    private LinearLayout llMainCenter;
    private TextView tvCenter;
    private TextView tvCenterSub;
    private View customCenter;

    private boolean fillStatusBar;
    private int titleBarColor;
    private int titleBarHeight;
    private int statusBarColor;
    private int statusBarMode;

    private boolean showBottomLine;
    private int bottomLineColor;
    private float bottomShadowHeight;

    private int leftType;
    private String leftText;
    private int leftTextColor;
    private float leftTextSize;
    private int leftDrawable;
    private float leftDrawablePadding;
    private int leftImageResource;
    private int leftCustomViewRes;
    private int leftBackground;
    private float leftPaddingLeft;
    private float leftPaddingRight;

    private int rightType;
    private String rightText;
    private int rightTextColor;
    private float rightTextSize;
    private int rightImageResource;
    private int rightBackground;
    private int rightCustomViewRes;
    private float rightPaddingLeft;
    private float rightPaddingRight;

    private int centerType;
    private String centerText;
    private int centerTextColor;
    private float centerTextSize;
    private boolean centerTextMarquee;
    private String centerSubText;
    private int centerSubTextColor;
    private float centerSubTextSize;
    private int centerCustomViewRes;
    private float centerPaddingLeft;
    private float centerPaddingRight;

    private OnTitleClickListener listener;

    public TitleBar(Context context) {
        this(context, null);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttributes(context, attrs);
        initStruct(context);
        initTitle(context);
    }

    private void initAttributes(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TitleBar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            fillStatusBar = array.getBoolean(R.styleable.TitleBar_fillStatusBar, DefaultValues.FILL_STATUS_BAR);
        }
        titleBarColor = array.getColor(R.styleable.TitleBar_titleBarColor, Color.parseColor(DefaultValues.DEFAULT_THEME_COLOR));
        titleBarHeight = (int) array.getDimension(R.styleable.TitleBar_titleBarHeight, DimenUtil.dp2px(context, DefaultValues.HEADER_HEIGHT));
        statusBarColor = array.getColor(R.styleable.TitleBar_statusBarColor, Color.parseColor(DefaultValues.DEFAULT_THEME_COLOR));
        statusBarMode = array.getInt(R.styleable.TitleBar_statusBarMode, DefaultValues.DARK);

        showBottomLine = array.getBoolean(R.styleable.TitleBar_showBottomLine, DefaultValues.SHOW_BOTTOM_LINE);
        bottomLineColor = array.getColor(R.styleable.TitleBar_bottomLineColor, Color.parseColor(DefaultValues.DEFAULT_DIVIDER_COLOR));
        bottomShadowHeight = array.getDimension(R.styleable.TitleBar_bottomShadowHeight, DefaultValues.DEFAULT_NONE_RES);

        float defaultPadding = (int) array.getDimension(R.styleable.TitleBar_defaultPadding, DimenUtil.dp2px(context, DefaultValues.DEFAULT_PADDING));

        leftPaddingLeft = array.getDimension(R.styleable.TitleBar_leftPaddingLeft, defaultPadding);
        leftPaddingRight = array.getDimension(R.styleable.TitleBar_leftPaddingRight, defaultPadding);
        rightPaddingLeft = array.getDimension(R.styleable.TitleBar_rightPaddingLeft, defaultPadding);
        rightPaddingRight = array.getDimension(R.styleable.TitleBar_rightPaddingRight, defaultPadding);
        centerPaddingLeft = array.getDimension(R.styleable.TitleBar_centerPaddingLeft, defaultPadding);
        centerPaddingRight = array.getDimension(R.styleable.TitleBar_centerPaddingRight, defaultPadding);

        leftType = array.getInt(R.styleable.TitleBar_leftType, TYPE_NONE);
        if (leftType == TYPE_TEXT_VIEW) {
            leftText = array.getString(R.styleable.TitleBar_leftText);
            leftTextColor = array.getColor(R.styleable.TitleBar_leftTextColor, Color.parseColor(DefaultValues.DEFAULT_TEXT_COLOR));
            leftTextSize = array.getDimension(R.styleable.TitleBar_leftTextSize, DimenUtil.sp2px(context, DefaultValues.DEFAULT_TEXT_SIZE));
            leftDrawable = array.getResourceId(R.styleable.TitleBar_leftDrawable, DefaultValues.DEFAULT_NONE_RES);
            leftDrawablePadding = array.getDimension(R.styleable.TitleBar_leftDrawablePadding, DefaultValues.DEFAULT_DRAWABLE_PADDING);
        } else if (leftType == TYPE_IMAGE_VIEW) {
            leftImageResource = array.getResourceId(R.styleable.TitleBar_leftImageResource, R.drawable.ic_back_black);
        } else if (leftType == TYPE_CUSTOM_VIEW) {
            leftCustomViewRes = array.getResourceId(R.styleable.TitleBar_leftCustomView, DefaultValues.DEFAULT_NONE_RES);
        }
        leftBackground = array.getResourceId(R.styleable.TitleBar_leftBackground, DefaultValues.DEFAULT_NONE_RES);

        rightType = array.getInt(R.styleable.TitleBar_rightType, TYPE_NONE);
        if (rightType == TYPE_TEXT_VIEW) {
            rightText = array.getString(R.styleable.TitleBar_rightText);
            rightTextColor = array.getColor(R.styleable.TitleBar_rightTextColor, Color.parseColor(DefaultValues.DEFAULT_TEXT_COLOR));
            rightTextSize = array.getDimension(R.styleable.TitleBar_rightTextSize, DimenUtil.sp2px(context, DefaultValues.DEFAULT_TEXT_SIZE));
        } else if (rightType == TYPE_IMAGE_VIEW) {
            rightImageResource = array.getResourceId(R.styleable.TitleBar_rightImageResource, DefaultValues.DEFAULT_NONE_RES);
        } else if (rightType == TYPE_CUSTOM_VIEW) {
            rightCustomViewRes = array.getResourceId(R.styleable.TitleBar_rightCustomView, DefaultValues.DEFAULT_NONE_RES);
        }
        rightBackground = array.getResourceId(R.styleable.TitleBar_rightBackground, DefaultValues.DEFAULT_NONE_RES);

        centerType = array.getInt(R.styleable.TitleBar_centerType, TYPE_NONE);
        if (centerType == TYPE_TEXT_VIEW) {
            centerText = array.getString(R.styleable.TitleBar_centerText);
            centerTextColor = array.getColor(R.styleable.TitleBar_centerTextColor, Color.parseColor(DefaultValues.DEFAULT_TEXT_COLOR));
            centerTextSize = array.getDimension(R.styleable.TitleBar_centerTextSize, DimenUtil.sp2px(context, DefaultValues.DEFAULT_TITLE_TEXT_SIZE));
            centerTextMarquee = array.getBoolean(R.styleable.TitleBar_centerTextMarquee, DefaultValues.TITLE_MARQUEE);
            centerSubText = array.getString(R.styleable.TitleBar_centerSubText);
            centerSubTextColor = array.getColor(R.styleable.TitleBar_centerSubTextColor, Color.parseColor(DefaultValues.DEFAULT_SUB_TEXT_COLOR));
            centerSubTextSize = array.getDimension(R.styleable.TitleBar_centerSubTextSize, DimenUtil.sp2px(context, DefaultValues.DEFAULT_SUB_TITLE_TEXT_SIZE));
        } else if (centerType == TYPE_CUSTOM_VIEW) {
            centerCustomViewRes = array.getResourceId(R.styleable.TitleBar_centerCustomView, DefaultValues.DEFAULT_NONE_RES);
        }

        array.recycle();
    }

    private void initStruct(Context context) {
        ViewGroup.LayoutParams globalParams = new ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        setLayoutParams(globalParams);

        boolean transparentStatusBar = StatusBarUtil.supportTransparentStatusBar();

        if (fillStatusBar && transparentStatusBar) {
            int statusBarHeight = StatusBarUtil.getStatusBarHeight(context);
            viewStatusBarFill = new View(context);
            viewStatusBarFill.setId(StatusBarUtil.generateViewId());
            viewStatusBarFill.setBackgroundColor(statusBarColor);
            LayoutParams statusBarParams = new LayoutParams(MATCH_PARENT, statusBarHeight);
            statusBarParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            addView(viewStatusBarFill, statusBarParams);
        }

        rlMain = new RelativeLayout(context);
        rlMain.setId(StatusBarUtil.generateViewId());
        rlMain.setBackgroundColor(titleBarColor);
        LayoutParams mainParams = new LayoutParams(MATCH_PARENT, titleBarHeight);
        if (fillStatusBar && transparentStatusBar) {
            mainParams.addRule(RelativeLayout.BELOW, viewStatusBarFill.getId());
        } else {
            mainParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        }

        // 计算主布局高度
        if (showBottomLine) {
            mainParams.height = titleBarHeight - Math.max(1, DimenUtil.dp2px(context, DefaultValues.DIVIDER_HEIGHT));
            if (mainParams.height < 0) {
                mainParams.height = 0;
                showBottomLine = false;
            }
        } else {
            mainParams.height = titleBarHeight;
        }
        addView(rlMain, mainParams);

        // 构建分割线视图
        if (showBottomLine) {
            // 已设置显示标题栏分隔线,5.0以下机型,显示分隔线
            viewBottomLine = new View(context);
            viewBottomLine.setBackgroundColor(bottomLineColor);
            LayoutParams bottomLineParams = new LayoutParams(MATCH_PARENT, Math.max(1, DimenUtil.dp2px(context, DefaultValues.DIVIDER_HEIGHT)));
            bottomLineParams.addRule(RelativeLayout.BELOW, rlMain.getId());

            addView(viewBottomLine, bottomLineParams);
        } else if (bottomShadowHeight != 0) {
            viewBottomShadow = new View(context);
            viewBottomShadow.setBackgroundResource(R.drawable.titlebar_bottom_shadow);
            LayoutParams bottomShadowParams = new LayoutParams(MATCH_PARENT, DimenUtil.dp2px(context, bottomShadowHeight));
            bottomShadowParams.addRule(RelativeLayout.BELOW, rlMain.getId());

            addView(viewBottomShadow, bottomShadowParams);
        }
    }

    private void initTitle(Context context) {
        if (leftType != TYPE_NONE) {
            initMainLeftViews(context);
        }
        if (rightType != TYPE_NONE) {
            initMainRightViews(context);
        }
        if (centerType != TYPE_NONE) {
            initMainCenterViews(context);
        }
    }

    private void initMainLeftViews(Context context) {
        LayoutParams leftInnerParams = new LayoutParams(WRAP_CONTENT, MATCH_PARENT);
        leftInnerParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        leftInnerParams.addRule(RelativeLayout.CENTER_VERTICAL);

        if (leftType == TYPE_TEXT_VIEW) {
            tvLeft = new TextView(context);
            tvLeft.setId(StatusBarUtil.generateViewId());
            tvLeft.setText(leftText);
            tvLeft.setTextColor(leftTextColor);
            tvLeft.setTextSize(TypedValue.COMPLEX_UNIT_PX, leftTextSize);
            tvLeft.setGravity(Gravity.CENTER_VERTICAL);
            tvLeft.setSingleLine(true);
            tvLeft.setOnClickListener(this);

            if (leftDrawable != 0) {
                tvLeft.setCompoundDrawablePadding((int) leftDrawablePadding);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    tvLeft.setCompoundDrawablesRelativeWithIntrinsicBounds(leftDrawable, 0, 0, 0);
                } else {
                    tvLeft.setCompoundDrawablesWithIntrinsicBounds(leftDrawable, 0, 0, 0);
                }
            }
            tvLeft.setPadding((int) leftPaddingLeft, 0, (int) leftPaddingRight, 0);
            tvLeft.setBackgroundResource(leftBackground);

            rlMain.addView(tvLeft, leftInnerParams);
        } else if (leftType == TYPE_IMAGE_VIEW) {
            imgLeft = new ImageButton(context);
            imgLeft.setId(StatusBarUtil.generateViewId());
            imgLeft.setBackgroundColor(Color.TRANSPARENT);
            imgLeft.setImageResource(leftImageResource);
            imgLeft.setPadding((int) leftPaddingLeft, 0, (int) leftPaddingRight, 0);
            imgLeft.setOnClickListener(this);
            imgLeft.setBackgroundResource(leftBackground);

            rlMain.addView(imgLeft, leftInnerParams);
        } else if (leftType == TYPE_CUSTOM_VIEW) {
            customLeft = LayoutInflater.from(context).inflate(leftCustomViewRes, rlMain, false);
            if (customLeft.getId() == View.NO_ID) {
                customLeft.setId(StatusBarUtil.generateViewId());
            }
            customLeft.setOnClickListener(this);

            rlMain.addView(customLeft, leftInnerParams);
        }
    }

    private void initMainRightViews(Context context) {
        LayoutParams rightInnerParams = new LayoutParams(WRAP_CONTENT, MATCH_PARENT);
        rightInnerParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        rightInnerParams.addRule(RelativeLayout.CENTER_VERTICAL);

        if (rightType == TYPE_TEXT_VIEW) {
            tvRight = new TextView(context);
            tvRight.setId(StatusBarUtil.generateViewId());
            tvRight.setText(rightText);
            tvRight.setTextColor(rightTextColor);
            tvRight.setTextSize(TypedValue.COMPLEX_UNIT_PX, rightTextSize);
            tvRight.setGravity(Gravity.CENTER_VERTICAL);
            tvRight.setSingleLine(true);
            tvRight.setPadding((int) rightPaddingLeft, 0, (int) rightPaddingRight, 0);
            tvRight.setOnClickListener(this);
            tvRight.setBackgroundResource(rightBackground);

            rlMain.addView(tvRight, rightInnerParams);
        } else if (rightType == TYPE_IMAGE_VIEW) {
            imgRight = new ImageButton(context);
            imgRight.setId(StatusBarUtil.generateViewId());
            imgRight.setImageResource(rightImageResource);
            imgRight.setBackgroundColor(Color.TRANSPARENT);
            imgRight.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            imgRight.setPadding((int) rightPaddingLeft, 0, (int) rightPaddingRight, 0);
            imgRight.setOnClickListener(this);
            imgRight.setBackgroundResource(rightBackground);

            rlMain.addView(imgRight, rightInnerParams);
        } else if (rightType == TYPE_CUSTOM_VIEW) {
            customRight = LayoutInflater.from(context).inflate(rightCustomViewRes, rlMain, false);
            if (customRight.getId() == View.NO_ID) {
                customRight.setId(StatusBarUtil.generateViewId());
            }
            customRight.setOnClickListener(this);
            rlMain.addView(customRight, rightInnerParams);
        }
    }

    private void initMainCenterViews(Context context) {
        if (centerType == TYPE_TEXT_VIEW) {
            // 初始化中间子布局
            llMainCenter = new LinearLayout(context);
            llMainCenter.setId(StatusBarUtil.generateViewId());
            llMainCenter.setGravity(Gravity.CENTER);
            llMainCenter.setOrientation(LinearLayout.VERTICAL);
            llMainCenter.setOnClickListener(this);

            LayoutParams centerParams = new LayoutParams(WRAP_CONTENT, MATCH_PARENT);
            centerParams.leftMargin = (int) centerPaddingLeft;
            centerParams.rightMargin = (int) centerPaddingRight;
            centerParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            rlMain.addView(llMainCenter, centerParams);

            // 初始化标题栏TextView
            tvCenter = new TextView(context);
            tvCenter.setText(centerText);
            tvCenter.setTextColor(centerTextColor);
            tvCenter.setTextSize(TypedValue.COMPLEX_UNIT_PX, centerTextSize);
            tvCenter.setGravity(Gravity.CENTER);
            tvCenter.setSingleLine(true);
            // 设置跑马灯效果
            tvCenter.setMaxWidth(DimenUtil.getScreenWidth(context) * 3 / 5);
            if (centerTextMarquee) {
                tvCenter.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                tvCenter.setMarqueeRepeatLimit(-1);
                tvCenter.requestFocus();
                tvCenter.setSelected(true);
            }

            LinearLayout.LayoutParams centerTextParams = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
            llMainCenter.addView(tvCenter, centerTextParams);

            // 初始化副标题栏
            tvCenterSub = new TextView(context);
            tvCenterSub.setText(centerSubText);
            tvCenterSub.setTextColor(centerSubTextColor);
            tvCenterSub.setTextSize(TypedValue.COMPLEX_UNIT_PX, centerSubTextSize);
            tvCenterSub.setGravity(Gravity.CENTER);
            tvCenterSub.setSingleLine(true);
            if (TextUtils.isEmpty(centerSubText)) {
                tvCenterSub.setVisibility(View.GONE);
            }

            LinearLayout.LayoutParams centerSubTextParams = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
            llMainCenter.addView(tvCenterSub, centerSubTextParams);
        } else if (centerType == TYPE_CUSTOM_VIEW) {
            // 初始化中间自定义布局
            customCenter = LayoutInflater.from(context).inflate(centerCustomViewRes, rlMain, false);
            if (customCenter.getId() == View.NO_ID) {
                customCenter.setId(StatusBarUtil.generateViewId());
            }
            LayoutParams centerCustomParams = new LayoutParams(DimenUtil.getScreenWidth(context) * 7 / 10, MATCH_PARENT);
            centerCustomParams.leftMargin = (int) centerPaddingLeft;
            centerCustomParams.rightMargin = (int) centerPaddingRight;
            centerCustomParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            customCenter.setOnClickListener(this);
            rlMain.addView(customCenter, centerCustomParams);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!fillStatusBar) {
            return;
        }
        setUpImmersionTitleBar();
    }

    private void setUpImmersionTitleBar() {
        Window window = getWindow();
        if (window == null) return;
        // 设置状态栏背景透明
        StatusBarUtil.transparentStatusBar(window);
        // 设置图标主题
        if (statusBarMode == DefaultValues.DARK) {
            StatusBarUtil.setDarkMode(window);
        } else {
            StatusBarUtil.setLightMode(window);
        }
    }

    private Window getWindow() {
        Context context = getContext();
        Activity activity;
        if (context instanceof Activity) {
            activity = (Activity) context;
        } else {
            activity = (Activity) ((ContextWrapper) context).getBaseContext();
        }
        if (activity != null) {
            return activity.getWindow();
        }
        return null;
    }

    /**
     * 设置背景颜色
     */
    @Override
    public void setBackgroundColor(int color) {
        if (viewStatusBarFill != null) {
            viewStatusBarFill.setBackgroundColor(color);
        }
        rlMain.setBackgroundColor(color);
    }

    /**
     * 设置背景图片
     */
    @Override
    public void setBackgroundResource(int resource) {
        setBackgroundColor(Color.TRANSPARENT);
        super.setBackgroundResource(resource);
    }

    /**
     * 设置状态栏颜色
     */
    public void setStatusBarColor(int color) {
        if (viewStatusBarFill != null) {
            viewStatusBarFill.setBackgroundColor(color);
        }
    }

    /**
     * 是否填充状态栏
     */
    public void showStatusBar(boolean show) {
        if (viewStatusBarFill != null) {
            viewStatusBarFill.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * 切换状态栏模式
     */
    public void toggleStatusBarMode() {
        Window window = getWindow();
        if (window == null) return;
        StatusBarUtil.transparentStatusBar(window);
        if (statusBarMode == 0) {
            statusBarMode = 1;
            StatusBarUtil.setLightMode(window);
        } else {
            statusBarMode = 0;
            StatusBarUtil.setDarkMode(window);
        }
    }

    /**
     * 获取标题栏底部横线
     */
    public View getBottomLine() {
        return viewBottomLine;
    }

    public TextView getLeftTextView() {
        return tvLeft;
    }

    /**
     * 获取标题栏左边ImageButton，对应leftType = imageButton
     */
    public ImageView getLeftImageView() {
        return imgLeft;
    }

    public TextView getRightTextView() {
        return tvRight;
    }

    public ImageView getRightImageView() {
        return imgRight;
    }

    public LinearLayout getCenterLayout() {
        return llMainCenter;
    }

    public TextView getCenterTextView() {
        return tvCenter;
    }

    public TextView getCenterSubTextView() {
        return tvCenterSub;
    }

    /**
     * 获取左边自定义布局
     */
    public View getLeftCustomView() {
        return customLeft;
    }

    /**
     * 获取右边自定义布局
     */
    public View getRightCustomView() {
        return customRight;
    }

    /**
     * 获取中间自定义布局视图
     */
    public View getCenterCustomView() {
        return customCenter;
    }

    public void setLeftView(View leftView) {
        if (leftView.getId() == View.NO_ID) {
            leftView.setId(StatusBarUtil.generateViewId());
        }
        customLeft = leftView;
        customLeft.setOnClickListener(this);
        LayoutParams leftInnerParams = new LayoutParams(WRAP_CONTENT, MATCH_PARENT);
        leftInnerParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        leftInnerParams.addRule(RelativeLayout.CENTER_VERTICAL);
        rlMain.addView(leftView, leftInnerParams);
    }

    public void setCenterView(View centerView) {
        if (centerView.getId() == View.NO_ID) {
            centerView.setId(StatusBarUtil.generateViewId());
        }
        customCenter = centerView;
        customCenter.setOnClickListener(this);
        LayoutParams centerInnerParams = new LayoutParams(WRAP_CONTENT, MATCH_PARENT);
        centerInnerParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        centerInnerParams.addRule(RelativeLayout.CENTER_VERTICAL);
        rlMain.addView(centerView, centerInnerParams);
    }

    public void setRightView(View rightView) {
        if (rightView.getId() == View.NO_ID) {
            rightView.setId(StatusBarUtil.generateViewId());
        }
        customRight = rightView;
        customRight.setOnClickListener(this);
        LayoutParams rightInnerParams = new LayoutParams(WRAP_CONTENT, MATCH_PARENT);
        rightInnerParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        rightInnerParams.addRule(RelativeLayout.CENTER_VERTICAL);
        rlMain.addView(rightView, rightInnerParams);
    }

    public View getTitleBottomShadow() {
        return viewBottomShadow;
    }

    @Override
    public void onClick(View v) {
        if (listener == null) {
            return;
        }
        if (v == tvLeft || v == imgLeft || v == customLeft) {
            listener.onTitleClicked(v, OnTitleClickListener.TYPE_LEFT);
        } else if (v == tvRight || v == imgRight || v == customRight) {
            listener.onTitleClicked(v, OnTitleClickListener.TYPE_RIGHT);
        } else if (v == llMainCenter || v == customCenter) {
            listener.onTitleClicked(v, OnTitleClickListener.TYPE_CENTER);
        }
    }

    public void setTitleClickListener(OnTitleClickListener listener) {
        this.listener = listener;
    }

    public interface OnTitleClickListener {
        /*被点击的部分，只需要分为左边右边还是中间，因为每个部分的内容只会有一种存在*/
        int TYPE_LEFT = 0;
        int TYPE_RIGHT = 1;
        int TYPE_CENTER = 2;

        /**
         * @param view        被点击的View
         * @param clickedType 可根据被点击的类型，判断是哪部分被点击，值包含：
         */
        void onTitleClicked(View view, int clickedType);
    }

    class DefaultValues {
        static final int DEFAULT_NONE_RES = 0;

        static final int DARK = 0;//default
        static final int LIGHT = 1;

        static final boolean FILL_STATUS_BAR = true;
        static final boolean SHOW_BOTTOM_LINE = true;
        static final boolean TITLE_MARQUEE = true;

        static final String DEFAULT_THEME_COLOR = "#ffffff";
        static final String DEFAULT_DIVIDER_COLOR = "#d8d8d8";
        static final String DEFAULT_TEXT_COLOR = "#222222";
        static final String DEFAULT_SUB_TEXT_COLOR = "#666666";

        static final int HEADER_HEIGHT = 56;
        static final int DEFAULT_PADDING = 16;
        static final int DEFAULT_DRAWABLE_PADDING = 4;
        static final int DEFAULT_TEXT_SIZE = 14;
        static final int DEFAULT_TITLE_TEXT_SIZE = 18;
        static final int DEFAULT_SUB_TITLE_TEXT_SIZE = 14;
        static final float DIVIDER_HEIGHT = 0.5f;

        static final int TYPE_NONE = 0;
        static final int TYPE_TEXT_VIEW = 1;
        static final int TYPE_IMAGE_VIEW = 2;
        static final int TYPE_CUSTOM_VIEW = 3;

        static final int MATCH_PARENT = ViewGroup.LayoutParams.MATCH_PARENT;
        static final int WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT;
    }
}
