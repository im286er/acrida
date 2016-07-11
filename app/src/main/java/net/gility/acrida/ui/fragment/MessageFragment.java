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
import net.gility.acrida.ui.adapter.MessageAdapter;
import net.gility.acrida.network.OperationResponseHandler;
import net.gility.acrida.network.OSChinaApi;
import net.gility.acrida.model.Constants;
import net.gility.acrida.content.MessageList;
import net.gility.acrida.content.Messages;
import net.gility.acrida.content.Notice;
import net.gility.acrida.content.Result;
import net.gility.acrida.content.ResultBean;
import net.gility.acrida.utils.NoticeUtils;
import net.gility.acrida.ui.MainActivity;
import net.gility.acrida.ui.widget.StateView;
import net.gility.acrida.utils.DialogHelp;
import net.gility.acrida.utils.HTMLUtil;
import net.gility.acrida.utils.TDevice;
import net.gility.acrida.utils.UIHelper;
import net.gility.acrida.utils.XmlUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;

public class MessageFragment extends BaseListFragment<Messages> implements
        OnItemLongClickListener {
    protected static final String TAG = ActiveFragment.class.getSimpleName();
    private static final String CACHE_KEY_PREFIX = "message_list";
    private boolean mIsWatingLogin;

    private final BroadcastReceiver mLogoutReceiver = new BroadcastReceiver() {

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
        getActivity().registerReceiver(mLogoutReceiver, filter);
    }

    @Override
    public void onDestroy() {
        getActivity().unregisterReceiver(mLogoutReceiver);
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

    private void refreshNotice() {
        Notice notice = MainActivity.mNotice;
        if (notice != null && notice.getMsgCount() > 0) {
            onRefresh();
        }
    }

    @Override
    protected MessageAdapter getListAdapter() {
        return new MessageAdapter();
    }

    @Override
    protected String getCacheKeyPrefix() {
        return CACHE_KEY_PREFIX;
    }

    @Override
    protected MessageList parseList(InputStream is) throws Exception {
        MessageList list = XmlUtils.toBean(MessageList.class, is);
        return list;
    }

    @Override
    protected MessageList readList(Serializable seri) {
        return ((MessageList) seri);
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        mListView.setDivider(null);
        mListView.setDividerHeight(0);
        mListView.setOnItemLongClickListener(this);
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
        OSChinaApi.getMessageList(ApplicationLoader.getInstance().getLoginUid(),
                mCurrentPage, mHandler);
    }

    @Override
    protected void onRefreshNetworkSuccess() {
        if (2 == NoticeViewPagerFragment.sCurrentPage
                || NoticeViewPagerFragment.sShowCount[2] > 0) { // 在page中第三个位置
            NoticeUtils.clearNotice(Notice.TYPE_MESSAGE);
            UIHelper.sendBroadcastForNotice(getActivity());
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
        Messages message = (Messages) mAdapter.getItem(position);
        if (message != null)
            UIHelper.showMessageDetail(getActivity(), message.getFriendId(),
                    message.getFriendName());
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view,
            int position, long id) {
        final Messages message = (Messages) mAdapter.getItem(position);
        DialogHelp.getSelectDialog(getActivity(), getResources().getStringArray(R.array.message_list_options), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case 0:
                        TDevice.copyTextToBoard(HTMLUtil.delHTMLTag(message
                                .getContent()));
                        break;
                    case 1:
                        handleDeleteMessage(message);
                        break;
                    default:
                        break;
                }
            }
        }).show();
        return true;
    }

    private void handleDeleteMessage(final Messages message) {

        DialogHelp.getConfirmDialog(getActivity(), getString(R.string.confirm_delete_message,
                message.getFriendName()), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                showWaitDialog(R.string.progress_submit);

                OSChinaApi.deleteMessage(ApplicationLoader.getInstance()
                                .getLoginUid(), message.getFriendId(),
                        new DeleteMessageOperationHandler(message));
            }
        }).show();
    }

    class DeleteMessageOperationHandler extends OperationResponseHandler {

        public DeleteMessageOperationHandler(Object... args) {
            super(args);
        }

        @Override
        public void onSuccess(int code, ByteArrayInputStream is, Object[] args)
                throws Exception {
            Result res = XmlUtils.toBean(ResultBean.class, is).getResult();
            if (res.OK()) {
                Messages msg = (Messages) args[0];
                mAdapter.removeItem(msg);
                mAdapter.notifyDataSetChanged();
                hideWaitDialog();
                ApplicationLoader.showToastShort(R.string.tip_delete_success);
            } else {
                ApplicationLoader.showToastShort(res.getErrorMessage());
                hideWaitDialog();
            }
        }

        @Override
        public void onFailure(int code, String errorMessage, Object[] args) {
            ApplicationLoader.showToastShort(R.string.tip_delete_faile);
            hideWaitDialog();
        }
    }
}
