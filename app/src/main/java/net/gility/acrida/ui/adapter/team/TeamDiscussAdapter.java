package net.gility.acrida.ui.adapter.team;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.gility.acrida.R;
import net.gility.acrida.content.team.TeamDiscuss;
import net.gility.acrida.ui.adapter.BaseCellAdapter;
import net.gility.acrida.ui.adapter.ListBaseAdapter;
import net.gility.acrida.ui.cell.TeamDiscussCell;
import net.gility.acrida.ui.widget.AvatarView;
import net.gility.acrida.utils.HTMLUtil;
import net.gility.acrida.utils.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * team 讨论区帖子
 *
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @created 2014年10月9日 下午6:22:54
 *
 * @author Alimy
 */
public class TeamDiscussAdapter extends BaseCellAdapter<TeamDiscuss, TeamDiscussCell> {

    @Override
    protected int getLayoutId() {
        return R.layout.cell_team_discuss;
    }
}
