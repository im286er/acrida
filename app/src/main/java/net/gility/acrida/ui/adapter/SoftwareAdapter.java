package net.gility.acrida.ui.adapter;

import net.gility.acrida.R;
import net.gility.acrida.content.SoftwareDec;
import net.gility.acrida.ui.cell.SoftwareCell;

public class SoftwareAdapter extends BaseCellAdapter<SoftwareDec, SoftwareCell> {

    @Override
    protected int getLayoutId() {
        return R.layout.cell_software;
    }
}
