package net.gility.acrida.ui.fragment.team;

import java.io.InputStream;
import java.io.Serializable;

import net.gility.acrida.android.ApplicationLoader;
import net.gility.acrida.network.OSChinaApi;
import net.gility.acrida.ui.fragment.BaseListFragment;
import net.gility.acrida.ui.adapter.team.TeamIssueAdapter;
import net.gility.acrida.content.team.Team;
import net.gility.acrida.content.team.TeamIssue;
import net.gility.acrida.content.team.TeamIssueList;
import net.gility.acrida.ui.TeamMainActivity;
import net.gility.acrida.utils.UIHelper;
import net.gility.acrida.utils.XmlUtils;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

/**
 * 我的任务列表界面
 * 
 * @author kymjs (https://github.com/kymjs)
 * 
 */
public class MyIssueFragment extends BaseListFragment<TeamIssue> {

    protected static final String TAG = TeamIssueFragment.class.getSimpleName();
    private static final String CACHE_KEY_PREFIX = "my_issue_";

    private Team mTeam;
    private String type = "all";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mTeam = (Team) bundle
                    .getSerializable(TeamMainActivity.BUNDLE_KEY_TEAM);
            type = bundle.getString(MyIssuePagerfragment.MY_ISSUEDETAIL_KEY,
                    "all");
        }
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        mListView.setDivider(new ColorDrawable(0x00000000));
        mListView.setSelector(new ColorDrawable(0x00000000));
    }

    @Override
    protected TeamIssueAdapter getListAdapter() {
        return new TeamIssueAdapter();
    }

    /**
     * 获取当前展示页面的缓存数据
     */
    @Override
    protected String getCacheKeyPrefix() {
        return CACHE_KEY_PREFIX + ApplicationLoader.getInstance().getLoginUid() + "_"
                + mTeam.getId() + mCurrentPage + type;
    }

    @Override
    protected TeamIssueList parseList(InputStream is) throws Exception {
        TeamIssueList list = XmlUtils.toBean(TeamIssueList.class, is);
        return list;
    }

    @Override
    protected TeamIssueList readList(Serializable seri) {
        return ((TeamIssueList) seri);
    }

    @Override
    protected void sendRequestData() {
        OSChinaApi.getMyIssue(mTeam.getId() + "", ApplicationLoader.getInstance()
                .getLoginUid() + "", mCurrentPage, type, mHandler);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
        // TODO Auto-generated method stub
        TeamIssue issue = mAdapter.getItem(position);
        if (issue != null) {
            UIHelper.showTeamIssueDetail(getActivity(), mTeam, issue, null);
        }
    }
}
