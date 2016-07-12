package net.gility.acrida.ui.cell;

import android.content.Context;
import android.text.Spanned;
import android.util.AttributeSet;
import android.widget.TextView;

import net.gility.acrida.R;
import net.gility.acrida.content.TweetLike;
import net.gility.acrida.ui.widget.AvatarView;
import net.gility.acrida.ui.widget.MyLinkMovementMethod;
import net.gility.acrida.ui.widget.MyURLSpan;
import net.gility.acrida.ui.widget.TweetTextView;
import net.gility.acrida.utils.PlatfromUtil;
import net.gility.acrida.utils.StringUtils;
import net.gility.acrida.utils.UIHelper;

import butterknife.BindView;

/**
 * @author Alimy
 */

public class TweetLikeCell extends RelativeCell<TweetLike> {
    @BindView(R.id.tv_name) TextView name;
    @BindView(R.id.tv_from) TextView from;
    @BindView(R.id.tv_time) TextView time;
    @BindView(R.id.tv_action) TextView action;
    @BindView(R.id.tv_reply) TweetTextView reply;
    @BindView(R.id.iv_avatar) AvatarView avatar;

    public TweetLikeCell(Context context) {
        super(context);
    }

    public TweetLikeCell(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TweetLikeCell(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void bindTo(TweetLike data) {
        name.setText(data.getUser().getName().trim());
        action.setText("赞了我的动弹");
        time.setText(StringUtils.friendly_time(data.getDatatime().trim()));
        PlatfromUtil.setPlatFromString(from, data.getAppClient());
        avatar.setUserInfo(data.getUser().getId(), data.getUser().getName());
        avatar.setAvatarUrl(data.getUser().getPortrait());

        reply.setMovementMethod(MyLinkMovementMethod.a());
        reply.setFocusable(false);
        reply.setDispatchToParent(true);
        reply.setLongClickable(false);
        Spanned span = UIHelper.parseActiveReply(data.getTweet().getAuthor(),
                data.getTweet().getBody());
        reply.setText(span);
        MyURLSpan.parseLinkText(reply, span);
    }
}
