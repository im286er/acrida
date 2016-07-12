package net.gility.acrida.ui.adapter;

import net.gility.acrida.R;
import net.gility.acrida.content.SoftwareCatalogList.SoftwareType;
import net.gility.acrida.ui.cell.SoftwareCatalogCell;

public class SoftwareCatalogListAdapter extends BaseCellAdapter<SoftwareType, SoftwareCatalogCell> {


	@Override
	protected int getLayoutId() {
		return R.layout.cell_software_catalog;
	}
}
