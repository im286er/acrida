package net.gility.acrida.ui.adapter.team;

import android.graphics.Paint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.gility.acrida.R;
import net.gility.acrida.content.team.TeamIssue;
import net.gility.acrida.ui.adapter.ListBaseAdapter;
import net.gility.acrida.ui.cell.TeamIssueCell;
import net.gility.acrida.utils.StringUtils;
import net.gility.acrida.utils.TypefaceUtils;
import net.gility.acrida.utils.ViewUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 任务列表适配器
 *
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @author Alimy
 * @version 创建时间：2015年1月14日 下午5:28:51
 *
 */
public class TeamIssueAdapter extends ListBaseAdapter<TeamIssue> {

    @Override
    protected View getRealView(int position, View convertView, ViewGroup parent) {

        int type = getItemViewType(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cell_team_issue, parent, false);
        }

        TeamIssueCell cell = (TeamIssueCell) convertView;
        TeamIssue item = mDatas.get(position);

        cell.title.setText(item.getTitle());

        String date = StringUtils.friendly_time2(item.getCreateTime());
        String preDate = "";
        if (position > 0) {
            preDate = StringUtils.friendly_time2(mDatas.get(position - 1)
                    .getCreateTime());
        }
        if (preDate.equals(date)) {
            cell.title_line.setVisibility(View.GONE);
        } else {
            cell.title_line.setText(date);
            cell.title_line.setVisibility(View.VISIBLE);
        }

        setIssueState(cell, item);

        setIssueSource(cell, item);

        cell.author.setText(item.getAuthor().getName());
        if (item.getToUser() == null
                || TextUtils.isEmpty(item.getToUser().getName())) {
            cell.to.setText("未指派");
            cell.touser.setVisibility(View.GONE);
        } else {
            cell.to.setText("指派给");
            cell.touser.setVisibility(View.VISIBLE);
            cell.touser.setText(item.getToUser().getName());
        }

        cell.time.setText(StringUtils.friendly_time(item.getCreateTime()));
        cell.comment.setText(item.getReplyCount() + "");

        if (item.getProject() != null && item.getProject().getGit() != null) {
            cell.project.setVisibility(View.VISIBLE);
            String gitState = item.getGitpush() == TeamIssue.TEAM_ISSUE_GITPUSHED ? ""
                    : " -未同步";
            setText(cell.project, item.getProject().getGit().getName() + gitState);
        } else {
            cell.project.setVisibility(View.GONE);
        }

        String deadlineTime = item.getDeadlineTime();
        if (!StringUtils.isEmpty(deadlineTime)) {
            cell.accept_time.setVisibility(View.VISIBLE);
            setText(cell.accept_time, getDeadlineTime(item), true);
        } else {
            cell.accept_time.setVisibility(View.GONE);
        }

        if (item.getAttachments().getTotalCount() != 0) {
            cell.attachments.setVisibility(View.VISIBLE);
            cell.attachments.setText("附件" + item.getAttachments().getTotalCount()
                    + "");
        } else {
            cell.attachments.setVisibility(View.GONE);
        }

        if (item.getChildIssues() != null
                && item.getChildIssues().getTotalCount() != 0) {
            cell.childissues.setVisibility(View.VISIBLE);
            setText(cell.childissues, "子任务("
                    + item.getChildIssues().getClosedCount() + "/"
                    + item.getChildIssues().getTotalCount() + ")");
        } else {
            cell.childissues.setVisibility(View.GONE);
        }

        if (item.getRelations().getTotalCount() != 0) {
            cell.relations.setVisibility(View.VISIBLE);
            cell.relations.setText("关联" + item.getRelations().getTotalCount()
                    + "");
        } else {
            cell.relations.setVisibility(View.GONE);
        }

        return convertView;
    }

    private void setIssueState(TeamIssueCell cell, TeamIssue teamIssue) {
        String state = teamIssue.getState();
        if (TextUtils.isEmpty(state))
            return;
        TypefaceUtils.setTypeface(cell.state, teamIssue.getIssueStateFaTextId());

        if (teamIssue.getState().equals("closed")
                || teamIssue.getState().equals("accepted")) {
            ViewUtils.setTextViewLineFlag(cell.title, Paint.STRIKE_THRU_TEXT_FLAG
                    | Paint.ANTI_ALIAS_FLAG);
        } else {
            ViewUtils.setTextViewLineFlag(cell.title, 0 | Paint.ANTI_ALIAS_FLAG);
        }
    }

    private void setIssueSource(TeamIssueCell cell, TeamIssue teamIssue) {
        String source = teamIssue.getSource();
        if (TextUtils.isEmpty(source))
            return;
        TextView tv = cell.issueSource;
        if (source.equalsIgnoreCase(TeamIssue.TEAM_ISSUE_SOURCE_GITOSC)) {
            // 来自gitosc
            TypefaceUtils.setTypeface(tv, R.string.fa_gitosc);
        } else if (source.equalsIgnoreCase(TeamIssue.TEAM_ISSUE_SOURCE_GITHUB)) {
            // 来自github
            TypefaceUtils.setTypeface(tv, R.string.fa_github);
        } else {
            // 来自teamosc
            TypefaceUtils.setTypeface(tv, R.string.fa_team);
        }
    }

    private String getDeadlineTime(TeamIssue teamIssue) {
        SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = StringUtils.toDate(teamIssue.getUpdateTime(), dataFormat);
        return DateFormat.getDateInstance(DateFormat.SHORT).format(date);
    }
}
