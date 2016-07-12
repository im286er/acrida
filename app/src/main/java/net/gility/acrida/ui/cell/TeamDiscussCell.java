package net.gility.acrida.ui.cell;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import net.gility.acrida.R;
import net.gility.acrida.content.team.TeamDiscuss;
import net.gility.acrida.ui.widget.AvatarView;
import net.gility.acrida.utils.HTMLUtil;
import net.gility.acrida.utils.StringUtils;

import butterknife.BindView;

/**
 * @author Alimy
 */

public class TeamDiscussCell extends RelativeCell<TeamDiscuss> {
    @BindView(R.id.tv_title) TextView title;
    @BindView(R.id.tv_description) TextView description;
    @BindView(R.id.tv_author) TextView author;
    @BindView(R.id.tv_date) TextView time;
    @BindView(R.id.tv_count) TextView comment_count;
    @BindView(R.id.tv_vote_up) TextView vote_up;
    @BindView(R.id.iv_face) AvatarView face;

    public TeamDiscussCell(Context context) {
        super(context);
    }

    public TeamDiscussCell(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TeamDiscussCell(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void bindTo(TeamDiscuss data) {
        face.setUserInfo(data.getAuthor().getId(), data.getAuthor().getName());
        face.setAvatarUrl(data.getAuthor().getPortrait());
        title.setText(data.getTitle());
        String body = data.getBody().trim();
        description.setVisibility(View.GONE);
        if (null != body || !StringUtils.isEmpty(body)) {
            description.setVisibility(View.VISIBLE);
            description.setText(HTMLUtil.replaceTag(data.getBody()).trim());
        }
        author.setText(data.getAuthor().getName());
        time.setText(StringUtils.friendly_time(data.getCreateTime()));
        vote_up.setText(data.getVoteUp() + "");
        comment_count.setText(data.getAnswerCount() + "");
    }
}
