package net.gility.acrida.ui.cell;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import net.gility.acrida.R;
import net.gility.acrida.content.Tweet;
import net.gility.acrida.ui.widget.AvatarView;
import net.gility.acrida.ui.widget.TweetTextView;

import butterknife.BindView;

/**
 * @author Alimy
 */

public class TweetCell extends RelativeCell<Tweet> {
    @BindView(R.id.tv_tweet_name) public TextView author;
    @BindView(R.id.tv_tweet_time) public TextView time;
    @BindView(R.id.tweet_item) public TweetTextView content;
    @BindView(R.id.tv_tweet_comment_count) public TextView commentcount;
    @BindView(R.id.tv_tweet_platform) public TextView platform;
    @BindView(R.id.iv_tweet_face) public AvatarView face;
    @BindView(R.id.iv_tweet_image) public ImageView image;
    @BindView(R.id.tv_like_state) public TextView tvLikeState;
    @BindView(R.id.tv_del) public TextView del;
    @BindView(R.id.tv_likeusers) public TextView likeUsers;

    public TweetCell(Context context) {
        super(context);
    }

    public TweetCell(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TweetCell(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void bindTo(Tweet data) {

    }
}
