package net.gility.acrida.ui.cell;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import net.gility.acrida.R;
import net.gility.acrida.android.ApplicationLoader;
import net.gility.acrida.content.Messages;
import net.gility.acrida.ui.widget.AvatarView;
import net.gility.acrida.ui.widget.MyLinkMovementMethod;
import net.gility.acrida.ui.widget.MyURLSpan;
import net.gility.acrida.ui.widget.TweetTextView;
import net.gility.acrida.utils.StringUtils;

import butterknife.BindView;

/**
 * @author Alimy
 */

public class MessageCell extends LinearCell<Messages> {
    @BindView(R.id.iv_avatar) AvatarView avatar;
    @BindView(R.id.tv_name) TextView name;
    @BindView(R.id.tv_sender) TextView sender;
    @BindView(R.id.tv_time) TextView time;
    @BindView(R.id.tv_count) TextView count;
    @BindView(R.id.tv_content) TweetTextView content;

    public MessageCell(Context context) {
        super(context);
    }

    public MessageCell(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MessageCell(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void bindTo(Messages messages) {
        if (ApplicationLoader.getInstance().getLoginUid() == messages.getSenderId()) {
            sender.setVisibility(View.VISIBLE);
        } else {
            sender.setVisibility(View.GONE);
        }

        name.setText(messages.getFriendName());

        content.setMovementMethod(MyLinkMovementMethod.a());
        content.setFocusable(false);
        content.setDispatchToParent(true);
        content.setLongClickable(false);
        Spanned span = Html.fromHtml(messages.getContent());
        content.setText(span);
        MyURLSpan.parseLinkText(content, span);

        time.setText(StringUtils.friendly_time(messages.getPubDate()));
        count.setText(getResources().getString(R.string.message_count,
                String.valueOf(messages.getMessageCount())));

        avatar.setAvatarUrl(messages.getPortrait());
        avatar.setUserInfo(messages.getSenderId(), messages.getSender());
    }
}
