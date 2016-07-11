package net.gility.acrida.ui.adapter.team;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.gility.acrida.R;
import net.gility.acrida.content.team.TeamIssueCatalog;
import net.gility.acrida.ui.adapter.ListBaseAdapter;
import net.gility.acrida.utils.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * TeamIssueCatalogAdapter.java
 * 
 * @author 火蚁(http://my.oschina.net/u/253900)
 * 
 * @data 2015-3-1 下午3:37:03
 */
public class TeamIssueCatalogAdapter extends ListBaseAdapter<TeamIssueCatalog> {

    @Override
    protected View getRealView(int position, View convertView, ViewGroup parent) {
	ViewHolder vh = null;
	if (convertView == null || convertView.getTag() == null) {
	    convertView = getLayoutInflater(parent.getContext()).inflate(
		    R.layout.list_cell_team_issue_catalog, null);
	    vh = new ViewHolder(convertView);
	    convertView.setTag(vh);
	} else {
	    vh = (ViewHolder) convertView.getTag();
	}

	TeamIssueCatalog item = mDatas.get(position);

	vh.title.setText(item.getTitle());
	vh.state.setText(item.getOpenedIssueCount() + "/"
		+ item.getAllIssueCount());
	
	String description = item.getDescription();
	if (description != null && !StringUtils.isEmpty(description)) {
	    vh.description.setText(description);
	} else {
	    vh.description.setText("暂无描述");
	}

	return convertView;
    }

    static class ViewHolder {

	@BindView(R.id.tv_team_issue_catalog_title)
	TextView title;
	@BindView(R.id.tv_team_issue_catalog_desc)
	TextView description;
	@BindView(R.id.tv_team_issue_catalog_state)
	TextView state;

	public ViewHolder(View view) {
	    ButterKnife.bind(this, view);
	}
    }
}
