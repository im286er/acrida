package net.gility.acrida.ui.fragment.team;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;

import net.gility.acrida.android.ApplicationLoader;
import net.gility.acrida.R;
import net.gility.acrida.network.OperationResponseHandler;
import net.gility.acrida.network.OSChinaApi;
import net.gility.acrida.network.OSChinaTeamApi;
import net.gility.acrida.ui.fragment.BeseHaveHeaderListFragment;
import net.gility.acrida.ui.adapter.ListBaseAdapter;
import net.gility.acrida.content.CommentList;
import net.gility.acrida.content.ListEntity;
import net.gility.acrida.content.Result;
import net.gility.acrida.content.ResultBean;
import net.gility.acrida.ui.widget.emoji.OnSendClickListener;
import net.gility.acrida.ui.adapter.team.TeamReplyAdapter;
import net.gility.acrida.content.team.TeamActive;
import net.gility.acrida.content.team.TeamActiveDetail;
import net.gility.acrida.content.team.TeamRepliesList;
import net.gility.acrida.content.team.TeamReply;
import net.gility.acrida.ui.DetailActivity;
import net.gility.acrida.ui.ImagePreviewActivity;
import net.gility.acrida.ui.widget.StateView;
import net.gility.acrida.utils.DialogHelp;
import net.gility.acrida.utils.HTMLUtil;
import net.gility.acrida.utils.StringUtils;
import net.gility.acrida.utils.TDevice;
import net.gility.acrida.utils.UIHelper;
import net.gility.acrida.utils.XmlUtils;
import net.gility.acrida.ui.widget.AvatarView;
import net.gility.acrida.ui.widget.MyLinkMovementMethod;
import net.gility.acrida.ui.widget.MyURLSpan;
import net.gility.acrida.ui.widget.TweetTextView;

import cz.msebera.android.httpclient.Header;
import org.kymjs.kjframe.KJBitmap;
import org.kymjs.kjframe.bitmap.BitmapCallBack;
import org.kymjs.kjframe.utils.ImageUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;

/**
 * Team动态的详情界面
 * 
 * @author kymjs (https://github.com/kymjs)
 * 
 */
