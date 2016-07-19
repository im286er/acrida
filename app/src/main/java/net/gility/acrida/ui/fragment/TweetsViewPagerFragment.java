package net.gility.acrida.ui.fragment;

import net.gility.acrida.R;
import net.gility.acrida.ui.adapter.ViewPageFragmentAdapter;
import net.gility.acrida.content.TweetsList;
import net.gility.acrida.ui.help.OnTabReselectListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * 动弹界面（包括最新动弹、热门动弹、我的动弹）
 * 
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @created 2014年9月25日 下午2:21:52
 * 
 */
public class TweetsViewPagerFragment extends BaseViewPagerFragment implements
        OnTabReselectListener {

    @Override
    protected void onSetupTabAdapter(ViewPageFragmentAdapter adapter) {

        String[] title = getResources().getStringArray(
                R.array.tweets_viewpage_arrays);
        adapter.addTab(title[0], "new_tweets", RtfTweetsFragment.class,
                getBundle(TweetsList.CATALOG_LATEST));
        adapter.addTab(title[1], "hot_tweets", RtfTweetsFragment.class,
                getBundle(TweetsList.CATALOG_HOT));
        adapter.addTab(title[2], "my_tweets", RtfTweetsFragment.class,
                getBundle(TweetsList.CATALOG_ME));
    }

    private Bundle getBundle(int catalog) {
        Bundle bundle = new Bundle();
        bundle.putInt(BaseListFragment.BUNDLE_KEY_CATALOG, catalog);
        return bundle;
    }

    @Override
    public void onClick(View v) {}

    @Override
    public void initView(View view) {}

    @Override
    public void initData() {}

    @Override
    public void onTabReselect() {
        try {
            int currentIndex = mViewPager.getCurrentItem();
            Fragment currentFragment = getChildFragmentManager().getFragments()
                    .get(currentIndex);
            if (currentFragment != null
                    && currentFragment instanceof OnTabReselectListener) {
                OnTabReselectListener listener = (OnTabReselectListener) currentFragment;
                listener.onTabReselect();
            }
        } catch (NullPointerException e) {
        }
    }
}