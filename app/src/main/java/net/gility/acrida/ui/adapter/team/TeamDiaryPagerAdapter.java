package net.gility.acrida.ui.adapter.team;

import net.gility.acrida.utils.StringUtils;
import net.gility.acrida.ui.widget.DiaryPageContentView;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * 周报ViewPager适配器
 * 
 * @author kymjs (http://www.kymjs.com)
 */
public class TeamDiaryPagerAdapter extends PagerAdapter {
    private final Context cxt;
    private final int currentYear;
    private final int teamId;

    public TeamDiaryPagerAdapter(Context cxt, int currentYear, int teamId) {
        this.currentYear = currentYear;
        this.cxt = cxt;
        this.teamId = teamId;
    }

    @Override
    public int getCount() {
        return currentYear == 2015 ? StringUtils.getWeekOfYear() : 52;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View pagerView = new DiaryPageContentView(cxt, teamId, currentYear,
                position + 1).getView();
        (container).addView(pagerView);
        return pagerView;
    }
}
