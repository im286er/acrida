package net.gility.acrida.ui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.gility.acrida.R;
import net.gility.acrida.content.SoftwareCatalogList.SoftwareType;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SoftwareCatalogListAdapter extends ListBaseAdapter<SoftwareType> {
	
	static class ViewHold{
		@BindView(R.id.tv_software_catalog_name)TextView name;
		public ViewHold(View view) {
			ButterKnife.bind(this,view);
		}
	}

	@Override
	protected View getRealView(int position, View convertView, ViewGroup parent) {
		
		ViewHold vh = null;
		
		if (convertView == null || convertView.getTag() == null) {
			convertView = getLayoutInflater(parent.getContext()).inflate(R.layout.list_cell_softwarecatalog, null);
			vh = new ViewHold(convertView);
			convertView.setTag(vh);
		} else {
			vh = (ViewHold)convertView.getTag();
		}
		
		SoftwareType softwareType = (SoftwareType) mDatas.get(position);
		vh.name.setText(softwareType.getName());
		return convertView;
		
	}
}
