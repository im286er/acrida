package net.gility.acrida.ui.fragment.team;

import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

import net.gility.acrida.R;
import net.gility.acrida.network.OSChinaTeamApi;
import net.gility.acrida.ui.fragment.BaseListFragment;
import net.gility.acrida.ui.adapter.ListBaseAdapter;
import net.gility.acrida.ui.adapter.team.TeamProjectMemberAdapter;
import net.gility.acrida.content.team.Team;
import net.gility.acrida.content.team.TeamMember;
import net.gility.acrida.content.team.TeamMemberList;
import net.gility.acrida.content.team.TeamProject;
import net.gility.acrida.ui.TeamMainActivity;
import net.gility.acrida.ui.widget.StateView;
import net.gility.acrida.utils.UIHelper;
import net.gility.acrida.utils.XmlUtils;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

/**
 * TeamProjectFragment.java
 * 
 * @author 火蚁(http://my.oschina.net/u/253900)
 * 
 * @data 2015-2-28 下午4:08:58
 */
public class TeamProjectMemberFragment extends
	BaseListFragment<TeamMember> {

    private Team mTeam;

    private int mTeamId;

    private TeamProject mTeamProject;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	Bundle bundle = getArguments();
	if (bundle != null) {
	    mTeam = (Team) bundle
		    .getSerializable(TeamMainActivity.BUNDLE_KEY_TEAM);
	    
	    mTeamProject = (TeamProject) bundle.getSerializable(TeamMainActivity.BUNDLE_KEY_PROJECT);

	    mTeamId = mTeam.getId();
	}
    }

    @Override
    protected TeamProjectMemberAdapter getListAdapter() {
	// TODO Auto-generated method stub
	return new TeamProjectMemberAdapter();
    }

    @Override
    protected String getCacheKeyPrefix() {
	return "team_project_member_list_" + mTeamId + "_" + mTeamProject.getGit().getId();
    }

    @Override
    protected TeamMemberList parseList(InputStream is) throws Exception {
	TeamMemberList list = XmlUtils.toBean(
		TeamMemberList.class, is);
	return list;
    }

    @Override
    protected TeamMemberList readList(Serializable seri) {
	return ((TeamMemberList) seri);
    }

    @Override
    protected void sendRequestData() {
	// TODO Auto-generated method stub
	OSChinaTeamApi.getTeamProjectMemberList(mTeamId, mTeamProject,
		mHandler);
    }

    @Override
    protected void executeOnLoadDataSuccess(List<TeamMember> data) {
	// TODO Auto-generated method stub
	super.executeOnLoadDataSuccess(data);
	if (mAdapter.getData().isEmpty()) {
	    setNoProjectMember();
	}
	mAdapter.setState(ListBaseAdapter.STATE_NO_MORE);
    }

    private void setNoProjectMember() {
	mErrorLayout.setStateType(StateView.TYPE_NO_DATA);
	mErrorLayout.setErrorImag(R.drawable.page_icon_empty);
	String str = getResources().getString(
		R.string.team_empty_project_member);
	mErrorLayout.setErrorMessage(str);
    }
    
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
        // TODO Auto-generated method stub
	TeamMember teamMember = mAdapter.getItem(position);
	if (teamMember != null) {
	    UIHelper.showTeamMemberInfo(getActivity(), mTeamId, teamMember);
	}
    }
}
