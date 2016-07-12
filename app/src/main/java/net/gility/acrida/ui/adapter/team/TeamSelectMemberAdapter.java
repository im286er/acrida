package net.gility.acrida.ui.adapter.team;

import net.gility.acrida.R;
import net.gility.acrida.content.team.TeamMember;
import net.gility.acrida.ui.adapter.BaseCellAdapter;
import net.gility.acrida.ui.cell.TeamProjectMemberCell;

public class TeamSelectMemberAdapter extends BaseCellAdapter<TeamMember, TeamProjectMemberCell> {

	@Override
	protected int getLayoutId() {
		return R.layout.cell_team_project_member;
	}
}
