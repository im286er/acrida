package net.gility.acrida.ui.adapter.team;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.gility.acrida.R;
import net.gility.acrida.content.team.TeamReply;
import net.gility.acrida.ui.adapter.ListBaseAdapter;
import net.gility.acrida.ui.cell.TeamReplyCell;
import net.gility.acrida.ui.widget.AvatarView;
import net.gility.acrida.ui.widget.TweetTextView;
import net.gility.acrida.utils.HTMLUtil;
import net.gility.acrida.utils.StringUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 评论适配器 TeamReply.java
 *
 * @author 火蚁(http://my.oschina.net/u/253900)
 * @author Alimy
 * @data 2015-1-30 下午4:05:00
 */
public class TeamReplyAdapter extends ListBaseAdapter<TeamReply> {

    @Override
    protected View getRealView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = getLayoutInflater(parent.getContext()).inflate(
                    R.layout.cell_team_reply, parent, false);
        }

        TeamReplyCell cell = (TeamReplyCell) convertView;
        TeamReply item = mDatas.get(position);

        cell.name.setText(item.getAuthor().getName());
        cell.avatar.setAvatarUrl(item.getAuthor().getPortrait());
        setContent(cell.content, HTMLUtil.delHTMLTag(item.getContent()));
        cell.time.setText(StringUtils.friendly_time(item.getCreateTime()));

        if (StringUtils.isEmpty(item.getAppName())) {
            cell.from.setVisibility(View.GONE);
        } else {
            cell.from.setVisibility(View.VISIBLE);
            cell.from.setText(item.getAppName());
        }
        setReplies(parent.getContext(), item, cell);
        return convertView;
    }

    private void setReplies(Context context, TeamReply item, TeamReplyCell cell) {
        List<TeamReply> replies = item.getReplies();
        cell.relies.removeAllViews();
        if (replies == null || replies.size() <= 0) {
            cell.relies.setVisibility(View.GONE);
        } else {
            cell.relies.setVisibility(View.VISIBLE);

            // add count layout
            View countView = getLayoutInflater(context).inflate(
                    R.layout.list_cell_reply_count, null, false);
            TextView count = (TextView) countView
                    .findViewById(R.id.tv_comment_reply_count);
            count.setText(context.getResources().getString(
                    R.string.comment_reply_count, String.valueOf(replies.size())));
            cell.relies.addView(countView);

            for (TeamReply teamReply : replies) {
                View replyItemView = getLayoutInflater(
                        context).inflate(R.layout.list_cell_team_reply_refers,
                        null, false);
                replyItemView.setBackgroundResource(R.drawable.comment_background);

                AvatarView avatarView = (AvatarView) replyItemView.findViewById(R.id.iv_avatar);
                avatarView.setAvatarUrl(teamReply.getAuthor().getPortrait());
                TextView name = (TextView) replyItemView.findViewById(R.id.tv_name);
                name.setText(teamReply.getAuthor().getName());
                TweetTextView content = (TweetTextView) replyItemView.findViewById(R.id.tv_content);
                setContent(content, HTMLUtil.delHTMLTag(teamReply.getContent()));
                TextView time = (TextView) replyItemView.findViewById(R.id.tv_time);
                time.setText(StringUtils.friendly_time(teamReply.getCreateTime()));
                TextView from = (TextView) replyItemView.findViewById(R.id.tv_from);
                if (StringUtils.isEmpty(teamReply.getAppName())) {
                    from.setVisibility(View.GONE);
                } else {
                    from.setVisibility(View.VISIBLE);
                    from.setText(teamReply.getAppName());
                }
                cell.relies.addView(replyItemView);
            }
        }
    }
}
