package net.gility.acrida.ui.fragment.team;

import java.io.InputStream;
import java.io.Serializable;

import net.gility.acrida.android.ApplicationLoader;
import net.gility.acrida.network.OSChinaTeamApi;
import net.gility.acrida.ui.fragment.BaseListFragment;
import net.gility.acrida.model.SimpleBackPage;
import net.gility.acrida.ui.adapter.team.TeamIssueCatalogAdapter;
import net.gility.acrida.content.team.Team;
import net.gility.acrida.content.team.TeamIssueCatalog;
import net.gility.acrida.content.team.TeamIssueCatalogList;
import net.gility.acrida.content.team.TeamProject;
import net.gility.acrida.ui.TeamMainActivity;
import net.gility.acrida.utils.UIHelper;
import net.gility.acrida.utils.XmlUtils;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

/**
 * 任务分组列表
 * 
 * TeamIssueCatalogFragment.java
 * 
 * @author 火蚁(http://my.oschina.net/u/253900)
 *
 * @data 2015-3-1 下午3:36:03
 */
public class TeamIssueCatalogFragment extends
	BaseListFragment<TeamIssueCatalog> {
    
    private Team mTeam;
    
    private TeamProject mTeamProject;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mTeam = (Team) args.getSerializable(TeamMainActivity.BUNDLE_KEY_TEAM);
            mTeamProject = (TeamProject) args.getSerializable(TeamMainActivity.BUNDLE_KEY_PROJECT);
        }
    }

    @Override
    protected TeamIssueCatalogAdapter getListAdapter() {
	// TODO Auto-generated method stub
	return new TeamIssueCatalogAdapter();
    }
    
    @Override
    protected TeamIssueCatalogList parseList(InputStream is)
            throws Exception {
        return XmlUtils.toBean(TeamIssueCatalogList.class, is);
    }
    
    @Override
    protected TeamIssueCatalogList readList(Serializable seri) {
        return (TeamIssueCatalogList)seri;
    }

    @Override
    protected void sendRequestData() {
	int uid = ApplicationLoader.getInstance().getLoginUid();
	int teamId= mTeam.getId();
	int projectId = mTeamProject.getGit().getId();
	String source = mTeamProject.getSource();
	OSChinaTeamApi.getTeamCatalogIssueList(uid, teamId, projectId, source, mHandler);
    }
    
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
        // TODO Auto-generated method stub
	TeamIssueCatalog teamIssueCatalog = mAdapter.getItem(position);
	if (teamIssueCatalog != null) {
	    Bundle bundle = new Bundle();
	    bundle.putSerializable(TeamMainActivity.BUNDLE_KEY_TEAM, mTeam);
	    bundle.putSerializable(TeamMainActivity.BUNDLE_KEY_PROJECT, mTeamProject);
	    bundle.putSerializable(TeamMainActivity.BUNDLE_KEY_ISSUE_CATALOG, teamIssueCatalog);
	    UIHelper.showSimpleBack(getActivity(), SimpleBackPage.TEAM_ISSUECATALOG_ISSUE_LIST, bundle);
	}
    }
    
    @Override
    protected String getCacheKeyPrefix() {
        // TODO Auto-generated method stub
        return "team_issue_catalog_list" + mTeam.getId() + "_" + mTeamProject.getGit().getId();
    }
}

