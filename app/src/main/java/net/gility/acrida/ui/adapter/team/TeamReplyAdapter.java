package net.gility.acrida.ui.adapter.team;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.gility.acrida.R;
import net.gility.acrida.content.team.TeamReply;
import net.gility.acrida.ui.adapter.ListBaseAdapter;
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
 * 
 * @data 2015-1-30 下午4:05:00
 */
public class TeamReplyAdapter extends ListBaseAdapter<TeamReply> {

    @Override
    protected View getRealView(int position, View convertView, ViewGroup parent) {
	ViewHolder vh = null;
	if (convertView == null || convertView.getTag() == null) {
	    convertView = getLayoutInflater(parent.getContext()).inflate(
		    R.layout.list_cell_team_reply, null);
	    vh = new ViewHolder(convertView);
	    convertView.setTag(vh);
	} else {
	    vh = (ViewHolder) convertView.getTag();
	}

	TeamReply item = mDatas.get(position);
	vh.name.setText(item.getAuthor().getName());
	vh.avatar.setAvatarUrl(item.getAuthor().getPortrait());
	setContent(vh.content, HTMLUtil.delHTMLTag(item.getContent()));
	vh.time.setText(StringUtils.friendly_time(item.getCreateTime()));

	if (StringUtils.isEmpty(item.getAppName())) {
	    vh.from.setVisibility(View.GONE);
	} else {
	    vh.from.setVisibility(View.VISIBLE);
	    vh.from.setText(item.getAppName());
	}
	setReplies(parent.getContext(), item, vh);
	return convertView;
    }

    private void setReplies(Context context, TeamReply item, ViewHolder vh) {
	List<TeamReply> replies = item.getReplies();
	vh.relies.removeAllViews();
	if (replies == null || replies.size() <= 0) {
	    vh.relies.setVisibility(View.GONE);
	} else {
	    vh.relies.setVisibility(View.VISIBLE);
	    
	    // add count layout
            View countView = getLayoutInflater(context).inflate(
                    R.layout.list_cell_reply_count, null, false);
            TextView count = (TextView) countView
                    .findViewById(R.id.tv_comment_reply_count);
            count.setText(context.getResources().getString(
                    R.string.comment_reply_count, replies.size()));
            vh.relies.addView(countView);
	    
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
		vh.relies.addView(replyItemView);
	    }
	}
    }

    static class ViewHolder {

	@BindView(R.id.iv_avatar)
	AvatarView avatar;
	@BindView(R.id.tv_name)
	TextView name;
	@BindView(R.id.tv_time)
	TextView time;
	@BindView(R.id.tv_from)
	TextView from;
	@BindView(R.id.tv_content)
	TweetTextView content;
	@BindView(R.id.ly_relies)
	LinearLayout relies;

	public ViewHolder(View view) {
	    ButterKnife.bind(this, view);
	}
    }
}
