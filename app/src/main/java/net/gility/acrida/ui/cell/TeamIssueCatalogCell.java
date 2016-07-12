package net.gility.acrida.ui.cell;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import net.gility.acrida.R;
import net.gility.acrida.content.team.TeamIssueCatalog;
import net.gility.acrida.utils.StringUtils;

import butterknife.BindView;

/**
 * @author Alimy
 */

public class TeamIssueCatalogCell extends RelativeCell<TeamIssueCatalog> {
    @BindView(R.id.tv_team_issue_catalog_title) TextView title;
    @BindView(R.id.tv_team_issue_catalog_desc) TextView description;
    @BindView(R.id.tv_team_issue_catalog_state) TextView state;

    public TeamIssueCatalogCell(Context context) {
        super(context);
    }

    public TeamIssueCatalogCell(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TeamIssueCatalogCell(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void bindTo(TeamIssueCatalog data) {
        title.setText(data.getTitle());
        state.setText(data.getOpenedIssueCount() + "/" + data.getAllIssueCount());

        String description = data.getDescription();
        if (description != null && !StringUtils.isEmpty(description)) {
            this.description.setText(description);
        } else {
            this.description.setText("暂无描述");
        }
    }
}
