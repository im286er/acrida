package net.gility.acrida.ui.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.loopj.android.http.AsyncHttpResponseHandler;

import net.gility.acrida.R;
import net.gility.acrida.android.ApplicationLoader;
import net.gility.acrida.content.Tweet;
import net.gility.acrida.network.OSChinaApi;
import net.gility.acrida.ui.ImagePreviewActivity;
import net.gility.acrida.ui.cell.TweetCell;
import net.gility.acrida.ui.widget.AvatarView;
import net.gility.acrida.ui.widget.MyLinkMovementMethod;
import net.gility.acrida.ui.widget.MyURLSpan;
import net.gility.acrida.ui.widget.emoji.InputHelper;
import net.gility.acrida.utils.DialogHelp;
import net.gility.acrida.utils.ImageUtils;
import net.gility.acrida.utils.KJAnimations;
import net.gility.acrida.utils.PlatfromUtil;
import net.gility.acrida.utils.StringUtils;
import net.gility.acrida.utils.TypefaceUtils;
import net.gility.acrida.utils.UIHelper;

import org.kymjs.kjframe.KJBitmap;
import org.kymjs.kjframe.bitmap.BitmapCallBack;
import org.kymjs.kjframe.utils.DensityUtils;

import cz.msebera.android.httpclient.Header;

/**
 * @author HuangWenwei
 * @author kymjs
 * @author Alimy
 * @date 2014年10月10日
 */
public class TweetAdapter extends ListBaseAdapter<Tweet> {

    private Bitmap recordBitmap;
    private Context context;
    private final KJBitmap kjb = new KJBitmap();

