package net.gility.acrida.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;

import net.gility.acrida.android.ApplicationLoader;
import net.gility.acrida.R;
import net.gility.acrida.ui.adapter.ActiveAdapter;
import net.gility.acrida.network.OSChinaApi;
import net.gility.acrida.content.Active;
import net.gility.acrida.content.ActiveList;
import net.gility.acrida.model.Constants;
import net.gility.acrida.content.Notice;
import net.gility.acrida.utils.NoticeUtils;
import net.gility.acrida.ui.MainActivity;
import net.gility.acrida.ui.widget.StateView;
import net.gility.acrida.utils.DialogHelp;
import net.gility.acrida.utils.HTMLUtil;
import net.gility.acrida.utils.TDevice;
import net.gility.acrida.utils.UIHelper;
import net.gility.acrida.utils.XmlUtils;

import java.io.InputStream;
import java.io.Serializable;

/**
 * 动态fragment
 * 
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @author kymjs (https://github.com/kymjs)
 * @created 2014年10月22日 下午3:35:43
 * 
 */
public class ActiveFragment extends BaseListFragment<Active> implements
        OnItemLongClickListener {

    protected static final String TAG = ActiveFragment.class.getSimpleName();
    private static final String CACHE_KEY_PREFIX = "active_list";
    private boolean mIsWatingLogin; // 还没登陆

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (mErrorLayout != null) {
                mIsWatingLogin = true;
                mErrorLayout.setStateType(StateView.TYPE_NETWORK_ERROR);
                mErrorLayout.setErrorMessage(getString(R.string.unlogin_tip));
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter filter = new IntentFilter(Constants.INTENT_ACTION_LOGOUT);
        getActivity().registerReceiver(mReceiver, filter);
    }

    @Override
    public void onDestroy() {
        getActivity().unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    @Override
    public void onResume() {
        if (mIsWatingLogin) {
            mCurrentPage = 0;
            mState = STATE_REFRESH;
            requestData(false);
        }
        refreshNotice();
        super.onResume();
    }

    /**
     * 开始刷新请求
     */
    private void refreshNotice() {
        Notice notice = MainActivity.mNotice;
        if (notice == null) {
            return;
        }
        if (notice.getAtmeCount() > 0 && mCatalog == ActiveList.CATALOG_ATME) {
            onRefresh();
        } else if (notice.getReviewCount() > 0
                && mCatalog == ActiveList.CATALOG_COMMENT) {
            onRefresh();
        }
    }

    @Override
    protected ActiveAdapter getListAdapter() {
        return new ActiveAdapter();
    }

    @Override
    protected String getCacheKeyPrefix() {
        return new StringBuffer(CACHE_KEY_PREFIX + mCatalog).append(
                ApplicationLoader.getInstance().getLoginUid()).toString();
    }

    @Override
    protected ActiveList parseList(InputStream is) {
        ActiveList list = XmlUtils.toBean(ActiveList.class, is);
        return list;
    }

    @Override
    protected ActiveList readList(Serializable seri) {
        return ((ActiveList) seri);
    }

    @Override
    public void initView(View view) {
        if (mCatalog == ActiveList.CATALOG_LASTEST) {
            setHasOptionsMenu(true);
        }
        super.initView(view);
        mListView.setOnItemLongClickListener(this);
        mListView.setOnItemClickListener(this);
        mErrorLayout.setOnLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ApplicationLoader.getInstance().isLogin()) {
                    mErrorLayout.setStateType(StateView.TYPE_NETWORK_LOADING);
                    requestData(false);
                } else {
                    UIHelper.showLoginActivity(getActivity());
                }
            }
        });
        if (ApplicationLoader.getInstance().isLogin()) {
            UIHelper.sendBroadcastForNotice(getActivity());
        }
    }

    @Override
    protected void requestData(boolean refresh) {
        if (ApplicationLoader.getInstance().isLogin()) {
            mIsWatingLogin = false;
            super.requestData(refresh);
        } else {
            mIsWatingLogin = true;
            mErrorLayout.setStateType(StateView.TYPE_NETWORK_ERROR);
            mErrorLayout.setErrorMessage(getString(R.string.unlogin_tip));
        }
    }

    @Override
    protected void sendRequestData() {
        OSChinaApi.getActiveList(ApplicationLoader.getInstance().getLoginUid(),
                mCatalog, mCurrentPage, mHandler);
    }

    @Override
    protected void onRefreshNetworkSuccess() {
        if (ApplicationLoader.getInstance().isLogin()) {
            if (0 == NoticeViewPagerFragment.sCurrentPage) {
                NoticeUtils.clearNotice(Notice.TYPE_ATME);
            } else if (1 == NoticeViewPagerFragment.sCurrentPage
                    || NoticeViewPagerFragment.sShowCount[1] > 0) { // 如果当前显示的是评论页，则发送评论页已被查看的Http请求
                NoticeUtils.clearNotice(Notice.TYPE_COMMENT);
            } else {
                NoticeUtils.clearNotice(Notice.TYPE_ATME);
            }
            UIHelper.sendBroadcastForNotice(getActivity());
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
        Active active = mAdapter.getItem(position);
        if (active != null)
            UIHelper.showActiveRedirect(view.getContext(), active);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view,
            int position, long id) {
        final Active active = mAdapter.getItem(position);
        if (active == null)
            return false;
        String[] items = new String[] { getResources().getString(R.string.copy) };
        DialogHelp.getSelectDialog(getActivity(), items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                TDevice.copyTextToBoard(HTMLUtil.delHTMLTag(active.getMessage()));
            }
        }).show();
        return true;
    }

    @Override
    protected long getAutoRefreshTime() {
        // 最新动态，即是好友圈
        if (mCatalog == ActiveList.CATALOG_LASTEST) {
            return 5 * 60;
        }
        return super.getAutoRefreshTime();
    }
}
