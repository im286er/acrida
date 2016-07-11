package net.gility.acrida.ui.fragment;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;

import net.gility.acrida.android.ApplicationLoader;
import net.gility.acrida.R;
import net.gility.acrida.ui.adapter.TweetAdapter;
import net.gility.acrida.network.OperationResponseHandler;
import net.gility.acrida.network.OSChinaApi;
import net.gility.acrida.ui.BaseActivity;
import net.gility.acrida.content.Result;
import net.gility.acrida.content.ResultBean;
import net.gility.acrida.content.Tweet;
import net.gility.acrida.content.TweetsList;
import net.gility.acrida.utils.ServerTaskUtils;
import net.gility.acrida.utils.UIHelper;
import net.gility.acrida.utils.XmlUtils;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;

public class SoftWareTweetsFrament extends BaseListFragment<Tweet> implements
        OnItemLongClickListener {

    public static final String BUNDLE_KEY_ID = "BUNDLE_KEY_ID";
    protected static final String TAG = SoftWareTweetsFrament.class
            .getSimpleName();
    private static final String CACHE_KEY_PREFIX = "software_tweet_list";

    private int mId;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        BaseActivity act = ((BaseActivity) activity);
        try {
            activity.findViewById(R.id.emoji_container).setVisibility(
                    View.VISIBLE);
        } catch (NullPointerException e) {
        }
    }

    protected int getLayoutRes() {
        return R.layout.fragment_pull_refresh_listview;
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        mListView.setOnItemLongClickListener(this);
    }

    @Override
    public void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mId = args.getInt(BUNDLE_KEY_ID, 0);
        }

        int mode = WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;
        getActivity().getWindow().setSoftInputMode(mode);
    }

    @Override
    protected TweetAdapter getListAdapter() {
        return new TweetAdapter();
    }

    @Override
    protected String getCacheKeyPrefix() {
        return new StringBuilder(CACHE_KEY_PREFIX).append("_").append(mId)
                .toString();
    }

    @Override
    protected TweetsList parseList(InputStream is) throws Exception {
        return XmlUtils.toBean(TweetsList.class, is);
    }

    @Override
    protected TweetsList readList(Serializable seri) {
        return ((TweetsList) seri);
    }

    @Override
    protected void sendRequestData() {
        OSChinaApi.getSoftTweetList(mId, mCurrentPage, mHandler);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
        final Tweet tweet = mAdapter.getItem(position);
        if (tweet == null) {
            return;
        }
        UIHelper.showTweetDetail(parent.getContext(), tweet, tweet.getId());
    }

    private void handleComment(String text) {
        Tweet tweet = new Tweet();
        tweet.setAuthorid(ApplicationLoader.getInstance().getLoginUid());
        tweet.setBody(text);
        ServerTaskUtils.pubSoftWareTweet(getActivity(), tweet, mId);
    }

    class DeleteOperationResponseHandler extends OperationResponseHandler {

        DeleteOperationResponseHandler(Object... args) {
            super(args);
        }

        @Override
        public void onSuccess(int code, ByteArrayInputStream is, Object[] args) {
            try {
                Result res = XmlUtils.toBean(ResultBean.class, is).getResult();
                if (res.OK()) {
                    // ApplicationLoader.showToastShort(R.string.delete_success);
                    // mAdapter.removeItem(args[0]);
                } else {
                    ApplicationLoader.showToastShort(res.getErrorMessage());
                }
            } catch (Exception e) {
                e.printStackTrace();
                onFailure(code, e.getMessage(), args);
            }
        }

        @Override
        public void onFailure(int code, String errorMessage, Object[] args) {
            // ApplicationLoader.showToastShort(R.string.delete_faile);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view,
            int position, long id) {
        // final Comment item = (Comment) mAdapter.getItem(position - 1);
        // if (item == null)
        // return false;
        // String[] items = new String[] {
        // getResources().getString(R.string.copy) };
        // final CommonDialog dialog = DialogHelper
        // .getPinterestDialogCancelable(getActivity());
        // dialog.setNegativeButton(R.string.cancle, null);
        // dialog.setItemsWithoutChk(items, new OnItemClickListener() {
        //
        // @Override
        // public void onItemClick(AdapterView<?> parent, View view,
        // int position, long id) {
        // dialog.dismiss();
        // TDevice.copyTextToBoard(HTMLSpirit.delHTMLTag(item
        // .getContent()));
        // }
        // });
        // dialog.show();
        return true;
    }
}
