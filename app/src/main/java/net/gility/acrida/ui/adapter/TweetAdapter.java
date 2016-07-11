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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;

import net.gility.acrida.R;
import net.gility.acrida.android.ApplicationLoader;
import net.gility.acrida.content.Tweet;
import net.gility.acrida.network.OSChinaApi;
import net.gility.acrida.ui.ImagePreviewActivity;
import net.gility.acrida.ui.widget.AvatarView;
import net.gility.acrida.ui.widget.MyLinkMovementMethod;
import net.gility.acrida.ui.widget.MyURLSpan;
import net.gility.acrida.ui.widget.TweetTextView;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * @author HuangWenwei
 * @author kymjs
 * @date 2014年10月10日
 */
public class TweetAdapter extends ListBaseAdapter<Tweet> {

    static class ViewHolder {
        @BindView(R.id.tv_tweet_name)
        TextView author;
        @BindView(R.id.tv_tweet_time)
        TextView time;
        @BindView(R.id.tweet_item)
        TweetTextView content;
        @BindView(R.id.tv_tweet_comment_count)
        TextView commentcount;
        @BindView(R.id.tv_tweet_platform)
        TextView platform;
        @BindView(R.id.iv_tweet_face)
        AvatarView face;
        @BindView(R.id.iv_tweet_image)
        ImageView image;
        @BindView(R.id.tv_like_state)
        TextView tvLikeState;
        @BindView(R.id.tv_del)
        TextView del;
        @BindView(R.id.tv_likeusers)
        TextView likeUsers;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

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
    protected View getRealView(final int position, View convertView,
                               final ViewGroup parent) {
        context = parent.getContext();
        final ViewHolder vh;
        if (convertView == null || convertView.getTag() == null) {
            convertView = getLayoutInflater(parent.getContext()).inflate(
                    R.layout.list_cell_tweet, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        final Tweet tweet = mDatas.get(position);

        if (tweet.getAuthorid() == ApplicationLoader.getInstance().getLoginUid()) {
            vh.del.setVisibility(View.VISIBLE);
            vh.del.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    optionDel(parent.getContext(), tweet, position);
                }
            });
        } else {
            vh.del.setVisibility(View.GONE);
        }

        vh.face.setUserInfo(tweet.getAuthorid(), tweet.getAuthor());
        vh.face.setAvatarUrl(tweet.getPortrait());
        vh.author.setText(tweet.getAuthor());
        vh.time.setText(StringUtils.friendly_time(tweet.getPubDate()));
        vh.content.setMovementMethod(MyLinkMovementMethod.a());
        vh.content.setFocusable(false);
        vh.content.setDispatchToParent(true);
        vh.content.setLongClickable(false);

        Spanned span = Html.fromHtml(tweet.getBody().trim());

        if (!StringUtils.isEmpty(tweet.getAttach())) {
            if (recordBitmap == null) {
                initRecordImg(parent.getContext());
            }
            ImageSpan recordImg = new ImageSpan(parent.getContext(),
                    recordBitmap);
            SpannableString str = new SpannableString("c");
            str.setSpan(recordImg, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            vh.content.setText(str);
            span = InputHelper.displayEmoji(context.getResources(), span);
            vh.content.append(span);
        } else {
            span = InputHelper.displayEmoji(context.getResources(), span);
            vh.content.setText(span);
        }
        MyURLSpan.parseLinkText(vh.content, span);

        vh.commentcount.setText(tweet.getCommentCount() + "");

        showTweetImage(vh, tweet.getImgSmall(), tweet.getImgBig(),
                parent.getContext());
        tweet.setLikeUsers(context, vh.likeUsers, true);
        final ViewHolder vh1 = vh;
        OnClickListener likeClick = new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ApplicationLoader.getInstance().isLogin()) {
                    updateLikeState(vh1, tweet);
                } else {
                    ApplicationLoader.showToast("先登陆再赞~");
                    UIHelper.showLoginActivity(parent.getContext());
                }
            }
        };
        if (tweet.getLikeUser() == null) {
            vh.tvLikeState.setVisibility(View.GONE);
        }

        vh.tvLikeState.setOnClickListener(likeClick);

        TypefaceUtils.setTypeface(vh.tvLikeState);
        if (tweet.getIsLike() == 1) {
            vh.tvLikeState.setTextColor(ApplicationLoader.getInstance().getResources().getColor(R.color.day_colorPrimary));
        } else {
            vh.tvLikeState.setTextColor(ApplicationLoader.getInstance().getResources().getColor(R.color.gray));
        }
        PlatfromUtil.setPlatFromString(vh.platform, tweet.getAppclient());
        return convertView;
    }

    private void updateLikeState(ViewHolder vh, Tweet tweet) {
        if (tweet.getIsLike() == 1) {
            tweet.setIsLike(0);
            tweet.setLikeCount(tweet.getLikeCount() - 1);
            if (!tweet.getLikeUser().isEmpty()) {
                tweet.getLikeUser().remove(0);
            }
            OSChinaApi.pubUnLikeTweet(tweet.getId(), tweet.getAuthorid(),
                    handler);
            vh.tvLikeState.setTextColor(ApplicationLoader.getInstance().getResources().getColor(R.color.gray));
        } else {
            tweet.setIsLike(1);
            vh.tvLikeState
                    .setAnimation(KJAnimations.getScaleAnimation(1.5f, 300));
            tweet.getLikeUser().add(0, ApplicationLoader.getInstance().getLoginUser());
            OSChinaApi
                    .pubLikeTweet(tweet.getId(), tweet.getAuthorid(), handler);
            vh.tvLikeState.setTextColor(ApplicationLoader.getInstance().getResources().getColor(R.color.day_colorPrimary));
            tweet.setIsLike(1);
            tweet.setLikeCount(tweet.getLikeCount() + 1);
        }
        tweet.setLikeUsers(context, vh.likeUsers, true);
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
    private void showTweetImage(final ViewHolder vh, String imgSmall,
                                final String imgBig, final Context context) {
        if (imgSmall != null && !TextUtils.isEmpty(imgSmall)) {
            kjb.display(vh.image, imgSmall, new BitmapCallBack() {
                @Override
                public void onPreLoad() {
                    super.onPreLoad();
                    vh.image.setImageResource(R.drawable.pic_bg);
                }

                @Override
                public void onSuccess(Bitmap bitmap) {
                    super.onSuccess(bitmap);
                    if (bitmap != null) {
                        float scale = 300f / bitmap.getHeight();
                        vh.image.setImageBitmap(ImageUtils.scaleBitmap(bitmap, scale));
                    }
                }
            });
            vh.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImagePreviewActivity.showImagePrivew(context, 0,
                            new String[]{imgBig});
                }
            });
            vh.image.setVisibility(AvatarView.VISIBLE);
        } else {
            vh.image.setVisibility(AvatarView.GONE);
        }
    }
}
