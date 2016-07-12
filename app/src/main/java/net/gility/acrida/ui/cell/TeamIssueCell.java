package net.gility.acrida.ui.cell;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import net.gility.acrida.R;
import net.gility.acrida.content.team.TeamIssue;

import butterknife.BindView;

/**
 * @author Alimy
 */

public class TeamIssueCell extends LinearCell<TeamIssue> {
    @BindView(R.id.iv_issue_state) public TextView state;
    @BindView(R.id.tv_title) public TextView title;
    @BindView(R.id.iv_issue_source) public TextView issueSource;
    @BindView(R.id.tv_project) public TextView project;
    @BindView(R.id.tv_attachments) public TextView attachments;// 附件
    @BindView(R.id.tv_childissues) public TextView childissues;// 子任务
    @BindView(R.id.tv_relations) public TextView relations;// 关联任务
    @BindView(R.id.tv_accept_time) public TextView accept_time;
    @BindView(R.id.tv_author) public TextView author;
    @BindView(R.id.tv_to) public TextView to;
    @BindView(R.id.tv_touser) public TextView touser;
    @BindView(R.id.tv_time) public TextView time;
    @BindView(R.id.tv_comment_count) public TextView comment;
    @BindView(R.id.title) public TextView title_line;

    public TeamIssueCell(Context context) {
        super(context);
    }

    public TeamIssueCell(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TeamIssueCell(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public void bindTo(TeamIssue data) {

    }
}