public class TeamTweetDetailFragment extends
        BeseHaveHeaderListFragment<TeamReply, TeamActiveDetail> implements
        OnItemClickListener, OnItemLongClickListener, OnSendClickListener {

    private static final String CACHE_KEY_PREFIX = "team_tweet_";

    private AvatarView img_head;
    private TextView tv_name;
    private TextView mTvCommentCount;
    private TweetTextView tv_content;
    private TextView tv_client;
    private ImageView iv_pic;
    private TextView tv_date;

    private TeamActive active;
    private int teamId;
    private static int rectSize;
    private final KJBitmap kjb = new KJBitmap();

    private DetailActivity outAty;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // 使用simpleBackActivity传递的时候使用
        // Bundle bundle = getActivity().getIntent().getBundleExtra(
        // SimpleBackActivity.BUNDLE_KEY_ARGS);
        Bundle bundle = getActivity().getIntent().getExtras();
        active = (TeamActive) bundle
                .getSerializable(TeamActiveFragment.DYNAMIC_FRAGMENT_KEY);
        teamId = bundle.getInt(TeamActiveFragment.DYNAMIC_FRAGMENT_TEAM_KEY, 0);
        outAty = (DetailActivity) getActivity();
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected View initHeaderView() {
        View headView = View.inflate(getActivity(),
                R.layout.frag_dynamic_detail, null);
        initImageSize(aty);
        img_head = (AvatarView) headView.findViewById(R.id.iv_avatar);
        tv_name = (TextView) headView.findViewById(R.id.tv_name);
        mTvCommentCount = (TextView) headView
                .findViewById(R.id.tv_comment_count);
        tv_content = (TweetTextView) headView.findViewById(R.id.tv_content);
        tv_client = (TextView) headView.findViewById(R.id.tv_from);
        tv_date = (TextView) headView.findViewById(R.id.tv_time);
        iv_pic = (ImageView) headView.findViewById(R.id.iv_pic);

        tv_content.setMovementMethod(MyLinkMovementMethod.a());
        tv_content.setFocusable(false);
        tv_content.setDispatchToParent(true);
        tv_content.setLongClickable(false);
        Spanned span = Html.fromHtml(active.getBody().getDetail().trim());
        MyURLSpan.parseLinkText(tv_content, span);

        img_head.setAvatarUrl(active.getAuthor().getPortrait());
        tv_name.setText(active.getAuthor().getName());
        tv_date.setText(StringUtils.friendly_time(active.getCreateTime()));
        tv_client.setText("Android");
        tv_client.setVisibility(View.GONE);
        String imgPath = active.getBody().getImage();
        if (!StringUtils.isEmpty(imgPath)) {
            iv_pic.setVisibility(View.VISIBLE);
            setTweetImage(iv_pic, imgPath, active.getBody().getImageOrigin());
        } else {
            iv_pic.setVisibility(View.GONE);
        }
        return headView;
    }

    /**
     * 处理回复的提交
     * 
     * @param text
     */
    private void handleComment(String text) {
        showWaitDialog(R.string.progress_submit);
        if (!ApplicationLoader.getInstance().isLogin()) {
            UIHelper.showLoginActivity(getActivity());
            return;
        }
        OSChinaTeamApi.pubTeamTweetReply(teamId, active.getType(),
                active.getId(), text, mCommentHandler);
    }

    private void initImageSize(Context cxt) {
        if (cxt != null && rectSize == 0) {
            rectSize = (int) cxt.getResources().getDimension(R.dimen.space_100);
        } else {
            rectSize = 300;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        outAty.emojiFragment.hideFlagButton();
    }

    /**
     * 动态设置图片显示样式
     * 
     * @param url
     *            缩略图地址
     * @param realUrl
     *            点击图片后要显示的大图图片地址
     * 
     * @author kymjs
     */
    private void setTweetImage(final ImageView pic, final String url,
            final String realUrl) {
        pic.setVisibility(View.VISIBLE);

        kjb.display(pic, url, R.drawable.pic_bg, rectSize, rectSize,
                new BitmapCallBack() {
                    @Override
                    public void onSuccess(Bitmap bitmap) {
                        super.onSuccess(bitmap);
                        bitmap = ImageUtils.reduce(bitmap, rectSize, rectSize
                                / bitmap.getHeight(), true);
                        pic.setImageBitmap(bitmap);
                    }
                });

        pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePreviewActivity.showImagePrivew(aty, 0,
                        new String[] { realUrl });
            }
        });
    }

    /*********************************************************/

    @Override
    protected void requestDetailData(boolean isRefresh) {
        OSChinaApi.getDynamicDetail(active.getId(), teamId, ApplicationLoader
                .getInstance().getLoginUid(), mDetailHandler);
    }

    @Override
    protected String getDetailCacheKey() {
        return CACHE_KEY_PREFIX + active.getId();
    }

    @Override
    protected String getCacheKeyPrefix() {
        return CACHE_KEY_PREFIX + active.getId() + mCurrentPage;
    }

    @Override
    protected TeamRepliesList readList(Serializable seri) {
        super.readList(seri);
        return (TeamRepliesList) seri;
    }

    @Override
    protected void executeOnLoadDetailSuccess(TeamActiveDetail detailBean) {
        mErrorLayout.setStateType(StateView.TYPE_HIDE_LAYOUT);
        this.active = detailBean.getTeamActive();
        tv_content.setText(stripTags(this.active.getBody().getTitle()));
        Spanned span = Html.fromHtml(active.getBody().getDetail());
        MyURLSpan.parseLinkText(tv_content, span);
        mAdapter.setNoDataText(R.string.comment_empty);
    }

    @Override
    protected ListEntity<TeamReply> parseList(InputStream is) throws Exception {
        super.parseList(is);
        TeamRepliesList list = XmlUtils.toBean(TeamRepliesList.class, is);
        return list;
    }

    @Override
    protected TeamActiveDetail getDetailBean(ByteArrayInputStream is) {
        return XmlUtils.toBean(TeamActiveDetail.class, is);
    }

    @Override
    protected ListBaseAdapter<TeamReply> getListAdapter() {
        return new TeamReplyAdapter();
    }

    @Override
    protected void sendRequestData() {
        OSChinaTeamApi.getTeamCommentList(teamId, active.getId(), mCurrentPage,
                mHandler);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view,
            int position, long id) {
        if (position - 1 == -1) {
            return false;
        }
        final TeamReply item = mAdapter.getItem(position - 1);
        if (item == null)
            return false;
        int itemsLen = item.getAuthor().getId() == ApplicationLoader.getInstance()
                .getLoginUid() ? 2 : 1;
        String[] items = new String[itemsLen];
        items[0] = getResources().getString(R.string.copy);
        if (itemsLen == 2) {
            items[1] = getResources().getString(R.string.delete);
        }
        DialogHelp.getSelectDialog(getActivity(), items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0) {
                    TDevice.copyTextToBoard(HTMLUtil.delHTMLTag(item
                            .getContent()));
                } else if (i == 1) {
                    handleDeleteComment(item);
                }
            }
        }).show();

        return true;
    }

    private void handleDeleteComment(TeamReply comment) {
        if (!ApplicationLoader.getInstance().isLogin()) {
            UIHelper.showLoginActivity(getActivity());
            return;
        }
        ApplicationLoader.showToastShort(R.string.deleting);
        OSChinaApi.deleteComment(active.getId(), CommentList.CATALOG_TWEET,
                comment.getId(), comment.getAuthor().getId(),
                new DeleteOperationResponseHandler(comment));
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
                    ApplicationLoader.showToastShort(R.string.delete_success);
                    mAdapter.removeItem(args[0]);
                    // setTweetCommentCount();
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
            ApplicationLoader.showToastShort(R.string.delete_faile);
        }
    }

    private final AsyncHttpResponseHandler mCommentHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
            onRefresh();
        }

        @Override
        public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                Throwable arg3) {
            ApplicationLoader.showToastShort(R.string.comment_publish_faile);
        }

        @Override
        public void onFinish() {
            hideWaitDialog();
        };
    };

    @Override
    protected void executeOnLoadDataSuccess(java.util.List<TeamReply> data) {
        super.executeOnLoadDataSuccess(data);
        if (mTvCommentCount != null && data != null) {
            mTvCommentCount.setText((mAdapter.getCount() - 1) + "");
        }
    };

    /**
     * 移除字符串中的Html标签
     * 
     * @author kymjs (https://github.com/kymjs)
     * @param pHTMLString
     * @return
     */
    public static Spanned stripTags(final String pHTMLString) {
        // String str = pHTMLString.replaceAll("\\<.*?>", "");
        String str = pHTMLString.replaceAll("\\t*", "");
        str = str.replaceAll("<\\s*img\\s+([^>]*)\\s*>", "  ");
        return Html.fromHtml(str);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
        if (position < 1) { // header view
            return;
        }
        final TeamReply comment = mAdapter.getItem(position - 1);
        if (comment == null) {
            return;
        }
    }

    @Override
    public void onClickSendButton(Editable str) {
        if (!TDevice.hasInternet()) {
            ApplicationLoader.showToastShort(R.string.tip_network_error);
            return;
        }
        if (!ApplicationLoader.getInstance().isLogin()) {
            UIHelper.showLoginActivity(getActivity());
            return;
        }
        if (TextUtils.isEmpty(str)) {
            ApplicationLoader.showToastShort(R.string.tip_comment_content_empty);
            return;
        }
        handleComment(str.toString());
    }

    @Override
    public void onClickFlagButton() {}

}
