package net.gility.acrida.ui.fragment.team;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;

import net.gility.acrida.network.OSChinaApi;
import net.gility.acrida.ui.fragment.BaseListFragment;
import net.gility.acrida.ui.adapter.team.TeamActiveAdapter;
import net.gility.acrida.content.team.Team;
import net.gility.acrida.content.team.TeamActive;
import net.gility.acrida.content.team.TeamActives;
import net.gility.acrida.ui.TeamMainActivity;
import net.gility.acrida.utils.TLog;
import net.gility.acrida.utils.UIHelper;
import net.gility.acrida.utils.XmlUtils;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

/**
 * Team动态界面
 * 
 * @author kymjs (kymjs123@gmail.com)
 * 
 */
public class TeamActiveFragment extends BaseListFragment<TeamActive> {

    public final static String BUNDLE_KEY_UID = "UID";

    public static final String DYNAMIC_FRAGMENT_KEY = "DynamicFragment";
    public static final String DYNAMIC_FRAGMENT_TEAM_KEY = "DynamicFragment_teamid";
    protected static final String TAG = TeamActiveFragment.class
            .getSimpleName();
    private static final String CACHE_KEY_PREFIX = "DynamicFragment_list";

    private Activity aty;
    private Team team;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            team = (Team) bundle
                    .getSerializable(TeamMainActivity.BUNDLE_KEY_TEAM);
        }
        if (team == null) {
            team = new Team();
            TLog.log(getClass().getSimpleName(), "team对象初始化异常");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
            @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        aty = getActivity();
        return view;
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        mListView.setDivider(new ColorDrawable(0x00000000));
        mListView.setSelector(new ColorDrawable(0x00000000));
    }

    @Override
    protected TeamActiveAdapter getListAdapter() {
        return new TeamActiveAdapter(aty);
    }

    @Override
    protected String getCacheKeyPrefix() {
        String str = CACHE_KEY_PREFIX + "_" + team.getId() + "_" + mCurrentPage;
        return str;
    }

    @Override
    protected TeamActives parseList(InputStream is) throws Exception {
        TeamActives list = XmlUtils.toBean(TeamActives.class, is);
        if (list.getList() == null) {
            list.setActives(new ArrayList<TeamActive>());
        }
        return list;
    }

    @Override
    protected TeamActives readList(Serializable seri) {
        return (TeamActives) seri;
    }

    @Override
    protected void sendRequestData() {
        OSChinaApi.teamDynamic(team, mCurrentPage, mHandler);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
        try {
            TeamActive active = mAdapter.getItem(position);
            if (active != null) {
                UIHelper.showTeamActiveDetail(aty, team.getId(), active);
            }
        } catch (IndexOutOfBoundsException e) {
        }
    }

    @Override
    protected long getAutoRefreshTime() {
        // 1小时间距，主动刷新列表
        return 1 * 60 * 60;
    }

}
