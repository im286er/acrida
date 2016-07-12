package net.gility.acrida.ui.adapter.team;

import net.gility.acrida.R;
import net.gility.acrida.content.team.TeamProject;
import net.gility.acrida.ui.adapter.BaseCellAdapter;
import net.gility.acrida.ui.cell.TeamProjectCell;

/**
 * 团队项目适配器
 *
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @author Alimy
 * @version 创建时间：2015年1月19日 下午6:00:33
 */

public class TeamProjectListAdapterNew extends BaseCellAdapter<TeamProject, TeamProjectCell> {

    @Override
    protected int getLayoutId() {
        return R.layout.cell_team_project;
    }
}
