package net.gility.acrida.ui.cell;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.gility.acrida.R;
import net.gility.acrida.content.Comment;
import net.gility.acrida.ui.widget.AvatarView;
import net.gility.acrida.ui.widget.FloorView;
import net.gility.acrida.ui.widget.MyLinkMovementMethod;
import net.gility.acrida.ui.widget.MyURLSpan;
import net.gility.acrida.ui.widget.TweetTextView;
import net.gility.acrida.ui.widget.emoji.InputHelper;
import net.gility.acrida.utils.PlatfromUtil;
import net.gility.acrida.utils.StringUtils;

import java.util.List;

import butterknife.BindView;

/**
 * @author Alimy
 */

public class CommentCell extends RelativeCell<Comment> {
    @BindView(R.id.iv_avatar) AvatarView avatar;
    @BindView(R.id.tv_name) TextView name;
    @BindView(R.id.tv_time) TextView time;
    @BindView(R.id.tv_from) TextView from;
    @BindView(R.id.tv_content) TweetTextView content;
    @BindView(R.id.ly_relies) LinearLayout relies;
    @BindView(R.id.ly_refers) FloorView refers;

    public CommentCell(Context context) {
        super(context);
    }

    public CommentCell(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CommentCell(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void bindTo(Comment comment) {
        try {
            // 若Authorid为0，则显示非会员
            name.setText(comment.getAuthor() + (comment.getAuthorId() == 0 ? "(非会员)" : ""));

            content.setMovementMethod(MyLinkMovementMethod.a());
            content.setFocusable(false);
            content.setDispatchToParent(true);
            content.setLongClickable(false);
            Spanned span = Html.fromHtml(TweetTextView.modifyPath(comment.getContent()));
            span = InputHelper.displayEmoji(getContext().getResources(), span.toString());
            content.setText(span);
            MyURLSpan.parseLinkText(content, span);

            time.setText(StringUtils.friendly_time(comment.getPubDate()));

            PlatfromUtil.setPlatFromString(from, comment.getAppClient());

            // setup refers
            setupRefers(comment.getRefers());

            // setup replies
            setupReplies(comment.getReplies());

            avatar.setAvatarUrl(comment.getPortrait());
            avatar.setUserInfo(comment.getAuthorId(), comment.getAuthor());
        } catch (Exception e) {
        }
    }

    private void setupRefers(List<Comment.Refer> refs) {
        refers.removeAllViews();
        if (refs == null || refs.size() <= 0) {
            refers.setVisibility(View.GONE);
        } else {
            refers.setVisibility(View.VISIBLE);
            refers.setComments(refs);
        }
    }

    private void setupReplies(List<Comment.Reply> replies) {
        this.relies.removeAllViews();
        if (replies == null || replies.size() <= 0) {
            this.relies.setVisibility(View.GONE);
        } else {
            this.relies.setVisibility(View.VISIBLE);

            // add count layout
            View countView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_cell_reply_count, null, false);
            TextView count = (TextView) countView.findViewById(R.id.tv_comment_reply_count);
            count.setText(getContext().getResources().getString(
                    R.string.comment_reply_count, String.valueOf(replies.size())));
            this.relies.addView(countView);

            // add reply item
            for (Comment.Reply reply : replies) {
                LinearLayout replyItemView = (LinearLayout) LayoutInflater.from(getContext())
                        .inflate(R.layout.list_cell_reply_name_content, null, false);

                replyItemView.setOrientation(LinearLayout.HORIZONTAL);
                replyItemView.setBackgroundResource(R.drawable.comment_background);

                TextView name = (TextView) replyItemView.findViewById(R.id.tv_reply_name);
                name.setText(reply.rauthor + ":");

                TweetTextView replyContent = (TweetTextView) replyItemView
                        .findViewById(R.id.tv_reply_content);
                replyContent.setMovementMethod(MyLinkMovementMethod.a());
                replyContent.setFocusable(false);
                replyContent.setDispatchToParent(true);
                replyContent.setLongClickable(false);
                Spanned rcontent = Html.fromHtml(reply.rcontent);
                replyContent.setText(rcontent);
                MyURLSpan.parseLinkText(replyContent, rcontent);

                this.relies.addView(replyItemView);
            }
        }
    }
}
