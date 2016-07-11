package net.gility.acrida.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import net.gility.acrida.R;
import net.gility.acrida.utils.TDevice;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StateView extends FrameLayout implements
        android.view.View.OnClickListener {// , ISkinUIObserver {

    public static final int TYPE_HIDE_LAYOUT = 4;
    public static final int TYPE_NETWORK_ERROR = 1;
    public static final int TYPE_NETWORK_LOADING = 2;
    public static final int TYPE_NO_DATA = 3;
    public static final int TYPE_NO_DATA_ENABLE_CLICK = 5;
    public static final int TYPE_NO_LOGIN = 6;

    @BindView(R.id.animProgress) protected ProgressBar mProgressBar;
    @BindView(R.id.tv_error_layout) protected TextView mStateInfo;
    @BindView(R.id.img_error_layout) public ImageView mStateIcon;
    private boolean mClickEnable = true;
    private android.view.View.OnClickListener mListener;
    private int mStateType;
    private String mNoDataContent = "";
    private Context mContext;


    public StateView(Context context) {
        super(context);
        mContext = context;
    }

    public StateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        init();
    }

    private void init() {
        inflate(mContext, R.layout.view_state, this);
        ButterKnife.bind(this);
        setBackgroundColor(-1);
        setOnClickListener(this);
        mStateIcon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mClickEnable) {
                    // setStateType(TYPE_NETWORK_LOADING);
                    if (mListener != null)
                        mListener.onClick(v);
                }
            }
        });
        changeErrorLayoutBgMode(mContext);
    }

    public void changeErrorLayoutBgMode(Context context1) {
        // mLayout.setBackgroundColor(SkinsUtil.getColor(context1,
        // "bgcolor01"));
        // tv.setTextColor(SkinsUtil.getColor(context1, "textcolor05"));
    }

    public void dismiss() {
        mStateType = TYPE_HIDE_LAYOUT;
        setVisibility(View.GONE);
    }

    public int getStateType() {
        return mStateType;
    }

    public boolean isLoadError() {
        return mStateType == TYPE_NETWORK_ERROR;
    }

    public boolean isLoading() {
        return mStateType == TYPE_NETWORK_LOADING;
    }

    @Override
    public void onClick(View v) {
        if (mClickEnable) {
            // setStateType(TYPE_NETWORK_LOADING);
            if (mListener != null)
                mListener.onClick(v);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        // MyApplication.getInstance().getAtSkinObserable().registered(this);
        onSkinChanged();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        // MyApplication.getInstance().getAtSkinObserable().unregistered(this);
    }

    public void onSkinChanged() {
        // mLayout.setBackgroundColor(SkinsUtil
        // .getColor(getContext(), "bgcolor01"));
        // tv.setTextColor(SkinsUtil.getColor(getContext(), "textcolor05"));
    }

    public void setDayNight(boolean flag) {}

    public void setErrorMessage(String msg) {
        mStateInfo.setText(msg);
    }

    /**
     * 新添设置背景
     * 
     * @author 火蚁 2015-1-27 下午2:14:00
     * 
     */
    public void setErrorImag(int imgResource) {
        try {
            mStateIcon.setImageResource(imgResource);
        } catch (Exception e) {
        }
    }

    public void setStateType(int i) {
        setVisibility(View.VISIBLE);
        switch (i) {
        case TYPE_NETWORK_ERROR:
            mStateType = TYPE_NETWORK_ERROR;
            // img.setBackgroundDrawable(SkinsUtil.getDrawable(context,"pagefailed_bg"));
            if (TDevice.hasInternet()) {
                mStateInfo.setText(R.string.error_view_load_error_click_to_refresh);
                mStateIcon.setBackgroundResource(R.drawable.pagefailed_bg);
            } else {
                mStateInfo.setText(R.string.error_view_network_error_click_to_refresh);
                mStateIcon.setBackgroundResource(R.drawable.page_icon_network);
            }
            mStateIcon.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
            mClickEnable = true;
            break;
        case TYPE_NETWORK_LOADING:
            mStateType = TYPE_NETWORK_LOADING;
            // animProgress.setBackgroundDrawable(SkinsUtil.getDrawable(context,"loadingpage_bg"));
            mProgressBar.setVisibility(View.VISIBLE);
            mStateIcon.setVisibility(View.GONE);
            mStateInfo.setText(R.string.error_view_loading);
            mClickEnable = false;
            break;
        case TYPE_NO_DATA:
            mStateType = TYPE_NO_DATA;
            // img.setBackgroundDrawable(SkinsUtil.getDrawable(context,"page_icon_empty"));
            mStateIcon.setBackgroundResource(R.drawable.page_icon_empty);
            mStateIcon.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
            setNoDataContent();
            mClickEnable = true;
            break;
        case TYPE_HIDE_LAYOUT:
            setVisibility(View.GONE);
            break;
        case TYPE_NO_DATA_ENABLE_CLICK:
            mStateType = TYPE_NO_DATA_ENABLE_CLICK;
            mStateIcon.setBackgroundResource(R.drawable.page_icon_empty);
            // img.setBackgroundDrawable(SkinsUtil.getDrawable(context,"page_icon_empty"));
            mStateIcon.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
            setNoDataContent();
            mClickEnable = true;
            break;
        default:
            break;
        }
    }

    public void setNoDataContent(String noDataContent) {
        mNoDataContent = noDataContent;
    }

    public void setOnLayoutClickListener(View.OnClickListener listener) {
        this.mListener = listener;
    }

    public void setNoDataContent() {
        if (!mNoDataContent.equals(""))
            mStateInfo.setText(mNoDataContent);
        else
            mStateInfo.setText(R.string.error_view_no_data);
    }

    @Override
    public void setVisibility(int visibility) {
        if (visibility == View.GONE)
            mStateType = TYPE_HIDE_LAYOUT;
        super.setVisibility(visibility);
    }
}
