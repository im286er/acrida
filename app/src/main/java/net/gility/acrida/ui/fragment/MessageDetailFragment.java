package net.gility.acrida.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;

import com.loopj.android.http.AsyncHttpResponseHandler;

import net.gility.acrida.android.ApplicationLoader;
import net.gility.acrida.R;
import net.gility.acrida.ui.adapter.MessageDetailAdapter;
import net.gility.acrida.network.OperationResponseHandler;
import net.gility.acrida.network.OSChinaApi;
import net.gility.acrida.ui.BaseActivity;
import net.gility.acrida.ui.adapter.ListBaseAdapter;
import net.gility.acrida.content.Comment;
import net.gility.acrida.content.CommentList;
import net.gility.acrida.model.Constants;
import net.gility.acrida.content.Result;
import net.gility.acrida.content.ResultBean;
import net.gility.acrida.ui.widget.emoji.KJEmojiFragment;
import net.gility.acrida.ui.widget.emoji.OnSendClickListener;
import net.gility.acrida.ui.widget.StateView;
import net.gility.acrida.utils.DialogHelp;
import net.gility.acrida.utils.HTMLUtil;
import net.gility.acrida.utils.TDevice;
import net.gility.acrida.utils.UIHelper;
import net.gility.acrida.utils.XmlUtils;

import cz.msebera.android.httpclient.Header;
import org.kymjs.kjframe.utils.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 与某人的聊天记录界面（留言详情）
 * 
 * @author kymjs (http://www.kymjs.com/)
 * 
 */
public class MessageDetailFragment extends BaseListFragment<Comment> implements
        OnItemLongClickListener, OnSendClickListener {
    protected static final String TAG = ActiveFragment.class.getSimpleName();
    public static final String BUNDLE_KEY_FID = "BUNDLE_KEY_FID";
    public static final String BUNDLE_KEY_FNAME = "BUNDLE_KEY_FNAME";
    private static final String CACHE_KEY_PREFIX = "message_detail_list";

    private int mFid;
    private String mFName;
    public KJEmojiFragment emojiFragment = new KJEmojiFragment();

    private final AsyncHttpResponseHandler mPublicHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
            hideWaitDialog();
            try {
                ResultBean resb = XmlUtils.toBean(ResultBean.class,
                        new ByteArrayInputStream(arg2));
                Result res = resb.getResult();
                if (res.OK()) {
                    ApplicationLoader
                            .showToastShort(R.string.tip_message_public_success);
                    mAdapter.addItem(0, resb.getComment());
                } else {
                    ApplicationLoader.showToastShort(res.getErrorMessage());
                }
                emojiFragment.clean();
            } catch (Exception e) {
                e.printStackTrace();
                onFailure(arg0, arg1, arg2, e);
            }
        }

        @Override
        public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                Throwable arg3) {
            hideWaitDialog();
            ApplicationLoader.showToastShort(R.string.tip_message_public_faile);
        }
    };

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (mErrorLayout != null) {
                mErrorLayout.setStateType(StateView.TYPE_NETWORK_ERROR);
                mErrorLayout.setErrorMessage(getString(R.string.unlogin_tip));
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mFid = args.getInt(BUNDLE_KEY_FID);
            mFName = args.getString(BUNDLE_KEY_FNAME);
            mCatalog = CommentList.CATALOG_MESSAGE;
        }
        IntentFilter filter = new IntentFilter(Constants.INTENT_ACTION_LOGOUT);
        getActivity().registerReceiver(mReceiver, filter);

        ((BaseActivity) getActivity()).setActionBarTitle(mFName);

        int mode = WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;
        getActivity().getWindow().setSoftInputMode(mode);

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.emoji_container, emojiFragment).commit();
    }

    @Override
    public void onDestroy() {
        getActivity().unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    @Override
    public boolean onBackPressed() {
        if (emojiFragment.isShowEmojiKeyBoard()) {
            emojiFragment.hideAllKeyBoard();
            return true;
        } else {
            return super.onBackPressed();
        }
    }

    @Override
    protected MessageDetailAdapter getListAdapter() {
        return new MessageDetailAdapter();
    }

    @Override
    protected String getCacheKeyPrefix() {
        return CACHE_KEY_PREFIX + mFid;
    }

    @Override
    protected CommentList parseList(InputStream is) throws Exception {
        CommentList list = XmlUtils.toBean(CommentList.class, is);
        return list;
    }

    @Override
    protected CommentList readList(Serializable seri) {
        CommentList list = ((CommentList) seri);
        return list;
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
                    requestData(false);
                } else {
                    UIHelper.showLoginActivity(getActivity());
                }
            }
        });
    }

    @Override
    protected void requestData(boolean refresh) {
        mErrorLayout.setErrorMessage("");
        if (ApplicationLoader.getInstance().isLogin()) {
            super.requestData(refresh);
        } else {
            mErrorLayout.setStateType(StateView.TYPE_NETWORK_ERROR);
            mErrorLayout.setErrorMessage(getString(R.string.unlogin_tip));
        }
    }

    @Override
    protected void sendRequestData() {
        OSChinaApi.getCommentList(mFid, mCatalog, mCurrentPage, mHandler);
    }

    @Override
    protected boolean isReadCacheData(boolean refresh) {
        if (!TDevice.hasInternet()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void executeOnLoadDataSuccess(List<Comment> data) {
        mErrorLayout.setStateType(StateView.TYPE_HIDE_LAYOUT);
        if (data == null) {
            data = new ArrayList<Comment>(1);
        }
        if (mAdapter != null) {
            if (mCurrentPage == 0)
                mAdapter.clear();
            mAdapter.addData(data);
            if (data.size() == 0 && mState == STATE_REFRESH) {
                mErrorLayout.setStateType(StateView.TYPE_NO_DATA);
            } else if (data.size() < getPageSize()) {
                mAdapter.setState(ListBaseAdapter.STATE_OTHER);
            } else {
                mAdapter.setState(ListBaseAdapter.STATE_LOAD_MORE);
            }
            mAdapter.notifyDataSetChanged();
            mListView.setSelection(mListView.getBottom());
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {}

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view,
            int position, long id) {
        final Comment message = mAdapter.getItem(position);
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

    @Override
    public void onResume() {
        super.onResume();
        emojiFragment.hideFlagButton();
    }

    private void handleDeleteMessage(final Comment message) {
        DialogHelp.getConfirmDialog(getActivity(), "是否删除该留言?", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                showWaitDialog(R.string.progress_submit);
                OSChinaApi.deleteComment(mFid,
                        CommentList.CATALOG_MESSAGE, message.getId(),
                        message.getAuthorId(),
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
                Comment msg = (Comment) args[0];
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

    @Override
    public void onClickSendButton(Editable str) {
        if (!ApplicationLoader.getInstance().isLogin()) {
            UIHelper.showLoginActivity(getActivity());
            return;
        }
        if (StringUtils.isEmpty(str)) {
            ApplicationLoader.showToastShort(R.string.tip_content_empty);
            return;
        }
        showWaitDialog("提交中...");
        OSChinaApi.publicMessage(ApplicationLoader.getInstance().getLoginUid(), mFid,
                str.toString(), mPublicHandler);
    }

    @Override
    public void onClickFlagButton() {}
}
