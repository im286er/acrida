package net.gility.acrida.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import net.gility.acrida.android.ApplicationLoader;
import net.gility.acrida.content.News;
import net.gility.acrida.content.NewsList;
import net.gility.acrida.dagger.Injector;
import net.gility.acrida.network.OSChinaApi;
import net.gility.acrida.network.OSChinaService;
import net.gility.acrida.ui.adapter.ListBaseAdapter;
import net.gility.acrida.ui.adapter.NewsAdapter;
import net.gility.acrida.ui.help.OnTabReselectListener;
import net.gility.acrida.ui.widget.StateView;
import net.gility.acrida.utils.UIHelper;
import net.gility.acrida.utils.XmlUtils;

import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import retrofit2.adapter.rxjava.Result;
import rx.Observable;

/**
 * 新闻资讯
 * 
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @created 2014年11月12日 下午4:17:45
 * 
 */
public class RtfNewsFragment extends InjectListFragment<News, NewsList> implements
        OnTabReselectListener {

    protected static final String TAG = RtfNewsFragment.class.getSimpleName();
    private static final String CACHE_KEY_PREFIX = "newslist_";

    @Inject OSChinaService mOSChinaService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Injector.obtain().inject(this);
    }

    @Override
    protected NewsAdapter getListAdapter() {
        return new NewsAdapter();
    }

    @Override
    protected String getCacheKeyPrefix() {
        return CACHE_KEY_PREFIX + mCatalog;
    }

    @Override
    protected NewsList parseList(InputStream is) throws Exception {
        NewsList list = null;
        try {
            list = XmlUtils.toBean(NewsList.class, is);
        } catch (NullPointerException e) {
            list = new NewsList();
        }
        return list;
    }

    @Override
    protected NewsList readList(Serializable seri) {
        return ((NewsList) seri);
    }

    @Override
    protected void sendRequestData() {
        OSChinaApi.getNewsList(mCatalog, mCurrentPage, mHandler);
    }

    protected Observable<Result<NewsList>> obtainData() {
        String show = "week";
        if (mCatalog == NewsList.CATALOG_WEEK) {
            show = "week";
        } else if (mCatalog == NewsList.CATALOG_MONTH) {
            show = "month";
        }
        return mOSChinaService.getNewsList(mCatalog, mCurrentPage, ApplicationLoader.PAGE_SIZE);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
        News news = mAdapter.getItem(position);
        if (news != null) {
            UIHelper.showNewsRedirect(view.getContext(), news);

            // 放入已读列表
            saveToReadedList(view, NewsList.PREF_READED_NEWS_LIST, news.getId() + "");
        }
    }

    @Override
    protected void executeOnLoadDataSuccess(List<News> data) {
        if (mCatalog == NewsList.CATALOG_WEEK
                || mCatalog == NewsList.CATALOG_MONTH) {
            mErrorLayout.setStateType(StateView.TYPE_HIDE_LAYOUT);
            if (mState == STATE_REFRESH)
                mAdapter.clear();
            mAdapter.addData(data);
            mState = STATE_NOMORE;
            mAdapter.setState(ListBaseAdapter.STATE_NO_MORE);
            return;
        }
        super.executeOnLoadDataSuccess(data);
    }

    @Override
    public void onTabReselect() {
        onRefresh();
    }

    @Override
    protected long getAutoRefreshTime() {
        // 最新资讯两小时刷新一次
        if (mCatalog == NewsList.CATALOG_ALL) {

            return 2 * 60 * 60;
        }
        return super.getAutoRefreshTime();
    }
}
