package net.gility.acrida.ui.adapter.team;

import net.gility.acrida.R;
import net.gility.acrida.content.team.TeamIssueCatalog;
import net.gility.acrida.ui.adapter.BaseCellAdapter;
import net.gility.acrida.ui.cell.TeamIssueCatalogCell;

/**
 * TeamIssueCatalogAdapter.java
 * 
 * @author 火蚁(http://my.oschina.net/u/253900)
 * @author Alimy
 * 
 * @data 2015-3-1 下午3:37:03
 */
public class TeamIssueCatalogAdapter extends BaseCellAdapter<TeamIssueCatalog, TeamIssueCatalogCell> {

	@Override
	protected int getLayoutId() {
		return  R.layout.cell_team_issue_catalog;
	}
}
