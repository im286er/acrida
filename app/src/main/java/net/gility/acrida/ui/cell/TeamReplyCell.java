package net.gility.acrida.ui.cell;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.gility.acrida.R;
import net.gility.acrida.content.team.TeamReply;
import net.gility.acrida.ui.widget.AvatarView;
import net.gility.acrida.ui.widget.TweetTextView;

import butterknife.BindView;

/**
 * @author Alimy
 */

public class TeamReplyCell extends RelativeCell<TeamReply> {
    @BindView(R.id.iv_avatar) public AvatarView avatar;
    @BindView(R.id.tv_name) public TextView name;
    @BindView(R.id.tv_time) public TextView time;
    @BindView(R.id.tv_from) public TextView from;
    @BindView(R.id.tv_content) public TweetTextView content;
    @BindView(R.id.ly_relies) public LinearLayout relies;

    public TeamReplyCell(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TeamReplyCell(Context context) {
        super(context);
    }

    public TeamReplyCell(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void bindTo(TeamReply data) {

    }
}
