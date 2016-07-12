package net.gility.acrida.ui.adapter.team;

import net.gility.acrida.R;
import net.gility.acrida.content.team.TeamMember;
import net.gility.acrida.ui.adapter.BaseCellAdapter;
import net.gility.acrida.ui.cell.TeamProjectMemberCell;

/**
 * 团队项目适配器
 * 
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @author Alimy
 * @version 创建时间：2015年1月19日 下午6:00:33
 * 
 */

public class TeamProjectMemberAdapter extends BaseCellAdapter<TeamMember, TeamProjectMemberCell> {

	@Override
	protected int getLayoutId() {
		return R.layout.cell_team_project_member;
	}
}
