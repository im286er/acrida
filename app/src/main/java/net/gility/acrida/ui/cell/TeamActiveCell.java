package net.gility.acrida.ui.cell;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import net.gility.acrida.R;
import net.gility.acrida.content.team.TeamActive;
import net.gility.acrida.ui.widget.AvatarView;
import net.gility.acrida.ui.widget.TweetTextView;

import butterknife.BindView;

/**
 * @author Alimy
 */

public class TeamActiveCell extends RelativeCell<TeamActive> {
    @BindView(R.id.event_listitem_userface) public AvatarView img_head;
    @BindView(R.id.event_listitem_username) public TextView tv_name;
    @BindView(R.id.event_listitem_content) public TweetTextView tv_content;
    @BindView(R.id.event_listitem_client) public TextView tv_client;
    @BindView(R.id.event_listitem_date) public TextView tv_date;
    @BindView(R.id.tv_comment_count) public TextView tv_commit;
    @BindView(R.id.title) public TextView tv_title;
    @BindView(R.id.iv_pic) public ImageView iv_pic;

    public TeamActiveCell(Context context) {
        super(context);
    }

    public TeamActiveCell(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TeamActiveCell(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void bindTo(TeamActive data) {

    }
}
