package net.gility.acrida.ui.fragment.team;

import java.io.InputStream;
import java.io.Serializable;

import net.gility.acrida.android.ApplicationLoader;
import net.gility.acrida.network.OSChinaTeamApi;
import net.gility.acrida.ui.fragment.BaseListFragment;
import net.gility.acrida.content.ListEntity;
import net.gility.acrida.ui.adapter.team.TeamIssueAdapter;
import net.gility.acrida.content.team.TeamIssue;
import net.gility.acrida.content.team.TeamIssueList;
import net.gility.acrida.utils.XmlUtils;

/**
 * 任务列表界面
 * 
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @version 创建时间：2015年1月14日 下午5:15:46
 * 
 */

public class IssueListFragment extends BaseListFragment<TeamIssue> {

    private final String CACHE_KEY_PREFIX = "issue_list_";

    private final int mTeamId = 12481;

    private final int mProjectId = 435;

    @Override
    protected TeamIssueAdapter getListAdapter() {
        return new TeamIssueAdapter();
    }

    @Override
    protected String getCacheKeyPrefix() {
        return CACHE_KEY_PREFIX + "_" + mCurrentPage;
    }

    @Override
    protected TeamIssueList parseList(InputStream is) throws Exception {
        TeamIssueList list = XmlUtils.toBean(TeamIssueList.class, is);
        return list;
    }

    @Override
    protected void sendRequestData() {
        OSChinaTeamApi.getTeamIssueList(mTeamId, mProjectId, 0, "", 253900,
                "state", "scope", mCurrentPage, ApplicationLoader.PAGE_SIZE, mHandler);
    }

    @Override
    protected ListEntity<TeamIssue> readList(Serializable seri) {
        return null;
    }
}
