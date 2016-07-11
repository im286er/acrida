package net.gility.acrida.ui.fragment.team;

import net.gility.acrida.R;
import net.gility.acrida.ui.adapter.ViewPageFragmentAdapter;
import net.gility.acrida.ui.fragment.BaseViewPagerFragment;
import net.gility.acrida.content.team.Team;
import net.gility.acrida.ui.TeamMainActivity;
import net.gility.acrida.ui.TeamNewActiveActivity;
import net.gility.acrida.utils.UIHelper;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

/**
 * Team主界面
 * 
 * @author kymjs (https://github.com/kymjs)
 * 
 */
public class TeamMainViewPagerFragment extends BaseViewPagerFragment {

    private Team mTeam;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.team_main_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.team_new_active:
            showCreateNewActive();
            break;
        case R.id.team_new_issue:
            UIHelper.showCreateNewIssue(getActivity(), mTeam, null, null);
            break;
        default:
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showCreateNewActive() {
        Intent intent = new Intent(getActivity(), TeamNewActiveActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(TeamMainActivity.BUNDLE_KEY_TEAM, mTeam);
        intent.putExtras(bundle);
        getActivity().startActivity(intent);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        mViewPager.setOffscreenPageLimit(2);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mTeam = (Team) bundle
                    .getSerializable(TeamMainActivity.BUNDLE_KEY_TEAM);
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void initView(View view) {

    }

    @Override
    public void initData() {

    }

    @Override
    protected void onSetupTabAdapter(ViewPageFragmentAdapter adapter) {
        String[] arraStrings = getResources().getStringArray(
                R.array.team_main_viewpager);

        adapter.addTab(arraStrings[0], "team_board", TeamBoardFragment.class,
                getArguments());
        Bundle issueFragmentBundle = getArguments();
        issueFragmentBundle.putBoolean("needmenu", false);
        adapter.addTab(arraStrings[1], "team_issue", TeamIssueFragment.class,
                issueFragmentBundle);
        adapter.addTab(arraStrings[2], "team_member", TeamMemberFragment.class,
                getArguments());
    }
}
