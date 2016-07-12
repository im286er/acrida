package net.gility.acrida.ui.cell;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import net.gility.acrida.R;
import net.gility.acrida.content.SoftwareCatalogList;

import butterknife.BindView;

/**
 * @author Alimy
 */

public class SoftwareCatalogCell extends RelativeCell<SoftwareCatalogList.SoftwareType> {
    @BindView(R.id.tv_software_catalog_name)TextView name;

    public SoftwareCatalogCell(Context context) {
        super(context);
    }

    public SoftwareCatalogCell(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SoftwareCatalogCell(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void bindTo(SoftwareCatalogList.SoftwareType data) {
        name.setText(data.getName());
    }
}
