package net.gility.acrida.ui.fragment;

import net.gility.acrida.R;
import net.gility.acrida.ui.adapter.ViewPageFragmentAdapter;
import net.gility.acrida.content.EventList;

import android.os.Bundle;
import android.view.View;

/** 
 * 活动viewpager页面
 * 
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @version 创建时间：2014年12月24日 下午4:46:04 
 * 
 */
public class EventViewPagerFragment extends BaseViewPagerFragment {

	@Override
	protected void onSetupTabAdapter(ViewPageFragmentAdapter adapter) {
		String[] title = getResources().getStringArray(R.array.events);
		adapter.addTab(title[0], "new_event", EventFragment.class, getBundle(EventList.EVENT_LIST_TYPE_NEW_EVENT));
		adapter.addTab(title[1], "my_event", EventFragment.class, getBundle(EventList.EVENT_LIST_TYPE_MY_EVENT));
	}
	
	private Bundle getBundle(int event_type) {
		Bundle bundle = new Bundle();
		bundle.putInt(EventFragment.BUNDLE_KEY_EVENT_TYPE, event_type);
		return bundle;
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
}
