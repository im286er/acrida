package net.gility.acrida.ui.fragment;

import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

import net.gility.acrida.ui.adapter.SoftwareAdapter;
import net.gility.acrida.network.OSChinaApi;
import net.gility.acrida.content.Entity;
import net.gility.acrida.content.ListEntity;
import net.gility.acrida.content.SoftwareDec;
import net.gility.acrida.content.SoftwareList;
import net.gility.acrida.utils.UIHelper;
import net.gility.acrida.utils.XmlUtils;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

public class SoftwareListFragment extends BaseListFragment<SoftwareDec> {

    public static final String BUNDLE_SOFTWARE = "BUNDLE_SOFTWARE";

    protected static final String TAG = SoftwareListFragment.class
	    .getSimpleName();
    private static final String CACHE_KEY_PREFIX = "softwarelist_";

    private String softwareType = "recommend";

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	Bundle args = getArguments();
	if (args != null) {
	    softwareType = args.getString(BUNDLE_SOFTWARE);
	}
    }

    @Override
    protected SoftwareAdapter getListAdapter() {
	return new SoftwareAdapter();
    }

    @Override
    protected String getCacheKeyPrefix() {
	return CACHE_KEY_PREFIX + softwareType;
    }

    @Override
    protected SoftwareList parseList(InputStream is) throws Exception {
	SoftwareList list = XmlUtils.toBean(SoftwareList.class, is);
	return list;
    }

    @Override
    protected ListEntity readList(Serializable seri) {
	return ((SoftwareList) seri);
    }

    @Override
    protected void sendRequestData() {
	OSChinaApi.getSoftwareList(softwareType, mCurrentPage, mHandler);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
	    long id) {
	SoftwareDec softwaredec = (SoftwareDec) mAdapter.getItem(position);
	if (softwaredec != null) {
	    String ident = softwaredec.getUrl().substring(softwaredec.getUrl().lastIndexOf("/") + 1);
	    UIHelper.showSoftwareDetail(getActivity(), ident);
	    // 放入已读列表
	    saveToReadedList(view, SoftwareList.PREF_READED_SOFTWARE_LIST,
		    softwaredec.getName());
	}
    }

    @Override
    protected boolean compareTo(List<? extends Entity> data, Entity enity) {
	int s = data.size();
	if (enity != null) {
	    for (int i = 0; i < s; i++) {
		if (((SoftwareDec) enity).getName().equals(
			((SoftwareDec) data.get(i)).getName())) {
		    return true;
		}
	    }
	}
	return false;
    }

}