    final private AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
        }

        @Override
        public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                              Throwable arg3) {
        }
    };

    private void initRecordImg(Context cxt) {
        recordBitmap = BitmapFactory.decodeResource(cxt.getResources(),
                R.drawable.audio3);
        recordBitmap = ImageUtils.zoomBitmap(recordBitmap,
                DensityUtils.dip2px(cxt, 20f), DensityUtils.dip2px(cxt, 20f));
    }

    @Override
    protected View getRealView(final int position, View convertView, final ViewGroup parent) {
        context = parent.getContext();

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.cell_tweet, parent, false);
        }

        TweetCell cell = (TweetCell) convertView;

        final Tweet tweet = mDatas.get(position);

        if (tweet.getAuthorid() == ApplicationLoader.getInstance().getLoginUid()) {
            cell.del.setVisibility(View.VISIBLE);
            cell.del.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    optionDel(parent.getContext(), tweet, position);
                }
            });
        } else {
            cell.del.setVisibility(View.GONE);
        }

        cell.face.setUserInfo(tweet.getAuthorid(), tweet.getAuthor());
        cell.face.setAvatarUrl(tweet.getPortrait());
        cell.author.setText(tweet.getAuthor());
        cell.time.setText(StringUtils.friendly_time(tweet.getPubDate()));
        cell.content.setMovementMethod(MyLinkMovementMethod.a());
        cell.content.setFocusable(false);
        cell.content.setDispatchToParent(true);
        cell.content.setLongClickable(false);

        Spanned span = Html.fromHtml(tweet.getBody().trim());

        if (!StringUtils.isEmpty(tweet.getAttach())) {
            if (recordBitmap == null) {
                initRecordImg(parent.getContext());
            }
            ImageSpan recordImg = new ImageSpan(parent.getContext(),
                    recordBitmap);
            SpannableString str = new SpannableString("c");
            str.setSpan(recordImg, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            cell.content.setText(str);
            span = InputHelper.displayEmoji(context.getResources(), span);
            cell.content.append(span);
        } else {
            span = InputHelper.displayEmoji(context.getResources(), span);
            cell.content.setText(span);
        }
        MyURLSpan.parseLinkText(cell.content, span);

        cell.commentcount.setText(tweet.getCommentCount() + "");

        showTweetImage(cell, tweet.getImgSmall(), tweet.getImgBig(),
                parent.getContext());
        tweet.setLikeUsers(context, cell.likeUsers, true);
        final TweetCell tweetCell = cell;
        OnClickListener likeClick = new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ApplicationLoader.getInstance().isLogin()) {
                    updateLikeState(tweetCell, tweet);
                } else {
                    ApplicationLoader.showToast("先登陆再赞~");
                    UIHelper.showLoginActivity(parent.getContext());
                }
            }
        };
        if (tweet.getLikeUser() == null) {
            cell.tvLikeState.setVisibility(View.GONE);
        }

        cell.tvLikeState.setOnClickListener(likeClick);

        TypefaceUtils.setTypeface(cell.tvLikeState);
        if (tweet.getIsLike() == 1) {
            cell.tvLikeState.setTextColor(ApplicationLoader.getInstance().getResources().getColor(R.color.day_colorPrimary));
        } else {
            cell.tvLikeState.setTextColor(ApplicationLoader.getInstance().getResources().getColor(R.color.gray));
        }
        PlatfromUtil.setPlatFromString(cell.platform, tweet.getAppclient());
        return convertView;
    }

    private void updateLikeState(TweetCell cell, Tweet tweet) {
        if (tweet.getIsLike() == 1) {
            tweet.setIsLike(0);
            tweet.setLikeCount(tweet.getLikeCount() - 1);
            if (!tweet.getLikeUser().isEmpty()) {
                tweet.getLikeUser().remove(0);
            }
            OSChinaApi.pubUnLikeTweet(tweet.getId(), tweet.getAuthorid(),
                    handler);
            cell.tvLikeState.setTextColor(ApplicationLoader.getInstance().getResources().getColor(R.color.gray));
        } else {
            tweet.setIsLike(1);
            cell.tvLikeState
                    .setAnimation(KJAnimations.getScaleAnimation(1.5f, 300));
            tweet.getLikeUser().add(0, ApplicationLoader.getInstance().getLoginUser());
            OSChinaApi
                    .pubLikeTweet(tweet.getId(), tweet.getAuthorid(), handler);
            cell.tvLikeState.setTextColor(ApplicationLoader.getInstance().getResources().getColor(R.color.day_colorPrimary));
            tweet.setIsLike(1);
            tweet.setLikeCount(tweet.getLikeCount() + 1);
        }
        tweet.setLikeUsers(context, cell.likeUsers, true);
    }

    private void optionDel(Context context, final Tweet tweet,
                           final int position) {

        DialogHelp.getConfirmDialog(context, "确定删除吗?", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                OSChinaApi.deleteTweet(tweet.getAuthorid(), tweet.getId(),
                        new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int arg0, Header[] arg1,
                                                  byte[] arg2) {
                                mDatas.remove(position);
                                notifyDataSetChanged();
                            }

                            @Override
                            public void onFailure(int arg0, Header[] arg1,
                                                  byte[] arg2, Throwable arg3) {
                            }
                        });
            }
        }).show();
    }

    /**
     * 动态设置动弹列表图片显示规则
     *
     * @author kymjs
     */
    private void showTweetImage(final TweetCell cell, String imgSmall,
                                final String imgBig, final Context context) {
        if (imgSmall != null && !TextUtils.isEmpty(imgSmall)) {
            kjb.display(cell.image, imgSmall, new BitmapCallBack() {
                @Override
                public void onPreLoad() {
                    super.onPreLoad();
                    cell.image.setImageResource(R.drawable.pic_bg);
                }

                @Override
                public void onSuccess(Bitmap bitmap) {
                    super.onSuccess(bitmap);
                    if (bitmap != null) {
                        float scale = 300f / bitmap.getHeight();
                        cell.image.setImageBitmap(ImageUtils.scaleBitmap(bitmap, scale));
                    }
                }
            });
            cell.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImagePreviewActivity.showImagePrivew(context, 0,
                            new String[]{imgBig});
                }
            });
            cell.image.setVisibility(AvatarView.VISIBLE);
        } else {
            cell.image.setVisibility(AvatarView.GONE);
        }
    }
}
