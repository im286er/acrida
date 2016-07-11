package net.gility.acrida.ui.fragment;

import android.text.Editable;
import android.text.TextUtils;

import net.gility.acrida.android.ApplicationLoader;
import net.gility.acrida.R;
import net.gility.acrida.network.OSChinaApi;
import net.gility.acrida.content.FavoriteList;
import net.gility.acrida.content.Software;
import net.gility.acrida.content.SoftwareDetail;
import net.gility.acrida.content.Tweet;
import net.gility.acrida.utils.StringUtils;
import net.gility.acrida.utils.TDevice;
import net.gility.acrida.utils.ThemeSwitchUtils;
import net.gility.acrida.utils.UIHelper;
import net.gility.acrida.utils.URLsUtils;
import net.gility.acrida.utils.XmlUtils;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by 火蚁 on 15/5/26.
 */
public class SoftwareDetailFragment extends CommonDetailFragment<Software> {

    private String mIden;

    @Override
    protected String getCacheKey() {
        if (TextUtils.isEmpty(mIden)) {
            return "software_" + mId;
        }
        return "software_" + mIden;
    }

    @Override
    protected void sendRequestDataForNet() {
        // 通过id来获取软件详情
        if (mId > 0) {
            OSChinaApi.getSoftwareDetail(mId, mDetailHeandler);
            return;
        }

        if (TextUtils.isEmpty(mIden)) {
            executeOnLoadDataError();
            return;
        }
        OSChinaApi.getSoftwareDetail(mIden, mDetailHeandler);
    }

    @Override
    public void initData() {
        super.initData();
        mIden = getActivity().getIntent().getStringExtra("ident");
        if (TextUtils.isEmpty(mIden)) {
            return;
        }
        try {
            mIden = URLEncoder.encode(mIden, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Software parseData(InputStream is) {
        return XmlUtils.toBean(SoftwareDetail.class, is).getSoftware();
    }

    @Override
    protected String getWebViewBody(Software detail) {
        mId = detail.getId();
        if (TextUtils.isEmpty(detail.getBody())) {
            return "";
        }
        StringBuffer body = new StringBuffer();
        body.append(ThemeSwitchUtils.getWebViewBodyString());
        body.append(UIHelper.WEB_STYLE).append(UIHelper.WEB_LOAD_IMAGES);
        // 添加title
        String title = "";
        // 判断是否推荐
        if (mDetail.getRecommended() == 4) {
            title = String.format("<div class='title'><img src=\"%s\"/>%s %s <img class='recommend' src=\"%s\"/></div>", mDetail.getLogo(), mDetail.getExtensionTitle(), mDetail.getTitle(), "file:///android_asset/ic_soft_recommend.png");
        } else {
            title = String.format("<div class='title'><img src=\"%s\"/>%s %s</div>", mDetail.getLogo(), mDetail.getExtensionTitle(), mDetail.getTitle());
        }
        body.append(title);
        // 添加图片点击放大支持
        body.append(UIHelper.setHtmlCotentSupportImagePreview(mDetail.getBody()));

        // 软件信息
        body.append("<div class='software_attr'>");
        if (!TextUtils.isEmpty(mDetail.getAuthor())) {
            String author = String.format("<a class='author' href='http://my.oschina.net/u/%s'>%s</a>", mDetail.getAuthorId(), mDetail.getAuthor());
            body.append(String.format("<li class='software'>软件作者:&nbsp;&nbsp;%s</li>", author));
        }
        body.append(String.format("<li class='software'>开源协议:&nbsp;&nbsp;%s</li>", mDetail.getLicense()));
        body.append(String.format("<li class='software'>开发语言:&nbsp;&nbsp;%s</li>", mDetail.getLanguage()));
        body.append(String.format("<li class='software'>操作系统:&nbsp;&nbsp;%s</li>", mDetail.getOs()));
        body.append(String.format("<li class='software'>收录时间:&nbsp;&nbsp;%s</li>", mDetail.getRecordtime()));
        body.append("</div>");

        // 软件的首页、文档、下载
        body.append("<div class='software_urls'>");
        if (!TextUtils.isEmpty(mDetail.getHomepage())) {
            body.append(String.format("<li class='software'><a href='%s'>软件首页</a></li>", mDetail.getHomepage()));
        }
        if (!TextUtils.isEmpty(mDetail.getDocument())) {
            body.append(String.format("<li class='software'><a href='%s'>软件文档</a></li>", mDetail.getDocument()));
        }
        if (!TextUtils.isEmpty(mDetail.getDownload())) {
            body.append(String.format("<li class='software'><a href='%s'>软件下载</a></li>", mDetail.getDownload()));
        }
        body.append("</div>");
        // 封尾
        body.append("</div></body>");
        return body.toString();
    }

    @Override
    protected void showCommentView() {
        if (mDetail != null)
            UIHelper.showSoftWareTweets(getActivity(), mDetail.getId());
    }

    @Override
    public void onClickSendButton(Editable str) {
        if (mDetail.getId() == 0) {
            ApplicationLoader.showToast("无法获取该软件~");
            return;
        }
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
        Tweet tweet = new Tweet();
        tweet.setAuthorid(ApplicationLoader.getInstance().getLoginUid());
        tweet.setBody(str.toString());
        showWaitDialog(R.string.progress_submit);
        OSChinaApi.pubSoftWareTweet(tweet, mDetail.getId(), mCommentHandler);
    }

    @Override
    protected int getCommentType() {
        return 0;
    }

    @Override
    protected int getFavoriteTargetType() {
        return FavoriteList.TYPE_SOFTWARE;
    }

    @Override
    protected int getFavoriteState() {
        return mDetail.getFavorite();
    }

    @Override
    protected void updateFavoriteChanged(int newFavoritedState) {
        mDetail.setFavorite(newFavoritedState);
        saveCache(mDetail);
    }

    @Override
    protected int getCommentCount() {
        return mDetail.getTweetCount();
    }

    @Override
    protected String getShareTitle() {
        return String.format("%s %s", mDetail.getExtensionTitle(), mDetail.getTitle());
    }

    @Override
    protected String getShareContent() {
        return StringUtils.getSubString(0, 55,
                getFilterHtmlBody(mDetail.getBody()));
    }

    @Override
    protected String getShareUrl() {
        return String.format(URLsUtils.URL_MOBILE + "p/%s", mIden);
    }
}
