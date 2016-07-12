package net.gility.acrida.ui.cell;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import net.gility.acrida.R;
import net.gility.acrida.content.team.TeamProject;
import net.gility.acrida.utils.TypefaceUtils;

import butterknife.BindView;

/**
 * @author Alimy
 */

public class TeamProjectCell extends RelativeCell<TeamProject> {
    @BindView(R.id.iv_source) TextView source;
    @BindView(R.id.tv_project_name) TextView name;
    @BindView(R.id.tv_project_issue) TextView issue;

    public TeamProjectCell(Context context) {
        super(context);
    }

    public TeamProjectCell(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TeamProjectCell(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void bindTo(TeamProject data) {
        String source = data.getSource();
        TextView tvSource = this.source;
        if (TextUtils.isEmpty(source)) {
            if (data.getGit().getId() == -1) {
                TypefaceUtils.setTypeface(tvSource, R.string.fa_tasks);
            } else {
                TypefaceUtils.setTypeface(tvSource, R.string.fa_inbox);
            }
        } else if (source.equalsIgnoreCase(TeamProject.GITOSC)) {
            TypefaceUtils.setTypeface(tvSource, R.string.fa_gitosc);
        } else if (source.equalsIgnoreCase(TeamProject.GITHUB)) {
            TypefaceUtils.setTypeface(tvSource, R.string.fa_github);
        } else {
            TypefaceUtils.setTypeface(tvSource, R.string.fa_list_alt);
        }

        name.setText(data.getGit().getOwnerName() + " / " + data.getGit().getName());
        issue.setText(data.getIssue().getOpened() + "/" + data.getIssue().getAll());
    }
}
