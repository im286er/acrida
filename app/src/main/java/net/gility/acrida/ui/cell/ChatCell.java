package net.gility.acrida.ui.cell;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.util.AttributeSet;
import android.widget.TextView;

import net.gility.acrida.R;
import net.gility.acrida.content.Comment;
import net.gility.acrida.ui.widget.AvatarView;
import net.gility.acrida.ui.widget.MyLinkMovementMethod;
import net.gility.acrida.ui.widget.MyURLSpan;
import net.gility.acrida.ui.widget.TweetTextView;
import net.gility.acrida.ui.widget.emoji.InputHelper;
import net.gility.acrida.utils.StringUtils;

import butterknife.BindView;

/**
 * @author Alimy
 */

public abstract class ChatCell extends RelativeCell<Comment> {
    public static final int CHAT_FROM = 0;
    public static final int CHAT_TO = 1;

    @BindView(R.id.iv_avatar) AvatarView avatar;
    @BindView(R.id.tv_time) TextView time;
    @BindView(R.id.tv_content) TweetTextView content;

    public ChatCell(Context context) {
        super(context);
    }

    public ChatCell(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChatCell(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void bindTo(Comment comment) {
        content.setMovementMethod(MyLinkMovementMethod.a());
        content.setFocusable(false);
        content.setDispatchToParent(true);
        content.setLongClickable(false);
        Spanned span = Html.fromHtml(comment.getContent());
        span = InputHelper.displayEmoji(getResources(), span);
        content.setText(span);
        MyURLSpan.parseLinkText(content, span);

        avatar.setAvatarUrl(comment.getPortrait());
        avatar.setUserInfo(comment.getAuthorId(), comment.getAuthor());
        time.setText(StringUtils.friendly_time(comment.getPubDate()));
    }

    public abstract int getChatType();
}
